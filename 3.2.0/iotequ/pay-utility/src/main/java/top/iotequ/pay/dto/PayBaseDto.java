package top.iotequ.pay.dto;


import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import top.iotequ.pay.exception.PayException;
import top.iotequ.pay.utility.PayUtil;

@Getter
@Setter
@Root(name = "root")
public abstract class PayBaseDto implements PayDto {

    @Transient
    private DeviceChecker checker = null;
    @Transient
    private ParameterChecker parameterChecker = null;
    @Transient
    private Object payPos = null;
    @Transient
    private String workKey = null;

    @Override
    public void getReadyToSend(String key) throws PayException {
        if (parameterChecker != null)
            parameterChecker.checkParameters(this);
        setSignature(calculateSignature());    //  先计算md5，再加密。md5是否加密由用户配置
        encrypt(key);
    }

    public String calculateSignature() throws PayException {
        return PayUtil.calculateSignature(PayUtil.objectString(this));
    }

    public void encrypt(String key) throws PayException {
        if (PayUtil.isEmpty(key)) {
            key = getWorkKey();
        }
        PayUtil.objectEncrypt(this, key, true);
    }

    @Override
    public void afterReceived(String key) throws PayException {
        DeviceChecker deviceChecker = this.getChecker();
        if (deviceChecker != null) {
            deviceChecker.checkDeviceStatus(this);
        }
        decrypt(key);
        checkSignature();
        if (parameterChecker != null)
            parameterChecker.checkParameters(this);
    }

    public void checkSignature() throws PayException {
        if (PayUtil.isEmpty(getSignature())) {
            throw new PayException(PayException.DataDestroyed, "签名空");
        }
        String emd5 = calculateSignature();
        if (!getSignature().equals(emd5)) {
            throw new PayException(PayException.DataDestroyed, "签名错误");
        }
    }

    public void decrypt(String key) throws PayException {
        if (PayUtil.isEmpty(key)) {
            key = getWorkKey();
        }
        PayUtil.objectEncrypt(this, key, false);
    }
}
