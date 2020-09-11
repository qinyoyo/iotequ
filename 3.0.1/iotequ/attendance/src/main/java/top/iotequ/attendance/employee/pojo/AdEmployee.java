/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.employee.pojo;
import top.iotequ.framework.util.*;
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

//  Pojo entity : AdEmployee (考勤职员表)
@CgTableAnnotation(name="ad_employee",
                   title="adEmployee",
                   join="RIGHT JOIN sys_user ON ad_employee.id = sys_user.id",
                   baseUrl="/attendance/employee/adEmployee",
                   hasLicence=true,
                   trialLicence=100,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="adEmployee",
                   module="attendance")
@Getter
@Setter
public class AdEmployee implements CgEntity {
    @SerializedName(value = "employeeNo", alternate = {"employee_no","EMPLOYEE_NO"})
    @CgFieldAnnotation(name="ad_employee.employee_no",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String employeeNo;		//工号 db field:employee_no

    @SerializedName(value = "emLevel", alternate = {"em_level","EM_LEVEL"})
    @CgFieldAnnotation(name="ad_employee.em_level",jdbcType="INTEGER",nullable=true,format="")
    private Integer emLevel;		//职务级别 db field:em_level

    @SerializedName(value = "isAttendance", alternate = {"is_attendance","IS_ATTENDANCE"})
    @CgFieldAnnotation(name="ad_employee.is_attendance",jdbcType="TINYINT",nullable=false,format="")
    private Boolean isAttendance;		//是否考勤 db field:is_attendance

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "enterDate", alternate = {"enter_date","ENTER_DATE"})
    @CgFieldAnnotation(name="ad_employee.enter_date",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date enterDate;		//入职日期 db field:enter_date

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "leaveDate", alternate = {"leave_date","LEAVE_DATE"})
    @CgFieldAnnotation(name="ad_employee.leave_date",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date leaveDate;		//离职日期 db field:leave_date

    @SerializedName(value = "overtimeMinutes", alternate = {"overtime_minutes","OVERTIME_MINUTES"})
    @CgFieldAnnotation(name="ad_employee.overtime_minutes",jdbcType="INTEGER",nullable=false,format="")
    private Integer overtimeMinutes;		//可调休时间 db field:overtime_minutes

    @SerializedName(value = "shiftId", alternate = {"shift_id","SHIFT_ID"})
    @CgFieldAnnotation(name="ad_employee.shift_id",jdbcType="INTEGER",nullable=true,format="")
    private Integer shiftId;		//考勤排班 db field:shift_id

    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ad_employee.id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String id;		//用户 db field:id
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="sys_user.real_name",jdbcType="VARCHAR")
    private String realName;
    @SerializedName(value = "sex", alternate = {"SEX"})
    @CgFieldAnnotation(name="sys_user.sex",jdbcType="VARCHAR")
    private String sex;
    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="sys_user.org_code",jdbcType="VARCHAR")
    private Integer orgCode;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "birthDate", alternate = {"birth_date","BIRTH_DATE"})
    @CgFieldAnnotation(name="sys_user.birth_date",jdbcType="VARCHAR")
    private Date birthDate;

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
