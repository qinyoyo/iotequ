package top.iotequ.ewallet.utility;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import top.iotequ.ewallet.pojo.*;
import top.iotequ.ewallet.utility.dto.*;
import top.iotequ.ewallet.utility.entity.Transaction;
import top.iotequ.ewallet.utility.entity.TransactionType;
import top.iotequ.ewallet.utility.exception.*;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.Util;
import top.iotequ.pay.dto.AccountType;
import top.iotequ.pay.dto.request.LogoutRequest.StatInfo;
import top.iotequ.pay.dto.response.UpdateInfoResponse.TradeAccount;
import top.iotequ.pay.exception.PayException;
import top.iotequ.pay.utility.PayUtil;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Interface of E-wallet.
 * Created by ao on 2019-07-24
 */
@Component
public class EWalletUtils implements ApplicationRunner {

    public static final int MONEY_COST = AccountType.WALLET;
    public static final int COUNT_COST = AccountType.COUNT_ACCOUNT;
    public static final int TIME_COST = AccountType.TIME_ACCOUNT;

    public static final int WALLET_PROJECT_ID = 1;

    @Autowired
    Environment environment;
    @Autowired
    private EwDataAgent ewDataAgent;
    @Autowired
    private EwDataRepo ewDataRepo;

    private boolean useWallet = true;
    private boolean useCountWallet = true;
    private boolean useTimeWallet = true;

    public boolean isUseWallet() {
        return useWallet;
    }

    public boolean isUseCountWallet() {
        return useCountWallet;
    }

    public boolean isUseTimeWallet() {
        return useTimeWallet;
    }

    /**
     * 1.3 账户类别查询接口
     *
     * @param version          版本，保留
     * @param deviceNo         设备号，保留
     * @param ewalletActive    是否支持现金钱包
     * @param countProjectList 需要查询的计次项目列表
     * @param timeProjectList  需要查询的计时项目列表
     * @return 账户类别列表
     */
    public List<TradeAccount> queryTradeAccountInfo(String version, String deviceNo, boolean ewalletActive, String countProjectList, String timeProjectList) {
        List<TradeAccount> list = new ArrayList<>();
        if (useWallet && ewalletActive) {
            list.add(new TradeAccount("现金钱包", EWalletUtils.WALLET_PROJECT_ID, AccountType.WALLET, 1, null, null));
        }
        if (useCountWallet) {
            if (Objects.nonNull(countProjectList) && !countProjectList.isEmpty()) {
                List<EwCountProject> countProjects = ewDataRepo.listCountProjectBy("id in (" + countProjectList + ")", "id");
                if (!Objects.isNull(countProjects)) {
                    for (EwCountProject cp : countProjects) {
                        list.add(new TradeAccount(cp.getName(), cp.getId(), AccountType.COUNT_ACCOUNT,
                                cp.getBasePrice(), intTime(cp.getStartTime()),
                                intTime(cp.getEndTime())));
                    }
                }
            }
        }
        if (useTimeWallet) {
            if (Objects.nonNull(timeProjectList) && !timeProjectList.isEmpty()) {
                List<EwTimeProject> timeProjects = ewDataRepo.listTimeProjectBy("id in (" + timeProjectList + ")", "id");
                if (!Objects.isNull(timeProjects)) {
                    for (EwTimeProject tp : timeProjects) {
                        list.add(new TradeAccount(tp.getName(), tp.getId(), AccountType.TIME_ACCOUNT,
                                tp.getBasePrice(), intTime(tp.getStartTime()),
                                intTime(tp.getEndTime())));
                    }
                }
            }
        }
        return list;
    }

    private Integer intTime(Date dt) {
        if (Objects.isNull(dt)) {
            return null;
        }
        return (Integer.parseInt(PayUtil.date2String(dt, "HH"))) * 100 + (Integer
                .parseInt(PayUtil.date2String(dt, "mm")));
    }

    public EwUser sqlSelectUserInterface(@NonNull String userNo) {
        return this.ewDataRepo.sqlSelectUserByUserNo(userNo);
    }

    /**
     * 1.4 余额查询接口
     *
     * @param userNo 用户号
     * @return 查询成功返回 {@link BalanceResponseDto}.
     * 对于其包含的计次账户与计时账户, 仅返回checkCode匹配的对象。对于不匹配的对象不做抛出异常处理。
     * 计次账户与计时账户为non-null list.但可以为empty list.
     * @throws EWalletInvalidObjectException 用户已失效
     * @throws EWalletUserNotExistException  用户不存在
     * @throws EWalletInactiveUserException  用户已注销
     * @throws EWalletUserExpireException    用户已失效
     * @throws EWalletNotActiveYetException  用户未到激活时间
     */

