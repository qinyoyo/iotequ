/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevNewDevice (未注册设备)
@CgTableAnnotation(name="dev_new_device",
                   title="devNewDevice",
                   pk="reader_no",
                   entityPk="readerNo",
                   baseUrl="/reader/devNewDevice",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="3",
                   generatorName="devNewDevice",
                   module="reader")
@Getter
@Setter
public class DevNewDevice implements CgEntity {
    @SerializedName(value = "readerNo", alternate = {"reader_no","READER_NO"})
    @CgFieldAnnotation(name="reader_no",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String readerNo;		//模块号 db field:reader_no

    @SerializedName(value = "snNo", alternate = {"sn_no","SN_NO"})
    @CgFieldAnnotation(name="sn_no",jdbcType="VARCHAR",length=36,nullable=true,format="@")
    private String snNo;		//序列号 db field:sn_no

    @SerializedName(value = "type", alternate = {"TYPE"})
    @CgFieldAnnotation(name="type",jdbcType="VARCHAR",length=30,nullable=false,format="@")
    private String type;		//型号 db field:type

    @SerializedName(value = "ip", alternate = {"IP"})
    @CgFieldAnnotation(name="ip",jdbcType="VARCHAR",length=20,nullable=true,format="@")
    private String ip;		//IP地址 db field:ip

    @Override public Object getPkValue(){ return getReaderNo(); }
    @Override
    public void setPkValue(Object value) {
        if (value==null) setReaderNo(null);
        else setReaderNo(String.valueOf(value.toString()));
    }
    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^ Your code start:
//^_^ Your code end.
}
