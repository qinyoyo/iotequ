/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevAuthGroup (人员授权分组)
@CgTableAnnotation(name="dev_auth_group",
                   title="devAuthGroup",
                   baseUrl="/reader/devAuthGroup",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devAuthGroup",
                   module="reader")
@Getter
@Setter
public class DevAuthGroup implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//分组名 db field:name

    @SerializedName(value = "auth", alternate = {"AUTH"})
    @CgFieldAnnotation(name="auth",jdbcType="VARCHAR",length=256,nullable=false,format="@")
    private String auth;		//授权权限 db field:auth

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
