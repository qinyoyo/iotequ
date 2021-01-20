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

//  Pojo entity : CgFormField (表单字段定义)
@CgTableAnnotation(name="cg_form_field",
                   title="cgFormField",
                   baseUrl="/codegenerator/cgFormField",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="cgFormField",
                   module="codegenerator")
@Getter
@Setter
public class CgFormField implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "formId", alternate = {"form_id","FORM_ID"})
    @CgFieldAnnotation(name="form_id",jdbcType="VARCHAR",length=64,nullable=false,format="@")
    private String formId;		//formId db field:form_id

    @SerializedName(value = "orderNum", alternate = {"order_num","ORDER_NUM"})
    @CgFieldAnnotation(name="order_num",jdbcType="INTEGER",nullable=false,format="")
    private Integer orderNum;		//排序 db field:order_num

    @SerializedName(value = "entityField", alternate = {"entity_field","ENTITY_FIELD"})
    @CgFieldAnnotation(name="entity_field",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String entityField;		//表字段 db field:entity_field

    @SerializedName(value = "width", alternate = {"WIDTH"})
    @CgFieldAnnotation(name="width",jdbcType="INTEGER",nullable=false,format="")
    private Integer width;		//宽度 db field:width

    @SerializedName(value = "groupTitle", alternate = {"group_title","GROUP_TITLE"})
    @CgFieldAnnotation(name="group_title",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String groupTitle;		//分组标签 db field:group_title

    @SerializedName(value = "itemProperties", alternate = {"item_properties","ITEM_PROPERTIES"})
    @CgFieldAnnotation(name="item_properties",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String itemProperties;		//输入控件属性 db field:item_properties

    @SerializedName(value = "formItemProperties", alternate = {"form_item_properties","FORM_ITEM_PROPERTIES"})
    @CgFieldAnnotation(name="form_item_properties",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String formItemProperties;		//form_item属性 db field:form_item_properties

    @SerializedName(value = "readonly", alternate = {"READONLY"})
    @CgFieldAnnotation(name="readonly",jdbcType="TINYINT",nullable=false,format="")
    private Boolean readonly;		//只读 db field:readonly

    @SerializedName(value = "mustInput", alternate = {"must_input","MUST_INPUT"})
    @CgFieldAnnotation(name="must_input",jdbcType="TINYINT",nullable=false,format="")
    private Boolean mustInput;		//必填 db field:must_input

    @SerializedName(value = "icon", alternate = {"ICON"})
    @CgFieldAnnotation(name="icon",jdbcType="VARCHAR",length=64,nullable=true,format="@")
    private String icon;		//图标 db field:icon

    @SerializedName(value = "href", alternate = {"HREF"})
    @CgFieldAnnotation(name="href",jdbcType="VARCHAR",length=64,nullable=true,format="@")
    private String href;		//超链接 db field:href

    @SerializedName(value = "hidden", alternate = {"HIDDEN"})
    @CgFieldAnnotation(name="hidden",jdbcType="TINYINT",nullable=false,format="")
    private Boolean hidden;		//隐藏 db field:hidden

    @SerializedName(value = "validateAsTitle", alternate = {"validate_as_title","VALIDATE_AS_TITLE"})
    @CgFieldAnnotation(name="validate_as_title",jdbcType="TINYINT",nullable=false,format="")
    private Boolean validateAsTitle;		//显示title提示 db field:validate_as_title

    @SerializedName(value = "slotTemplates", alternate = {"slot_templates","SLOT_TEMPLATES"})
    @CgFieldAnnotation(name="slot_templates",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String slotTemplates;		//slot模板 db field:slot_templates

    @SerializedName(value = "showType", alternate = {"show_type","SHOW_TYPE"})
    @CgFieldAnnotation(name="show_type",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String showType;		//更改控件类型为 db field:show_type

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
