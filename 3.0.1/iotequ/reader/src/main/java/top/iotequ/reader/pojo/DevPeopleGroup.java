/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.framework.util.CgFieldAnnotation;
import top.iotequ.framework.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevPeopleGroup (分组人员)
@CgTableAnnotation(name="dev_people_group",
                   title="devPeopleGroup",
                   join="RIGHT JOIN dev_people ON dev_people_group.user_no = dev_people.user_no",
                   baseUrl="/reader/devPeopleGroup",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="devPeopleGroup",
                   module="reader")
@Getter
@Setter
public class DevPeopleGroup implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="dev_people_group.id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;		//id db field:id

    @SerializedName(value = "groupId", alternate = {"group_id","GROUP_ID"})
    @CgFieldAnnotation(name="dev_people_group.group_id",jdbcType="INTEGER",nullable=false,format="")
    private Integer groupId;		//分组id db field:group_id

    @SerializedName(value = "userNo", alternate = {"user_no","USER_NO"})
    @CgFieldAnnotation(name="dev_people_group.user_no",jdbcType="VARCHAR",length=45,nullable=false,format="@")
    private String userNo;		//姓名 db field:user_no
    @SerializedName(value = "realName", alternate = {"real_name","REAL_NAME"})
    @CgFieldAnnotation(name="dev_people.real_name",jdbcType="VARCHAR")
    private String realName;
    @SerializedName(value = "orgCode", alternate = {"org_code","ORG_CODE"})
    @CgFieldAnnotation(name="dev_people.org_code",jdbcType="VARCHAR")
    private Integer orgCode;

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
