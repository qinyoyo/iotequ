/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;

//  Pojo entity : DevOrgGroup (分组部门)
@CgTableAnnotation(name="dev_org_group",
                   title="devOrgGroup",
                   join="RIGHT JOIN sys_org ON dev_org_group.org_id = sys_org.org_code",
                   baseUrl="/reader/devOrgGroup",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devOrgGroup",
                   module="reader")
@Getter
@Setter
public class DevOrgGroup implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="dev_org_group.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "groupId", alternate = {"group_id","GROUP_ID"})
    @CgFieldAnnotation(name="dev_org_group.group_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer groupId;		//分组id db field:group_id

    @SerializedName(value = "isIncludeSubOrg", alternate = {"is_include_sub_org","IS_INCLUDE_SUB_ORG"})
    @CgFieldAnnotation(name="dev_org_group.is_include_sub_org",jdbcType="TINYINT",nullable=false,format="")
    private Boolean isIncludeSubOrg;		//包括子部门 db field:is_include_sub_org

    @SerializedName(value = "orgId", alternate = {"org_id","ORG_ID"})
    @CgFieldAnnotation(name="dev_org_group.org_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer orgId;		//部门 db field:org_id
    @SerializedName(value = "orgName", alternate = {"org_name","ORG_NAME"})
    @CgFieldAnnotation(name="sys_org.name",jdbcType="INTEGER")
    private String orgName;

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
