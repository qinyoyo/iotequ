package top.iotequ.pay.exception;


import lombok.NonNull;
import top.iotequ.pay.utility.PayUtil;
public class PayException extends Exception {

    //错误代码与类别常量
    public static final CodeAndMessage DataDestroyed = new CodeAndMessage(101, "数据损坏或错误的工作密钥");
    public static final CodeAndMessage DataOperateTypeError = new CodeAndMessage(102, "操作类型不正确");
    public static final CodeAndMessage DataWorkKeyLost = new CodeAndMessage(103, "工作密钥丢失");
    public static final CodeAndMessage DataFieldRequired = new CodeAndMessage(104, "字段必须赋值");
    public static final CodeAndMessage DataFieldFailure = new CodeAndMessage(105, "错误的参数");

    public static final CodeAndMessage DeviceNotFound = new CodeAndMessage(121, "设备未找到");
    public static final CodeAndMessage DeviceNotInitialed = new CodeAndMessage(122, "设备没有初始化");
    public static final CodeAndMessage DeviceInitialed = new CodeAndMessage(123, "设备已经初始化");
    public static final CodeAndMessage DeviceNotLogin = new CodeAndMessage(124, "设备未签到");
    public static final CodeAndMessage DeviceNotLogout = new CodeAndMessage(125, "设备未签退");
    public static final CodeAndMessage DeviceNotBelongShop = new CodeAndMessage(126, "设备不属于该商家");
    public static final CodeAndMessage DeviceErrorLoginId = new CodeAndMessage(127, "签到编号不匹配");

    public static final CodeAndMessage OperatorNotFound = new CodeAndMessage(130, "操作员不存在或密码错");
    public static final CodeAndMessage OperateFailure = new CodeAndMessage(131, "操作失败");

    public static final CodeAndMessage UserAuthFailure = new CodeAndMessage(140, "指静脉认证失败");
    public static final CodeAndMessage UserInfoNull = new CodeAndMessage(141, "用户信息空");
    public static final CodeAndMessage UserNotFound = new CodeAndMessage(142, "用户信息空");
    public static final CodeAndMessage UserInvalid = new CodeAndMessage(143, "用户账号被冻结");
    public static final CodeAndMessage UserInactive = new CodeAndMessage(144, "用户已注销");
    public static final CodeAndMessage UserExpired = new CodeAndMessage(145, "账户已过有效期");
    public static final CodeAndMessage UserNotActivate = new CodeAndMessage(146, "账户未启用");
    public static final CodeAndMessage UserExists = new CodeAndMessage(147, "账户已存在");
    public static final CodeAndMessage UserInfoNotFound = new CodeAndMessage(148, "用户信息不存在");

    public static final CodeAndMessage InvalidObject = new CodeAndMessage(150, "无效的对象");
    public static final CodeAndMessage AlreadyCancelled = new CodeAndMessage(151, "交易已经取消");
    public static final CodeAndMessage InvalidCheckCode = new CodeAndMessage(152, "数据被破坏");
    public static final CodeAndMessage CountProjectNotFound = new CodeAndMessage(153, "计次项目不存在");
    public static final CodeAndMessage TimeProjectNotFound = new CodeAndMessage(154, "计时项目不存在");
    public static final CodeAndMessage DuplicateBillNumber = new CodeAndMessage(155, "流水号重复");
    public static final CodeAndMessage DuplicateUserNo = new CodeAndMessage(156, "用户号重复");
    public static final CodeAndMessage ExceedCostLimit = new CodeAndMessage(157, "消费超过限额");
    public static final CodeAndMessage ExceedDayLimit = new CodeAndMessage(158, "当日消费超过限额");
    public static final CodeAndMessage IllegalArgument = new CodeAndMessage(159, "错误的参数");
    public static final CodeAndMessage InsufficientBalance = new CodeAndMessage(159, "余额不足");
    public static final CodeAndMessage NoBillFound = new CodeAndMessage(160, "账单不存在");
    public static final CodeAndMessage OperationForbidden = new CodeAndMessage(161, "禁止操作");
    public static final CodeAndMessage UserCountAlreadyExist = new CodeAndMessage(162, "计次账户已存在");
    public static final CodeAndMessage UserTimeAlreadyExist = new CodeAndMessage(163, "计时账户已存在");
    protected static final CodeAndMessage UnsupportedType = new CodeAndMessage(164, "不支持消费类型");

    int code;

    public PayException(int code, String message) {
        super(message);
        this.code = code;
    }

    public PayException(@NonNull CodeAndMessage cm, String message) {
        super(PayUtil.isEmpty(message) ? cm.getMessage() : cm.getMessage() + "[" + message + "]");
        this.code = cm.getCode();
    }

    public PayException(@NonNull CodeAndMessage cm, @NonNull Exception e, String message) {
        super(PayUtil.isEmpty(message) ? cm.getMessage() + "[" + e.getMessage() + "]"
                : cm.getMessage() + "[" + message + ":" + e.getMessage() + "]");
        this.code = cm.getCode();
    }

    public PayException(@NonNull Exception e, String message) {
        super(PayUtil.isEmpty(message) ? e.getMessage() : message + "[" + e.getMessage() + "]");
        this.code = 1;
    }

    public int getCode() {
        return code;
    }

    ;
}
