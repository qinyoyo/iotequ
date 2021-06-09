/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.reader.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : DevData (设备数据)
@CgTableAnnotation(name="null",
                   title="devData",
                   baseUrl="/reader/devData",
                   hasLicence=false,
                   pkType="String",
                   pkKeyType="2",
                   generatorName="devData",
                   module="reader")
@Getter
@Setter
public class DevData implements CgEntity {
    private String id;		//id db field:id

    private String readerSelection;		//选择的设备 db field:reader_selection
    private String readerName;

    private String userSelection;		//选择人员 db field:user_selection
    private String userName;

    private String noteHtml;		//提示 非数据库字段

    private Boolean covered;		//是否覆盖设备中的用户 非数据库字段

    private String operation;		//操作 非数据库字段

    private String deviceSelectionMode;		//设备选择模式 非数据库字段

    private String groupSelection;		//选择的设备分组 非数据库字段

    private Boolean includeSubOrg;		//包含下级部门 非数据库字段

    private String userSelectionMode;		//人员选择模式 非数据库字段

    private String orgSelection;		//选择组织机构 非数据库字段

    private Boolean includeSubGroup;		//包含下级分组 非数据库字段

    private Boolean uploadAllUser;		//上传所有用户 非数据库字段

    private Boolean uploadNewUser;		//上传新用户 非数据库字段

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
