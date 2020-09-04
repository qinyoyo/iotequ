/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.framework.util.StringUtil;
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
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevAuthConfig (权限配置)
@CgTableAnnotation(name="dev_auth_config",
                   title="devAuthConfig",
                   baseUrl="/reader/devAuthConfig",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devAuthConfig",
                   module="reader")
@Getter
@Setter
public class DevAuthConfig implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",length=11,nullable=false,format="")
    private Integer id;		//主键 db field:id

    @SerializedName(value = "roleId", alternate = {"role_id","ROLE_ID"})
    @CgFieldAnnotation(name="role_id",jdbcType="INTEGER",length=11,nullable=false,format="")
    private Integer roleId;		//配置id db field:role_id

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "beginAt", alternate = {"begin_at","BEGIN_AT"})
    @CgFieldAnnotation(name="begin_at",jdbcType="DATE",length=36,nullable=true,format="yyyy-mm-dd")
    private Date beginAt;		//开始日期 db field:begin_at

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "endAt", alternate = {"end_at","END_AT"})
    @CgFieldAnnotation(name="end_at",jdbcType="DATE",length=36,nullable=true,format="yyyy-mm-dd")
    private Date endAt;		//结束日期 db field:end_at

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "startTime", alternate = {"start_time","START_TIME"})
    @CgFieldAnnotation(name="start_time",jdbcType="TIME",length=6,nullable=true,format="hh:mm")
    private Date startTime;		//开始时间 db field:start_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "endTime", alternate = {"end_time","END_TIME"})
    @CgFieldAnnotation(name="end_time",jdbcType="TIME",length=6,nullable=true,format="hh:mm")
    private Date endTime;		//结束时间 db field:end_time

    @SerializedName(value = "onlyWorkDay", alternate = {"only_work_day","ONLY_WORK_DAY"})
    @CgFieldAnnotation(name="only_work_day",jdbcType="VARCHAR",length=36,nullable=false,format="")
    private Boolean onlyWorkDay;		//仅工作日有效 db field:only_work_day

    @SerializedName(value = "auth", alternate = {"AUTH"})
    @CgFieldAnnotation(name="auth",jdbcType="INTEGER",length=11,nullable=false,format="")
    private Integer auth;		//权限 db field:auth

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
