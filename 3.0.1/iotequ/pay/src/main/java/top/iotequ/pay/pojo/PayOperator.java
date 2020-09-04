/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.pay.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : PayOperator (操作员)
@CgTableAnnotation(name="pay_operator",
                   title="payOperator",
                   baseUrl="/pay/payOperator",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="payOperator",
                   module="pay")
@Getter
@Setter
public class PayOperator implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//登录名 db field:name

    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="real_name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String realName;		//真实名 db field:real_name

    @SerializedName(value = "password", alternate = {"PASSWORD"})
    @CgFieldAnnotation(name="password",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String password;		//密码 db field:password

    @SerializedName(value = "shopId", alternate = {"shop_id","SHOP_ID"})
    @CgFieldAnnotation(name="shop_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer shopId;		//所属店铺 db field:shop_id

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String userNo;		//用户编号 db field:user_no

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
