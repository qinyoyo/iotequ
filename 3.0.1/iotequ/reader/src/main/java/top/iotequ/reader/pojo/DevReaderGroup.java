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
import java.util.*;

//  Pojo entity : DevReaderGroup (设备分组)
@CgTableAnnotation(name="dev_reader_group",
                   title="devReaderGroup",
                   baseUrl="/reader/devReaderGroup",
                   hasLicence=false,
                   parentEntityField="parent",
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devReaderGroup",
                   module="reader")
@Getter
@Setter
public class DevReaderGroup implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//主键 db field:id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String name;		//组名称 db field:name

    @SerializedName(value = "parent", alternate = {"PARENT"})
    @CgFieldAnnotation(name="parent",jdbcType="INTEGER",nullable=true,format="")
    private Integer parent;		//父节点ID db field:parent

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="org_code",jdbcType="INTEGER",nullable=false,format="")
    private Integer orgCode;		//归属部门 db field:org_code

    @SerializedName(value = "orgAuth", alternate = {"org_auth","ORG_AUTH"})
    @CgFieldAnnotation(name="org_auth",jdbcType="VARCHAR",length=256,nullable=true,format="@")
    private String orgAuth;		//部门通行权限 db field:org_auth

    @SerializedName(value = "subOrgAuth", alternate = {"sub_org_auth","SUB_ORG_AUTH"})
    @CgFieldAnnotation(name="sub_org_auth",jdbcType="VARCHAR",length=256,nullable=true,format="@")
    private String subOrgAuth;		//子部门通行权限 db field:sub_org_auth

    @SerializedName(value = "authGroupList", alternate = {"auth_group_list","AUTH_GROUP_LIST"})
    @CgFieldAnnotation(name="auth_group_list",jdbcType="VARCHAR",length=256,nullable=true,format="@")
    private String authGroupList;		//人员分组授权列表 db field:auth_group_list

    private List<DevReaderGroup> children;
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
