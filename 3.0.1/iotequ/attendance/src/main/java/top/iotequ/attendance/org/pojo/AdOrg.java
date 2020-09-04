/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.org.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : AdOrg (考勤部门信息表)
@CgTableAnnotation(name="ad_org",
                   title="adOrg",
                   join="RIGHT JOIN sys_org ON ad_org.org_code = sys_org.org_code LEFT JOIN sys_user ON ad_org.hr = sys_user.id LEFT JOIN sys_user sys_user1 ON ad_org.manager = sys_user1.id",
                   pk="org_code",
                   entityPk="orgCode",
                   baseUrl="/attendance/org/adOrg",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="3",
                   generatorName="adOrg",
                   module="attendance")
@Getter
@Setter
public class AdOrg implements CgEntity {
    @SerializedName(value = "shiftId", alternate = {"shift_id","SHIFT_ID"})
    @CgFieldAnnotation(name="ad_org.shift_id",jdbcType="INTEGER",nullable=true,format="")
    private Integer shiftId;		//部门排班 db field:shift_id

    @SerializedName(value = "manageLimit", alternate = {"manage_limit","MANAGE_LIMIT"})
    @CgFieldAnnotation(name="ad_org.manage_limit",jdbcType="INTEGER",nullable=true,format="")
    private Integer manageLimit;		//审核权限 db field:manage_limit

    @SerializedName(value = "deviation", alternate = {"DEVIATION"})
    @CgFieldAnnotation(name="ad_org.deviation",jdbcType="INTEGER",nullable=true,format="")
    private Integer deviation;		//允许误差 db field:deviation

    @SerializedName(value = "floatLimit", alternate = {"float_limit","FLOAT_LIMIT"})
    @CgFieldAnnotation(name="ad_org.float_limit",jdbcType="INTEGER",nullable=true,format="")
    private Integer floatLimit;		//浮动范围 db field:float_limit

    @SerializedName(value = "absentLimit", alternate = {"absent_limit","ABSENT_LIMIT"})
    @CgFieldAnnotation(name="ad_org.absent_limit",jdbcType="INTEGER",nullable=true,format="")
    private Integer absentLimit;		//旷工底限 db field:absent_limit

    @SerializedName(value = "freeWorkLimit", alternate = {"free_work_limit","FREE_WORK_LIMIT"})
    @CgFieldAnnotation(name="ad_org.free_work_limit",jdbcType="INTEGER",nullable=true,format="")
    private Integer freeWorkLimit;		//自由加班起限 db field:free_work_limit

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="ad_org.org_code",jdbcType="INTEGER",nullable=false,format="")
    private Integer orgCode;		//机构代码 db field:org_code
    @SerializedName(value = "sysOrgCode", alternate = {"sys_org_code","SYS_ORG_CODE"})
    @CgFieldAnnotation(name="sys_org.org_code",jdbcType="INTEGER")
    private Integer sysOrgCode;
    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="sys_org.name",jdbcType="INTEGER")
    private String name;
    @SerializedName(value = "parent", alternate = {"PARENT"})
    @CgFieldAnnotation(name="sys_org.parent",jdbcType="INTEGER")
    private Integer parent;

    @SerializedName(value = "hr", alternate = {"HR"})
    @CgFieldAnnotation(name="ad_org.hr",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String hr;		//人事 db field:hr
    @SerializedName(value = "hrName", alternate = {"hr_name","HR_NAME"})
    @CgFieldAnnotation(name="sys_user.real_name",jdbcType="VARCHAR")
    private String hrName;

    @SerializedName(value = "manager", alternate = {"MANAGER"})
    @CgFieldAnnotation(name="ad_org.manager",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String manager;		//考勤审核人 db field:manager
    @SerializedName(value = "managerName", alternate = {"manager_name","MANAGER_NAME"})
    @CgFieldAnnotation(name="sys_user1.real_name",jdbcType="VARCHAR")
    private String managerName;
    @SerializedName(value = "managerOrgCode", alternate = {"manager_org_code","MANAGER_ORG_CODE"})
    @CgFieldAnnotation(name="sys_user1.org_code",jdbcType="VARCHAR")
    private Integer managerOrgCode;

    @Override public Object getPkValue(){ return getOrgCode(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setOrgCode(null);
        else setOrgCode(Integer.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
//^_^ Your code end.
}
