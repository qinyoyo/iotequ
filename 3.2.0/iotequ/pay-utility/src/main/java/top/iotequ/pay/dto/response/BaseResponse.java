package top.iotequ.pay.dto.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import top.iotequ.pay.dto.PayBaseDto;
import top.iotequ.pay.exception.PayException;

@Getter
@Setter
@Root(name = "root")
@Default
public abstract class BaseResponse extends PayBaseDto {

    private Integer code;
    private String message;
    @Transient
    private String deviceNo;

    public BaseResponse(@NonNull Exception e) {
        super();
        setCode(e instanceof PayException ? ((PayException) e).getCode() : 1);
        String msg = e.getMessage();
        setMessage(msg == null ? e.getClass().getName() : msg);
    }

    public BaseResponse() {
        super();
    }

    @Override
    public String calculateSignature() throws PayException {
        if (getCode() == null) setCode(0);
        if (getMessage() == null) setMessage(getCode() == 0 ? "Success" : "Failure");
        return super.calculateSignature();
    }
}
