/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.checkIn.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.DateSerializer;
import top.iotequ.framework.serializer.gson.GsonDateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import top.iotequ.framework.serializer.jackson.TimeSerializer;
import top.iotequ.framework.serializer.gson.GsonTimeTypeAdapter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : CkRegister (上机记录)
@CgTableAnnotation(name="ck_register",
                   title="ckRegister",
                   baseUrl="/check-in/ckRegister",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="ckRegister",
                   module="check-in")
@Getter
@Setter
public class CkRegister implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="CHAR",length=32,nullable=false,format="@")
    private String id;		//ID db field:id

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String userNo;		//用户号 db field:user_no

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String name;		//姓名 db field:name

    @SerializedName(value = "sex", alternate = {"SEX"})
    @CgFieldAnnotation(name="sex",jdbcType="VARCHAR",length=1,nullable=false,format="@")
    private String sex;		//性别 db field:sex

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "birthDate", alternate = {"birth_date","BIRTH_DATE"})
    @CgFieldAnnotation(name="birth_date",jdbcType="DATE",length=36,nullable=true,format="yyyy-mm-dd")
    private Date birthDate;		//出生日 db field:birth_date

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="org_code",jdbcType="INTEGER",length=36,nullable=false,format="")
    private Integer orgCode;		//科室编号 db field:org_code

    @SerializedName(value = "orgName", alternate = {"org_name","ORG_NAME"})
    @CgFieldAnnotation(name="org_name",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String orgName;		//医院科室 db field:org_name

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "inDate", alternate = {"in_date","IN_DATE"})
    @CgFieldAnnotation(name="in_date",jdbcType="DATE",length=36,nullable=false,format="yyyy-mm-dd")
    private Date inDate;		//上机日 db field:in_date

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "onTime", alternate = {"on_time","ON_TIME"})
    @CgFieldAnnotation(name="on_time",jdbcType="TIME",length=6,nullable=false,format="hh:mm")
    private Date onTime;		//上机时间 db field:on_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "offTime", alternate = {"off_time","OFF_TIME"})
    @CgFieldAnnotation(name="off_time",jdbcType="TIME",length=6,nullable=true,format="hh:mm")
    private Date offTime;		//下机时间 db field:off_time

    private String mode;		//模式 非数据库字段

    @Override public Object getPkValue(){ return getId(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setId(null);
        else setId(String.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
//^_^ Your code end.
}
