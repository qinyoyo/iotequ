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

//  Pojo entity : DevReaderPeople (设备人员信息)
@CgTableAnnotation(name="dev_reader_people",
                   title="devReaderPeople",
                   join="LEFT JOIN dev_people ON dev_reader_people.user_no = dev_people.user_no",
                   baseUrl="/reader/devReaderPeople",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devReaderPeople",
                   module="reader")
@Getter
@Setter
public class DevReaderPeople implements CgEntity {
    @SerializedName(value = "type", alternate = {"TYPE"})
    @CgFieldAnnotation(name="dev_reader_people.type",jdbcType="INTEGER",length=36,nullable=false,format="")
    private Integer type;		//操作类型 db field:type

    @SerializedName(value = "orderNum", alternate = {"order_num","ORDER_NUM"})
    @CgFieldAnnotation(name="dev_reader_people.order_num",jdbcType="INTEGER",length=11,nullable=true,format="")
    private Integer orderNum;		//排序 db field:order_num

    @SerializedName(value = "status", alternate = {"STATUS"})
    @CgFieldAnnotation(name="dev_reader_people.status",jdbcType="INTEGER",length=2,nullable=true,format="")
    private Integer status;		//状态 db field:status

    @SerializedName(value = "readerNo", alternate = {"reader_no","READER_NO"})
    @CgFieldAnnotation(name="dev_reader_people.reader_no",jdbcType="VARCHAR",length=20,nullable=true,format="@")
    private String readerNo;		//设备编号1234 db field:reader_no

    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="dev_reader_people.id",jdbcType="INTEGER",length=11,nullable=false,format="")
    private Integer id;		//主键 db field:id

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="dev_reader_people.user_no",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String userNo;		//用户编号 db field:user_no
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "birthDate", alternate = {"birth_date","BIRTH_DATE"})
    @CgFieldAnnotation(name="dev_people.birth_date",jdbcType="VARCHAR")
    private Date birthDate;
    @SerializedName(value = "cardNo", alternate = {"card_no","CARD_NO"})
    @CgFieldAnnotation(name="dev_people.card_no",jdbcType="VARCHAR")
    private String cardNo;
    @SerializedName(value = "devPassword", alternate = {"dev_password","DEV_PASSWORD"})
    @CgFieldAnnotation(name="dev_people.dev_password",jdbcType="VARCHAR")
    private String devPassword;
    @SerializedName(value = "dutyRank", alternate = {"duty_rank","DUTY_RANK"})
    @CgFieldAnnotation(name="dev_people.duty_rank",jdbcType="VARCHAR")
    private String dutyRank;
    @SerializedName(value = "email", alternate = {"EMAIL"})
    @CgFieldAnnotation(name="dev_people.email",jdbcType="VARCHAR")
    private String email;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "expiredDate", alternate = {"expired_date","EXPIRED_DATE"})
    @CgFieldAnnotation(name="dev_people.expired_date",jdbcType="VARCHAR")
    private Date expiredDate;
    @SerializedName(value = "homeAddr", alternate = {"home_addr","HOME_ADDR"})
    @CgFieldAnnotation(name="dev_people.home_addr",jdbcType="VARCHAR")
    private String homeAddr;
    @SerializedName(value = "idNation", alternate = {"id_nation","ID_NATION"})
    @CgFieldAnnotation(name="dev_people.id_nation",jdbcType="VARCHAR")
    private String idNation;
    @SerializedName(value = "idNumber", alternate = {"id_number","ID_NUMBER"})
    @CgFieldAnnotation(name="dev_people.id_number",jdbcType="VARCHAR")
    private String idNumber;
    @SerializedName(value = "idType", alternate = {"id_type","ID_TYPE"})
    @CgFieldAnnotation(name="dev_people.id_type",jdbcType="VARCHAR")
    private Integer idType;
    @SerializedName(value = "mobilePhone", alternate = {"mobile_phone","MOBILE_PHONE"})
    @CgFieldAnnotation(name="dev_people.mobile_phone",jdbcType="VARCHAR")
    private String mobilePhone;
    @SerializedName(value = "note", alternate = {"NOTE"})
    @CgFieldAnnotation(name="dev_people.note",jdbcType="VARCHAR")
    private String note;
    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="dev_people.org_code",jdbcType="VARCHAR")
    private Integer orgCode;
    @SerializedName(value = "photo", alternate = {"PHOTO"})
    @CgFieldAnnotation(name="dev_people.photo",jdbcType="VARCHAR")
    private String photo;
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="dev_people.real_name",jdbcType="VARCHAR")
    private String realName;
    @SerializedName(value = "regFingers", alternate = {"reg_fingers","REG_FINGERS"})
    @CgFieldAnnotation(name="dev_people.reg_fingers",jdbcType="VARCHAR")
    private Integer regFingers;
    @SerializedName(value = "registerType", alternate = {"register_type","REGISTER_TYPE"})
    @CgFieldAnnotation(name="dev_people.register_type",jdbcType="VARCHAR")
    private Integer registerType;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "regTime", alternate = {"reg_time","REG_TIME"})
    @CgFieldAnnotation(name="dev_people.reg_time",jdbcType="VARCHAR")
    private Date regTime;
    @SerializedName(value = "sex", alternate = {"SEX"})
    @CgFieldAnnotation(name="dev_people.sex",jdbcType="VARCHAR")
    private String sex;
    @SerializedName(value = "userNoDevPeopleUserNo", alternate = {"user_no_dev_people_user_no","USER_NO_DEV_PEOPLE_USER_NO"})
    @CgFieldAnnotation(name="dev_people.user_no",jdbcType="VARCHAR")
    private String userNoDevPeopleUserNo;
    @SerializedName(value = "userType", alternate = {"user_type","USER_TYPE"})
    @CgFieldAnnotation(name="dev_people.user_type",jdbcType="VARCHAR")
    private Integer userType;
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "validDate", alternate = {"valid_date","VALID_DATE"})
    @CgFieldAnnotation(name="dev_people.valid_date",jdbcType="VARCHAR")
    private Date validDate;

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
