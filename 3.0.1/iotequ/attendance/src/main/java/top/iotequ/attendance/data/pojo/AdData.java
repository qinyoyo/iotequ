/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.data.pojo;
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

//  Pojo entity : AdData (考勤数据)
@CgTableAnnotation(name="ad_data",
                   title="adData",
                   join="LEFT JOIN ad_employee ON ad_data.employee_no = ad_employee.employee_no LEFT JOIN sys_user employee_no_sys_user ON ad_employee.id = employee_no_sys_user.id",
                   baseUrl="/attendance/data/adData",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="adData",
                   module="attendance")
@Getter
@Setter
public class AdData implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ad_data.id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String id;

    @SerializedName(value = "recSourceType", alternate = {"rec_source_type","REC_SOURCE_TYPE"})
    @CgFieldAnnotation(name="ad_data.rec_source_type",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String recSourceType;		//来源类别 db field:rec_source_type

    @SerializedName(value = "recSource", alternate = {"rec_source","REC_SOURCE"})
    @CgFieldAnnotation(name="ad_data.rec_source",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String recSource;		//来源 db field:rec_source

    @SerializedName(value = "recType", alternate = {"rec_type","REC_TYPE"})
    @CgFieldAnnotation(name="ad_data.rec_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer recType;		//类别 db field:rec_type

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "recTime", alternate = {"rec_time","REC_TIME"})
    @CgFieldAnnotation(name="ad_data.rec_time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date recTime;		//时间 db field:rec_time

    @SerializedName(value = "isUsed", alternate = {"is_used","IS_USED"})
    @CgFieldAnnotation(name="ad_data.is_used",jdbcType="VARCHAR",nullable=false,format="")
    private Boolean isUsed;		//被使用 db field:is_used

    @SerializedName(value = "employeeNo", alternate = {"employee_no","EMPLOYEE_NO"})
    @CgFieldAnnotation(name="ad_data.employee_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String employeeNo;		//工号 db field:employee_no
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="employee_no_sys_user.real_name",jdbcType="VARCHAR")
    private String realName;
    @SerializedName(value = "isAttendance", alternate = {"is_attendance","IS_ATTENDANCE"})
    @CgFieldAnnotation(name="ad_employee.is_attendance",jdbcType="VARCHAR")
    private Boolean isAttendance;
    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="employee_no_sys_user.org_code",jdbcType="VARCHAR")
    private Integer orgCode;

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "dateDate", alternate = {"date_date","DATE_DATE"})
    @CgFieldAnnotation(name="date_date",expression="date(rec_time)",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date dateDate;		//日期 db field:date_date:date(rec_time)

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "dateTime", alternate = {"date_time","DATE_TIME"})
    @CgFieldAnnotation(name="date_time",expression="time(rec_time)",jdbcType="TIME",nullable=true,format="hh:mm")
    private Date dateTime;		//时间 db field:date_time:time(rec_time)

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
