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

//  Pojo entity : CgForm (表单定义表)
@CgTableAnnotation(name="cg_form",
                   title="cgForm",
                   baseUrl="/codegenerator/cgForm",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="cgForm",
                   module="codegenerator")
@Getter
@Setter
public class CgForm implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="VARCHAR",length=64,nullable=false,format="@")
    private String id;		//ID db field:id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//名称 db field:name

    @SerializedName(value = "path", alternate = {"PATH"})
    @CgFieldAnnotation(name="path",jdbcType="VARCHAR",length=200,nullable=false,format="@")
    private String path;		//路径 db field:path

    @SerializedName(value = "tableId", alternate = {"table_id","TABLE_ID"})
    @CgFieldAnnotation(name="table_id",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String tableId;		//对应表定义 db field:table_id

    @SerializedName(value = "headTitle", alternate = {"head_title","HEAD_TITLE"})
    @CgFieldAnnotation(name="head_title",jdbcType="VARCHAR",length=400,nullable=false,format="@")
    private String headTitle;		//form标题 db field:head_title

    @SerializedName(value = "isFlow", alternate = {"is_flow","IS_FLOW"})
    @CgFieldAnnotation(name="is_flow",jdbcType="TINYINT",nullable=false,format="")
    private Boolean isFlow;		//是否流程定义 db field:is_flow

    @SerializedName(value = "icon", alternate = {"ICON"})
    @CgFieldAnnotation(name="icon",jdbcType="VARCHAR",length=300,nullable=true,format="@")
    private String icon;		//图标 db field:icon

    @SerializedName(value = "tagTitle", alternate = {"tag_title","TAG_TITLE"})
    @CgFieldAnnotation(name="tag_title",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String tagTitle;		//tag标题 db field:tag_title

    @SerializedName(value = "labelPosition", alternate = {"label_position","LABEL_POSITION"})
    @CgFieldAnnotation(name="label_position",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String labelPosition;		//字段标签位置 db field:label_position

    @SerializedName(value = "isDialog", alternate = {"is_dialog","IS_DIALOG"})
    @CgFieldAnnotation(name="is_dialog",jdbcType="TINYINT",nullable=false,format="")
    private Boolean isDialog;		//对话框模式 db field:is_dialog

    @SerializedName(value = "continueAdd", alternate = {"continue_add","CONTINUE_ADD"})
    @CgFieldAnnotation(name="continue_add",jdbcType="TINYINT",nullable=false,format="")
    private Boolean continueAdd;		//连续编辑 db field:continue_add

    @SerializedName(value = "images", alternate = {"IMAGES"})
    @CgFieldAnnotation(name="images",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String images;		//顶部轮播图像 db field:images

    @SerializedName(value = "viewProperties", alternate = {"view_properties","VIEW_PROPERTIES"})
    @CgFieldAnnotation(name="view_properties",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String viewProperties;		//视图属性 db field:view_properties

    @SerializedName(value = "formProperties", alternate = {"form_properties","FORM_PROPERTIES"})
    @CgFieldAnnotation(name="form_properties",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String formProperties;		//表单属性 db field:form_properties

    @SerializedName(value = "slotTemplates", alternate = {"slot_templates","SLOT_TEMPLATES"})
    @CgFieldAnnotation(name="slot_templates",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String slotTemplates;		//slot模板 db field:slot_templates

    @SerializedName(value = "actionList", alternate = {"action_list","ACTION_LIST"})
    @CgFieldAnnotation(name="action_list",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String actionList;		//功能清单 db field:action_list

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
