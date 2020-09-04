package top.iotequ.pay.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class ChargeRequest extends BaseRequest {

    private String tradeNo;
    private Integer accountId;
    private Integer accountType;
    private String deviceDatetime;
    private Integer tradeMoney;
    private String userInfo;
    private String operator;
    private String batchNo;
    private String signature;

}
