/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.adjust.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : AdAdjust (考勤调整|Action of attendance)
@CgTableAnnotation(name="ad_adjust",
                   title="adAdjust",
                   join="LEFT JOIN sys_user ON ad_adjust.employee = sys_user.id LEFT JOIN sys_user sys_user1 ON ad_adjust.hr = sys_user1.id LEFT JOIN sys_user sys_user2 ON ad_adjust.approver = sys_user2.id",
                   baseUrl="/attendance/adjust/adAdjust",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="adAdjust",
                   module="attendance")
@Getter
@Setter
public class AdAdjust implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ad_adjust.id",jdbcType="CHAR",length=32,nullable=false,format="@")
    private String id;		//id db field:id

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="ad_adjust.org_code",jdbcType="INTEGER",nullable=false,format="")
    private Integer orgCode;		//部门 db field:org_code

    @SerializedName(value = "adjustType", alternate = {"adjust_type","ADJUST_TYPE"})
    @CgFieldAnnotation(name="ad_adjust.adjust_type",jdbcType="INTEGER",nullable=true,format="")
    private Integer adjustType;		//调整类别 db field:adjust_type

    @SerializedName(value = "state", alternate = {"STATE"})
    @CgFieldAnnotation(name="ad_adjust.state",jdbcType="INTEGER",nullable=false,format="")
    private Integer state;		//审批状态 db field:state

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "registerTime", alternate = {"register_time","REGISTER_TIME"})
    @CgFieldAnnotation(name="ad_adjust.register_time",jdbcType="TIMESTAMP",length=12,nullable=false,format="yyyy-mm-dd hh:mm")
    private Date registerTime;		//登记时间 db field:register_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "startTime", alternate = {"start_time","START_TIME"})
    @CgFieldAnnotation(name="ad_adjust.start_time",jdbcType="TIMESTAMP",nullable=true,format="yyyy-mm-dd hh:mm")
    private Date startTime;		//开始时间 db field:start_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "endTime", alternate = {"end_time","END_TIME"})
    @CgFieldAnnotation(name="ad_adjust.end_time",jdbcType="TIMESTAMP",nullable=true,format="yyyy-mm-dd hh:mm")
    private Date endTime;		//结束时间 db field:end_time

    @SerializedName(value = "description", alternate = {"DESCRIPTION"})
    @CgFieldAnnotation(name="ad_adjust.description",jdbcType="VARCHAR",length=500,nullable=false,format="@")
    private String description;		//详情描述 db field:description

    @SerializedName(value = "approveOrg", alternate = {"approve_org","APPROVE_ORG"})
    @CgFieldAnnotation(name="ad_adjust.approve_org",jdbcType="INTEGER",nullable=true,format="")
    private Integer approveOrg;		//审批部门 db field:approve_org

    @SerializedName(value = "addFile", alternate = {"add_file","ADD_FILE"})
    @CgFieldAnnotation(name="ad_adjust.add_file",jdbcType="VARCHAR",length=200,nullable=true,multiple=true,format="@")
    private String addFile;		//附件 db field:add_file

    @SerializedName(value = "employee", alternate = {"EMPLOYEE"})
    @CgFieldAnnotation(name="ad_adjust.employee",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String employee;		//职员 db field:employee
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="sys_user.real_name",jdbcType="VARCHAR")
    private String realName;

    @SerializedName(value = "hr", alternate = {"HR"})
    @CgFieldAnnotation(name="ad_adjust.hr",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String hr;		//人事 db field:hr
    @SerializedName(value = "hrRealName", alternate = {"hr_real_name","HR_REAL_NAME"})
    @CgFieldAnnotation(name="sys_user1.real_name",jdbcType="VARCHAR")
    private String hrRealName;

    @SerializedName(value = "approver", alternate = {"APPROVER"})
    @CgFieldAnnotation(name="ad_adjust.approver",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String approver;		//后续审批人 db field:approver
    @SerializedName(value = "approverName", alternate = {"approver_name","APPROVER_NAME"})
    @CgFieldAnnotation(name="sys_user2.real_name",jdbcType="VARCHAR")
    private String approverName;

    private String flowAvailableActions;
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
