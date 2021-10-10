package top.iotequ.ewallet.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.iotequ.ewallet.pojo.*;
import top.iotequ.ewallet.utility.dto.UserCountRequestDto;
import top.iotequ.ewallet.utility.dto.UserRequestDto;
import top.iotequ.ewallet.utility.dto.UserTimeRequestDto;
import top.iotequ.ewallet.utility.entity.Transaction;
import top.iotequ.ewallet.utility.exception.*;
import top.iotequ.framework.util.EntityUtil;
import top.iotequ.framework.util.Util;
import top.iotequ.pay.exception.PayException;
import top.iotequ.pay.utility.EncryptUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Private Data Agent that communicates with Data layer.
 * Created by ao on 2019-07-24
 */
@Component
class EwDataAgent {

    private static final int NO_LIMIT = 0;
    private static final int NEGATIVE = -1;
    @Autowired
    private EwDataRepo ewDataRepo;

    @Autowired
    private BillNumberGenerator billNumberGenerator;

    /**
     * Calculate bonus point for a given transaction.
     */
    int calculateEarningBonusPoint(String userNo, Transaction transaction)
            throws EWalletCountProjectNotExistException, EWalletTimeProjectNotExistException {

        int gainedBonus = 0;
        switch (transaction.getType()) {

            case MONEY_COST:
                //todo: Assuming one point per cost
                gainedBonus = transaction.getValue();
                break;
            case COUNT_COST:
                EwCountProject ewCountProject = this.ewDataRepo.findCountProjectById(
                        transaction.getItemId()
                );
                int countDivision = transaction.getValue() / ewCountProject.getBaseValue();
                gainedBonus = (int) (countDivision * ewCountProject.getBonusPoint());
                break;
            case TIME_COST:
                EwTimeProject ewTimeProject = this.ewDataRepo.findTimeProjectById(
                        transaction.getItemId()
                );
                int timeDivision = transaction.getValue() / ewTimeProject.getBaseValue();
                gainedBonus = (int) (timeDivision * ewTimeProject.getBonusPoint());
                break;
        }

        return gainedBonus;
    }

    /**
     * update the bonus point for the given user based on the transaction
     *
     * @param transaction, depending on different costTypes, the cost varies:
     *                     MONEY_COST: the amount of money cost
     *                     COUNT_COST: the number of counts cost
     *                     TIME_COST: the number of time units cost
     * @return gained bonus point through this transaction
     */
    int updateBonusPoint(String userNo, Transaction transaction)
            throws EWalletInvalidObjectException, EWalletUserNotExistException, EWalletTimeProjectNotExistException, EWalletCountProjectNotExistException, EWalletInactiveUserException, EWalletUserExpireException {

        EwUser ewUser = this.ewDataRepo.findUserByUserNo(userNo);
        int gainedBonus = this.calculateEarningBonusPoint(userNo, transaction);
        ewUser.setBonusPoint(ewUser.getBonusPoint() + gainedBonus);
        this.ewDataRepo.updateEwUser(ewUser);

        return gainedBonus;
    }

    /**
     * Generate the Billing number when being invoked.
     * The generated number is unique.
     */
    private String generateBillNo() {
        return this.billNumberGenerator.generateNextBillNumber();
    }

