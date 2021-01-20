/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.people.pojo;
import top.iotequ.util.*;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;

//  Pojo entity : PmPeople (项目人员)
@CgTableAnnotation(name="pm_people",
                   title="pmPeople",
                   join="RIGHT JOIN pm_people_group ON pm_people.group_id = pm_people_group.id RIGHT JOIN sys_user ON pm_people.user_id = sys_user.id",
                   baseUrl="/project/people/pmPeople",
                   hasLicence=true,
                   trialLicence=10,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="pmPeople",
                   module="project")
@Getter
@Setter
public class PmPeople implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="pm_people.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//编号 db field:id

    @SerializedName(value = "groupId", alternate = {"group_id","GROUP_ID"})
    @CgFieldAnnotation(name="pm_people.group_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer groupId;		//职能分组 db field:group_id
    @SerializedName(value = "groupName", alternate = {"group_name","GROUP_NAME"})
    @CgFieldAnnotation(name="pm_people_group.name",jdbcType="INTEGER")
    private String groupName;

    @SerializedName(value = "userId", alternate = {"user_id","USER_ID"})
    @CgFieldAnnotation(name="pm_people.user_id",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String userId;		//职员 db field:user_id
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="sys_user.real_name",jdbcType="VARCHAR")
    private String realName;
    @SerializedName(value = "sex", alternate = {"SEX"})
    @CgFieldAnnotation(name="sys_user.sex",jdbcType="VARCHAR")
    private String sex;
    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="sys_user.org_code",jdbcType="VARCHAR")
    private Integer orgCode;
    @SerializedName(value = "mobilePhone", alternate = {"mobile_phone","MOBILE_PHONE"})
    @CgFieldAnnotation(name="sys_user.mobile_phone",jdbcType="VARCHAR")
    private String mobilePhone;

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
