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

//  Pojo entity : CgListField (列表视图字段定义)
@CgTableAnnotation(name="cg_list_field",
                   title="cgListField",
                   baseUrl="/codegenerator/cgListField",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="cgListField",
                   module="codegenerator")
@Getter
@Setter
public class CgListField implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//ID db field:id

    @SerializedName(value = "listId", alternate = {"list_id","LIST_ID"})
    @CgFieldAnnotation(name="list_id",jdbcType="VARCHAR",length=64,nullable=false,format="@")
    private String listId;		//listID db field:list_id

    @SerializedName(value = "orderNum", alternate = {"order_num","ORDER_NUM"})
    @CgFieldAnnotation(name="order_num",jdbcType="INTEGER",nullable=false,format="")
    private Integer orderNum;		//排序 db field:order_num

    @SerializedName(value = "entityField", alternate = {"entity_field","ENTITY_FIELD"})
    @CgFieldAnnotation(name="entity_field",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String entityField;		//表entity字段 db field:entity_field

    @SerializedName(value = "queryMode", alternate = {"query_mode","QUERY_MODE"})
    @CgFieldAnnotation(name="query_mode",jdbcType="INTEGER",nullable=false,format="")
    private Integer queryMode;		//查询模式 db field:query_mode

    @SerializedName(value = "fix", alternate = {"FIX"})
    @CgFieldAnnotation(name="fix",jdbcType="TINYINT",nullable=false,format="")
    private Boolean fix;		//固定列 db field:fix

    @SerializedName(value = "expand", alternate = {"EXPAND"})
    @CgFieldAnnotation(name="expand",jdbcType="TINYINT",nullable=false,format="")
    private Boolean expand;		//展开列 db field:expand

    @SerializedName(value = "overflowTooltip", alternate = {"overflow_tooltip","OVERFLOW_TOOLTIP"})
    @CgFieldAnnotation(name="overflow_tooltip",jdbcType="TINYINT",nullable=false,format="")
    private Boolean overflowTooltip;		//提示隐藏内容 db field:overflow_tooltip

    @SerializedName(value = "align", alternate = {"ALIGN"})
    @CgFieldAnnotation(name="align",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String align;		//对齐 db field:align

    @SerializedName(value = "headerAlign", alternate = {"header_align","HEADER_ALIGN"})
    @CgFieldAnnotation(name="header_align",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String headerAlign;		//表头对齐 db field:header_align

    @SerializedName(value = "width", alternate = {"WIDTH"})
    @CgFieldAnnotation(name="width",jdbcType="INTEGER",nullable=false,format="")
    private Integer width;		//默认宽度 db field:width

    @SerializedName(value = "columnProperties", alternate = {"column_properties","COLUMN_PROPERTIES"})
    @CgFieldAnnotation(name="column_properties",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String columnProperties;		//列属性 db field:column_properties

    @SerializedName(value = "hidden", alternate = {"HIDDEN"})
    @CgFieldAnnotation(name="hidden",jdbcType="TINYINT",nullable=false,format="")
    private Boolean hidden;		//隐藏字段 db field:hidden

    @SerializedName(value = "editInline", alternate = {"edit_inline","EDIT_INLINE"})
    @CgFieldAnnotation(name="edit_inline",jdbcType="TINYINT",nullable=false,format="")
    private Boolean editInline;		//行内编辑 db field:edit_inline

    @SerializedName(value = "defaultQueryValue", alternate = {"default_query_value","DEFAULT_QUERY_VALUE"})
    @CgFieldAnnotation(name="default_query_value",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String defaultQueryValue;		//缺省查询条件 db field:default_query_value

    @SerializedName(value = "cellDisplaySlot", alternate = {"cell_display_slot","CELL_DISPLAY_SLOT"})
    @CgFieldAnnotation(name="cell_display_slot",jdbcType="VARCHAR",length=500,nullable=true,format="@")
    private String cellDisplaySlot;		//自定义字段显示 db field:cell_display_slot

    @SerializedName(value = "showType", alternate = {"show_type","SHOW_TYPE"})
    @CgFieldAnnotation(name="show_type",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String showType;		//修改控件类型为 db field:show_type

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
