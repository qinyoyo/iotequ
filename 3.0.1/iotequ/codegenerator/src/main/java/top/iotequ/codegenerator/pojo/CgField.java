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

//  Pojo entity : CgField (字段定义表)
@CgTableAnnotation(name="cg_field",
                   title="cgField",
                   baseUrl="/codegenerator/cgField",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="cgField",
                   module="codegenerator")
@Getter
@Setter
public class CgField implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String id;		//主键ID db field:id

    @SerializedName(value = "tableId", alternate = {"table_id","TABLE_ID"})
    @CgFieldAnnotation(name="table_id",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String tableId;		//表ID db field:table_id

    @SerializedName(value = "orderNum", alternate = {"order_num","ORDER_NUM"})
    @CgFieldAnnotation(name="order_num",jdbcType="INTEGER",nullable=false,format="")
    private Integer orderNum;		//排序 db field:order_num

    @SerializedName(value = "entityName", alternate = {"entity_name","ENTITY_NAME"})
    @CgFieldAnnotation(name="entity_name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String entityName;		//Entity名 db field:entity_name

    @SerializedName(value = "title", alternate = {"TITLE"})
    @CgFieldAnnotation(name="title",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String title;		//标题 db field:title

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=32,nullable=true,format="@")
    private String name;		//字段名 db field:name

    @SerializedName(value = "keyType", alternate = {"key_type","KEY_TYPE"})
    @CgFieldAnnotation(name="key_type",jdbcType="VARCHAR",length=2,nullable=false,format="@")
    private String keyType;		//索引 db field:key_type

    @SerializedName(value = "defaultValue", alternate = {"default_value","DEFAULT_VALUE"})
    @CgFieldAnnotation(name="default_value",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String defaultValue;		//默认值 db field:default_value

    @SerializedName(value = "showType", alternate = {"show_type","SHOW_TYPE"})
    @CgFieldAnnotation(name="show_type",jdbcType="VARCHAR",length=30,nullable=false,format="@")
    private String showType;		//表单控件类型 db field:show_type

    @SerializedName(value = "formatter", alternate = {"FORMATTER"})
    @CgFieldAnnotation(name="formatter",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String formatter;		//显示格式 db field:formatter

    @SerializedName(value = "isNull", alternate = {"is_null","IS_NULL"})
    @CgFieldAnnotation(name="is_null",jdbcType="VARCHAR",length=5,nullable=false,format="")
    private Boolean isNull;		//可空 db field:is_null

    @SerializedName(value = "validExpression", alternate = {"valid_expression","VALID_EXPRESSION"})
    @CgFieldAnnotation(name="valid_expression",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String validExpression;		//校验正则表达式 db field:valid_expression

    @SerializedName(value = "validTitle", alternate = {"valid_title","VALID_TITLE"})
    @CgFieldAnnotation(name="valid_title",jdbcType="VARCHAR",length=1000,nullable=true,format="@")
    private String validTitle;		//校验提示 db field:valid_title

    @SerializedName(value = "dictTable", alternate = {"dict_table","DICT_TABLE"})
    @CgFieldAnnotation(name="dict_table",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String dictTable;		//字典表或SQL语句 db field:dict_table

    @SerializedName(value = "dictField", alternate = {"dict_field","DICT_FIELD"})
    @CgFieldAnnotation(name="dict_field",jdbcType="VARCHAR",length=1000,nullable=true,format="@")
    private String dictField;		//字典code db field:dict_field

    @SerializedName(value = "dictMultiple", alternate = {"dict_multiple","DICT_MULTIPLE"})
    @CgFieldAnnotation(name="dict_multiple",jdbcType="TINYINT",length=1,nullable=false,format="")
    private Boolean dictMultiple;		//多选 db field:dict_multiple

    @SerializedName(value = "dictText", alternate = {"dict_text","DICT_TEXT"})
    @CgFieldAnnotation(name="dict_text",jdbcType="VARCHAR",length=1000,nullable=true,format="@")
    private String dictText;		//字典Text db field:dict_text

    @SerializedName(value = "dynaCondition", alternate = {"dyna_condition","DYNA_CONDITION"})
    @CgFieldAnnotation(name="dyna_condition",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String dynaCondition;		//动态条件 db field:dyna_condition

    @SerializedName(value = "dictFullName", alternate = {"dict_full_name","DICT_FULL_NAME"})
    @CgFieldAnnotation(name="dict_full_name",jdbcType="TINYINT",length=1,nullable=false,format="")
    private Boolean dictFullName;		//显示全名 db field:dict_full_name

    @SerializedName(value = "dictParent", alternate = {"dict_parent","DICT_PARENT"})
    @CgFieldAnnotation(name="dict_parent",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String dictParent;		//父亲字段名 db field:dict_parent

    @SerializedName(value = "dictParentField", alternate = {"dict_parent_field","DICT_PARENT_FIELD"})
    @CgFieldAnnotation(name="dict_parent_field",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String dictParentField;		//树键值字段 db field:dict_parent_field

    @SerializedName(value = "type", alternate = {"TYPE"})
    @CgFieldAnnotation(name="type",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String type;		//字段类型 db field:type

    @SerializedName(value = "length", alternate = {"LENGTH"})
    @CgFieldAnnotation(name="length",jdbcType="INTEGER",nullable=true,format="")
    private Integer length;		//字段长 db field:length

    @SerializedName(value = "numericPrecision", alternate = {"numeric_precision","NUMERIC_PRECISION"})
    @CgFieldAnnotation(name="numeric_precision",jdbcType="INTEGER",nullable=true,format="")
    private Integer numericPrecision;		//小数位长 db field:numeric_precision

    @SerializedName(value = "numericScale", alternate = {"numeric_scale","NUMERIC_SCALE"})
    @CgFieldAnnotation(name="numeric_scale",jdbcType="INTEGER",nullable=true,format="")
    private Integer numericScale;		//小数精度 db field:numeric_scale

    @SerializedName(value = "fkTable", alternate = {"fk_table","FK_TABLE"})
    @CgFieldAnnotation(name="fk_table",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String fkTable;		//关联主表 db field:fk_table

    @SerializedName(value = "fkColumn", alternate = {"fk_column","FK_COLUMN"})
    @CgFieldAnnotation(name="fk_column",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String fkColumn;		//关联主表字段 db field:fk_column

    @SerializedName(value = "fkOnDelete", alternate = {"fk_on_delete","FK_ON_DELETE"})
    @CgFieldAnnotation(name="fk_on_delete",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String fkOnDelete;		//On Delete db field:fk_on_delete

    @SerializedName(value = "fkOnUpdate", alternate = {"fk_on_update","FK_ON_UPDATE"})
    @CgFieldAnnotation(name="fk_on_update",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String fkOnUpdate;		//On Update db field:fk_on_update

    @SerializedName(value = "defaultQueryValue", alternate = {"default_query_value","DEFAULT_QUERY_VALUE"})
    @CgFieldAnnotation(name="default_query_value",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String defaultQueryValue;

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
