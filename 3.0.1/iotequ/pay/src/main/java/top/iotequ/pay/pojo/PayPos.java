/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : PayPos (交易终端)
@CgTableAnnotation(name="pay_pos",
                   title="payPos",
                   baseUrl="/pay/payPos",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="payPos",
                   module="pay")
@Getter
@Setter
public class PayPos implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "no", alternate = {"NO"})
    @CgFieldAnnotation(name="no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String no;		//终端编号 db field:no

    @SerializedName(value = "shopId", alternate = {"shop_id","SHOP_ID"})
    @CgFieldAnnotation(name="shop_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer shopId;		//归属商店 db field:shop_id

    @SerializedName(value = "securityCode", alternate = {"security_code","SECURITY_CODE"})
    @CgFieldAnnotation(name="security_code",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String securityCode;		//安全码 db field:security_code

    @SerializedName(value = "workCode", alternate = {"work_code","WORK_CODE"})
    @CgFieldAnnotation(name="work_code",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String workCode;		//工作密钥 db field:work_code

    @SerializedName(value = "loginId", alternate = {"login_id","LOGIN_ID"})
    @CgFieldAnnotation(name="login_id",jdbcType="INTEGER",nullable=true,format="")
    private Integer loginId;		//签到ID db field:login_id

    @SerializedName(value = "ewalletActive", alternate = {"ewallet_active","EWALLET_ACTIVE"})
    @CgFieldAnnotation(name="ewallet_active",jdbcType="TINYINT",nullable=false,format="")
    private Boolean ewalletActive;		//钱包账户可用 db field:ewallet_active

    @SerializedName(value = "countProjectList", alternate = {"count_project_list","COUNT_PROJECT_LIST"})
    @CgFieldAnnotation(name="count_project_list",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String countProjectList;		//可用计次项目列表 db field:count_project_list

    @SerializedName(value = "timeProjectList", alternate = {"time_project_list","TIME_PROJECT_LIST"})
    @CgFieldAnnotation(name="time_project_list",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String timeProjectList;		//可用计时项目列表 db field:time_project_list

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
