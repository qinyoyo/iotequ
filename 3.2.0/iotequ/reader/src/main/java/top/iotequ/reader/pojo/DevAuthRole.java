/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevAuthRole (权限定义|Authorization role)
@CgTableAnnotation(name="dev_auth_role",
                   title="devAuthRole",
                   baseUrl="/reader/devAuthRole",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devAuthRole",
                   module="reader")
@Getter
@Setter
public class DevAuthRole implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",length=11,nullable=false,format="")
    private Integer id;		//主键 db field:id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String name;		//配置名称 db field:name

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
