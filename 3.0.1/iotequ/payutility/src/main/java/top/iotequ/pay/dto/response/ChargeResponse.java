package top.iotequ.pay.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class ChargeResponse extends BaseResponse {

    private String orderNo;
    private String name;
    @Element(required = false)
    private Integer bonus;
    private Integer balance;
    private String signature;
}
