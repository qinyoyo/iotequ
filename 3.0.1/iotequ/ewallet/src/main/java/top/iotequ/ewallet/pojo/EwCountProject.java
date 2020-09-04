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

//  Pojo entity : EwCountProject (计次消费项目)
@CgTableAnnotation(name="ew_count_project",
                   title="ewCountProject",
                   baseUrl="/ewallet/ewCountProject",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="ewCountProject",
                   module="ewallet")
@Getter
@Setter
public class EwCountProject implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//编号 db field:id

    @SerializedName(value = "icon", alternate = {"ICON"})
    @CgFieldAnnotation(name="icon",jdbcType="VARCHAR",length=45,nullable=true,multiple=false,format="@")
    private String icon;		//图标 db field:icon

    @SerializedName(value = "name", alternate = {"NAME"})
    @CgFieldAnnotation(name="name",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String name;		//消费名称 db field:name

    @SerializedName(value = "basePrice", alternate = {"base_price","BASE_PRICE"})
    @CgFieldAnnotation(name="base_price",jdbcType="INTEGER",nullable=false,format="")
    private Integer basePrice;		//基础价格 db field:base_price

    @SerializedName(value = "baseValue", alternate = {"base_value","BASE_VALUE"})
    @CgFieldAnnotation(name="base_value",jdbcType="INTEGER",nullable=false,format="")
    private Integer baseValue;		//基本消费单元 db field:base_value

    @SerializedName(value = "bonusPoint", alternate = {"bonus_point","BONUS_POINT"})
    @CgFieldAnnotation(name="bonus_point",jdbcType="DOUBLE",nullable=false,format="0.00")
    private Double bonusPoint;		//每单元默认积分数 db field:bonus_point

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "startTime", alternate = {"start_time","START_TIME"})
    @CgFieldAnnotation(name="start_time",jdbcType="TIME",nullable=true,format="hh:mm")
    private Date startTime;		//有效开始时间 db field:start_time

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    @JsonAdapter(value= GsonTimeTypeAdapter.class)
    @SerializedName(value = "endTime", alternate = {"end_time","END_TIME"})
    @CgFieldAnnotation(name="end_time",jdbcType="TIME",nullable=true,format="hh:mm")
    private Date endTime;		//有效结束时间 db field:end_time

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
