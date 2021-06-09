/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.framework.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : Action (功能列表)
@CgTableAnnotation(name="sys_action",
                   title="sysAction",
                   baseUrl="/framework/sysAction",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="sysAction",
                   module="framework")
@Getter
@Setter
public class Action implements CgEntity {
    @SerializedName(value = "note", alternate = {"NOTE"})
    @CgFieldAnnotation(name="note",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String note;		//描述 db field:note

    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//ID db field:id

    @SerializedName(value = "value", alternate = {"VALUE"})
    @CgFieldAnnotation(name="value",jdbcType="VARCHAR",length=100,nullable=false,format="@")
    private String value;		//值 db field:value

    @SerializedName(value = "params", alternate = {"PARAMS"})
    @CgFieldAnnotation(name="params",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String params;		//参数 db field:params

    @SerializedName(value = "method", alternate = {"METHOD"})
    @CgFieldAnnotation(name="method",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String method;		//方法 db field:method

    private String htmlNote;		//注意 非数据库字段

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
