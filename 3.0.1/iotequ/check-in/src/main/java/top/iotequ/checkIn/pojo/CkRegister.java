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
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import top.iotequ.framework.serializer.jackson.DateSerializer;
import top.iotequ.framework.serializer.gson.GsonDateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : CkRegister (上机记录)
@CgTableAnnotation(name="ck_register",
                   title="ckRegister",
                   join="LEFT JOIN dev_people ON ck_register.user_no = dev_people.user_no",
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
    @CgFieldAnnotation(name="ck_register.id",jdbcType="CHAR",length=32,nullable=false,format="@")
    private String id;		//ID db field:id

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="ck_register.org_code",jdbcType="INTEGER",length=36,nullable=false,format="")
    private Integer orgCode;		//医院科室 db field:org_code

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "inTime", alternate = {"in_time","IN_TIME"})
    @CgFieldAnnotation(name="ck_register.in_time",jdbcType="TIMESTAMP",length=36,nullable=false,format="yyyy-mm-dd hh:mm")
    private Date inTime;		//上机日 db field:in_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "outTime", alternate = {"out_time","OUT_TIME"})
    @CgFieldAnnotation(name="ck_register.out_time",jdbcType="TIMESTAMP",length=36,nullable=true,format="yyyy-mm-dd hh:mm")
    private Date outTime;		//下机时间 db field:out_time

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="ck_register.user_no",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String userNo;		//用户号 db field:user_no
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="dev_people.real_name",jdbcType="VARCHAR")
    private String realName;
    @SerializedName(value = "sex", alternate = {"SEX"})
    @CgFieldAnnotation(name="dev_people.sex",jdbcType="VARCHAR")
    private String sex;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "birthDate", alternate = {"birth_date","BIRTH_DATE"})
    @CgFieldAnnotation(name="dev_people.birth_date",jdbcType="VARCHAR")
    private Date birthDate;

    @SerializedName(value = "onTime", alternate = {"on_time","ON_TIME"})
    @CgFieldAnnotation(name="on_time",expression="in_time",jdbcType="VARCHAR",length=36,nullable=true,format="hh:mm")
    private String onTime;		//上机时间 db field:on_time:in_time

    @SerializedName(value = "offTime", alternate = {"off_time","OFF_TIME"})
    @CgFieldAnnotation(name="off_time",expression="out_time",jdbcType="VARCHAR",length=36,nullable=true,format="hh:mm")
    private String offTime;		//下机时间 db field:off_time:out_time

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
