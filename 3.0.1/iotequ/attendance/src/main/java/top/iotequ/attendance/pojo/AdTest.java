/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : AdTest (她她)
@CgTableAnnotation(name="ad_test",
                   title="adTest",
                   join="LEFT JOIN ad_employee ON ad_test.id = ad_employee.id LEFT JOIN sys_user id_sys_user ON ad_employee.id = id_sys_user.id",
                   baseUrl="/attendance/adTest",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="adTest",
                   module="attendance")
@Getter
@Setter
public class AdTest implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ad_test.id",jdbcType="CHAR",length=32,nullable=false,format="@")
    private String id;		//ID db field:id
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="id_sys_user.real_name",jdbcType="CHAR")
    private String realName;
    @SerializedName(value = "isAttendance", alternate = {"is_attendance","IS_ATTENDANCE"})
    @CgFieldAnnotation(name="ad_employee.is_attendance",jdbcType="CHAR")
    private Boolean isAttendance;
    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="id_sys_user.org_code",jdbcType="CHAR")
    private Integer orgCode;

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
