/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : CgProject (项目)
@CgTableAnnotation(name="cg_project",
                   title="cgProject",
                   baseUrl="/codegenerator/cgProject",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="cgProject",
                   module="codegenerator")
@Getter
@Setter
public class CgProject implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String id;		//id db field:id

    @SerializedName(value = "code", alternate = {"CODE"})
    @CgFieldAnnotation(name="code",jdbcType="VARCHAR",length=10,nullable=false,format="@")
    private String code;		//代码 db field:code

    @SerializedName(value = "creator", alternate = {"CREATOR"})
    @CgFieldAnnotation(name="creator",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String creator;		//创建人 db field:creator

    @SerializedName(value = "groupId", alternate = {"group_id","GROUP_ID"})
    @CgFieldAnnotation(name="group_id",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String groupId;		//组织机构 db field:group_id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String name;		//项目名称 db field:name

    @SerializedName(value = "version", alternate = {"VERSION"})
    @CgFieldAnnotation(name="version",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String version;		//版本 db field:version

    @SerializedName(value = "note", alternate = {"NOTE"})
    @CgFieldAnnotation(name="note",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String note;		//项目描述 db field:note

    @SerializedName(value = "modules", alternate = {"MODULES"})
    @CgFieldAnnotation(name="modules",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String modules;		//包含的iotequ模块 db field:modules

    @SerializedName(value = "springModule", alternate = {"spring_module","SPRING_MODULE"})
    @CgFieldAnnotation(name="spring_module",jdbcType="TINYINT",length=1,nullable=false,format="")
    private Boolean springModule;		//spring模块 db field:spring_module

    @SerializedName(value = "mavenModule", alternate = {"maven_module","MAVEN_MODULE"})
    @CgFieldAnnotation(name="maven_module",jdbcType="TINYINT",nullable=false,format="")
    private Boolean mavenModule;		//maven模块 db field:maven_module

    @SerializedName(value = "mavenServer", alternate = {"maven_server","MAVEN_SERVER"})
    @CgFieldAnnotation(name="maven_server",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String mavenServer;		//内部maven库地址 db field:maven_server

    @SerializedName(value = "addtionalDependencies", alternate = {"addtional_dependencies","ADDTIONAL_DEPENDENCIES"})
    @CgFieldAnnotation(name="addtional_dependencies",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String addtionalDependencies;		//附件依赖 db field:addtional_dependencies

    @SerializedName(value = "test", alternate = {"TEST"})
    @CgFieldAnnotation(name="test",jdbcType="TINYINT",nullable=false,format="")
    private Boolean test;		//test依赖模块 db field:test

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
