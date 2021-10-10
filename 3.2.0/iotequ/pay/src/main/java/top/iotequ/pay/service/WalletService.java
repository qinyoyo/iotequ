package top.iotequ.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import top.iotequ.framework.util.DateUtil;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.svasclient.SvasClient;
import top.iotequ.svasclient.SvasClient.Matched;
import top.iotequ.svasclient.SvasService;
import top.iotequ.ewallet.pojo.EwBill;
import top.iotequ.ewallet.pojo.EwCountProject;
import top.iotequ.ewallet.pojo.EwTimeProject;
import top.iotequ.ewallet.pojo.EwUser;
import top.iotequ.ewallet.utility.EWalletUtils;
import top.iotequ.ewallet.utility.dto.*;
import top.iotequ.ewallet.utility.exception.EWalletCountProjectNotExistException;
import top.iotequ.ewallet.utility.exception.EWalletTimeProjectNotExistException;
import top.iotequ.ewallet.utility.exception.EWalletUserNotExistException;
import top.iotequ.framework.util.Util;
import top.iotequ.pay.dto.AccountType;
import top.iotequ.pay.dto.TradeType;
import top.iotequ.pay.dto.request.*;
import top.iotequ.pay.dto.request.LogoutRequest.StatInfo;
import top.iotequ.pay.dto.response.*;
import top.iotequ.pay.dto.response.QueryBalanceResponse.AccountInfo;
import top.iotequ.pay.dto.response.UpdateInfoResponse.TradeAccount;
import top.iotequ.pay.exception.PayException;
import top.iotequ.pay.utility.PayUtil;

import java.util.*;

@Service
public class WalletService implements PayService {

    @Autowired
    private SvasService svasService;
    @Autowired
    private EWalletUtils eWalletUtils;

    @Override
    public List<TradeAccount> getTradeAccountInfo(String version, String deviceNo,boolean ewalletActive,String countProjectList,String timeProjectList)
            throws PayException {
        return eWalletUtils.queryTradeAccountInfo(version, deviceNo,ewalletActive,countProjectList,timeProjectList);
    }

