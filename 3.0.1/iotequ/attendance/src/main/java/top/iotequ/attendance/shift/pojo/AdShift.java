/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.shift.pojo;
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
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : AdShift (考勤排班表)
@CgTableAnnotation(name="ad_shift",
                   title="adShift",
                   baseUrl="/attendance/shift/adShift",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="adShift",
                   module="attendance")
@Getter
@Setter
public class AdShift implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//排班名称 db field:name

    @SerializedName(value = "adMode", alternate = {"ad_mode","AD_MODE"})
    @CgFieldAnnotation(name="ad_mode",jdbcType="INTEGER",nullable=false,format="")
    private Integer adMode;		//考勤模式 db field:ad_mode

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "startDate", alternate = {"start_date","START_DATE"})
    @CgFieldAnnotation(name="start_date",jdbcType="DATE",nullable=false,format="yyyy-mm-dd")
    private Date startDate;		//启用日期 db field:start_date

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "endDate", alternate = {"end_date","END_DATE"})
    @CgFieldAnnotation(name="end_date",jdbcType="DATE",nullable=false,format="yyyy-mm-dd")
    private Date endDate;		//终止时间(含) db field:end_date

    @SerializedName(value = "minutesPerDay", alternate = {"minutes_per_day","MINUTES_PER_DAY"})
    @CgFieldAnnotation(name="minutes_per_day",jdbcType="INTEGER",nullable=false,format="")
    private Integer minutesPerDay;		//工作时长(分) db field:minutes_per_day

    @SerializedName(value = "description", alternate = {"DESCRIPTION"})
    @CgFieldAnnotation(name="description",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String description;		//描述 db field:description

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
