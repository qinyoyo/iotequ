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
public class QueryBalanceResponse extends BaseResponse {

    @ElementList(inline = true, required = false)
    List<AccountInfo> accountInfo;
    private String userNo;
    private String name;
    private String signature;
    private String activeSince;
    private String expireAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Default
    public static class AccountInfo {
        private String name;
        private Integer accountId;
        private Integer accountType;
        private Integer balance;
        @Element(required = false)
        private Integer bonus;
        private Integer isValidNow;
        @Element(required = false)
        private String startTime;
        @Element(required = false)
        private String endTime;
    }
}
