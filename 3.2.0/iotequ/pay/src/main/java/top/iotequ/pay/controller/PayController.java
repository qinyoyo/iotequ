package top.iotequ.pay.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.iotequ.framework.util.EntityUtil;
import top.iotequ.framework.util.RestJson;
import top.iotequ.svasclient.SvasClient;
import top.iotequ.svasclient.SvasService;
import top.iotequ.ewallet.pojo.EwUser;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.util.Util;
import top.iotequ.pay.dao.PayLoginDao;
import top.iotequ.pay.dao.PayOperatorDao;
import top.iotequ.pay.dao.PayPosDao;
import top.iotequ.pay.dto.DeviceChecker;
import top.iotequ.pay.dto.OperateType;
import top.iotequ.pay.dto.ParameterChecker;
import top.iotequ.pay.dto.PayDto;
import top.iotequ.pay.dto.request.*;
import top.iotequ.pay.dto.response.*;
import top.iotequ.pay.exception.PayException;
import top.iotequ.pay.pojo.PayLogin;
import top.iotequ.pay.pojo.PayOperator;
import top.iotequ.pay.pojo.PayPos;
import top.iotequ.pay.service.WalletService;
import top.iotequ.pay.utility.EncryptUtil;
import top.iotequ.pay.utility.HttpXmlUtil;
import top.iotequ.pay.utility.PayUtil;
import top.iotequ.pay.utility.XmlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {
    private final DeviceChecker checker = new DeviceChecker() {
        @Override
        public void checkDeviceStatus(@NonNull PayDto dto) throws PayException {
            if (PayUtil.isEmpty(dto.getDeviceNo())) {
                throw new PayException(PayException.DeviceNotFound, null);
            }
            PosAndLogin pos = new PosAndLogin(dto.getDeviceNo());
            if (Objects.isNull(pos) || Objects.isNull(pos.getId())) {
                throw new PayException(PayException.DeviceNotFound, null);
            }
            if (Objects.isNull(pos.getLoginId())) {
                pos.setWorkCode(null);
            }
            if (!Objects.isNull(pos.getLoginId()) && Objects.isNull(pos.getWorkCode())) {
                throw new PayException(PayException.DataWorkKeyLost, null);
            }
            dto.setPayPos(pos);
            if (dto instanceof TranKeyRequest) {    // 初始化
                if (!PayUtil.isEmpty(pos.getSecurityCode())) {
                    throw new PayException(PayException.DeviceInitialed, null);
                }
            } else if (dto instanceof BaseRequest) { // 已经初始化
                if (PayUtil.isEmpty(pos.getSecurityCode())) {
                    throw new PayException(PayException.DeviceNotInitialed, null);
                }
                if (dto instanceof LoginRequest) {
                    if (Objects.nonNull(pos.getWorkCode())) {  // 允许多次签到
                        log.debug("Multiple times login ,login id = {}", pos.getLoginId());
                    }
                } else {
                    if (PayUtil.isEmpty(pos.getLoginId())) {
                        throw new PayException(PayException.DeviceNotLogin, null);
                    }
                    if (!EntityUtil.entityEquals(pos.getLoginId(), ((BaseRequest) dto).getLoginId())) {
                        throw new PayException(PayException.DeviceErrorLoginId, null);
                    }
                }
            }
            if (!PayUtil.isSame(pos.getShopId(), PayUtil.getFieldValue(dto, "shopId"))) {
                throw new PayException(PayException.DeviceNotBelongShop,
                        pos.getShopId().toString());
            }
            if (!PayUtil.isEmpty(pos.getLoginId()) && PayUtil.isEmpty(pos.getWorkCode())) {
                throw new PayException(PayException.DataWorkKeyLost,
                        pos.getNo());
            }
            //设置工作密钥
            if (PayUtil.isEmpty(pos.getLoginId()) || dto instanceof LoginRequest) {  // 未登录,或签到，多次签到
                dto.setWorkKey(pos.getSecurityCode());
            } else {
                String key = pos.getWorkCode();
                if (PayUtil.isEmpty(key)) {
                    throw new PayException(PayException.DataWorkKeyLost, dto.getDeviceNo());
                }
                dto.setWorkKey(key);
            }
        }
    };

    @Autowired
    private SvasService svasService;
    @Autowired
    private PayPosDao payPosDao;
    @Autowired
    private PayOperatorDao payOperatorDao;
    @Autowired
    private PayLoginDao payLoginDao;
    @Autowired
    private WalletService walletService;

    String getUserNoFromVeinInfo(String userInfo) throws PayException {
        try {
            SvasClient.Matched matches = svasService.auth(userInfo);
            if (matches.count == 1) {
                return matches.list.get(0).userNo;
            } else {
                throw new PayException(PayException.UserAuthFailure,
                        matches.count == 0 ? null : "重复" + matches.count);
            }
        } catch (Exception e) {
            throw new PayException(PayException.UserAuthFailure, e.getMessage());
        }
    }

    /*
     * 初始化，获取传输密钥，使用初始密钥(null)加密数据
     */
    @Transactional
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(HttpServletRequest request, HttpServletResponse response) {
        try {
            TranKeyRequest body = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new OperationTypeCheck(OperateType.INIT),
                            null, TranKeyRequest.class);
            PayPos pos = (PayPos) body.getPayPos();
            TranKeyResponse ack = new TranKeyResponse();
            if (PayUtil.isEmpty(pos.getSecurityCode())) {
                String tranKey = PayUtil.uuid().substring(0, 16);   //  传输密钥
                ack.setTranKey(tranKey);
                pos.setWorkCode(null);
                pos.setSecurityCode(tranKey);
                ack.setCheckValue(EncryptUtil.encryptString(body.getRandom(), tranKey, true));
                payPosDao.update(pos);
            } else {
                return getXmlFromObject(new ErrorResponse(
                                new PayException(PayException.DeviceInitialed, body.getDeviceNo())),
                        null);
            }
            return getXmlFromObject(ack, null);  //  返回值必须用 初始密钥加密
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    private String getXmlFromObject(PayDto dto, String key) {
        try {
            return XmlUtil.generateXmlPostBody(dto, key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            try {
                return XmlUtil.generateXmlPostBody(new ErrorResponse(e), key);
            } catch (Exception e1) {
                return e1.getMessage();
            }
        }
    }

    /*
     * 签到，使用传输密钥
     */
    @Transactional
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) {

        try {
            LoginRequest body = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new OperationTypeCheck(OperateType.LOGIN),
                            null, LoginRequest.class);
            PayPos pos = (PayPos) body.getPayPos();
            LoginResponse ack = new LoginResponse();
            String key = pos.getSecurityCode();
            ack.setWorkKey(key);   // 设置传输密钥为工作密钥
            PayOperator operator = null;
            if (body.getPassword().length() == 576) {
                String userNo = getUserNoFromVeinInfo(body.getPassword());
                operator = payOperatorDao.selectByUserNo(userNo);
                if (Objects.isNull(operator) || !Objects.equals(operator.getShopId(), body.getShopId())) {
                    throw new PayException(PayException.OperatorNotFound, body.getOperator());
                }
            } else {
                operator = payOperatorDao.selectByName(body.getOperator());
                if (Objects.isNull(operator) || !Objects.equals(operator.getShopId(), body.getShopId())
                        || !operator
                        .getPassword()
                        .equals(EncryptUtil.encryptString(body.getPassword(), null, true))
                ) {
                    throw new PayException(PayException.OperatorNotFound, body.getOperator());
                }
            }
            ack.setOperator(operator.getName());
            Integer loginId = pos.getLoginId();
            String batchNo = "000001";
            PayLogin login = null;
            if (loginId != null && loginId != 0) {
                login = payLoginDao.select(loginId);
                if (login == null) throw new PayException(PayException.OperateFailure, "Login");
                batchNo = login.getBatchNo();
            } else {
                login = new PayLogin();
                login.setAppVersion(body.getVersion());
                login.setBatchNo(batchNo);
                login.setDeviceStream(body.getDeviceOrderNo());
                login.setFailureCount(0);
                login.setTradeCount(0);
                login.setLoginTime(new Date());
                login.setLogoutTime(null);
                login.setOperatorId(operator.getId());
                login.setPosId(pos.getId());
                login.setShopId(body.getShopId());
                login.setRandomNo(body.getRandom());
                payLoginDao.insert(login);
                if (Objects.isNull(login.getId())) {
                    throw new PayException(PayException.OperateFailure, "Login");
                }
                batchNo = String.format("%06d", login.getId() % 1000000);
                login.setBatchNo(batchNo);
                payLoginDao.update(login);
                pos.setLoginId(login.getId());
            }
            String workKey = PayUtil.uuid().substring(0, 16);
            pos.setWorkCode(workKey);
            payPosDao.update(pos);
            ack.setVersion(body.getVersion());
            ack.setBatchNo(batchNo);
            ack.setWorkKey(workKey);
            ack.setLoginId(login.getId());
            ack.setVersion(body.getVersion());
            ack.setCheckValue(EncryptUtil.encryptString(body.getRandom(), workKey, true));
            return getXmlFromObject(ack, key);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    @Transactional
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public String updateInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            UpdateInfoRequest req = HttpXmlUtil.getPayRequestDtoFrom(request, checker, new OperationTypeCheck(OperateType.UPDATE_INFO),
                    null, UpdateInfoRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            String key = pos.getWorkCode();
            UpdateInfoResponse ack = new UpdateInfoResponse();
            ack.setWorkKey(key);
            ack.setVersion(req.getVersion());
            ack.setUpdateFlag(0);
            ack.setUpdateType(0);
            ack.setVersion(req.getVersion());
            ack.setCurrentTime(PayUtil.date2String(new Date(), null));
            ack.setTradeAccount(
                    walletService.getTradeAccountInfo(req.getVersion(), req.getDeviceNo(),
                            pos.getEwalletActive(), pos.getCountProjectList(), pos.getTimeProjectList()));
            return getXmlFromObject(ack, key);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    @Transactional
    @RequestMapping(value = "/queryBalance", method = RequestMethod.POST)
    public String queryBalance(HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryBalanceResponse ack = new QueryBalanceResponse();
            QueryBalanceRequest req = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new OperationTypeCheck(OperateType.BALANCE),
                            null, QueryBalanceRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            String key = pos.getWorkCode();
            ack.setWorkKey(key);
            ack.setAccountInfo(
                    walletService.getAccountInfo(ack, req.getUserInfo(), req.getAccountType()));
            return getXmlFromObject(ack, key);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    @Transactional
    @RequestMapping(value = "/cost", method = RequestMethod.POST)
    public String cost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CostRequest req = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new BatchNoCheck(OperateType.COST),
                            null, CostRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            String key = pos.getWorkCode();
            CostResponse ack = walletService.cost(req);
            ack.setWorkKey(key);
            return getXmlFromObject(ack, key);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }

    }

    @Transactional
    @RequestMapping(value = "/lastTradeInfo", method = RequestMethod.POST)
    public String lastTradeInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            LastTradeInfoRequest req = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new OperationTypeCheck(OperateType.LAST_TRADE),
                            null, LastTradeInfoRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            LastTradeInfoResponse ack = walletService.lastTradeInfo(req.getDeviceNo());
            ack.setWorkKey(pos.getWorkCode());
            return getXmlFromObject(ack, pos.getWorkCode());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }

    }

    @Transactional
    @RequestMapping(value = "/cancelTrade", method = RequestMethod.POST)
    public String cancelTrade(HttpServletRequest request, HttpServletResponse response) {
        try {
            CancelTradeRequest req = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new BatchNoCheck(OperateType.CANCEL_TRADE),
                            null, CancelTradeRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            CancelTradeResponse ack = walletService.cancelTrade(req);
            ack.setWorkKey(pos.getWorkCode());
            return getXmlFromObject(ack, pos.getWorkCode());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    @Transactional
    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public String charge(HttpServletRequest request, HttpServletResponse response) {
        try {
            ChargeRequest req = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new BatchNoCheck(OperateType.CHARGE),
                            null, ChargeRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            ChargeResponse ack = walletService.charge(req);
            ack.setWorkKey(pos.getWorkCode());
            return getXmlFromObject(ack, pos.getWorkCode());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    @Transactional
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            LogoutRequest req = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new OperationTypeCheck(OperateType.LOGOUT), null, LogoutRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            LogoutResponse ack = walletService.logout(req);
            ack.setWorkKey(pos.getWorkCode());
            String xml = getXmlFromObject(ack, pos.getWorkCode());
            int loginId = pos.getLoginId();
            pos.setLoginId(null);
            pos.setWorkCode(null);
            payPosDao.update(pos);
            PayLogin login = payLoginDao.select(loginId);
            if (!Objects.isNull(login)) {
                login.setLogoutTime(new Date());
                payLoginDao.update(login);
            }
            return xml;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    @Transactional
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String createAccount(HttpServletRequest request, HttpServletResponse response) {
        try {
            CreateAccountRequest req = HttpXmlUtil
                    .getPayRequestDtoFrom(request, checker, new OperationTypeCheck(OperateType.CREATE_ACCOUNT), null, CreateAccountRequest.class);
            PayPos pos = (PayPos) req.getPayPos();
            if (req.getUserNo().length() == 576) {
                String userNo = getUserNoFromVeinInfo(req.getUserNo());
                req.setUserNo(userNo);
            }
            CreateAccountResponse ack = walletService.createAccount(req);
            ack.setUserNo(req.getUserNo());
            ack.setWorkKey(pos.getWorkCode());
            return getXmlFromObject(ack, pos.getWorkCode());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return getXmlFromObject(new ErrorResponse(e), null);
        }
    }

    @RequestMapping(value = "/queryBill")
    public String queryBill(String userNo, Date dt0, Date dt1, Integer offset, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> map = walletService.queryBill(userNo, dt0, dt1, offset, limit);
            return Util.getGson().toJson(map);
        } catch (PayException e) {
            RestJson j = new RestJson();
            j.setSuccess(false);
            j.setMessage(e.getMessage());
            j.put("code", e.getCode());
            return j.toString();
        }
    }
    @RequestMapping(value = "/userInfo")
    public String userInfo(String userNo, HttpServletRequest request, HttpServletResponse response) {
        RestJson j = new RestJson();
        try {
            EwUser ewUser=walletService.queryUser(userNo);
            if (ewUser==null) throw new PayException(PayException.UserInfoNotFound,userNo);
            QueryBalanceResponse ack = new QueryBalanceResponse();
            j.put("accountInfo",walletService.getAccountInfo(ack, userNo, null));
            j.put("name",ewUser.getName());
            j.put("activeSince",ewUser.getActiveSince());
            j.put("expireAt",ewUser.getExpireAt());
        } catch (PayException e) {
            j.setSuccess(false);
            j.put("error",e.getCode());
            j.setMessage(e.getMessage());
        }
        return j.toString();
    }
    @RequestMapping(value = "/sendVC")
    public Map<String, Object> sendVC(String mobilePhone, String templateName, HttpServletRequest request, HttpServletResponse response) {
        RestJson j = new RestJson();
        int vc = new Random().nextInt() % 1000000;
        if (vc < 0) vc = -vc;
        String code = String.format("%06d", vc);
        try {
            String smsModules = Util.getBean(Environment.class).getProperty("sms.modules");
            if (smsModules == null || !smsModules.toLowerCase().contains("pay")) throw new Exception("模块pay没有短信权限");
            Map<String, Object> map = new HashMap<>();
            map.put("code", code);
            Util.sendTemplateSmsToMobile(mobilePhone, templateName, map);
            j.put("verifyCode", code);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMessage(e.getMessage());
            if (e instanceof IotequException) {
                j.put("error", ((IotequException) e).getError());
            }
        }
        return j.getAttributes();
    }

    @Data
    public class PosAndLogin extends PayPos {
        PayLogin payLogin;

        public PosAndLogin(String deviceNo) {
            super();
            PayPos pos = payPosDao.selectByNo(deviceNo);
            if ((Objects.isNull(pos))) payLogin = null;
            else {
                setLoginId(pos.getLoginId());
                setWorkCode(pos.getWorkCode());
                setSecurityCode(pos.getSecurityCode());
                setId(pos.getId());
                setNo(pos.getNo());
                setShopId(pos.getShopId());
                setEwalletActive(pos.getEwalletActive());
                setCountProjectList(pos.getCountProjectList());
                setTimeProjectList(pos.getTimeProjectList());
                if (Objects.isNull(pos.getLoginId())) payLogin = null;
                else payLogin = payLoginDao.select(pos.getLoginId());
            }
        }
    }

    public class OperationTypeCheck implements ParameterChecker {
        int operation;

        public OperationTypeCheck(int operation) {
            this.operation = operation;
        }

        @Override
        public void checkParameters(PayDto dto) throws PayException {
            if (dto instanceof BaseRequest) {
                if (((BaseRequest) dto).getOperation() != operation) {
                    throw new PayException(PayException.DataOperateTypeError, ((BaseRequest) dto).getOperation().toString());
                }
            }
        }
    }

    public class BatchNoCheck implements ParameterChecker {
        int operation;

        public BatchNoCheck(int operation) {
            this.operation = operation;
        }

        @Override
        public void checkParameters(PayDto dto) throws PayException {
            if (dto instanceof BaseRequest) {
                if (((BaseRequest) dto).getOperation() != operation) {
                    throw new PayException(PayException.DataOperateTypeError, ((BaseRequest) dto).getOperation().toString());
                }
            }
            if (!Objects.isNull(dto.getPayPos())) {
                if (dto.getPayPos() instanceof PosAndLogin) {
                    PosAndLogin pl = (PosAndLogin) (dto.getPayPos());
                    if (Objects.isNull(pl.getPayLogin())) return;
                    String batchNo = pl.getPayLogin().getBatchNo();
                    String operator = pl.getPayLogin().getOperatorIdPayOperatorName();
                    Object param = PayUtil.getFieldValue(dto, "batchNo");
                    if (Objects.isNull(param) || !batchNo.equals(param.toString())) {
                        throw new PayException(PayException.DataFieldFailure, "batchNo");
                    }
                    param = PayUtil.getFieldValue(dto, "operator");
                    if (Objects.isNull(param) || !operator.equals(param.toString())) {
                        throw new PayException(PayException.DataFieldFailure, "operator");
                    }
                }
            }
        }

    }
}
