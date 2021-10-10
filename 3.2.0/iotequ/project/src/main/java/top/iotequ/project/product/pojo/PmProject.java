/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.product.pojo;
import top.iotequ.util.*;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : PmProject (项目及产品列表)
@CgTableAnnotation(name="pm_project",
                   title="pmProject",
                   join="LEFT JOIN sys_user ON pm_project.flow_register_by = sys_user.id LEFT JOIN sys_user sys_user1 ON pm_project.flow_next_operator = sys_user1.id null",
                   baseUrl="/project/product/pmProject",
                   hasLicence=true,
                   trialLicence=10,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="pmProject",
                   module="project")
@Getter
@Setter
public class PmProject implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="pm_project.id",jdbcType="CHAR",length=36,nullable=false,format="@")
    private String id;		//id db field:id

    @SerializedName(value = "flowState", alternate = {"flow_state","FLOW_STATE"})
    @CgFieldAnnotation(name="pm_project.flow_state",jdbcType="INTEGER",nullable=false,format="")
    private Integer flowState;		//状态 db field:flow_state

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "flowRegisterTime", alternate = {"flow_register_time","FLOW_REGISTER_TIME"})
    @CgFieldAnnotation(name="pm_project.flow_register_time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date flowRegisterTime;		//登记时间 db field:flow_register_time

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="pm_project.name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//名称 db field:name

    @SerializedName(value = "type", alternate = {"TYPE"})
    @CgFieldAnnotation(name="pm_project.type",jdbcType="INTEGER",nullable=false,format="")
    private Integer type;		//类别 db field:type

    @SerializedName(value = "customer", alternate = {"CUSTOMER"})
    @CgFieldAnnotation(name="pm_project.customer",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String customer;		//客户信息 db field:customer

    @SerializedName(value = "marketSize", alternate = {"market_size","MARKET_SIZE"})
    @CgFieldAnnotation(name="pm_project.market_size",jdbcType="INTEGER",nullable=true,format="")
    private Integer marketSize;		//市场规模(万) db field:market_size

    @SerializedName(value = "humanCost", alternate = {"human_cost","HUMAN_COST"})
    @CgFieldAnnotation(name="pm_project.human_cost",jdbcType="INTEGER",nullable=true,format="")
    private Integer humanCost;		//人力投入(人月) db field:human_cost

    @SerializedName(value = "materialCost", alternate = {"material_cost","MATERIAL_COST"})
    @CgFieldAnnotation(name="pm_project.material_cost",jdbcType="INTEGER",nullable=true,format="")
    private Integer materialCost;		//物料成本(万) db field:material_cost

    @SerializedName(value = "code", alternate = {"CODE"})
    @CgFieldAnnotation(name="pm_project.code",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String code;		//版本编码 db field:code

    @SerializedName(value = "description", alternate = {"DESCRIPTION"})
    @CgFieldAnnotation(name="pm_project.description",jdbcType="VARCHAR",length=500,nullable=true,format="@")
    private String description;		//详细描述 db field:description

    @SerializedName(value = "flowNote", alternate = {"flow_note","FLOW_NOTE"})
    @CgFieldAnnotation(name="pm_project.flow_note",jdbcType="VARCHAR",length=1000,nullable=true,format="@")
    private String flowNote;		//已处理意见 db field:flow_note

    @SerializedName(value = "addFile", alternate = {"add_file","ADD_FILE"})
    @CgFieldAnnotation(name="pm_project.add_file",jdbcType="VARCHAR",length=45,nullable=true,multiple=true,format="@")
    private String addFile;		//附件 db field:add_file

    @SerializedName(value = "flowRegisterBy", alternate = {"flow_register_by","FLOW_REGISTER_BY"})
    @CgFieldAnnotation(name="pm_project.flow_register_by",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String flowRegisterBy;		//登记人 db field:flow_register_by
    @SerializedName(value = "registerByName", alternate = {"register_by_name","REGISTER_BY_NAME"})
    @CgFieldAnnotation(name="sys_user.real_name",jdbcType="VARCHAR")
    private String registerByName;

    @SerializedName(value = "flowNextOperator", alternate = {"flow_next_operator","FLOW_NEXT_OPERATOR"})
    @CgFieldAnnotation(name="pm_project.flow_next_operator",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String flowNextOperator;		//下一步处理人 db field:flow_next_operator
    @SerializedName(value = "nextOperatorName", alternate = {"next_operator_name","NEXT_OPERATOR_NAME"})
    @CgFieldAnnotation(name="sys_user1.real_name",jdbcType="VARCHAR")
    private String nextOperatorName;

    @SerializedName(value = "flowCopyToList", alternate = {"flow_copy_to_list","FLOW_COPY_TO_LIST"})
    @CgFieldAnnotation(name="pm_project.flow_copy_to_list",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String flowCopyToList;		//抄送列表 db field:flow_copy_to_list
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="sys_user2.real_name",jdbcType="VARCHAR")
    private String realName;

    private String flowOperations;		//操作 非数据库字段

    private String flowAvailableActions;
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
