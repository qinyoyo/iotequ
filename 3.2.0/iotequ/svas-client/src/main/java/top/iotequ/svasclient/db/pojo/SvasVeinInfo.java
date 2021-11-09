/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.svasclient.db.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;

//  Pojo entity : SvasVeinInfo (指静脉信息)
@CgTableAnnotation(name="dev_vein_info",
                   title="devVeinInfo",
                   baseUrl="/reader/devVeinInfo",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devVeinInfo",
                   module="reader")
@Getter
@Setter
public class SvasVeinInfo implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",nullable=false,format="@")
    private String userNo;		//用户号 db field:user_no

    @SerializedName(value = "fingerNo", alternate = {"finger_no","FINGER_NO"})
    @CgFieldAnnotation(name="finger_no",jdbcType="TINYINT",nullable=false,format="")
    private Byte fingerNo;		//手指编号 db field:finger_no

    @SerializedName(value = "templates", alternate = {"TEMPLATES"})
    @CgFieldAnnotation(name="templates",jdbcType="VARCHAR",nullable=false,format="@")
    private String templates;		//指静脉信息 db field:templates

    @SerializedName(value = "warning", alternate = {"WARNING"})
    @CgFieldAnnotation(name="warning",jdbcType="INTEGER",nullable=false,format="")
    private Integer warning;		//胁迫 db field:warning

    @SerializedName(value = "fingerName", alternate = {"finger_name","FINGER_NAME"})
    @CgFieldAnnotation(name="finger_name",jdbcType="VARCHAR",nullable=false,format="")
    private String fingerName;		//finger type db field:finger_name

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