    public BalanceResponseDto queryBalanceInterface(@NonNull String userNo)
            throws EWalletUserNotExistException, EWalletInvalidObjectException, EWalletInactiveUserException,
            EWalletUserExpireException, EWalletNotActiveYetException {

        EwUser ewUser = this.ewDataRepo.findUserByUserNo(userNo);
        this.ewDataRepo.checkUserStatus(userNo);

        BalanceResponseDto balanceResponseDto = new BalanceResponseDto();

        balanceResponseDto.setUserNo(userNo);
        balanceResponseDto.setAmount(ewUser.getAmountMoney());
        balanceResponseDto.setBonus(ewUser.getBonusPoint());
        balanceResponseDto.setTimes(
                this.ewDataRepo.findUserTimesByUserNo(userNo).stream()
                        .map(t -> new TimeResponseDto(
                                        t.getTimeId(),
                                        t.getTimeIdEwTimeProjectName(),
                                        t.getAmountTime()
                                )
                        ).collect(Collectors.toList())
        );
        balanceResponseDto.setCounts(
                this.ewDataRepo.findUserCountsByUserNo(userNo).stream()
                        .map(c -> new CountResponseDto(
                                        c.getCountId(),
                                        c.getCountIdEwCountProjectName(),
                                        c.getAmountCount()
                                )
                        ).collect(Collectors.toList())
        );

        return balanceResponseDto;
    }

    /**
     * 1.5 消费操作接口
     *
     * @param transactionRequestDto 注意该传入对象的所有fields皆@NonNull
     * @return {@link TransactionResponseDto}. 包含此次消费的账单流水号(EwBill.no)与此次消费获得的积分数
     * @throws EWalletIllegalArgumentException      传入对象包含不合法内容，消费类型不被识别
     * @throws EWalletInvalidObjectException        用户已失效
     * @throws EWalletCountProjectNotExistException 对应计次物品不存在
     * @throws EWalletUserNotExistException         用户不存在
     * @throws EWalletInsufficientBalanceException  用户余额不存
     * @throws EWalletTimeProjectNotExistException  对应计时商品不存在
     * @throws EWalletListException                 数据库访问list操作失败
     * @throws EWalletDuplicateBillNumberException  创建账单失败，试图创建相同账单流水号的账单
     * @throws EWalletExceedDayLimitException       消费超出日消费限额
     * @throws EWalletExceedCostLimitException      消费超过单次消费限额
     * @throws EWalletInactiveUserException         用户已注销
     * @throws EWalletUserExpireException           用户已失效
     * @throws EWalletNotActiveYetException         用户未到激活时间
     */
    public TransactionResponseDto expendInterface(TransactionRequestDto transactionRequestDto)
            throws EWalletIllegalArgumentException, EWalletInvalidObjectException,
            EWalletCountProjectNotExistException, EWalletUserNotExistException,
            EWalletInsufficientBalanceException, EWalletTimeProjectNotExistException,
            EWalletListException, EWalletDuplicateBillNumberException,
            EWalletExceedDayLimitException, EWalletExceedCostLimitException, EWalletInactiveUserException,
            EWalletUserExpireException, EWalletNotActiveYetException {
        this.ewDataRepo.checkUserStatus(transactionRequestDto.getUserNo());
        Transaction transaction = this.generateTransaction(
                transactionRequestDto.getCostType(),
                transactionRequestDto.getAmount(),
                transactionRequestDto.getProjectId()
        );
        if (transactionRequestDto.getCostType() == MONEY_COST)
            transactionRequestDto.setProjectId(EWalletUtils.WALLET_PROJECT_ID);
        String billNo = this.ewDataAgent.doCost(transactionRequestDto.getUserNo(), transaction);
        EwBill ewBill = this.generateBill(transactionRequestDto, transaction, billNo, false);
        this.ewDataRepo.insertBill(ewBill);
        int gainedBonus = this.ewDataAgent.updateBonusPoint(
                transactionRequestDto.getUserNo(),
                transaction
        );

        return new TransactionResponseDto(billNo, gainedBonus);
    }

