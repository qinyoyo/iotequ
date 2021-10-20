package top.iotequ.pay.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class CostRequest extends BaseRequest {

    private String tradeNo;
    private Integer tradeMoney;
    private Integer accountType;
    private Integer accountId;
    private String deviceDatetime;
    private String userInfo;
    private Byte isDisplay;
    private String operator;
    private String batchNo;
    private String signature;

}
