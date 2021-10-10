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

//  Pojo entity : PayShop (商店)
@CgTableAnnotation(name="pay_shop",
                   title="payShop",
                   baseUrl="/pay/payShop",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="payShop",
                   module="pay")
@Getter
@Setter
public class PayShop implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//ID db field:id

    @SerializedName(value = "corporationId", alternate = {"corporation_id","CORPORATION_ID"})
    @CgFieldAnnotation(name="corporation_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer corporationId;		//归属公司 db field:corporation_id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//店名 db field:name

    @SerializedName(value = "linkman", alternate = {"LINKMAN"})
    @CgFieldAnnotation(name="linkman",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String linkman;		//联系人 db field:linkman

    @SerializedName(value = "linkPhone", alternate = {"link_phone","LINK_PHONE"})
    @CgFieldAnnotation(name="link_phone",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String linkPhone;		//联系电话 db field:link_phone

    @SerializedName(value = "address", alternate = {"ADDRESS"})
    @CgFieldAnnotation(name="address",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String address;		//地址 db field:address

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