    private Transaction generateTransaction(int costType, int amount, int projectId)
            throws EWalletIllegalArgumentException {
        if (amount <= 0) throw new EWalletIllegalArgumentException("交易金额必须大于0, amount:" + amount);
        Transaction transaction;
        switch (costType) {
            case MONEY_COST:
                transaction = new Transaction(amount);
                break;
            case COUNT_COST:
                try {
                    ewDataRepo.findCountProjectById(projectId);
                } catch (EWalletCountProjectNotExistException e) {
                    throw new EWalletIllegalArgumentException(e.getMessage());
                }
                transaction = new Transaction(TransactionType.COUNT_COST, amount, projectId);
                break;
            case TIME_COST:
                try {
                    ewDataRepo.findTimeProjectById(projectId);
                } catch (EWalletTimeProjectNotExistException e) {
                    throw new EWalletIllegalArgumentException(e.getMessage());
                }
                transaction = new Transaction(TransactionType.TIME_COST, amount, projectId);
                break;
            default:
                throw new EWalletIllegalArgumentException("costType参数不合法, costType:" + costType);
        }

        return transaction;
    }

    private EwBill generateBill(TransactionRequestDto dto, Transaction transaction, String billNo,
                                boolean isCharge)
            throws EWalletInvalidObjectException, EWalletUserNotExistException, EWalletListException,
            EWalletTimeProjectNotExistException, EWalletCountProjectNotExistException, EWalletInactiveUserException, EWalletUserExpireException {

        EwUser ewUser = this.ewDataRepo.findUserByUserNo(dto.getUserNo());
        EwBill ewBill = new EwBill();

        ewBill.setNo(billNo);
        ewBill.setDt(Date.from(Instant.now()));
        ewBill.setIsCharge(isCharge);
        ewBill.setOperationType(dto.getOperationType());
        ewBill.setUserNo(dto.getUserNo());
        ewBill.setCostType(dto.getCostType());
        ewBill.setProjectId(dto.getProjectId());
        ewBill.setAmount(dto.getAmount());
        ewBill.setBatchNo(dto.getBatchNo());
        ewBill.setLoginId(dto.getLoginId());
        ewBill.setCanceled(false);
        switch (transaction.getType()) {

            case MONEY_COST:
                if (isCharge) {
                    ewBill.setAmountBefore(ewUser.getAmountMoney() - transaction.getValue());
                } else {
                    ewBill.setAmountBefore(ewUser.getAmountMoney() + transaction.getValue());
                }
                ewBill.setProjectName("钱包");
                ewBill.setProjectPrice(1);
                break;
            case COUNT_COST:
                EwUserCount ewUserCount = this.ewDataRepo.findExactOneUserCountByUserNoAndCountId(
                        dto.getUserNo(),
                        dto.getProjectId()
                );
                if (isCharge) {
                    ewBill.setAmountBefore(ewUserCount.getAmountCount() - transaction.getValue());
                } else {
                    ewBill.setAmountBefore(ewUserCount.getAmountCount() + transaction.getValue());
                }
                ewBill.setProjectName(ewUserCount.getCountIdEwCountProjectName());
                ewBill.setProjectPrice(ewUserCount.getCountIdEwCountProjectBasePrice());
                break;
            case TIME_COST:
                EwUserTime ewUserTime = this.ewDataRepo.findExactOneUserTimeByUserNoAndTimeId(
                        dto.getUserNo(),
                        dto.getProjectId()
                );
                if (isCharge) {
                    ewBill.setAmountBefore(ewUserTime.getAmountTime() - transaction.getValue());
                } else {
                    ewBill.setAmountBefore(ewUserTime.getAmountTime() + transaction.getValue());
                }
                ewBill.setProjectName(ewUserTime.getTimeIdEwTimeProjectName());
                ewBill.setProjectPrice(ewUserTime.getTimeIdEwTimeProjectBasePrice());
                break;
        }

        if (!isCharge) {
            ewBill.setBonus(
                    this.ewDataAgent.calculateEarningBonusPoint(dto.getUserNo(), transaction)
            );
        } else {
            ewBill.setBonus(0);
        }
        ewBill.setBonusBefore(ewUser.getBonusPoint());
        ewBill.setDeviceNo(dto.getDeviceNo());
        ewBill.setShopId(dto.getShopId());
        ewBill.setDeviceStream(dto.getDeviceStream());
        ewBill.setDeviceDt(dto.getDeviceDate());
        ewBill.setTradeNo(dto.getTradeNo());
        ewBill.setOperatorNo(dto.getOperatorNo());
        ewBill.setLinkNo(null);

        return ewBill;
    }

