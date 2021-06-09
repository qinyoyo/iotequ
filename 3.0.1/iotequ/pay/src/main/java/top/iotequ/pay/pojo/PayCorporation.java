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

//  Pojo entity : PayCorporation (运营公司)
@CgTableAnnotation(name="pay_corporation",
                   title="payCorporation",
                   baseUrl="/pay/payCorporation",
                   hasLicence=false,
                   parentEntityField="parentId",
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="payCorporation",
                   module="pay")
@Getter
@Setter
public class PayCorporation implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "parentId", alternate = {"parent_id","PARENT_ID"})
    @CgFieldAnnotation(name="parent_id",jdbcType="INTEGER",nullable=true,format="")
    private Integer parentId;		//上级公司 db field:parent_id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//名称 db field:name

    @SerializedName(value = "address", alternate = {"ADDRESS"})
    @CgFieldAnnotation(name="address",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String address;		//地址 db field:address

    @SerializedName(value = "linkman", alternate = {"LINKMAN"})
    @CgFieldAnnotation(name="linkman",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String linkman;		//联系人 db field:linkman

    @SerializedName(value = "linkphone", alternate = {"LINKPHONE"})
    @CgFieldAnnotation(name="linkphone",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String linkphone;		//联系电话 db field:linkphone

    private List<PayCorporation> children;
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
