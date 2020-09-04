/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : EwDevice (授信设备)
@CgTableAnnotation(name="ew_device",
                   title="ewDevice",
                   baseUrl="/ewallet/ewDevice",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="ewDevice",
                   module="ewallet")
@Getter
@Setter
public class EwDevice implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "deviceNo", alternate = {"device_no","DEVICE_NO"})
    @CgFieldAnnotation(name="device_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String deviceNo;		//设备号 db field:device_no

    @SerializedName(value = "shopId", alternate = {"shop_id","SHOP_ID"})
    @CgFieldAnnotation(name="shop_id",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String shopId;		//商家编号 db field:shop_id

    @SerializedName(value = "privilegeList", alternate = {"privilege_list","PRIVILEGE_LIST"})
    @CgFieldAnnotation(name="privilege_list",jdbcType="VARCHAR",length=64,nullable=false,format="@")
    private String privilegeList;		//消费权限 db field:privilege_list

    @Override public Object getPkValue(){ return getId(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setId(null);
        else setId(Integer.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
//^_^ Your code end.
}