    /**
     * 1.6	末笔交易结果查询
     *
     * @param deviceNo 设备号
     * @return 账单流水对象
     * @throws EWalletNoBillFoundException 未能查询到任何账单
     */
    public EwBill queryLastTradeInterface(String deviceNo) throws EWalletNoBillFoundException {

        EwBill ewBill = this.ewDataRepo.findLastBillByDeviceNo(deviceNo);

        if (Objects.isNull(ewBill)) {
            throw new EWalletNoBillFoundException(
                    "deviceNo:" + deviceNo + "没有匹配账单记录"
            );
        }

        return ewBill;
    }

    /**
     * 1.7	消费撤销
     *
     * @param tradeNo0   需要撤销的交易号,注意此不为账单流水号billNo
     * @param deviceNo   设备号
     * @param shopId     商户号
     * @param tradeNo    撤销交易的交易号
     * @param deviceTime 设备时间
     * @param batchNo    批次号
     * @param loginId    登录号
     * @return 账单
     * @throws EWalletNoBillFoundException          未能查询到对应账单根据提供信息
     * @throws EWalletDuplicateBillNumberException  查询到不唯一账单根据提供信息
     * @throws EWalletOperationForbiddenException   账单流水类型不允许此次取消操作
     * @throws EWalletListException                 数据库访问list操作失败
     * @throws EWalletUserNotExistException         账单流水对应用户不存在
     * @throws EWalletInvalidObjectException        账单流水对应用户失效
     * @throws EWalletBillAlreadyCancelledException 重复取消交易
     * @throws EWalletInactiveUserException         用户已注销
     * @throws EWalletUserExpireException           用户已失效
     * @throws EWalletNotActiveYetException         用户未到激活时间
     */
    public EwBill tradeCancelInterface(String tradeNo0, String deviceNo, String shopId,
                                       String tradeNo, Date deviceTime, String batchNo, Integer loginId)
            throws EWalletNoBillFoundException, EWalletListException,
            EWalletUserNotExistException, EWalletInvalidObjectException,
            EWalletBillAlreadyCancelledException, EWalletDuplicateBillNumberException,
            EWalletOperationForbiddenException, EWalletInactiveUserException, EWalletUserExpireException,
            EWalletNotActiveYetException {

        EwBill billToCancel = this.ewDataRepo.findExactOneBillByTradeNoAndDeviceNoAndShopId(
                tradeNo0,
                deviceNo,
                shopId
        );

        this.ewDataRepo.checkUserStatus(billToCancel.getUserNo());
        EwBill cancelBill = this.ewDataAgent.doCancel(billToCancel);
        cancelBill.setTradeNo(tradeNo);
        cancelBill.setDeviceDt(deviceTime);
        cancelBill.setBatchNo(batchNo);
        cancelBill.setLoginId(loginId);
        this.ewDataRepo.insertBill(cancelBill);
        billToCancel.setCanceled(true);
        this.ewDataRepo.updateBill(billToCancel);
        return cancelBill;
    }

    /**
     * 1.8 充值操作接口
     *
     * @param transactionRequestDto 注意该传入对象的所有fields皆@NonNull
     * @return {@link TransactionResponseDto}. 包含此次消费的账单流水号(EwBill.no)与此次消费获得的积分数(为 0)
     * @throws EWalletIllegalArgumentException      传入对象包含不合法内容，消费类型不被识别
     * @throws EWalletInvalidObjectException        用户已失效
     * @throws EWalletCountProjectNotExistException 对应计次物品不存在
     * @throws EWalletUserNotExistException         用户不存在
     * @throws EWalletTimeProjectNotExistException  对应计时商品不存在
     * @throws EWalletListException                 数据库访问list操作失败
     * @throws EWalletDuplicateBillNumberException  创建账单失败，试图创建相同账单流水号的账单
     * @throws EWalletInactiveUserException         用户已注销
     * @throws EWalletUserExpireException           用户已失效
     * @throws EWalletNotActiveYetException         用户未到激活时间
     */
    public TransactionResponseDto chargeInterface(TransactionRequestDto transactionRequestDto)
            throws EWalletIllegalArgumentException, EWalletListException, EWalletUserNotExistException,
            EWalletInvalidObjectException, EWalletCountProjectNotExistException, EWalletTimeProjectNotExistException,
            EWalletDuplicateBillNumberException, EWalletInactiveUserException,
            EWalletUserExpireException, EWalletNotActiveYetException {
        this.ewDataRepo.checkUserStatus(transactionRequestDto.getUserNo());
        Transaction transaction = this.generateTransaction(
                transactionRequestDto.getCostType(),
                transactionRequestDto.getAmount(),
                transactionRequestDto.getProjectId()
        );

        //计次计时充值自动开户
        if (transactionRequestDto.getCostType() == COUNT_COST) {
            try {
                openUserCountInterface(new UserCountRequestDto(transactionRequestDto.getProjectId(),
                        0, 1, 1, transactionRequestDto.getUserNo()));
            } catch (EWalletUserCountAlreadyExistException e) {
            }
        } else if (transactionRequestDto.getCostType() == TIME_COST) {
            try {
                openUserTimeInterface(new UserTimeRequestDto(transactionRequestDto.getProjectId(),
                        0, 1, 1, transactionRequestDto.getUserNo()));
            } catch (EWalletUserTimeAlreadyExistException e) {
            }
        }
        String billNo = this.ewDataAgent.doValueCharge(
                transactionRequestDto.getUserNo(), transaction
        );
        if (transactionRequestDto.getCostType() == MONEY_COST)
            transactionRequestDto.setProjectId(EWalletUtils.WALLET_PROJECT_ID);
        EwBill ewBill = this.generateBill(transactionRequestDto, transaction, billNo, true);
        this.ewDataRepo.insertBill(ewBill);
        return new TransactionResponseDto(billNo, 0);
    }

