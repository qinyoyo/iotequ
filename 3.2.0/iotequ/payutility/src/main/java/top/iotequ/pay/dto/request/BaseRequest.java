package top.iotequ.pay.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Root;
import top.iotequ.pay.dto.PayBaseDto;

@Getter
@Setter
@Root(name = "root")
@Default
public abstract class BaseRequest extends PayBaseDto {
    private Integer operation;      // 	操作类型	Byte , 必须使用private，否则javax报错
    private String  deviceNo;       // 	设备编号
    private Integer shopId;        //   商户编号	integer
    private String  deviceOrderNo;  //  设备流水号
    private Integer loginId;       //   登录号
}
