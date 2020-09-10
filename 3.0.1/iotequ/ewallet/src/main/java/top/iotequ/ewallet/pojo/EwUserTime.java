/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.TimeSerializer;
import top.iotequ.framework.serializer.gson.GsonTimeTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : EwUserTime (计时钱包)
@CgTableAnnotation(name="ew_user_time",
                   title="ewUserTime",
                   join="LEFT JOIN ew_user ON ew_user_time.user_no = ew_user.user_no LEFT JOIN ew_time_project ON ew_user_time.time_id = ew_time_project.id",
                   baseUrl="/ewallet/ewUserTime",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="ewUserTime",
                   module="ewallet")
@Getter
@Setter
public class EwUserTime implements CgEntity, EWallet {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ew_user_time.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "amountTime", alternate = {"amount_time","AMOUNT_TIME"})
    @CgFieldAnnotation(name="ew_user_time.amount_time",jdbcType="INTEGER",nullable=false,format="")
    private Integer amountTime;		//计时时长 db field:amount_time

    @SerializedName(value = "checkCode", alternate = {"check_code","CHECK_CODE"})
    @CgFieldAnnotation(name="ew_user_time.check_code",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String checkCode;		//检验码 db field:check_code

    @SerializedName(value = "costLimit", alternate = {"cost_limit","COST_LIMIT"})
    @CgFieldAnnotation(name="ew_user_time.cost_limit",jdbcType="INTEGER",nullable=false,format="")
    private Integer costLimit;		//消费限额 db field:cost_limit

    @SerializedName(value = "dayLimit", alternate = {"day_limit","DAY_LIMIT"})
    @CgFieldAnnotation(name="ew_user_time.day_limit",jdbcType="INTEGER",nullable=false,format="")
    private Integer dayLimit;		//日限额 db field:day_limit

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="ew_user_time.user_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String userNo;		//用户号 db field:user_no
    @SerializedName(value = "userName", alternate = {"user_name","USER_NAME"})
    @CgFieldAnnotation(name="ew_user.name",jdbcType="VARCHAR")
    private String userName;
    @SerializedName(value = "memberGroup", alternate = {"member_group","MEMBER_GROUP"})
    @CgFieldAnnotation(name="ew_user.member_group",jdbcType="VARCHAR")
    private String memberGroup;
    @SerializedName(value = "userIsActive", alternate = {"user_is_active","USER_IS_ACTIVE"})
    @CgFieldAnnotation(name="ew_user.is_active",jdbcType="VARCHAR")
    private Boolean userIsActive;

    @SerializedName(value = "timeId", alternate = {"time_id","TIME_ID"})
    @CgFieldAnnotation(name="ew_user_time.time_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer timeId;		//计时项目编号 db field:time_id
    @SerializedName(value = "projectName", alternate = {"project_name","PROJECT_NAME"})
    @CgFieldAnnotation(name="ew_time_project.name",jdbcType="INTEGER")
    private String projectName;
    @SerializedName(value = "basePrice", alternate = {"base_price","BASE_PRICE"})
    @CgFieldAnnotation(name="ew_time_project.base_price",jdbcType="INTEGER")
    private Integer basePrice;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "startTime", alternate = {"start_time","START_TIME"})
    @CgFieldAnnotation(name="ew_time_project.start_time",jdbcType="INTEGER")
    private Date startTime;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "endTime", alternate = {"end_time","END_TIME"})
    @CgFieldAnnotation(name="ew_time_project.end_time",jdbcType="INTEGER")
    private Date endTime;

    private Boolean isValid;		//有效性 非数据库字段

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
    @Override
    public String toSortedString() {
        return "[EwUserTime]"
                .concat(" id:" + this.id)
                .concat(" time id:" + this.timeId)
                .concat(" amount time:" + this.amountTime)
                .concat(" user No:" + this.userNo)
                .concat(" cost limit:" + this.costLimit)
                .concat(" day limit:" + this.dayLimit);
    }
//^_^ Your code end.
}
