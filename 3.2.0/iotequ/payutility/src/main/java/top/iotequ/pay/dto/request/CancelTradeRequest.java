package top.iotequ.pay.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;

@Getter
@Setter
@Root(name = "root")
@Default
public class CancelTradeRequest extends BaseRequest {

    private String tradeNo;
    private String deviceDatetime;
    private String tradeNo0;
    private String operator;
    private String batchNo;
    private String signature;
}
