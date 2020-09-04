/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.attendance.specialshift.pojo;
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

//  Pojo entity : AdSpecialShift (特殊排班表)
@CgTableAnnotation(name="ad_special_shift",
                   title="adSpecialShift",
                   baseUrl="/attendance/specialshift/adSpecialShift",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="adSpecialShift",
                   module="attendance")
@Getter
@Setter
public class AdSpecialShift implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "shiftMode", alternate = {"shift_mode","SHIFT_MODE"})
    @CgFieldAnnotation(name="shift_mode",jdbcType="INTEGER",nullable=false,format="")
    private Integer shiftMode;		//排班属性 db field:shift_mode

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//名称 db field:name

    @SerializedName(value = "modeProperty", alternate = {"mode_property","MODE_PROPERTY"})
    @CgFieldAnnotation(name="mode_property",jdbcType="INTEGER",nullable=true,format="")
    private Integer modeProperty;		//考勤模式 db field:mode_property

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "startDate", alternate = {"start_date","START_DATE"})
    @CgFieldAnnotation(name="start_date",jdbcType="DATE",nullable=false,format="yyyy-mm-dd")
    private Date startDate;		//启用时间 db field:start_date

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    @JsonAdapter(value= GsonDateTypeAdapter.class)
    @SerializedName(value = "endDate", alternate = {"end_date","END_DATE"})
    @CgFieldAnnotation(name="end_date",jdbcType="DATE",nullable=false,format="yyyy-mm-dd")
    private Date endDate;		//结束时间(含) db field:end_date

    @SerializedName(value = "orgCodes", alternate = {"org_codes","ORG_CODES"})
    @CgFieldAnnotation(name="org_codes",jdbcType="VARCHAR",length=300,nullable=false,format="@")
    private String orgCodes;		//部门 db field:org_codes

    @SerializedName(value = "sexProperty", alternate = {"sex_property","SEX_PROPERTY"})
    @CgFieldAnnotation(name="sex_property",jdbcType="INTEGER",nullable=true,format="")
    private Integer sexProperty;		//性别 db field:sex_property

    @SerializedName(value = "ageProperty0", alternate = {"age_property0","AGE_PROPERTY0"})
    @CgFieldAnnotation(name="age_property0",jdbcType="INTEGER",nullable=true,format="")
    private Integer ageProperty0;		//年龄0 db field:age_property0

    @SerializedName(value = "ageProperty1", alternate = {"age_property1","AGE_PROPERTY1"})
    @CgFieldAnnotation(name="age_property1",jdbcType="INTEGER",nullable=true,format="")
    private Integer ageProperty1;		//年龄1 db field:age_property1

    @SerializedName(value = "levelProperty0", alternate = {"level_property0","LEVEL_PROPERTY0"})
    @CgFieldAnnotation(name="level_property0",jdbcType="INTEGER",nullable=true,format="")
    private Integer levelProperty0;		//职级0 db field:level_property0

    @SerializedName(value = "levelProperty1", alternate = {"level_property1","LEVEL_PROPERTY1"})
    @CgFieldAnnotation(name="level_property1",jdbcType="INTEGER",nullable=true,format="")
    private Integer levelProperty1;		//职级1 db field:level_property1

    @SerializedName(value = "description", alternate = {"DESCRIPTION"})
    @CgFieldAnnotation(name="description",jdbcType="VARCHAR",length=45,nullable=true,format="@")
    private String description;		//备注 db field:description

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
