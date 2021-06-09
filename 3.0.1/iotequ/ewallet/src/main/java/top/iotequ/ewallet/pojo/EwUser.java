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
import top.iotequ.framework.serializer.jackson.DateSerializer;
import top.iotequ.framework.serializer.gson.GsonDateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : EwUser (电子钱包)
@CgTableAnnotation(name="ew_user",
                   title="ewUser",
                   pk="user_no",
                   entityPk="userNo",
                   baseUrl="/ewallet/ewUser",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="ewUser",
                   module="ewallet")
@Getter
@Setter
public class EwUser implements CgEntity, EWallet {
    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String userNo;		//账户号 db field:user_no

    @SerializedName(value = "isActive", alternate = {"is_active","IS_ACTIVE"})
    @CgFieldAnnotation(name="is_active",jdbcType="TINYINT",nullable=true,format="")
    private Boolean isActive;		//激活 db field:is_active

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String name;		//姓名 db field:name

    @SerializedName(value = "gender", alternate = {"GENDER"})
    @CgFieldAnnotation(name="gender",jdbcType="INTEGER",nullable=false,format="")
    private Integer gender;		//性别 db field:gender

    @SerializedName(value = "idType", alternate = {"id_type","ID_TYPE"})
    @CgFieldAnnotation(name="id_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer idType;		//证件类别 db field:id_type

    @SerializedName(value = "idNo", alternate = {"id_no","ID_NO"})
    @CgFieldAnnotation(name="id_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String idNo;		//证件号码 db field:id_no

    @SerializedName(value = "mobilePhone", alternate = {"mobile_phone","MOBILE_PHONE"})
    @CgFieldAnnotation(name="mobile_phone",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String mobilePhone;		//手机号码 db field:mobile_phone

    @SerializedName(value = "email", alternate = {"EMAIL"})
    @CgFieldAnnotation(name="email",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String email;		//邮箱 db field:email

    @SerializedName(value = "wechatOpenid", alternate = {"wechat_openid","WECHAT_OPENID"})
    @CgFieldAnnotation(name="wechat_openid",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String wechatOpenid;		//微信号 db field:wechat_openid

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "birthDate", alternate = {"birth_date","BIRTH_DATE"})
    @CgFieldAnnotation(name="birth_date",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date birthDate;		//生日 db field:birth_date

    @SerializedName(value = "memberGroup", alternate = {"member_group","MEMBER_GROUP"})
    @CgFieldAnnotation(name="member_group",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String memberGroup;		//会员级别 db field:member_group

    @SerializedName(value = "bonusPoint", alternate = {"bonus_point","BONUS_POINT"})
    @CgFieldAnnotation(name="bonus_point",jdbcType="INTEGER",nullable=false,format="")
    private Integer bonusPoint;		//积分 db field:bonus_point

    @SerializedName(value = "amountMoney", alternate = {"amount_money","AMOUNT_MONEY"})
    @CgFieldAnnotation(name="amount_money",jdbcType="INTEGER",nullable=false,format="")
    private Integer amountMoney;		//余额 db field:amount_money

    @SerializedName(value = "costLimit", alternate = {"cost_limit","COST_LIMIT"})
    @CgFieldAnnotation(name="cost_limit",jdbcType="INTEGER",nullable=false,format="")
    private Integer costLimit;		//消费限额 db field:cost_limit

    @SerializedName(value = "dayLimit", alternate = {"day_limit","DAY_LIMIT"})
    @CgFieldAnnotation(name="day_limit",jdbcType="INTEGER",nullable=false,format="")
    private Integer dayLimit;		//日限额 db field:day_limit

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "activeSince", alternate = {"active_since","ACTIVE_SINCE"})
    @CgFieldAnnotation(name="active_since",jdbcType="TIMESTAMP",nullable=true,format="yyyy-mm-dd hh:mm")
    private Date activeSince;		//生效时间 db field:active_since

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "expireAt", alternate = {"expire_at","EXPIRE_AT"})
    @CgFieldAnnotation(name="expire_at",jdbcType="TIMESTAMP",nullable=true,format="yyyy-mm-dd hh:mm")
    private Date expireAt;		//有效期限 db field:expire_at

    @SerializedName(value = "checkCode", alternate = {"check_code","CHECK_CODE"})
    @CgFieldAnnotation(name="check_code",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String checkCode;		//检验码 db field:check_code

    private Boolean isValid;		//有效性 非数据库字段

    @Override public Object getPkValue(){ return getUserNo(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setUserNo(null);
        else setUserNo(String.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
    @Override
    public String toSortedString() {
        return "[EwUserExt]"
                .concat(" userNo:" + this.userNo)
                .concat(" member group:" + (this.memberGroup == null ? "" : this.memberGroup))
                .concat(" amount money:" + this.amountMoney)
                .concat(" bonus point:" + this.bonusPoint)
                .concat(" cost limit:" + this.costLimit)
                .concat(" day limit:" + this.dayLimit);

    }
//^_^ Your code end.
}
