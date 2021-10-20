package top.iotequ.pay.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class QueryBalanceRequest extends BaseRequest {

    private String userInfo;
    @Element(required = false)
    private Integer accountType;
    private String signature;

}
