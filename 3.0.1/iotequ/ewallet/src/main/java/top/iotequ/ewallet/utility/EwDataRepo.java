package top.iotequ.ewallet.utility;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.iotequ.ewallet.dao.*;
import top.iotequ.ewallet.pojo.*;
import top.iotequ.ewallet.utility.exception.*;
import top.iotequ.framework.util.DateUtil;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.Util;
import top.iotequ.pay.exception.PayException;
import top.iotequ.pay.utility.PayUtil;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Repository which interacts with Dao Layer.
 * Created by ao on 2019-07-25
 */
@Component
public class EwDataRepo {

    @Autowired
    private EwUserDao ewUserDao;

    @Autowired
    private EwUserTimeDao ewUserTimeDao;

    @Autowired
    private EwUserCountDao ewUserCountDao;

    @Autowired
    private EwCountProjectDao ewCountProjectDao;

    @Autowired
    private EwTimeProjectDao ewTimeProjectDao;

    @Autowired
    private EwBillDao ewBillDao;

    /**
     * insert the given ewBill to the database, update its checkCode
     */
    void insertBill(EwBill ewBill) throws EWalletDuplicateBillNumberException {
        if (this.ewBillDao.select(ewBill.getNo()) != null) {
            throw new EWalletDuplicateBillNumberException("已存在订单号:" + ewBill.getNo());
        }

        ewBill.setCheckCode("");
        this.ewBillDao.insert(ewBill);
        EwBill dbEwBill = this.ewBillDao.select(ewBill.getNo());
        this.setCheckCode(dbEwBill);
        this.ewBillDao.update(dbEwBill);
    }

    /**
     * update the given ewBill to the database, update its checkCode
     */
    void updateBill(EwBill ewBill) throws EWalletDuplicateBillNumberException {
        this.setCheckCode(ewBill);
        this.ewBillDao.update(ewBill);
    }

    /**
     * Set the checkCode for the given {@link EWallet} Object.
     * Must be invoked when updating field of any EWallet.
     */
    void setCheckCode(EWallet eWallet) {
        EWallet.updateCheckCode(eWallet);
    }

    /**
     * Insert the given {@link EwUser} to the database, update its checkCode
     */
    void insertUser(EwUser ewUser) throws EWalletDuplicateUserNoException {
        if (this.ewUserDao.select(ewUser.getUserNo()) != null) {
            throw new EWalletDuplicateUserNoException("已存在用户名:" + ewUser.getUserNo());
        }

        this.setCheckCode(ewUser);
        this.ewUserDao.insert(ewUser);
    }

    /**
     * Insert the given {@link EwUserCount} to the database, update its checkCode
     */
    void insertUserCount(EwUserCount ewUserCount) throws EWalletUserNotExistException,
            EWalletCountProjectNotExistException, EWalletUserCountAlreadyExistException {
        if (this.ewUserDao.select(ewUserCount.getUserNo()) == null) {
            throw new EWalletUserNotExistException("对应用户名:" + ewUserCount.getUserNo() + " 不存在");
        }
        if (this.ewCountProjectDao.select(ewUserCount.getCountId()) == null) {
            throw new EWalletCountProjectNotExistException(
                    "对应计次物品countId: " + ewUserCount.getCountId() + " 不存在"
            );
        }
        String whereString = SqlUtil.sqlString(
                "ew_user_count.user_no=? and ew_user_count.count_id=?",
                ewUserCount.getUserNo(), ewUserCount.getCountId()
        );
        if (!this.ewUserCountDao.listBy(whereString, null).isEmpty()) {
            throw new EWalletUserCountAlreadyExistException(
                    "用户:" + ewUserCount.getUserNo() + "计次物品:" + ewUserCount.getCountId()
                            + "已存在对应计次账号, 不允许重复开户");
        }

        ewUserCount.setCheckCode("");
        this.ewUserCountDao.insert(ewUserCount);
        this.setCheckCode(ewUserCount);
        this.ewUserCountDao.update(ewUserCount);
    }

