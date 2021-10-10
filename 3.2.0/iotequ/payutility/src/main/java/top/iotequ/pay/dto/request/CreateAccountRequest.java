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
public class CreateAccountRequest extends BaseRequest{
    private String userNo;
    private Integer gender;

    @Element(required = false)
    private String mobilePhone;
    @Element(required = false)
    private String email;
    @Element(required = false)
    private String wechatOpenid;
    @Element(required = false)
    private String birthDate;
    @Element(required = false)
    private String memberGroup;

    private Integer costLimit;
    private Integer dayLimit;

    @Element(required = false)
    private String activeSince;
    @Element(required = false)
    private String expireAt;

    private String signature;
}
