package top.iotequ.pay.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class LastTradeInfoResponse extends BaseResponse {

    private Integer lastOperateType;
    private Integer tradeType;
    private String orderNo;
    private String tradeNo;
    private Integer tradeMoney;
    private Integer accountType;
    private String deviceDatetime;
    private String signature;
}
