package top.iotequ.pay.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class LoginRequest extends BaseRequest {

    private String version;        //  终端版本号
    private String operator;
    private String password;
    private String random;
    private String signature;

}
