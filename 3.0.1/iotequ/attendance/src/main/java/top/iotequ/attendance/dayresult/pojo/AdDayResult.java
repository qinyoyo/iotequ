/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.dayresult.pojo;
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

//  Pojo entity : AdDayResult (日考勤结果)
@CgTableAnnotation(name="ad_day_result",
                   title="adDayResult",
                   join="INNER JOIN ad_employee ON ad_day_result.employee_no = ad_employee.employee_no",
                   baseUrl="/attendance/dayresult/adDayResult",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="adDayResult",
                   module="attendance")
@Getter
@Setter
public class AdDayResult implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ad_day_result.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="ad_day_result.org_code",jdbcType="INTEGER",nullable=false,format="")
    private Integer orgCode;		//部门 db field:org_code

    @SerializedName(value = "orgName", alternate = {"org_name","ORG_NAME"})
    @CgFieldAnnotation(name="ad_day_result.org_name",jdbcType="VARCHAR",length=100,nullable=false,format="@")
    private String orgName;		//部门名 db field:org_name

    @SerializedName(value = "employee", alternate = {"EMPLOYEE"})
    @CgFieldAnnotation(name="ad_day_result.employee",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String employee;		//员工 db field:employee

    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="ad_day_result.real_name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String realName;		//姓名 db field:real_name

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "adDate", alternate = {"ad_date","AD_DATE"})
    @CgFieldAnnotation(name="ad_day_result.ad_date",jdbcType="DATE",nullable=false,format="yyyy-mm-dd")
    private Date adDate;		//日期 db field:ad_date

    @SerializedName(value = "shiftName", alternate = {"shift_name","SHIFT_NAME"})
    @CgFieldAnnotation(name="ad_day_result.shift_name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String shiftName;		//班次 db field:shift_name

    @SerializedName(value = "state", alternate = {"STATE"})
    @CgFieldAnnotation(name="ad_day_result.state",jdbcType="INTEGER",nullable=false,format="")
    private Integer state;		//考勤 db field:state

    @SerializedName(value = "stateName", alternate = {"state_name","STATE_NAME"})
    @CgFieldAnnotation(name="ad_day_result.state_name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String stateName;		//考勤描述 db field:state_name

    @SerializedName(value = "times", alternate = {"TIMES"})
    @CgFieldAnnotation(name="ad_day_result.times",jdbcType="INTEGER",nullable=false,format="")
    private Integer times;		//次数 db field:times

    @SerializedName(value = "minutes", alternate = {"MINUTES"})
    @CgFieldAnnotation(name="ad_day_result.minutes",jdbcType="INTEGER",nullable=false,format="")
    private Integer minutes;		//时长 db field:minutes

    @SerializedName(value = "workMinutes", alternate = {"work_minutes","WORK_MINUTES"})
    @CgFieldAnnotation(name="ad_day_result.work_minutes",jdbcType="INTEGER",nullable=false,format="")
    private Integer workMinutes;		//工作时长 db field:work_minutes

    @SerializedName(value = "shiftId", alternate = {"shift_id","SHIFT_ID"})
    @CgFieldAnnotation(name="ad_day_result.shift_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer shiftId;		//班次排序 db field:shift_id

    @SerializedName(value = "employeeNo", alternate = {"employee_no","EMPLOYEE_NO"})
    @CgFieldAnnotation(name="ad_day_result.employee_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String employeeNo;		//工号 db field:employee_no
    @SerializedName(value = "isAttendance", alternate = {"is_attendance","IS_ATTENDANCE"})
    @CgFieldAnnotation(name="ad_employee.is_attendance",jdbcType="VARCHAR")
    private Boolean isAttendance;

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