    /**
     * Execute one cost operation depending on the given costType.
     *
     * @return Bill number if success
     */
    String doCost(String userNo, Transaction transaction)
            throws EWalletInvalidObjectException, EWalletUserNotExistException, EWalletInsufficientBalanceException, EWalletListException, EWalletExceedCostLimitException, EWalletExceedDayLimitException, EWalletInactiveUserException, EWalletUserExpireException {

        EwUser ewUser = this.ewDataRepo.findUserByUserNo(userNo);

        int costValue = transaction.getValue();

        switch (transaction.getType()) {

            case MONEY_COST:

                int amountMoney = ewUser.getAmountMoney();
                if (amountMoney < costValue) {
                    throw new EWalletInsufficientBalanceException("用户:" + userNo + "账户余额不足");
                }
                if (ewUser.getCostLimit() != NO_LIMIT && costValue > ewUser.getCostLimit()) {
                    throw new EWalletExceedCostLimitException(
                            "消费交易额:" + costValue + " 超过单笔交易限额:" + ewUser.getCostLimit()
                    );
                }
                if (ewUser.getDayLimit() != NO_LIMIT) {
                    int todayAmountTotal = this.ewDataRepo.queryDayCost(EWalletUtils.WALLET_PROJECT_ID,
                            EWalletUtils.MONEY_COST, userNo
                    );
                    if (costValue + todayAmountTotal > ewUser.getDayLimit()) {
                        throw new EWalletExceedDayLimitException(
                                "消费交易额:" + costValue + " 今日已进行交易总额: " + todayAmountTotal
                                        + " 超过今日交易限额:" + ewUser.getDayLimit()

                        );
                    }
                }
                ewUser.setAmountMoney(amountMoney - costValue);
                this.ewDataRepo.updateEwUser(ewUser);
                break;
            case COUNT_COST:
                EwUserCount ewUserCount = this.ewDataRepo.findExactOneUserCountByUserNoAndCountId(
                        userNo,
                        transaction.getItemId()
                );

                int amountCount = ewUserCount.getAmountCount();
                if (amountCount < costValue) {
                    throw new EWalletInsufficientBalanceException("用户:" + userNo + "计次账户余额不足");
                }
                if (ewUserCount.getCostLimit() != NO_LIMIT && costValue > ewUserCount
                        .getCostLimit()) {
                    throw new EWalletExceedCostLimitException(
                            "计次交易次数:" + costValue + " 超过单笔交易次数限制:" + ewUserCount.getCostLimit()
                    );
                }
                if (ewUserCount.getDayLimit() != NO_LIMIT) {
                    int todayCountTotal = this.ewDataRepo.queryDayCost(transaction.getItemId(),
                            EWalletUtils.COUNT_COST, userNo
                    );
                    if (costValue + todayCountTotal > ewUserCount.getDayLimit()) {
                        throw new EWalletExceedDayLimitException(
                                "计次消费次数:" + costValue + " 今日已进行交易次数总数: " + todayCountTotal
                                        + " 超过今日交易次数限制:" + ewUserCount.getDayLimit()

                        );
                    }
                }

                ewUserCount.setAmountCount(amountCount - costValue);
                this.ewDataRepo.updateEwUserCount(ewUserCount);
                break;
            case TIME_COST:
                EwUserTime ewUserTime = this.ewDataRepo.findExactOneUserTimeByUserNoAndTimeId(
                        userNo,
                        transaction.getItemId()
                );

                int amountTime = ewUserTime.getAmountTime();
                if (amountTime < costValue) {
                    throw new EWalletInsufficientBalanceException("用户:" + userNo + "计时账户余额不足");
                }
                if (ewUserTime.getCostLimit() != NO_LIMIT && costValue > ewUserTime
                        .getCostLimit()) {
                    throw new EWalletExceedCostLimitException(
                            "计时交易时长:" + costValue + " 超过单次时长限制:" + ewUserTime.getCostLimit()
                    );
                }
                if (ewUserTime.getDayLimit() != NO_LIMIT) {
                    int todayTimeTotal = this.ewDataRepo.queryDayCost(transaction.getItemId(),
                            EWalletUtils.TIME_COST, userNo
                    );
                    if (costValue + todayTimeTotal > ewUserTime.getDayLimit()) {
                        throw new EWalletExceedDayLimitException(
                                "计时消费时长:" + costValue + " 今日已进行交易时长总数: " + todayTimeTotal
                                        + " 超过今日交易时长限制:" + ewUserTime.getDayLimit()
                        );
                    }
                }

                ewUserTime.setAmountTime(amountTime - costValue);
                this.ewDataRepo.updateEwUserTime(ewUserTime);
                break;
        }

        return this.generateBillNo();
    }