    public List<StatInfo> statInfoByDeviceNoAndBatchNo(Integer loginId) {
        try {
            return SqlUtil.sqlQuery(StatInfo.class, false,
                    "SELECT cost_type as accountType,count(*) as times,sum(amount) as totalMoney FROM ew_bill where login_id = ? group by cost_type order by cost_type",
                    loginId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 用户开户
     *
     * @param userRequestDto 注意除weChatId, 其余fields皆为@NonNull
     * @return 用户Id userNo
     * @throws EWalletDuplicateUserNoException 用户号重复
     */

    public String openUserInterface(UserRequestDto userRequestDto)
            throws EWalletDuplicateUserNoException {

        EwUser ewUser = this.ewDataAgent.createUserAccount(userRequestDto);

        return ewUser.getUserNo();
    }

    /**
     * 计次用户开户, 对应用户必须先存在
     *
     * @param userCountRequestDto 注意其fields皆为@NonNull
     * @return 计次用户id
     * @throws EWalletUserNotExistException          对应用户不存在
     * @throws EWalletCountProjectNotExistException  对应计次物品不存在
     * @throws EWalletUserCountAlreadyExistException 已存在对应计次账户
     */
    public Integer openUserCountInterface(UserCountRequestDto userCountRequestDto)
            throws EWalletUserNotExistException, EWalletCountProjectNotExistException,
            EWalletUserCountAlreadyExistException {

        EwUserCount ewUserCount = this.ewDataAgent.createUserCountAccount(userCountRequestDto);

        return ewUserCount.getId();
    }

    /**
     * 计时用户开户, 对应用户必须先存在
     *
     * @param userTimeRequestDto 注意其fields皆为@NonNull
     * @return 计时用户id
     * @throws EWalletUserNotExistException         对应用户不存在
     * @throws EWalletTimeProjectNotExistException  对应计时物品不存在
     * @throws EWalletUserTimeAlreadyExistException 对应计时用户已存在
     */
    public Integer openUserTimeInterface(UserTimeRequestDto userTimeRequestDto)
            throws EWalletUserNotExistException, EWalletTimeProjectNotExistException,
            EWalletUserTimeAlreadyExistException {

        EwUserTime ewUserTime = this.ewDataAgent.createUserTimeAccount(userTimeRequestDto);

        return ewUserTime.getId();
    }

    public EwCountProject selectCountProjectById(int projectId) throws EWalletCountProjectNotExistException {
        return ewDataRepo.findCountProjectById(projectId);
    }

    public EwTimeProject selectTimeProjectById(int projectId) throws EWalletTimeProjectNotExistException {
        return ewDataRepo.findTimeProjectById(projectId);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        useWallet = Util.boolValue(environment.getProperty("ewallet.wallet", Boolean.class));
        useCountWallet = Util.boolValue(environment.getProperty("ewallet.count-wallet", Boolean.class));
        useTimeWallet = Util.boolValue(environment.getProperty("ewallet.time-wallet", Boolean.class));
    }
    public Map<String, Object> queryBill(String userNo, Date dt0, Date dt1, Integer offset, Integer limit) throws PayException {
        return ewDataAgent.queryBill(userNo,dt0,dt1,offset,limit);
    }
}