    private String getShowMessage(String userNo,Integer accountType,int projectId) {
        try {
            BalanceResponseDto detail = eWalletUtils.queryBalanceInterface(userNo);
            EwUser ewUser=queryUser(userNo);
            if (ewUser==null) return null;
            Map<String,Object> map=new HashMap<>();
            map.put("name",ewUser.getName());
            map.put("userNo",userNo);
            map.put("accountType",accountType);
            map.put("projectId",projectId);
            if (Objects.isNull(accountType) || accountType == AccountType.WALLET) {
                map.put("amount", ewUser.getAmountMoney());
                map.put("projectName","现金");
                map.put("bonus", ewUser.getBonusPoint());
            } else if (accountType == AccountType.COUNT_ACCOUNT && notEmpty(detail.getCounts())) {
                for ( CountResponseDto cc : detail.getCounts()) {
                    if (cc.getCountId() == projectId) {
                        map.put("amount", cc.getAmount());
                        map.put("projectName",cc.getName());
                        break;
                    }
                }
            } else if (notEmpty(detail.getTimes()) && accountType == AccountType.TIME_ACCOUNT) {
                for (TimeResponseDto cc : detail.getTimes()) {
                    if (cc.getTimeId() == projectId) {
                        map.put("amount", cc.getAmount());
                        map.put("projectName",cc.getName());
                        break;
                    }
                }
            }
            return StringUtil.toJsonString(map);
        } catch (PayException e) {
            return null;
        }
    }
    private PayException exceptionWithUserInfo(@NonNull  PayException e,@NonNull EwUser ewUser) {
        Map<String,Object> map = new HashMap<>();
        map.put("message",e.getMessage());
        map.put("userNo",ewUser.getUserNo());
        map.put("name",ewUser.getName());
        return new PayException(e.getCode(),StringUtil.toJsonString(map));
    }
    @Override
    public List<AccountInfo> getAccountInfo(QueryBalanceResponse ack,String userInfo, Integer accountType)
            throws PayException {
        String userNo = getUserNoFromUserInfo(userInfo);
        EwUser ewUser=queryUser(userNo);
        if (ewUser==null) throw new PayException(PayException.UserInfoNotFound,null);
        try {
            ack.setUserNo(userNo);
            ack.setName(ewUser.getName());
            ack.setActiveSince(PayUtil.date2String(ewUser.getActiveSince(), null));
            ack.setExpireAt(PayUtil.date2String(ewUser.getExpireAt(), null));
            BalanceResponseDto detail = eWalletUtils
                    .queryBalanceInterface(userNo);

            List<AccountInfo> list = new ArrayList<>();
            // 有余额的钱包总是有效
            if ((Objects.isNull(accountType) || accountType == AccountType.WALLET) && detail.getAmount() > 0) {
                list.add(
                        new AccountInfo("现金钱包", EWalletUtils.WALLET_PROJECT_ID, AccountType.WALLET, detail.getAmount(), detail.getBonus(),
                                eWalletUtils.isUseWallet() ? 1 : 0, null, null
                        ));
            }
            if (notEmpty(detail.getCounts()) && (Objects.isNull(accountType)
                    || accountType == AccountType.COUNT_ACCOUNT)) {
                for (CountResponseDto cc : detail.getCounts()) {
                    try {
                        EwCountProject ewCountProject = queryCountProject(cc.getCountId());
                        list.add(new AccountInfo(cc.getName(), cc.getCountId(), AccountType.COUNT_ACCOUNT,
                                cc.getAmount(), 0,
                                eWalletUtils.isUseCountWallet() && PayUtil.isValidNow(ewCountProject.getStartTime(), ewCountProject.getEndTime(), "HHmmss") ? 1 : 0,
                                PayUtil.date2String(ewCountProject.getStartTime(), "HH:mm:ss"),
                                PayUtil.date2String(ewCountProject.getEndTime(), "HH:mm:ss")
                        ));
                    } catch (Exception e) {
                    }
                }
            }
            if (notEmpty(detail.getTimes()) && (Objects.isNull(accountType)
                    || accountType == AccountType.TIME_ACCOUNT)) {
                for (TimeResponseDto cc : detail.getTimes()) {
                    try {
                        EwTimeProject ewTimeProject = queryTimeProject(cc.getTimeId());
                        list.add(new AccountInfo(cc.getName(), cc.getTimeId(), AccountType.COUNT_ACCOUNT,
                                cc.getAmount(), 0,
                                eWalletUtils.isUseTimeWallet() && PayUtil.isValidNow(ewTimeProject.getStartTime(), ewTimeProject.getEndTime(), "HHmmss") ? 1 : 0,
                                PayUtil.date2String(ewTimeProject.getStartTime(), "HH:mm:ss"),
                                PayUtil.date2String(ewTimeProject.getEndTime(), "HH:mm:ss")
                        ));
                    } catch (Exception e) {
                    }
                }
            }
            return list;
        } catch (PayException e) {
            throw exceptionWithUserInfo(e,ewUser);
        }
    }

    boolean notEmpty(List<?> list) {
        return (!Objects.isNull(list) && !list.isEmpty());
    }

    String getUserNoFromUserInfo(String userInfo) throws PayException {
        if (PayUtil.isEmpty(userInfo)) {
            throw new PayException(PayException.UserInfoNull, null);
        } else if (userInfo.length() == 576) {  // 指静脉
            try {
                Matched matches = svasService.auth(userInfo);
                if (matches.count == 1) {
                    return matches.list.get(0).userNo;
                } else {
                    throw new PayException(PayException.UserAuthFailure,
                            matches.count == 0 ? null : "重复" + matches.count);
                }
            } catch (Exception e) {
                throw new PayException(PayException.UserAuthFailure, e.getMessage());
            }
        } else {
            return userInfo;
        }
    }