    /**
     * Insert the given {@link EwUserTime} to the database, update its checkCode
     */
    void insertUserTime(EwUserTime ewUserTime) throws EWalletUserNotExistException,
            EWalletTimeProjectNotExistException, EWalletUserTimeAlreadyExistException {
        if (this.ewUserDao.select(ewUserTime.getUserNo()) == null) {
            throw new EWalletUserNotExistException("对应用户名:" + ewUserTime.getUserNo() + " 不存在");
        }
        if (this.ewTimeProjectDao.select(ewUserTime.getTimeId()) == null) {
            throw new EWalletTimeProjectNotExistException(
                    "对应计时物品timeId: " + ewUserTime.getTimeId() + " 不存在"
            );
        }

        String whereString = SqlUtil.sqlString(
                "ew_user_time.user_no=? and ew_user_time.time_id=?",
                ewUserTime.getUserNo(), ewUserTime.getTimeId()
        );
        if (!this.ewUserTimeDao.listBy(whereString, null).isEmpty()) {
            throw new EWalletUserTimeAlreadyExistException(
                    "用户:" + ewUserTime.getUserNo() + "计时物品:" + ewUserTime.getTimeId()
                            + "已存在对应计时账号, 不允许重复开户");
        }

        ewUserTime.setCheckCode("");
        this.ewUserTimeDao.insert(ewUserTime);
        this.setCheckCode(ewUserTime);
        this.ewUserTimeDao.update(ewUserTime);
    }

    EwUser sqlSelectUserByUserNo(String userNo) {
        return this.ewUserDao.select(userNo);
    }

    /**
     * Retrieve a {@link EwUser } by its userNo, where userNo is the PK.
     *
     * @throws EWalletUserNotExistException  no such EwUser found
     * @throws EWalletInvalidObjectException user invalid
     * @throws EWalletInactiveUserException  user inactive
     */
    EwUser findUserByUserNo(String userNo)
            throws EWalletUserNotExistException, EWalletInvalidObjectException, EWalletInactiveUserException, EWalletUserExpireException {

        EwUser ewUser = this.ewUserDao.select(userNo);

        if (Objects.isNull(ewUser)) {
            throw new EWalletUserNotExistException("用户:".concat(userNo).concat(" 不存在"));
        }

        if (!this.isValid(ewUser)) {
            throw new EWalletInvalidObjectException("用户userNo:" + userNo + " 已失效，check code不匹配");
        }

        if (!ewUser.getIsActive()) {
            throw new EWalletInactiveUserException("用户userNo:" + userNo + "已注销");
        }
        if (!PayUtil.isValidNow(ewUser.getActiveSince(), ewUser.getExpireAt(), null)) {
            throw new EWalletUserExpireException("用户userNo:" + userNo + "已注销");
        }
        return ewUser;
    }

    /**
     * Check if the {@link EWallet} object is valid or not
     *
     * @param eWallet 需要检查的对象
     * @return true if valid; otherwise false
     */
    public boolean isValid(EWallet eWallet) {
        return EWallet.isValid(eWallet);
    }

    /**
     * Retrieve exact one {@link EwUserCount} by given userNo and countId.
     * The combination of the two parameter should uniquely define a {@link EwUserCount}
     */
    EwUserCount findExactOneUserCountByUserNoAndCountId(String userNo, int countId)
            throws EWalletListException, EWalletInvalidObjectException {
        String whereString = SqlUtil
                .sqlString("ew_user_count.user_no=? and count_id=?", userNo, countId);
        List<EwUserCount> userCounts = this.ewUserCountDao.listBy(whereString, null);

        if (userCounts.isEmpty()) {
            throw new EWalletListException("未找到匹配userNo:"
                    + userNo + " countId:" + countId + " 在EwUserCount表");
        } else if (userCounts.size() != 1) {
            throw new EWalletListException("存在不唯一userNo:"
                    + userNo + " countId:" + countId + " 在EwUserCount表");
        }

        EwUserCount ewUserCount = userCounts.get(0);

        if (!this.isValid(ewUserCount)) {
            throw new EWalletInvalidObjectException(
                    "计次用户id:" + ewUserCount.getId() + " 已失效，check code不匹配"
            );
        }

        return ewUserCount;
    }

