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
public class LogoutRequest extends BaseRequest {

    private String operator;
    private Integer statStatus;
    @ElementList(inline = true)
    private List<StatInfo> statInfo;
    private String signature;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Default
    public static class StatInfo {
        private Integer accountType;
        private Integer times;
        private Integer totalMoney;
    }

}
