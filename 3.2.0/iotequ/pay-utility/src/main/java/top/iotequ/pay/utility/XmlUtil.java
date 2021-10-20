package top.iotequ.pay.utility;

import lombok.NonNull;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import top.iotequ.pay.dto.DeviceChecker;
import top.iotequ.pay.dto.ParameterChecker;
import top.iotequ.pay.dto.PayDto;
import top.iotequ.pay.dto.request.BaseRequest;
import top.iotequ.pay.dto.response.BaseResponse;
import top.iotequ.pay.dto.response.ErrorResponse;

import java.io.StringWriter;

public class XmlUtil {
    /**
     * 从xml解析出一个对象
     * @param xmlStr  xml字符串
     * @param clazz   对象类型
     * @param <T>     类型泛型
     * @return  对象
     * @throws Exception 出错
     */
    public static <T> T objectFromXml(String xmlStr, Class<T> clazz)
            throws Exception {

        Serializer serializer = new Persister();
        return serializer.read(clazz, xmlStr);
    }

    /**
     * 将一个对象序列化为xml字符串
     * @param obj 对象
     * @return xml串
     * @throws Exception 出错抛出异常
     */
    public static String objectToXml(Object obj) throws Exception {
        Serializer serializer = new Persister();
        StringWriter writer = new StringWriter();
        serializer.write(obj, writer);

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + writer.toString();
    }

    /**
     * 按照通讯协议加密一个PayDto对象，自动计算signature
     * @param dto 对象
     * @param encryptKey 加密密钥，null时使用对象的默认工作密钥
     * @return 字段加密后的xml串
     * @throws Exception 出错抛出异常
     */
    public static String generateXmlPostBody(@NonNull PayDto dto, String encryptKey)
            throws Exception {
        dto.getReadyToSend(encryptKey);
        return XmlUtil.objectToXml(dto);

    }

    /**
     * 从https的应答中解析出一个符合协议的PayDto对象
     * @param xml https响应得到的串
     * @param checker 检查终端各种属性的检查对象，不检查是设置为null
     * @param parameterChecker 参数检查，不检查设置为null
     * @param encryptKey 解密用的密钥，设置为null时根据终端状态自动选择密钥
     * @param clazz 应答类别
     * @param <T> 应答类别的泛型
     * @return 应答解密和检查后的对象
     * @throws Exception 出错时抛出异常
     */
    public static <T extends BaseResponse> T getPayResponseDtoFrom(
            @NonNull String xml,
            DeviceChecker checker,
            ParameterChecker parameterChecker,
            String encryptKey,
            @NonNull Class<T> clazz
    ) throws Exception {
        ErrorResponse errorResponse = XmlUtil.objectFromXml(xml, ErrorResponse.class);
        if (errorResponse.getCode() == 0) {
            T dto = XmlUtil.objectFromXml(xml, clazz);
            dto.setChecker(checker);
            dto.setParameterChecker(parameterChecker);
            dto.afterReceived(encryptKey);
            return dto;
        } else {
            T dto =clazz.newInstance();
            dto.setCode(errorResponse.getCode());
            dto.setMessage(errorResponse.getMessage());
            dto.setSignature(errorResponse.getSignature());
            return dto;
        }
    }

    /**
     * 从https请求中获得请求对象
     * @param xml 请求的加密xml串
     * @param checker 检查终端各种属性的检查对象，不检查是设置为null
     * @param parameterChecker 参数检查，不检查设置为null
     * @param encryptKey 解密用的密钥，设置为null时根据终端状态自动选择密钥
     * @param clazz 应答类别
     * @param <T> 应答类别的泛型
     * @return 应答解密和检查后的对象
     * @throws Exception 出错时抛出异常
     */
    public static <T extends BaseRequest> T getPayRequestDtoFrom(
            @NonNull String xml,
            DeviceChecker checker,
            ParameterChecker parameterChecker,
            String encryptKey,
            @NonNull Class<T> clazz
    ) throws Exception {
        T dto = XmlUtil.objectFromXml(xml, clazz);
        dto.setChecker(checker);
        dto.setParameterChecker(parameterChecker);
        dto.afterReceived(encryptKey);
        return dto;
    }
}
