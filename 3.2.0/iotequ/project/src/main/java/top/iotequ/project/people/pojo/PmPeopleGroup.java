/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.people.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : PmPeopleGroup (职能人员分组)
@CgTableAnnotation(name="pm_people_group",
                   title="pmPeopleGroup",
                   baseUrl="/project/people/pmPeopleGroup",
                   hasLicence=false,
                   parentEntityField="parent",
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="pmPeopleGroup",
                   module="project")
@Getter
@Setter
public class PmPeopleGroup implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//编号 db field:id

    @SerializedName(value = "parent", alternate = {"PARENT"})
    @CgFieldAnnotation(name="parent",jdbcType="INTEGER",length=8,nullable=true,format="")
    private Integer parent;		//上级 db field:parent

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//名称 db field:name

    @SerializedName(value = "groupType", alternate = {"group_type","GROUP_TYPE"})
    @CgFieldAnnotation(name="group_type",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String groupType;		//类别 db field:group_type

    @SerializedName(value = "enabled", alternate = {"ENABLED"})
    @CgFieldAnnotation(name="enabled",jdbcType="TINYINT",nullable=false,format="")
    private Boolean enabled;		//激活 db field:enabled

    @SerializedName(value = "description", alternate = {"DESCRIPTION"})
    @CgFieldAnnotation(name="description",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String description;		//职能描述 db field:description

    private List<PmPeopleGroup> children;
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
