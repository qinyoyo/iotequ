package top.iotequ.pay.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class LoginResponse extends BaseResponse {

    private String version;
    private String workKey;
    private String batchNo;
    private Integer loginId;
    private String operator;
    private String checkValue;
    private String signature;
}