    /**
     * Retrieve exact one {@link EwUserTime} by given userNo and timeId.
     * The combination of the two parameter should uniquely define a {@link EwUserTime}
     */
    EwUserTime findExactOneUserTimeByUserNoAndTimeId(String userNo, int timeId)
            throws EWalletListException, EWalletInvalidObjectException {
        String whereString = SqlUtil
                .sqlString("ew_user_time.user_no=? and time_id=?", userNo, timeId);
        List<EwUserTime> userTimes = this.ewUserTimeDao.listBy(whereString, null);

        if (userTimes.isEmpty()) {
            throw new EWalletListException("未找到匹配userNo:"
                    + userNo + " timeId:" + timeId + " 在EwUserTime表");
        } else if (userTimes.size() != 1) {
            throw new EWalletListException("存在不唯一userNo:"
                    + userNo + " timeId:" + timeId + " 在EwUserTime表");
        }

        EwUserTime ewUserTime = userTimes.get(0);

        if (!this.isValid(ewUserTime)) {
            throw new EWalletInvalidObjectException(
                    "计时用户id:" + ewUserTime.getId() + " 已失效，check code不匹配"
            );
        }
        return ewUserTime;
    }

    /**
     * Retrieve all {@link EwUserTime} with the given userNo.
     */
    List<EwUserTime> findUserTimesByUserNo(String userNo) {
        String whereString = SqlUtil.sqlString("ew_user_time.user_no=?", userNo);
        List<EwUserTime> list = this.ewUserTimeDao.listBy(whereString, null);
        if (!Objects.isNull(list)) {
            Iterator<EwUserTime> iterator = list.iterator();
            while (iterator.hasNext()) {
                EwUserTime item = iterator.next();
                if (!this.isValid(item)) {
                    iterator.remove();
                }
            }
        }
        return list;
    }

    /**
     * Retrieve all {@link EwUserCount} with the given userNo.
     */
    List<EwUserCount> findUserCountsByUserNo(String userNo) {
        String whereString = SqlUtil.sqlString("ew_user_count.user_no=?", userNo);
        List<EwUserCount> list = this.ewUserCountDao.listBy(whereString, null);
        if (!Objects.isNull(list)) {
            Iterator<EwUserCount> iterator = list.iterator();
            while (iterator.hasNext()) {
                EwUserCount item = iterator.next();
                if (!this.isValid(item)) {
                    iterator.remove();
                }
            }
        }
        return list;
    }