    /**
     * Execute one charge operation depending on the given costType
     */
    String doValueCharge(String userNo, Transaction transaction)
            throws EWalletListException, EWalletInvalidObjectException,
            EWalletUserNotExistException, EWalletInactiveUserException, EWalletUserExpireException {

        EwUser ewUser = this.ewDataRepo.findUserByUserNo(userNo);

        switch (transaction.getType()) {

            case MONEY_COST:
                ewUser.setAmountMoney(ewUser.getAmountMoney() + transaction.getValue());
                this.ewDataRepo.updateEwUser(ewUser);
                break;
            case COUNT_COST:
                EwUserCount ewUserCount = this.ewDataRepo.findExactOneUserCountByUserNoAndCountId(
                        userNo,
                        transaction.getItemId()
                );

                ewUserCount.setAmountCount(ewUserCount.getAmountCount() + transaction.getValue());
                this.ewDataRepo.updateEwUserCount(ewUserCount);
                break;
            case TIME_COST:
                EwUserTime ewUserTime = this.ewDataRepo.findExactOneUserTimeByUserNoAndTimeId(
                        userNo,
                        transaction.getItemId()
                );

                ewUserTime.setAmountTime(ewUserTime.getAmountTime() + transaction.getValue());
                this.ewDataRepo.updateEwUserTime(ewUserTime);
                break;
        }

        return this.generateBillNo();
    }

    /**
     * Cancel the given bill transaction.
     */
    EwBill doCancel(EwBill ewBill)
            throws EWalletOperationForbiddenException, EWalletInvalidObjectException, EWalletUserNotExistException, EWalletListException, EWalletInactiveUserException, EWalletUserExpireException {
        EwBill cancelBill = EntityUtil.entityCopyFrom(EwBill.class, ewBill);
        if (Objects.nonNull(ewBill.getLinkNo())) {
            throw new EWalletOperationForbiddenException(
                    "该账单流水是取消类型，操作不允许. billNo:" + ewBill.getNo()
            );
        }

        EwUser ewUser = this.ewDataRepo.findUserByUserNo(ewBill.getUserNo());

        if (!ewBill.getIsCharge() && ewUser.getBonusPoint() < ewBill.getBonus()) {
            throw new EWalletOperationForbiddenException("用户积分不足，禁止此次取消消费操作");
        }

        cancelBill.setBonusBefore(ewUser.getBonusPoint());
        int amountBefore;
        switch (ewBill.getCostType()) {
            case EWalletUtils.MONEY_COST:
                amountBefore = ewUser.getAmountMoney();
                if (ewBill.getIsCharge()) {
                    int newBalance = amountBefore - ewBill.getAmount();
                    if (newBalance < 0) {
                        throw new EWalletOperationForbiddenException("用户账户钱包余额不足，禁止此次取消充值操作");
                    }
                    ewUser.setAmountMoney(newBalance);
                } else {
                    ewUser.setAmountMoney(amountBefore + ewBill.getAmount());
                }
                break;
            case EWalletUtils.COUNT_COST:
                EwUserCount ewUserCount = this.ewDataRepo.findExactOneUserCountByUserNoAndCountId(
                        ewUser.getUserNo(),
                        ewBill.getProjectId()
                );
                amountBefore = ewUserCount.getAmountCount();
                if (ewBill.getIsCharge()) {
                    int newBalance = amountBefore - ewBill.getAmount();
                    if (newBalance < 0) {
                        throw new EWalletOperationForbiddenException("用户计次账户余额不足，禁止此次取消充值操作");
                    }
                    ewUserCount.setAmountCount(newBalance);
                } else {
                    ewUserCount.setAmountCount(amountBefore + ewBill.getAmount());
                }

                this.ewDataRepo.updateEwUserCount(ewUserCount);
                break;
            case EWalletUtils.TIME_COST:
                EwUserTime ewUserTime = this.ewDataRepo.findExactOneUserTimeByUserNoAndTimeId(
                        ewUser.getUserNo(),
                        ewBill.getProjectId()
                );
                amountBefore = ewUserTime.getAmountTime();
                if (ewBill.getIsCharge()) {
                    int newBalance = amountBefore - ewBill.getAmount();
                    if (newBalance < 0) {
                        throw new EWalletOperationForbiddenException("用户时间账户余额不足，禁止此次取消充值操作");
                    }
                    ewUserTime.setAmountTime(newBalance);
                } else {
                    ewUserTime.setAmountTime(amountBefore + ewBill.getAmount());
                }
                this.ewDataRepo.updateEwUserTime(ewUserTime);
                break;
            default:
                throw new EWalletInvalidObjectException(
                        "订单异常，交易类型不识别.billNo:" + ewBill.getNo() + " costType:" + ewBill
                                .getCostType()
                );
        }
        cancelBill.setAmountBefore(amountBefore);
        ewUser.setBonusPoint(ewUser.getBonusPoint() - ewBill.getBonus());
        this.ewDataRepo.updateEwUser(ewUser);

        cancelBill.setLinkNo(ewBill.getNo());
        cancelBill.setNo(this.generateBillNo());
        cancelBill.setAmount(NEGATIVE * ewBill.getAmount());
        cancelBill.setBonus(NEGATIVE * ewBill.getBonus());

        return cancelBill;
    }

