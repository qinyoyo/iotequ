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
import top.iotequ.framework.serializer.jackson.DatetimeSerializer;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevDownloadPlan (下发计划|Download plan)
@CgTableAnnotation(name="dev_download_plan",
                   title="devDownloadPlan",
                   baseUrl="/reader/devDownloadPlan",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devDownloadPlan",
                   module="reader")
@Getter
@Setter
public class DevDownloadPlan implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",length=10,nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String userNo;		//用户编号 db field:user_no

    @SerializedName(value = "readerNo", alternate = {"reader_no","READER_NO"})
    @CgFieldAnnotation(name="reader_no",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String readerNo;		//设备编号 db field:reader_no

    @SerializedName(value = "type", alternate = {"TYPE"})
    @CgFieldAnnotation(name="type",jdbcType="INTEGER",length=2,nullable=false,format="")
    private Integer type;		//下发 db field:type

    @SerializedName(value = "downloadNum", alternate = {"download_num","DOWNLOAD_NUM"})
    @CgFieldAnnotation(name="download_num",jdbcType="INTEGER",length=2,nullable=false,format="")
    private Integer downloadNum;		//尝试下发次数 db field:download_num

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DatetimeSerializer.class)
    @SerializedName(value = "time", alternate = {"TIME"})
    @CgFieldAnnotation(name="time",jdbcType="TIMESTAMP",nullable=false,format="yyyy-mm-dd hh:mm")
    private Date time;		//创建时间 db field:time

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