    /**
     * Retrieve the last (in time) bill entry with the given deviceNo.
     */
    EwBill findLastBillByDeviceNo(String deviceNo) {
        try {
            return SqlUtil.sqlQueryFirst(EwBill.class, false,
                    "SELECT * FROM ew_bill where no = (select max(no) from ew_bill where device_no = ?)",
                    deviceNo);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieve exact one {@link EwBill} by given tradeNo and deviceNo and shopId.
     * The combination of the three parameters should uniquely define a {@link EwBill}
     */
    EwBill findExactOneBillByTradeNoAndDeviceNoAndShopId(String tradeNo, String deviceNo,
                                                         String shopId)
            throws EWalletNoBillFoundException, EWalletDuplicateBillNumberException, EWalletInvalidObjectException, EWalletBillAlreadyCancelledException {
        String whereString = SqlUtil.sqlString(
                "trade_no=? and device_no=? and shop_id=? and link_no is null",
                tradeNo, deviceNo, shopId
        );
        List<EwBill> bills = this.ewBillDao.listBy(whereString, null);

        if (Objects.isNull(bills) || bills.isEmpty()) {
            throw new EWalletNoBillFoundException(
                    "未查询到相应订单.tradeNo: " + tradeNo + " deviceNo:" + deviceNo + " shopId:" + shopId
            );
        } else if (bills.size() != 1) {
            throw new EWalletDuplicateBillNumberException(
                    "存在不唯一订单关联tradeNo: " + tradeNo + " deviceNo:" + deviceNo + " shopId:" + shopId
                            + "billNos: " + bills.stream().map(EwBill::getNo)
                            .collect(Collectors.toList())
            );
        }

        EwBill ewBill = bills.get(0);
        if (!this.isValid(ewBill)) {
            throw new EWalletInvalidObjectException(
                    "账单id:" + ewBill.getNo() + " 已失效，check code不匹配"
            );
        }

        whereString = SqlUtil.sqlString("link_no=?", ewBill.getNo());
        List<EwBill> linkedBills = this.ewBillDao.listBy(whereString, null);
        if (!linkedBills.isEmpty()) {
            throw new EWalletBillAlreadyCancelledException(
                    "账单id:" + ewBill.getNo() + " 已被取消, 对应取消流水号:" +
                            linkedBills.stream().map(EwBill::getNo).collect(Collectors.toList())
            );
        }

        return ewBill;
    }

    /**
     * Retrieve the time project item with given id.
     */
    EwTimeProject findTimeProjectById(int id) throws EWalletTimeProjectNotExistException {
        EwTimeProject ewTimeProject = this.ewTimeProjectDao.select(id);
        if (Objects.isNull(ewTimeProject)) {
            throw new EWalletTimeProjectNotExistException("计时商品id:" + id + " 不存在");
        }

        return ewTimeProject;
    }

    /**
     * Retrieve the project list.
     */
    List<EwCountProject> listCountProject() {
        return this.ewCountProjectDao.listBy(null, "id");
    }

    List<EwCountProject> listCountProjectBy(String whereString, String orderString) {
        return this.ewCountProjectDao.listBy(whereString, orderString);
    }

    List<EwTimeProject> listTimeProject() {

        return this.ewTimeProjectDao.listBy(null, "id");
    }

    List<EwTimeProject> listTimeProjectBy(String whereString, String orderString) {
        return this.ewTimeProjectDao.listBy(whereString, orderString);
    }

    /**
     * Retrieve the count project item with given id.
     */
    EwCountProject findCountProjectById(int id) throws EWalletCountProjectNotExistException {
        EwCountProject ewCountProject = this.ewCountProjectDao.select(id);
        if (Objects.isNull(ewCountProject)) {
            throw new EWalletCountProjectNotExistException(
                    "计次商品id:" + id + " 不存在");
        }

        return ewCountProject;
    }

    /**
     * Query the total cost of the current day for a given userNo with the specified costType.
     */
    Integer queryDayCost(int projectId, int costType, String userNo) {

        String sql = "SELECT sum(amount) FROM ew_bill where is_charge=0 and canceled=0 and link_no is null and (dt between ? and ?) and user_no=? and project_id=? and cost_type=?";
        Date now = new Date();
        String dt0 = DateUtil.date2String(now, "yyyy-MM-dd") + " 00:00:00";
        String dt1 = DateUtil.date2String(now, "yyyy-MM-dd") + " 23:59:59.999";
        try {
            Integer amount = SqlUtil.sqlQueryInteger(false, sql, dt0, dt1, userNo, projectId, costType);
            if (amount == null) return 0;
            else return amount;
        } catch (Exception e) {
            return 0;
        }
        /*
        String whereString = SqlUtil.sqlString(
                "ew_bill.user_no=? and DATEDIFF(dt,NOW()) = 0 and cost_type=?",
                userNo, costType
        );
        List<EwBill> ewBills = this.ewBillDao.listBy(whereString, null)
                .stream()
                .filter(this::isValid)
                .collect(Collectors.toList());

        return ewBills.stream()
                .filter(b -> Objects.isNull(b.getLinkNo()))
                .mapToInt(EwBill::getAmount)
                .sum() -
                ewBills.stream()
                        .filter(b -> Objects.nonNull(b.getLinkNo()))
                        .mapToInt(EwBill::getAmount)
                        .sum();

         */
    }

    /**
     * update the given {@link EwUser}, update checkCode as well.
     */
    void updateEwUser(EwUser ewUser) {
        this.setCheckCode(ewUser);
        this.ewUserDao.update(ewUser);
    }

    /**
     * update the given {@link EwUserCount}. update checkCode as well
     */
    void updateEwUserCount(EwUserCount ewUserCount) {
        this.setCheckCode(ewUserCount);
        this.ewUserCountDao.update(ewUserCount);
    }

    /**
     * update the given {@link EwUserTime}. update checkCode as well
     */
    void updateEwUserTime(EwUserTime ewUserTime) {
        this.setCheckCode(ewUserTime);
        this.ewUserTimeDao.update(ewUserTime);
    }

    void checkUserStatus(String userNo) throws EWalletUserNotExistException,
            EWalletInvalidObjectException, EWalletInactiveUserException,
            EWalletNotActiveYetException, EWalletUserExpireException {
        EwUser ewUser = this.ewUserDao.select(userNo);

        if (Objects.isNull(ewUser)) {
            throw new EWalletUserNotExistException("用户不存在.userNo:" + userNo);
        }

        if (!this.isValid(ewUser)) {
            throw new EWalletInvalidObjectException("用户已失效.userNo:" + userNo);
        }

        if (!ewUser.getIsActive()) {
            throw new EWalletInactiveUserException("用户已冻结.userNo:" + userNo);
        }

        if (Objects.nonNull(ewUser.getActiveSince()) &&
                ewUser.getActiveSince().after(Date.from(Instant.now()))) {
            throw new EWalletNotActiveYetException("用户还未开启.userNo:" + userNo);
        }

        if (Objects.nonNull(ewUser.getExpireAt()) &&
                ewUser.getExpireAt().before(Date.from(Instant.now()))) {
            throw new EWalletUserExpireException("用户已过期.userNo:" + userNo);
        }
    }

    public Map<String, Object> queryBill(String userNo, Date dt0, Date dt1, Integer offset, Integer limit) throws PayException {
        if (Objects.isNull(userNo)) throw new PayException(PayException.DataFieldRequired, "userNo");
        else if (Objects.isNull(dt0)) throw new PayException(PayException.DataFieldRequired, "dt0");
        else if (Objects.isNull(dt1)) throw new PayException(PayException.DataFieldRequired, "dt1");
        try {

            String whereString = SqlUtil.sqlString("is_charge=0 and user_no=? and dt>=? and dt<?", userNo, dt0, dt1);
            Integer sum = SqlUtil.sqlQueryInteger("select sum(amount * project_price) from ew_bill where " + whereString);
            if (offset == null || offset <= 0) offset = 0;
            if (limit != null && limit > 0) {
                if (offset == null || offset <= 0) offset = 0;
                PageHelper.startPage(offset / limit + 1, limit);
            }
            List<EwBill> data = ewBillDao.listBy(whereString, "dt desc");
            PageInfo<EwBill> pi = new PageInfo<>(data);
            Map<String, Object> obj = new HashMap<>();
            obj.put("total", pi.getTotal());
            obj.put("summation", sum);
            obj.put("fixedScroll", true);
            obj.put("data", pi.getList());
            obj.put("page", pi.getPageNum());
            obj.put("pages", pi.getPages());
            obj.put("pageSize", pi.getPageSize());
            obj.put("success", true);
            obj.put("message", null);
            return obj;
        } catch (Exception e) {
            throw new PayException(e, "SQL");
        }
    }
}
