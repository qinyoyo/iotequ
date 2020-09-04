/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.project.version.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : PmVersionApplication (版本申请)
@CgTableAnnotation(name="pm_version_application",
                   title="pmVersionApplication",
                   join="LEFT JOIN sys_user ON pm_version_application.flow_register_by = sys_user.id LEFT JOIN sys_user sys_user1 ON pm_version_application.flow_next_operator = sys_user1.id null",
                   baseUrl="/project/version/pmVersionApplication",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="pmVersionApplication",
                   module="project")
@Getter
@Setter
public class PmVersionApplication implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="pm_version_application.id",jdbcType="CHAR",length=36,nullable=false,format="@")
    private String id;		//id db field:id

    @SerializedName(value = "flowState", alternate = {"flow_state","FLOW_STATE"})
    @CgFieldAnnotation(name="pm_version_application.flow_state",jdbcType="INTEGER",nullable=false,format="")
    private Integer flowState;		//状态 db field:flow_state

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "flowRegisterTime", alternate = {"flow_register_time","FLOW_REGISTER_TIME"})
    @CgFieldAnnotation(name="pm_version_application.flow_register_time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date flowRegisterTime;		//申请时间 db field:flow_register_time

    @SerializedName(value = "project", alternate = {"PROJECT"})
    @CgFieldAnnotation(name="pm_version_application.project",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String project;		//项目 db field:project

    @SerializedName(value = "applicationType", alternate = {"application_type","APPLICATION_TYPE"})
    @CgFieldAnnotation(name="pm_version_application.application_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer applicationType;		//申请类别 db field:application_type

    @SerializedName(value = "customer", alternate = {"CUSTOMER"})
    @CgFieldAnnotation(name="pm_version_application.customer",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String customer;		//使用单位 db field:customer

    @SerializedName(value = "licence", alternate = {"LICENCE"})
    @CgFieldAnnotation(name="pm_version_application.licence",jdbcType="VARCHAR",length=100,nullable=false,format="@")
    private String licence;		//授权说明 db field:licence

    @SerializedName(value = "contractNo", alternate = {"contract_no","CONTRACT_NO"})
    @CgFieldAnnotation(name="pm_version_application.contract_no",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String contractNo;		//合同号 db field:contract_no

    @SerializedName(value = "description", alternate = {"DESCRIPTION"})
    @CgFieldAnnotation(name="pm_version_application.description",jdbcType="VARCHAR",length=200,nullable=false,format="@")
    private String description;		//详情描述 db field:description

    @SerializedName(value = "versionInfo", alternate = {"version_info","VERSION_INFO"})
    @CgFieldAnnotation(name="pm_version_application.version_info",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String versionInfo;		//发放版本号详情 db field:version_info

    @SerializedName(value = "flowNote", alternate = {"flow_note","FLOW_NOTE"})
    @CgFieldAnnotation(name="pm_version_application.flow_note",jdbcType="VARCHAR",length=1000,nullable=true,format="@")
    private String flowNote;		//已处理意见 db field:flow_note

    @SerializedName(value = "addFile", alternate = {"add_file","ADD_FILE"})
    @CgFieldAnnotation(name="pm_version_application.add_file",jdbcType="VARCHAR",length=200,nullable=true,multiple=true,format="@")
    private String addFile;		//附件 db field:add_file

    @SerializedName(value = "flowRegisterBy", alternate = {"flow_register_by","FLOW_REGISTER_BY"})
    @CgFieldAnnotation(name="pm_version_application.flow_register_by",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String flowRegisterBy;		//申请人 db field:flow_register_by
    @SerializedName(value = "registerByName", alternate = {"register_by_name","REGISTER_BY_NAME"})
    @CgFieldAnnotation(name="sys_user.real_name",jdbcType="VARCHAR")
    private String registerByName;

    @SerializedName(value = "flowNextOperator", alternate = {"flow_next_operator","FLOW_NEXT_OPERATOR"})
    @CgFieldAnnotation(name="pm_version_application.flow_next_operator",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String flowNextOperator;		//即将处理人 db field:flow_next_operator
    @SerializedName(value = "nextOperatorName", alternate = {"next_operator_name","NEXT_OPERATOR_NAME"})
    @CgFieldAnnotation(name="sys_user1.real_name",jdbcType="VARCHAR")
    private String nextOperatorName;

    @SerializedName(value = "flowCopyToList", alternate = {"flow_copy_to_list","FLOW_COPY_TO_LIST"})
    @CgFieldAnnotation(name="pm_version_application.flow_copy_to_list",jdbcType="VARCHAR",length=100,nullable=true,format="@")
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
