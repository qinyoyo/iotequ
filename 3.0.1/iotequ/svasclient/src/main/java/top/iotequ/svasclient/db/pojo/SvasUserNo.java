/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.svasclient.db.pojo;
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import top.iotequ.framework.serializer.jackson.DateDeserializer;
import top.iotequ.framework.serializer.jackson.DateSerializer;
import top.iotequ.framework.serializer.gson.GsonDateTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : SvasUserNo (用户号列表)
@CgTableAnnotation(name="dev_user_no",
        title="devUserNo",
        baseUrl="/reader/devUserNo",
        hasLicence=false,
        pkType="Integer",
        pkKeyType="1",
        generatorName="devUserNo",
        module="reader")
@Getter
@Setter
public class SvasUserNo implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",nullable=false,format="@")
    private String userNo;		//用户号 db field:user_no

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",nullable=true,format="@")
    private String name;		//姓名 db field:name

    @SerializedName(value = "sex", alternate = {"SEX"})
    @CgFieldAnnotation(name="sex",jdbcType="VARCHAR",nullable=true,format="@")
    private String sex;		//性别 db field:sex

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "birthDate", alternate = {"birth_date","BIRTH_DATE"})
    @CgFieldAnnotation(name="birth_date",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date birthDate;		//生日 db field:birth_date

    @SerializedName(value = "mobilePhone", alternate = {"mobile_phone","MOBILE_PHONE"})
    @CgFieldAnnotation(name="mobile_phone",jdbcType="VARCHAR",nullable=true,format="@")
    private String mobilePhone;		//手机号码 db field:mobile_phone

    @SerializedName(value = "email", alternate = {"EMAIL"})
    @CgFieldAnnotation(name="email",jdbcType="VARCHAR",nullable=true,format="@")
    private String email;		//邮箱 db field:email

    @SerializedName(value = "wechatOpenid", alternate = {"wechat_openid","WECHAT_OPENID"})
    @CgFieldAnnotation(name="wechat_openid",jdbcType="VARCHAR",nullable=true,format="@")
    private String wechatOpenid;		//微信openId db field:wechat_openid

    @SerializedName(value = "idType", alternate = {"id_type","ID_TYPE"})
    @CgFieldAnnotation(name="id_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer idType;		//证件类型 db field:id_type

    @SerializedName(value = "idNo", alternate = {"id_no","ID_NO"})
    @CgFieldAnnotation(name="id_no",jdbcType="VARCHAR",nullable=false,format="@")
    private String idNo;		//证件号 db field:id_no

    @SerializedName(value = "idNation", alternate = {"id_nation","ID_NATION"})
    @CgFieldAnnotation(name="id_nation",jdbcType="VARCHAR",nullable=true,format="@")
    private String idNation;		//民族 db field:id_nation

    @SerializedName(value = "idOrganization", alternate = {"id_organization","ID_ORGANIZATION"})
    @CgFieldAnnotation(name="id_organization",jdbcType="VARCHAR",nullable=true,format="@")
    private String idOrganization;		//发证机构 db field:id_organization

    @SerializedName(value = "homeAddr", alternate = {"home_addr","HOME_ADDR"})
    @CgFieldAnnotation(name="home_addr",jdbcType="VARCHAR",nullable=true,format="@")
    private String homeAddr;		//住址 db field:home_addr

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "validDate", alternate = {"valid_date","VALID_DATE"})
    @CgFieldAnnotation(name="valid_date",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date validDate;		//生效日期 db field:valid_date

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "expiredDate", alternate = {"expired_date","EXPIRED_DATE"})
    @CgFieldAnnotation(name="expired_date",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date expiredDate;		//过期日期 db field:expired_date

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "regTime", alternate = {"reg_time","REG_TIME"})
    @CgFieldAnnotation(name="reg_time",jdbcType="TIMESTAMP",nullable=true,format="yyyy-mm-dd hh:mm")
    private Date regTime;		//注册时间 db field:reg_time

    @SerializedName(value = "photo", alternate = {"PHOTO"})
    @CgFieldAnnotation(name="photo",jdbcType="VARCHAR",nullable=true,format="@")
    private String photo;		//photo db field:photo

    private Integer regFingers;   // not db fields

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