    /**
     * Create a new {@link EwUser} instance in database.
     *
     * @return created {@link EwUser}
     */
    EwUser createUserAccount(UserRequestDto userRequestDto)
            throws EWalletDuplicateUserNoException {

        EwUser ewUser = new EwUser();

        ewUser.setUserNo(userRequestDto.getUserNo());
        ewUser.setName(userRequestDto.getName());
        ewUser.setGender(userRequestDto.getGender());
        ewUser.setBirthDate(userRequestDto.getBirthDate());
        ewUser.setMobilePhone(userRequestDto.getMobilePhone());
        ewUser.setEmail(userRequestDto.getEmail());
        ewUser.setWechatOpenid(userRequestDto.getWeChatId());
        ewUser.setMemberGroup(userRequestDto.getMemberGroup());

        ewUser.setAmountMoney(0);
        ewUser.setBonusPoint(0);
        ewUser.setCostLimit(userRequestDto.getCostLimit());
        ewUser.setDayLimit(userRequestDto.getDayLimit());

        ewUser.setIdType(userRequestDto.getIdType());
        ewUser.setIdNo(userRequestDto.getIdNo());
        ewUser.setIsActive(true);

        ewUser.setActiveSince(userRequestDto.getActiveSince());
        ewUser.setExpireAt(userRequestDto.getExpireAt());

        this.ewDataRepo.insertUser(ewUser);

        return ewUser;
    }

    /**
     * Create a new {@link EwUserCount} instance in database.
     * The corresponding {@link EwUser} must exist first.
     *
     * @return created {@link EwUserCount}
     */
    EwUserCount createUserCountAccount(UserCountRequestDto userCountRequestDto)
            throws EWalletUserNotExistException, EWalletCountProjectNotExistException,
            EWalletUserCountAlreadyExistException {

        EwUserCount ewUserCount = new EwUserCount();

        ewUserCount.setCountId(userCountRequestDto.getCountId());
        ewUserCount.setAmountCount(userCountRequestDto.getAmountCount());
        ewUserCount.setCostLimit(userCountRequestDto.getCostLimit());
        ewUserCount.setDayLimit(userCountRequestDto.getDayLimit());
        ewUserCount.setUserNo(userCountRequestDto.getUserNo());

        this.ewDataRepo.insertUserCount(ewUserCount);

        return ewUserCount;
    }

    /**
     * Create a new {@link EwUserTime} instance in database.
     * The corresponding {@link EwUser} must exist.
     *
     * @return created {@link EwUserTime}
     */
    EwUserTime createUserTimeAccount(UserTimeRequestDto userTimeRequestDto)
            throws EWalletUserNotExistException, EWalletTimeProjectNotExistException,
            EWalletUserTimeAlreadyExistException {

        EwUserTime ewUserTime = new EwUserTime();

        ewUserTime.setTimeId(userTimeRequestDto.getTimeId());
        ewUserTime.setAmountTime(userTimeRequestDto.getAmountTime());
        ewUserTime.setCostLimit(userTimeRequestDto.getCostLimit());
        ewUserTime.setDayLimit(userTimeRequestDto.getDayLimit());
        ewUserTime.setUserNo(userTimeRequestDto.getUserNo());

        this.ewDataRepo.insertUserTime(ewUserTime);

        return ewUserTime;
    }

    public Map<String, Object> queryBill(String userNo, Date dt0, Date dt1, Integer offset, Integer limit) throws PayException {
        return ewDataRepo.queryBill(userNo,dt0,dt1,offset,limit);
    }
}
