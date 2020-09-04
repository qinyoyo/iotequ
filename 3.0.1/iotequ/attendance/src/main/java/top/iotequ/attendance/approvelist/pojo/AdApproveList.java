/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.approvelist.pojo;
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

//  Pojo entity : AdApproveList (审核信息表)
@CgTableAnnotation(name="ad_approve_list",
                   title="adApproveList",
                   join="LEFT JOIN sys_user ON ad_approve_list.approver = sys_user.id",
                   baseUrl="/attendance/approvelist/adApproveList",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="adApproveList",
                   module="attendance")
@Getter
@Setter
public class AdApproveList implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ad_approve_list.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "approveTime", alternate = {"approve_time","APPROVE_TIME"})
    @CgFieldAnnotation(name="ad_approve_list.approve_time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date approveTime;		//审批时间 db field:approve_time

    @SerializedName(value = "state1", alternate = {"STATE1"})
    @CgFieldAnnotation(name="ad_approve_list.state1",jdbcType="INTEGER",nullable=true,format="")
    private Integer state1;		//审批后状态 db field:state1

    @SerializedName(value = "state0", alternate = {"STATE0"})
    @CgFieldAnnotation(name="ad_approve_list.state0",jdbcType="INTEGER",nullable=true,format="")
    private Integer state0;		//初始状态 db field:state0

    @SerializedName(value = "approveNote", alternate = {"approve_note","APPROVE_NOTE"})
    @CgFieldAnnotation(name="ad_approve_list.approve_note",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String approveNote;		//审批意见 db field:approve_note

    @SerializedName(value = "adjustId", alternate = {"adjust_id","ADJUST_ID"})
    @CgFieldAnnotation(name="ad_approve_list.adjust_id",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String adjustId;		//考勤调整单编号 db field:adjust_id

    @SerializedName(value = "approver", alternate = {"APPROVER"})
    @CgFieldAnnotation(name="ad_approve_list.approver",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String approver;		//审批人 db field:approver
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="sys_user.real_name",jdbcType="VARCHAR")
    private String realName;

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
