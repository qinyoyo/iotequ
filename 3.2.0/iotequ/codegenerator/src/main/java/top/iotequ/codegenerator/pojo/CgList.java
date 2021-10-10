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

//  Pojo entity : CgList (列表视图定义|List design)
@CgTableAnnotation(name="cg_list",
                   title="cgList",
                   baseUrl="/codegenerator/cgList",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="cgList",
                   module="codegenerator")
@Getter
@Setter
public class CgList implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="VARCHAR",length=64,nullable=false,format="@")
    private String id;		//ID db field:id

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=50,nullable=false,format="@")
    private String name;		//名称 db field:name

    @SerializedName(value = "path", alternate = {"PATH"})
    @CgFieldAnnotation(name="path",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String path;		//路径最后一级 db field:path

    @SerializedName(value = "tableId", alternate = {"table_id","TABLE_ID"})
    @CgFieldAnnotation(name="table_id",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String tableId;		//对应表单 db field:table_id

    @SerializedName(value = "icon", alternate = {"ICON"})
    @CgFieldAnnotation(name="icon",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String icon;		//图标 db field:icon

    @SerializedName(value = "headTitle", alternate = {"head_title","HEAD_TITLE"})
    @CgFieldAnnotation(name="head_title",jdbcType="VARCHAR",length=64,nullable=true,format="@")
    private String headTitle;		//列表标题 db field:head_title

    @SerializedName(value = "tagTitle", alternate = {"tag_title","TAG_TITLE"})
    @CgFieldAnnotation(name="tag_title",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String tagTitle;		//tag标题 db field:tag_title

    @SerializedName(value = "editInline", alternate = {"edit_inline","EDIT_INLINE"})
    @CgFieldAnnotation(name="edit_inline",jdbcType="TINYINT",nullable=false,format="")
    private Boolean editInline;		//行内编辑 db field:edit_inline

    @SerializedName(value = "detailInline", alternate = {"detail_inline","DETAIL_INLINE"})
    @CgFieldAnnotation(name="detail_inline",jdbcType="TINYINT",nullable=false,format="")
    private Boolean detailInline;		//行内详情 db field:detail_inline

    @SerializedName(value = "sons", alternate = {"SONS"})
    @CgFieldAnnotation(name="sons",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String sons;		//子表 db field:sons

    @SerializedName(value = "sonFields", alternate = {"son_fields","SON_FIELDS"})
    @CgFieldAnnotation(name="son_fields",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String sonFields;		//子表外键字段列表 db field:son_fields

    @SerializedName(value = "sonAlign", alternate = {"son_align","SON_ALIGN"})
    @CgFieldAnnotation(name="son_align",jdbcType="VARCHAR",length=1,nullable=false,format="@")
    private String sonAlign;		//主从表排列 db field:son_align

    @SerializedName(value = "generatorType", alternate = {"generator_type","GENERATOR_TYPE"})
    @CgFieldAnnotation(name="generator_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer generatorType;		//主表宽度 db field:generator_type

    @SerializedName(value = "titleField", alternate = {"title_field","TITLE_FIELD"})
    @CgFieldAnnotation(name="title_field",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String titleField;		//显示在子表标题里的主表字段 db field:title_field

    @SerializedName(value = "parentEntity", alternate = {"parent_entity","PARENT_ENTITY"})
    @CgFieldAnnotation(name="parent_entity",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String parentEntity;		//tree的父级Entity db field:parent_entity

    @SerializedName(value = "treeShowEntity", alternate = {"tree_show_entity","TREE_SHOW_ENTITY"})
    @CgFieldAnnotation(name="tree_show_entity",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String treeShowEntity;		//树显示Entity db field:tree_show_entity

    @SerializedName(value = "showSearch", alternate = {"show_search","SHOW_SEARCH"})
    @CgFieldAnnotation(name="show_search",jdbcType="TINYINT",nullable=false,format="")
    private Boolean showSearch;		//显示模糊查询 db field:show_search

    @SerializedName(value = "toolbarMode", alternate = {"toolbar_mode","TOOLBAR_MODE"})
    @CgFieldAnnotation(name="toolbar_mode",jdbcType="INTEGER",nullable=false,format="")
    private Integer toolbarMode;		//工具条显示模式 db field:toolbar_mode

    @SerializedName(value = "tableHeight", alternate = {"table_height","TABLE_HEIGHT"})
    @CgFieldAnnotation(name="table_height",jdbcType="INTEGER",nullable=false,format="")
    private Integer tableHeight;		//表高 db field:table_height

    @SerializedName(value = "pagination", alternate = {"PAGINATION"})
    @CgFieldAnnotation(name="pagination",jdbcType="TINYINT",nullable=false,format="")
    private Boolean pagination;		//是否分页 db field:pagination

    @SerializedName(value = "orderBy", alternate = {"order_by","ORDER_BY"})
    @CgFieldAnnotation(name="order_by",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String orderBy;		//默认排序 db field:order_by

    @SerializedName(value = "sortField", alternate = {"sort_field","SORT_FIELD"})
    @CgFieldAnnotation(name="sort_field",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String sortField;		//拖拽排序字段 db field:sort_field

    @SerializedName(value = "stripe", alternate = {"STRIPE"})
    @CgFieldAnnotation(name="stripe",jdbcType="TINYINT",nullable=false,format="")
    private Boolean stripe;		//斑马纹风格 db field:stripe

    @SerializedName(value = "border", alternate = {"BORDER"})
    @CgFieldAnnotation(name="border",jdbcType="TINYINT",nullable=false,format="")
    private Boolean border;		//边框 db field:border

    @SerializedName(value = "stateEntity", alternate = {"state_entity","STATE_ENTITY"})
    @CgFieldAnnotation(name="state_entity",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String stateEntity;		//状态字段或函数 db field:state_entity

    @SerializedName(value = "maxHeight", alternate = {"max_height","MAX_HEIGHT"})
    @CgFieldAnnotation(name="max_height",jdbcType="INTEGER",nullable=true,format="")
    private Integer maxHeight;		//最大高度 db field:max_height

    @SerializedName(value = "highlightCurrentRow", alternate = {"highlight_current_row","HIGHLIGHT_CURRENT_ROW"})
    @CgFieldAnnotation(name="highlight_current_row",jdbcType="TINYINT",nullable=false,format="")
    private Boolean highlightCurrentRow;		//单选高亮 db field:highlight_current_row

    @SerializedName(value = "multiple", alternate = {"MULTIPLE"})
    @CgFieldAnnotation(name="multiple",jdbcType="TINYINT",nullable=false,format="")
    private Boolean multiple;		//多选 db field:multiple

    @SerializedName(value = "showSummary", alternate = {"show_summary","SHOW_SUMMARY"})
    @CgFieldAnnotation(name="show_summary",jdbcType="TINYINT",nullable=true,format="")
    private Boolean showSummary;		//统计栏 db field:show_summary

    @SerializedName(value = "spanEntities", alternate = {"span_entities","SPAN_ENTITIES"})
    @CgFieldAnnotation(name="span_entities",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String spanEntities;		//合并字段 db field:span_entities

    @SerializedName(value = "showHeader", alternate = {"show_header","SHOW_HEADER"})
    @CgFieldAnnotation(name="show_header",jdbcType="TINYINT",nullable=false,format="")
    private Boolean showHeader;		//显示表头 db field:show_header

    @SerializedName(value = "images", alternate = {"IMAGES"})
    @CgFieldAnnotation(name="images",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String images;		//顶部轮播图像 db field:images

    @SerializedName(value = "viewProperties", alternate = {"view_properties","VIEW_PROPERTIES"})
    @CgFieldAnnotation(name="view_properties",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String viewProperties;		//视图属性 db field:view_properties

    @SerializedName(value = "tableProperties", alternate = {"table_properties","TABLE_PROPERTIES"})
    @CgFieldAnnotation(name="table_properties",jdbcType="VARCHAR",length=4294967295L,nullable=true,format="@")
    private String tableProperties;		//列表属性 db field:table_properties

    @SerializedName(value = "sonsProperties", alternate = {"sons_properties","SONS_PROPERTIES"})
    @CgFieldAnnotation(name="sons_properties",jdbcType="VARCHAR",length=500,nullable=true,format="@")
    private String sonsProperties;		//子组件属性 db field:sons_properties

    @SerializedName(value = "actionList", alternate = {"action_list","ACTION_LIST"})
    @CgFieldAnnotation(name="action_list",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String actionList;		//功能清单 db field:action_list

    @SerializedName(value = "flowDataUrl", alternate = {"flow_data_url","FLOW_DATA_URL"})
    @CgFieldAnnotation(name="flow_data_url",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String flowDataUrl;		//流程状态数据url db field:flow_data_url

    @SerializedName(value = "localExport", alternate = {"local_export","LOCAL_EXPORT"})
    @CgFieldAnnotation(name="local_export",jdbcType="TINYINT",nullable=false,format="")
    private Boolean localExport;		//本地导出 db field:local_export

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
