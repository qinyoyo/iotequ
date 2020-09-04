package top.iotequ.pay.dto.response;

import lombok.*;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Getter
@Setter
@Root(name = "root")
@Default
public class UpdateInfoResponse extends BaseResponse {

    private String version;
    private String currentTime;
    @Element(required = false)
    private Integer updateFlag;
    @Element(required = false)
    private Integer updateType;
    @ElementList(inline=true,required = false)
    private List<TradeAccount> tradeAccount;
    private String signature;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Default
    public static class TradeAccount {
        @Element(required = false)
        private String name;
        private Integer accountId;
        private Integer accountType;
        @Element(required = false)
        private Integer basePrice;
        @Element(required = false)
        private Integer startTime;
        @Element(required = false)
        private Integer endTime;
    }
}
