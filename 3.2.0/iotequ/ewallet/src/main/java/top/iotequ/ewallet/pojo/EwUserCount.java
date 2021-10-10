/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.ewallet.pojo;
import top.iotequ.util.StringUtil;
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
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : EwUserCount (计次钱包)
@CgTableAnnotation(name="ew_user_count",
                   title="ewUserCount",
                   join="LEFT JOIN ew_user ON ew_user_count.user_no = ew_user.user_no LEFT JOIN ew_count_project ON ew_user_count.count_id = ew_count_project.id",
                   baseUrl="/ewallet/ewUserCount",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="ewUserCount",
                   module="ewallet")
@Getter
@Setter
public class EwUserCount implements CgEntity, EWallet {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="ew_user_count.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "amountCount", alternate = {"amount_count","AMOUNT_COUNT"})
    @CgFieldAnnotation(name="ew_user_count.amount_count",jdbcType="INTEGER",nullable=false,format="")
    private Integer amountCount;		//计次总数 db field:amount_count

    @SerializedName(value = "checkCode", alternate = {"check_code","CHECK_CODE"})
    @CgFieldAnnotation(name="ew_user_count.check_code",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String checkCode;		//检验码 db field:check_code

    @SerializedName(value = "costLimit", alternate = {"cost_limit","COST_LIMIT"})
    @CgFieldAnnotation(name="ew_user_count.cost_limit",jdbcType="INTEGER",nullable=false,format="")
    private Integer costLimit;		//消费限额 db field:cost_limit

    @SerializedName(value = "dayLimit", alternate = {"day_limit","DAY_LIMIT"})
    @CgFieldAnnotation(name="ew_user_count.day_limit",jdbcType="INTEGER",nullable=false,format="")
    private Integer dayLimit;		//日限额 db field:day_limit

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="ew_user_count.user_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String userNo;		//账户 db field:user_no
    @SerializedName(value = "userName", alternate = {"user_name","USER_NAME"})
    @CgFieldAnnotation(name="ew_user.name",jdbcType="VARCHAR")
    private String userName;
    @SerializedName(value = "memberGroup", alternate = {"member_group","MEMBER_GROUP"})
    @CgFieldAnnotation(name="ew_user.member_group",jdbcType="VARCHAR")
    private String memberGroup;
    @SerializedName(value = "userIsActive", alternate = {"user_is_active","USER_IS_ACTIVE"})
    @CgFieldAnnotation(name="ew_user.is_active",jdbcType="VARCHAR")
    private Boolean userIsActive;

    @SerializedName(value = "countId", alternate = {"count_id","COUNT_ID"})
    @CgFieldAnnotation(name="ew_user_count.count_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer countId;		//计次项目 db field:count_id
    @SerializedName(value = "projectName", alternate = {"project_name","PROJECT_NAME"})
    @CgFieldAnnotation(name="ew_count_project.name",jdbcType="INTEGER")
    private String projectName;
    @SerializedName(value = "basePrice", alternate = {"base_price","BASE_PRICE"})
    @CgFieldAnnotation(name="ew_count_project.base_price",jdbcType="INTEGER")
    private Integer basePrice;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "startTime", alternate = {"start_time","START_TIME"})
    @CgFieldAnnotation(name="ew_count_project.start_time",jdbcType="INTEGER")
    private Date startTime;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "endTime", alternate = {"end_time","END_TIME"})
    @CgFieldAnnotation(name="ew_count_project.end_time",jdbcType="INTEGER")
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
        return "[EwUserCount]"
                .concat(" id:" + this.id)
                .concat(" count id:" + this.countId)
                .concat(" user amount count:" + this.amountCount)
                .concat(" userNo:" + this.userNo)
                .concat(" cost limit:" + this.costLimit)
                .concat(" day limit:" + this.dayLimit);
    }
//^_^ Your code end.
}
