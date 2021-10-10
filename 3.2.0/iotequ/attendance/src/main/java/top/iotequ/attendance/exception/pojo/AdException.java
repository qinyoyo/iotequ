/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.exception.pojo;
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
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : AdException (节假日调休安排)
@CgTableAnnotation(name="ad_exception",
                   title="adException",
                   baseUrl="/attendance/exception/adException",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="adException",
                   module="attendance")
@Getter
@Setter
public class AdException implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "shiftId", alternate = {"shift_id","SHIFT_ID"})
    @CgFieldAnnotation(name="shift_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer shiftId;		//排班编号 db field:shift_id

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "endDate", alternate = {"end_date","END_DATE"})
    @CgFieldAnnotation(name="end_date",jdbcType="DATE",nullable=false,format="yyyy-mm-dd")
    private Date endDate;		//结束日期(含) db field:end_date

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "startDate", alternate = {"start_date","START_DATE"})
    @CgFieldAnnotation(name="start_date",jdbcType="DATE",nullable=false,format="yyyy-mm-dd")
    private Date startDate;		//开始日期 db field:start_date

    @SerializedName(value = "weekDay", alternate = {"week_day","WEEK_DAY"})
    @CgFieldAnnotation(name="week_day",jdbcType="INTEGER",nullable=false,format="")
    private Integer weekDay;		//工作属性 db field:week_day

    @SerializedName(value = "description", alternate = {"DESCRIPTION"})
    @CgFieldAnnotation(name="description",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String description;		//详细描述 db field:description

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
