/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.codegenerator.pojo;
import top.iotequ.framework.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

//  Pojo entity : GenProject (新建项目)
@Getter
@Setter
public class GenProject  {
    private String name;		//项目名称 非数据库字段

    private String version;		//版本 非数据库字段

    private String note;		//项目描述 非数据库字段

    private String modules;		//包含的iotequ模块 非数据库字段

    private Boolean mavenModule;		//maven模块 非数据库字段

    private String path;		//保存路径 非数据库字段

    @Override
    public String toString() {
    	return StringUtil.toJsonString(this);
    }
//^_^自定义代码: java代码 ,请不要删除和修改本行
//^_^自定义代码结束,请不要删除和修改本行
}
