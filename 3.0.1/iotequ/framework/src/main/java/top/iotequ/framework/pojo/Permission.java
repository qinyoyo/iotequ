/**************************************************
    Create by iotequ generator 3.0.0
    Author : Qinyoyo
**************************************************/

package top.iotequ.framework.pojo;
import top.iotequ.util.StringUtil;
import top.iotequ.framework.pojo.CgEntity;
import lombok.Getter;
import lombok.Setter;
import com.google.gson.annotations.SerializedName;
import top.iotequ.util.CgFieldAnnotation;
import top.iotequ.util.CgTableAnnotation;
import java.util.*;

//  Pojo entity : Permission (功能分配表)
@CgTableAnnotation(name="sys_permission",
                   title="sysPermission",
                   baseUrl="/framework/sysPermission",
                   hasLicence=false,
                   pkType="Integer",
                   pkKeyType="1",
                   generatorName="sysPermission",
                   module="framework")
@Getter
@Setter
public class Permission implements CgEntity {
    @SerializedName(value = "id", alternate = {"ID"})
    @CgFieldAnnotation(name="id",jdbcType="INTEGER",nullable=false,format="")
    private Integer id;

    @SerializedName(value = "role", alternate = {"ROLE"})
    @CgFieldAnnotation(name="role",jdbcType="INTEGER",nullable=false,format="")
    private Integer role;		//角色 db field:role

    @SerializedName(value = "action", alternate = {"ACTION"})
    @CgFieldAnnotation(name="action",jdbcType="INTEGER",nullable=false,format="")
    private Integer action;		//功能 db field:action

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
	private Role roleObject;
	private Action actionObject; // 为了提高检索速度
	public Role getRoleObject() {
		return roleObject;
	}
	public void setRoleObject(Role roleObject) {
		this.roleObject = roleObject;
	}
	public Action getActionObject() {
		return actionObject;
	}
	public void setActionObject(Action actionObject) {
		this.actionObject = actionObject;
	}
//^_^ Your code end.
}