    @Override
    public CostResponse cost(@NonNull CostRequest request) throws PayException {
        String userNo = getUserNoFromUserInfo(request.getUserInfo());
        EwUser ewUser=queryUser(userNo);
        if (ewUser==null) throw new PayException(PayException.UserInfoNotFound,null);
        try {
            TransactionRequestDto params = new TransactionRequestDto(userNo, request.getAccountType(),
                    request.getTradeMoney(), request.getAccountId(),
                    request.getOperation(), request.getDeviceNo(), request.getShopId().toString(),
                    request.getDeviceOrderNo(), DateUtil.string2Date(request.getDeviceDatetime()),
                    request.getTradeNo(),
                    request.getOperator(),
                    request.getBatchNo(),
                    request.getLoginId());
            TransactionResponseDto result = eWalletUtils.expendInterface(params);
            CostResponse ack = new CostResponse();
            ack.setBonus(result.getBonus());
            ack.setOrderNo(result.getBillNo());
            ack.setShowMsg(getShowMessage(userNo, request.getAccountType(), request.getAccountId()));
            return ack;
        } catch (PayException e) {
            throw exceptionWithUserInfo(e,ewUser);
        }
    }

    @Override
    public LastTradeInfoResponse lastTradeInfo(String deviceNo) throws PayException {
        EwBill bill = eWalletUtils.queryLastTradeInterface(deviceNo);
        LastTradeInfoResponse ack = new LastTradeInfoResponse();
        ack.setAccountType(bill.getCostType());
        ack.setOrderNo(bill.getNo());
        ack.setDeviceDatetime(PayUtil.date2String(bill.getDeviceDt(), "yyyy-mm-dd HH:mm:ss"));
        ack.setLastOperateType(bill.getOperationType());
        ack.setTradeMoney(bill.getAmount());
        ack.setTradeNo(bill.getTradeNo());
        if (Objects.isNull(bill.getLinkNo())) {
            ack.setTradeType(bill.getIsCharge() ? TradeType.CHARGE : TradeType.COST);
        } else {
            ack.setTradeType(bill.getIsCharge() ? TradeType.CANCEL_CHARGE : TradeType.CANCEL_COST);
        }
        return ack;
        //end
    }

    @Override
    public CancelTradeResponse cancelTrade(CancelTradeRequest req) throws PayException {
        EwBill cancelbill = eWalletUtils.tradeCancelInterface(req.getTradeNo0(), req.getDeviceNo(),
                req.getShopId().toString(),
                req.getTradeNo(), DateUtil.string2Date(req.getDeviceDatetime()),
                req.getBatchNo(),
                req.getLoginId());
        CancelTradeResponse ack = new CancelTradeResponse();
        ack.setBonus(cancelbill.getBonus());
        ack.setCancelMoney(cancelbill.getAmount());
        ack.setOrderNo(cancelbill.getNo());
        return ack;
    }

    @Override
    public ChargeResponse charge(ChargeRequest request) throws PayException {
        String userNo = getUserNoFromUserInfo(request.getUserInfo());
        EwUser ewUser=queryUser(userNo);
        if (ewUser==null) throw new EWalletUserNotExistException("用户:".concat(userNo).concat(" 不存在"));
        try {
            TransactionRequestDto params = new TransactionRequestDto(userNo, request.getAccountType(),
                    request.getTradeMoney(), request.getAccountId(),
                    request.getOperation(), request.getDeviceNo(), request.getShopId().toString(),
                    request.getDeviceOrderNo(), DateUtil.string2Date(request.getDeviceDatetime()),
                    request.getTradeNo(),
                    request.getOperator(),
                    request.getBatchNo(),
                    request.getLoginId());
            TransactionResponseDto result = eWalletUtils.chargeInterface(params);
            BalanceResponseDto detail = eWalletUtils
                    .queryBalanceInterface(userNo);
            ChargeResponse ack = new ChargeResponse();
            switch (request.getAccountType()) {
                case AccountType.WALLET:
                    ack.setBonus(detail.getBonus());
                    ack.setBalance(detail.getAmount());
                    break;
                case AccountType.COUNT_ACCOUNT:
                    ack.setBonus(0);
                    ack.setBalance(request.getTradeMoney());
                    for (CountResponseDto d : detail.getCounts()) {
                        if (d.getCountId() == request.getAccountId()) {
                            ack.setBalance(d.getAmount());
                            break;
                        }
                    }
                    break;
                case AccountType.TIME_ACCOUNT:
                    ack.setBonus(0);
                    ack.setBalance(request.getTradeMoney());
                    for (TimeResponseDto d : detail.getTimes()) {
                        if (d.getTimeId() == request.getAccountId()) {
                            ack.setBalance(d.getAmount());
                            break;
                        }
                    }
                    break;
            }
            ack.setOrderNo(result.getBillNo());
            ack.setName(ewUser.getName());
            return ack;
        } catch (PayException e) {
            throw exceptionWithUserInfo(e,ewUser);
        }
    }

