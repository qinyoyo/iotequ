package top.iotequ.pay.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class TranKeyRequest extends BaseRequest {

    private String random;        //	随机字符串	String(32)	是	设备随机生成
    private String signature;


}
