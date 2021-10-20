package top.iotequ.pay.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import top.iotequ.pay.dto.request.LogoutRequest.StatInfo;

import java.util.List;

@Getter
@Setter
@Root(name = "root")
@Default
public class LogoutResponse extends BaseResponse {

    @Element(required = false)
    private String showMsg;
    private String signature;
    @ElementList(inline=true,required = false)
    private List<StatInfo> statInfo;
}