    private boolean checkStatList(List<StatInfo> s1, List<StatInfo> s2) {
        if (Objects.isNull(s1) && Objects.isNull(s2)) return true;
        else if (Objects.isNull(s1) || Objects.isNull(s2)) return false;
        else if (s1.size() != s2.size()) return false;
        else {
            for (StatInfo info1 : s1) {
                boolean matched = false;
                for (StatInfo info2 : s2) {
                    if (info2.getAccountType().equals(info1.getAccountType()) &&
                            info2.getTimes().equals(info1.getTimes()) &&
                            info2.getTotalMoney().equals(info1.getTotalMoney())) {
                        matched = true;
                        break;
                    }
                }
                if (!matched) return false;
            }
            return true;
        }
    }

    @Override
    public LogoutResponse logout(LogoutRequest request) throws PayException {
        List<StatInfo> list = eWalletUtils.statInfoByDeviceNoAndBatchNo(request.getLoginId());
        LogoutResponse ack = new LogoutResponse();
        if (checkStatList(list, request.getStatInfo())) {
            ack.setShowMsg("OK");
        } else {
            ack.setShowMsg("交易统计数据不匹配");
            ack.setStatInfo(list);
        }
        return ack;
    }

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) throws PayException {
        if (!Objects.isNull(queryUser(request.getUserNo())))
            throw new PayException(PayException.UserExists,request.getUserNo());
        SvasClient.UserInfo userInfo=null;
        try {
            userInfo = svasService.getUserInfo(request.getUserNo());
        } catch (Exception e) {
            throw new PayException(e,request.getUserNo());
        }
        if (Objects.isNull(userInfo))
            throw new PayException(PayException.UserInfoNotFound,request.getUserNo());
        String userNo = eWalletUtils.openUserInterface(new UserRequestDto(request.getUserNo(),
                userInfo.name,request.getGender(),PayUtil.string2Date(request.getBirthDate(),"yyyy-MM-dd"),
                request.getMobilePhone(),request.getEmail(),request.getWechatOpenid(),request.getMemberGroup(),
                request.getCostLimit(),request.getDayLimit(),userInfo.idType,userInfo.idNo,
                PayUtil.string2Date(request.getActiveSince(),null),
                PayUtil.string2Date(request.getExpireAt(),null)
                ));
        if (request.getUserNo().equals(userNo)) {
            CreateAccountResponse ack = new CreateAccountResponse();
            ack.setCode(0);
            ack.setUserNo(userNo);
            ack.setMessage("Success");
            return ack;
        } else throw new PayException(1,"未知错误信息");
    }

    @Override
    public EwUser queryUser(String userNo) {
        return eWalletUtils.sqlSelectUserInterface(userNo);
    }

    @Override
    public Map<String, Object> queryBill(String userNo, Date dt0, Date dt1, Integer offset, Integer limit) throws PayException {
        return eWalletUtils.queryBill(userNo,dt0,dt1,offset,limit);
    }

    public EwCountProject queryCountProject(int projectId) throws EWalletCountProjectNotExistException {
        return eWalletUtils.selectCountProjectById(projectId);
    }
    public EwTimeProject queryTimeProject(int projectId) throws EWalletTimeProjectNotExistException {
        return eWalletUtils.selectTimeProjectById(projectId);
    }
}
