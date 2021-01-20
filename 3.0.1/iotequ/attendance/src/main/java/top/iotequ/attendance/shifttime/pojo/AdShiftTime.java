/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.shifttime.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.TimeSerializer;
import top.iotequ.framework.serializer.gson.GsonTimeTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : AdShiftTime (排班时间表)
@CgTableAnnotation(name="ad_shift_time",
                   title="adShiftTime",
                   baseUrl="/attendance/shifttime/adShiftTime",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="adShiftTime",
                   module="attendance")
@Getter
@Setter
public class AdShiftTime implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "shiftId", alternate = {"shift_id","SHIFT_ID"})
    @CgFieldAnnotation(name="shift_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer shiftId;		//排班编号 db field:shift_id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=100,nullable=false,format="@")
    private String name;		//详细描述 db field:name

    @SerializedName(value = "weekDays", alternate = {"week_days","WEEK_DAYS"})
    @CgFieldAnnotation(name="week_days",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String weekDays;		//工作日序列 db field:week_days

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "startWorkTime", alternate = {"start_work_time","START_WORK_TIME"})
    @CgFieldAnnotation(name="start_work_time",jdbcType="TIME",nullable=false,format="hh:mm")
    private Date startWorkTime;		//上班时间 db field:start_work_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "endWorkTime", alternate = {"end_work_time","END_WORK_TIME"})
    @CgFieldAnnotation(name="end_work_time",jdbcType="TIME",nullable=false,format="hh:mm")
    private Date endWorkTime;		//下班时间 db field:end_work_time

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
