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

//  Pojo entity : DevPeopleMapping (下发用户关系|Map of download users)
@CgTableAnnotation(name="dev_people_mapping",
                   title="devPeopleMapping",
                   baseUrl="/reader/devPeopleMapping",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devPeopleMapping",
                   module="reader")
@Getter
@Setter
public class DevPeopleMapping implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",length=10,nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "readerNo", alternate = {"reader_no","READER_NO"})
    @CgFieldAnnotation(name="reader_no",jdbcType="VARCHAR",length=20,nullable=false,format="@")
    private String readerNo;		//设备编号 db field:reader_no

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="user_no",jdbcType="VARCHAR",length=36,nullable=false,format="@")
    private String userNo;		//用户 db field:user_no

    @SerializedName(value = "status", alternate = {"STATUS"})
    @CgFieldAnnotation(name="status",jdbcType="VARCHAR",length=1,nullable=false,format="@")
    private String status;		//状态 db field:status

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
