package top.iotequ.pay.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class TranKeyResponse extends BaseResponse {

    private String tranKey;   // 只能用原来的密钥加密
    private String checkValue;
    private String signature;

}
