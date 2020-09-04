/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.specialshifttime.pojo;
import top.iotequ.framework.util.StringUtil;
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
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : AdSpecialShiftTime (特殊排班时间表)
@CgTableAnnotation(name="ad_special_shift_time",
                   title="adSpecialShiftTime",
                   baseUrl="/attendance/specialshifttime/adSpecialShiftTime",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="adSpecialShiftTime",
                   module="attendance")
@Getter
@Setter
public class AdSpecialShiftTime implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "specialShiftId", alternate = {"special_shift_id","SPECIAL_SHIFT_ID"})
    @CgFieldAnnotation(name="special_shift_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer specialShiftId;		//特殊排班编号 db field:special_shift_id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//名称 db field:name

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "startTime", alternate = {"start_time","START_TIME"})
    @CgFieldAnnotation(name="start_time",jdbcType="TIME",nullable=false,format="hh:mm")
    private Date startTime;		//开始时间 db field:start_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "endTime", alternate = {"end_time","END_TIME"})
    @CgFieldAnnotation(name="end_time",jdbcType="TIME",nullable=false,format="hh:mm")
    private Date endTime;		//结束时间 db field:end_time

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
