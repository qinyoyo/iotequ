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

//  Pojo entity : CgButton (按钮定义)
@CgTableAnnotation(name="cg_button",
                   title="cgButton",
                   baseUrl="/codegenerator/cgButton",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="cgButton",
                   module="codegenerator")
@Getter
@Setter
public class CgButton implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String id;		//id db field:id

    @SerializedName(value = "tableId", alternate = {"table_id","TABLE_ID"})
    @CgFieldAnnotation(name="table_id",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String tableId;		//所属表单 db field:table_id

    @SerializedName(value = "orderNum", alternate = {"order_num","ORDER_NUM"})
    @CgFieldAnnotation(name="order_num",jdbcType="INTEGER",nullable=false,format="")
    private Integer orderNum;		//排序 db field:order_num

    @SerializedName(value = "action", alternate = {"ACTION"})
    @CgFieldAnnotation(name="action",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String action;		//操作代码 db field:action

    @SerializedName(value = "title", alternate = {"TITLE"})
    @CgFieldAnnotation(name="title",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String title;		//标题 db field:title

    @SerializedName(value = "icon", alternate = {"ICON"})
    @CgFieldAnnotation(name="icon",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String icon;		//图标 db field:icon

    @SerializedName(value = "appendClass", alternate = {"append_class","APPEND_CLASS"})
    @CgFieldAnnotation(name="append_class",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String appendClass;		//执行函数或参数 db field:append_class

    @SerializedName(value = "actionProperty", alternate = {"action_property","ACTION_PROPERTY"})
    @CgFieldAnnotation(name="action_property",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String actionProperty;		//行为属性 db field:action_property

    @SerializedName(value = "rowProperty", alternate = {"row_property","ROW_PROPERTY"})
    @CgFieldAnnotation(name="row_property",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String rowProperty;		//行属性 db field:row_property

    @SerializedName(value = "displayProperties", alternate = {"display_properties","DISPLAY_PROPERTIES"})
    @CgFieldAnnotation(name="display_properties",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String displayProperties;		//显示属性 db field:display_properties

    @SerializedName(value = "confirmText", alternate = {"confirm_text","CONFIRM_TEXT"})
    @CgFieldAnnotation(name="confirm_text",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String confirmText;		//操作前提示 db field:confirm_text

    @SerializedName(value = "isRefreshList", alternate = {"is_refresh_list","IS_REFRESH_LIST"})
    @CgFieldAnnotation(name="is_refresh_list",jdbcType="TINYINT",nullable=false,format="")
    private Boolean isRefreshList;		//操作后刷新列表 db field:is_refresh_list

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
