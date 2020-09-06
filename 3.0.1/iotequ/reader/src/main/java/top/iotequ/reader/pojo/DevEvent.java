/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
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

//  Pojo entity : DevEvent (设备事件)
@CgTableAnnotation(name="dev_event",
                   title="devEvent",
                   join="LEFT JOIN dev_people ON dev_event.user_no = dev_people.user_no",
                   baseUrl="/reader/devEvent",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="devEvent",
                   module="reader")
@Getter
@Setter
public class DevEvent implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="dev_event.id",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String id;

    @SerializedName(value = "devType", alternate = {"dev_type","DEV_TYPE"})
    @CgFieldAnnotation(name="dev_event.dev_type",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String devType;		//设备类别 db field:dev_type

    @SerializedName(value = "devNo", alternate = {"dev_no","DEV_NO"})
    @CgFieldAnnotation(name="dev_event.dev_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String devNo;		//设备号 db field:dev_no

    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="dev_event.org_code",jdbcType="INTEGER",nullable=true,format="")
    private Integer orgCode;		//部门 db field:org_code

    @SerializedName(value = "status", alternate = {"STATUS"})
    @CgFieldAnnotation(name="dev_event.status",jdbcType="INTEGER",nullable=true,format="")
    private Integer status;		//状态 db field:status

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "time", alternate = {"TIME"})
    @CgFieldAnnotation(name="dev_event.time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date time;		//时间 db field:time

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="dev_event.user_no",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String userNo;		//用户 db field:user_no
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="dev_people.real_name",jdbcType="VARCHAR")
    private String realName;

    private String datDate;		//日期 非数据库字段

    private String datTime;		//时间 非数据库字段

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
