/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.checkIn.pojo;
import top.iotequ.util.*;
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
import top.iotequ.framework.serializer.jackson.TimeSerializer;
import top.iotequ.framework.serializer.gson.GsonTimeTypeAdapter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : CkSignIn (认证记录)
@CgTableAnnotation(name="ck_sign_in",
                   title="ckSignIn",
                   join="LEFT JOIN dev_people ON ck_sign_in.user_no = dev_people.user_no",
                   baseUrl="/check-in/ckSignIn",
                   hasLicence=true,
                   trialDays=160,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="ckSignIn",
                   module="check-in")
@Getter
@Setter
public class CkSignIn implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ck_sign_in.id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String id;

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="ck_sign_in.org_code",jdbcType="INTEGER",length=11,nullable=false,format="")
    private Integer orgCode;		//医院科室|Depart db field:org_code

    @SerializedName(value = "recSource", alternate = {"rec_source","REC_SOURCE"})
    @CgFieldAnnotation(name="ck_sign_in.rec_source",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String recSource;		//来源|Source db field:rec_source

    @SerializedName(value = "recSourceType", alternate = {"rec_source_type","REC_SOURCE_TYPE"})
    @CgFieldAnnotation(name="ck_sign_in.rec_source_type",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String recSourceType;		//来源类别|Device type db field:rec_source_type

    @SerializedName(value = "eventType", alternate = {"event_type","EVENT_TYPE"})
    @CgFieldAnnotation(name="ck_sign_in.event_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer eventType;		//事件类别 db field:event_type

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "recTime", alternate = {"rec_time","REC_TIME"})
    @CgFieldAnnotation(name="ck_sign_in.rec_time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date recTime;		//时间|Sign time db field:rec_time

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="ck_sign_in.user_no",jdbcType="VARCHAR",length=15,nullable=false,format="@")
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

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "dateDate", alternate = {"date_date","DATE_DATE"})
    @CgFieldAnnotation(name="date_date",expression="date(rec_time)",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date dateDate;		//日期|Date db field:date_date:date(rec_time)

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "dateTime", alternate = {"date_time","DATE_TIME"})
    @CgFieldAnnotation(name="date_time",expression="time(rec_time)",jdbcType="TIME",nullable=true,format="hh:mm")
    private Date dateTime;		//时间|Time db field:date_time:time(rec_time)

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
