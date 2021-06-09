/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : CgTable (表单定义|Cg table)
@CgTableAnnotation(name="cg_table",
                   title="cgTable",
                   join="JOIN cg_project ON cg_table.project_id = cg_project.id",
                   baseUrl="/codegenerator/cgTable",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="cgTable",
                   module="codegenerator")
@Getter
@Setter
public class CgTable implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="cg_table.id",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String id;		//主键ID db field:id

    @SerializedName(value = "code", alternate = {"CODE"})
    @CgFieldAnnotation(name="cg_table.code",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String code;		//cg代码 db field:code

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="cg_table.name",jdbcType="VARCHAR",length=32,nullable=true,format="@")
    private String name;		//数据库表 db field:name

    @SerializedName(value = "title", alternate = {"TITLE"})
    @CgFieldAnnotation(name="cg_table.title",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String title;		//标题 db field:title

    @SerializedName(value = "subPackage", alternate = {"sub_package","SUB_PACKAGE"})
    @CgFieldAnnotation(name="cg_table.sub_package",jdbcType="VARCHAR",length=30,nullable=true,format="@")
    private String subPackage;		//子包 db field:sub_package

    @SerializedName(value = "entity", alternate = {"ENTITY"})
    @CgFieldAnnotation(name="cg_table.entity",jdbcType="VARCHAR",length=32,nullable=true,format="@")
    private String entity;		//Entity类名 db field:entity

    @SerializedName(value = "template", alternate = {"TEMPLATE"})
    @CgFieldAnnotation(name="cg_table.template",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String template;		//模板名 db field:template

    @SerializedName(value = "trialLicence", alternate = {"trial_licence","TRIAL_LICENCE"})
    @CgFieldAnnotation(name="cg_table.trial_licence",jdbcType="INTEGER",nullable=true,format="")
    private Integer trialLicence;		//试用licence db field:trial_licence

    @SerializedName(value = "trialDays", alternate = {"trial_days","TRIAL_DAYS"})
    @CgFieldAnnotation(name="cg_table.trial_days",jdbcType="INTEGER",nullable=true,format="")
    private Integer trialDays;		//试用天数 db field:trial_days

    @SerializedName(value = "actionList", alternate = {"action_list","ACTION_LIST"})
    @CgFieldAnnotation(name="cg_table.action_list",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String actionList;		//功能列表 db field:action_list

    @SerializedName(value = "imports", alternate = {"IMPORTS"})
    @CgFieldAnnotation(name="cg_table.imports",jdbcType="VARCHAR",length=500,nullable=true,format="@")
    private String imports;		//import列表 db field:imports

    @SerializedName(value = "controllerExtends", alternate = {"controller_extends","CONTROLLER_EXTENDS"})
    @CgFieldAnnotation(name="cg_table.controller_extends",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String controllerExtends;		//controller基类以及实现接口 db field:controller_extends

    @SerializedName(value = "pojoImports", alternate = {"pojo_imports","POJO_IMPORTS"})
    @CgFieldAnnotation(name="cg_table.pojo_imports",jdbcType="VARCHAR",length=500,nullable=true,format="@")
    private String pojoImports;		//pojo基类列表 db field:pojo_imports

    @SerializedName(value = "pojoExtends", alternate = {"pojo_extends","POJO_EXTENDS"})
    @CgFieldAnnotation(name="cg_table.pojo_extends",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String pojoExtends;		//pojo实现接口和继承类 db field:pojo_extends

    @SerializedName(value = "pojoJavaCode", alternate = {"pojo_java_code","POJO_JAVA_CODE"})
    @CgFieldAnnotation(name="cg_table.pojo_java_code",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String pojoJavaCode;		//pojo自定义代码 db field:pojo_java_code

    @SerializedName(value = "creator", alternate = {"CREATOR"})
    @CgFieldAnnotation(name="cg_table.creator",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String creator;		//创建人 db field:creator

    @SerializedName(value = "flowDynaFieldsOp", alternate = {"flow_dyna_fields_op","FLOW_DYNA_FIELDS_OP"})
    @CgFieldAnnotation(name="cg_table.flow_dyna_fields_op",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String flowDynaFieldsOp;		//处理人关联字段 db field:flow_dyna_fields_op

    @SerializedName(value = "flowDynaFieldsCp", alternate = {"flow_dyna_fields_cp","FLOW_DYNA_FIELDS_CP"})
    @CgFieldAnnotation(name="cg_table.flow_dyna_fields_cp",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String flowDynaFieldsCp;		//抄送人关联字段 db field:flow_dyna_fields_cp

    @SerializedName(value = "projectId", alternate = {"project_id","PROJECT_ID"})
    @CgFieldAnnotation(name="cg_table.project_id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String projectId;		//项目id db field:project_id
    @SerializedName(value = "project", alternate = {"PROJECT"})
    @CgFieldAnnotation(name="cg_project.code",jdbcType="VARCHAR")
    private String project;
    @SerializedName(value = "groupId", alternate = {"group_id","GROUP_ID"})
    @CgFieldAnnotation(name="cg_project.group_id",jdbcType="VARCHAR")
    private String groupId;
    @SerializedName(value = "module", alternate = {"MODULE"})
    @CgFieldAnnotation(name="cg_project.name",jdbcType="VARCHAR")
    private String module;

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
