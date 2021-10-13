/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
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

//  Pojo entity : DevPeople (注册人员|People)
@CgTableAnnotation(name="dev_people",
                   title="devPeople",
                   pk="user_no",
                   entityPk="userNo",
                   baseUrl="/reader/devPeople",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="devPeople",
                   module="reader")
@Getter
@Setter
public class DevPeople implements CgEntity {
    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",length=15,nullable=false,format="@")
    private String userNo;		//用户号 db field:user_no

    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="real_name",jdbcType="VARCHAR",length=32,nullable=false,format="@")
    private String realName;		//姓名 db field:real_name

    @SerializedName(value = "sex", alternate = {"SEX"})
    @CgFieldAnnotation(name="sex",jdbcType="VARCHAR",length=1,nullable=true,format="@")
    private String sex;		//性别 db field:sex

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "birthDate", alternate = {"birth_date","BIRTH_DATE"})
    @CgFieldAnnotation(name="birth_date",jdbcType="DATE",nullable=true,format="yyyy-mm-dd")
    private Date birthDate;		//生日 db field:birth_date

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="org_code",jdbcType="INTEGER",nullable=false,format="")
    private Integer orgCode;		//部门 db field:org_code

    @SerializedName(value = "dutyRank", alternate = {"duty_rank","DUTY_RANK"})
    @CgFieldAnnotation(name="duty_rank",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String dutyRank;		//职务 db field:duty_rank

    @SerializedName(value = "cardNo", alternate = {"card_no","CARD_NO"})
    @CgFieldAnnotation(name="card_no",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String cardNo;		//卡号 db field:card_no

    @SerializedName(value = "idType", alternate = {"id_type","ID_TYPE"})
    @CgFieldAnnotation(name="id_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer idType;		//证件类型 db field:id_type

    @SerializedName(value = "idNumber", alternate = {"id_number","ID_NUMBER"})
    @CgFieldAnnotation(name="id_number",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String idNumber;		//证件号码 db field:id_number

    @SerializedName(value = "userType", alternate = {"user_type","USER_TYPE"})
    @CgFieldAnnotation(name="user_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer userType;		//用户类型 db field:user_type

    @SerializedName(value = "mobilePhone", alternate = {"mobile_phone","MOBILE_PHONE"})
    @CgFieldAnnotation(name="mobile_phone",jdbcType="VARCHAR",length=32,nullable=true,format="@")
    private String mobilePhone;		//手机号码 db field:mobile_phone

    @SerializedName(value = "email", alternate = {"EMAIL"})
    @CgFieldAnnotation(name="email",jdbcType="VARCHAR",length=50,nullable=true,format="@")
    private String email;		//邮箱 db field:email

    @SerializedName(value = "registerType", alternate = {"register_type","REGISTER_TYPE"})
    @CgFieldAnnotation(name="register_type",jdbcType="INTEGER",nullable=false,format="")
    private Integer registerType;		//创建类型 db field:register_type

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

    @SerializedName(value = "devPassword", alternate = {"dev_password","DEV_PASSWORD"})
    @CgFieldAnnotation(name="dev_password",jdbcType="VARCHAR",length=32,nullable=true,format="@")
    private String devPassword;		//设备密码 db field:dev_password

    @SerializedName(value = "regFingers", alternate = {"reg_fingers","REG_FINGERS"})
    @CgFieldAnnotation(name="reg_fingers",jdbcType="INTEGER",nullable=true,format="")
    private Integer regFingers;		//注册数 db field:reg_fingers

    @SerializedName(value = "note", alternate = {"NOTE"})
    @CgFieldAnnotation(name="note",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String note;		//备注 db field:note

    @SerializedName(value = "idNation", alternate = {"id_nation","ID_NATION"})
    @CgFieldAnnotation(name="id_nation",jdbcType="VARCHAR",length=100,nullable=true,format="@")
    private String idNation;		//民族 db field:id_nation

    @SerializedName(value = "photo", alternate = {"PHOTO"})
    @CgFieldAnnotation(name="photo",jdbcType="VARCHAR",length=200,nullable=true,multiple=false,format="@")
    private String photo;		//照片 db field:photo

    @SerializedName(value = "homeAddr", alternate = {"home_addr","HOME_ADDR"})
    @CgFieldAnnotation(name="home_addr",jdbcType="VARCHAR",length=200,nullable=true,format="@")
    private String homeAddr;		//住址 db field:home_addr

    private String fingerNo1;		//已采集手指1 非数据库字段

    private String fingerNo2;		//已采集手指2 非数据库字段

    private Boolean warning1;		//胁迫标识 非数据库字段

    private Boolean warning2;		//胁迫标识 非数据库字段

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
//^_^ Your code end.
}
