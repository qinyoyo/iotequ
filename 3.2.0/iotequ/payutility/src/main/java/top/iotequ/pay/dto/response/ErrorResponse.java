package top.iotequ.pay.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;
import top.iotequ.pay.exception.PayException;


@Getter
@Setter
@Root(name = "root", strict = false)
@Default
public class ErrorResponse extends BaseResponse {
    private String signature;

    public ErrorResponse(@NonNull Exception e) {
        super(e);
    }

    public ErrorResponse(BaseResponse ack){
        super();
        setCode(ack.getCode());
        setMessage(ack.getMessage());
        try {
            setSignature(calculateSignature());
        } catch (Exception e) {
            setSignature("");
        }
    }
    public ErrorResponse() {
        super();
    }
}
