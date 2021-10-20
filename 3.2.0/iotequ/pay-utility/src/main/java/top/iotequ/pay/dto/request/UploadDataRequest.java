package top.iotequ.pay.dto.request;

import lombok.*;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Getter
@Setter
@Root(name = "root")
@Default
public class UploadDataRequest extends BaseRequest {

    private String tradeNo;
    private String batchNo;
    private Integer total;
    private Integer startPos;
    private Integer currentTotal;
    @ElementList(inline = true)
    private List<TradeInfo> content;
    private String signature;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Default
    public static class TradeInfo {

        private Integer tradeType;
        private Integer tradeStatus;
        private Integer accountType;
        private Integer tradeMoney;
        private String tradeNo;
        private String orderNo;
        private String operatorNo;
        private String userNo;
        private String deviceDatetime;

    }
}
