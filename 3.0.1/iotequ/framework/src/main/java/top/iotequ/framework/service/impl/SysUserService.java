package top.iotequ.framework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import top.iotequ.framework.dao.UserDao;
import top.iotequ.framework.event.PeopleInfoChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.pojo.User;
import top.iotequ.framework.util.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;


@Service
public class SysUserService extends CgSysUserService implements ApplicationListener<PeopleInfoChangedEvent> {
	@Autowired
	private UserDao userDao;
	@Autowired
    private Environment env;
	@Autowired
	ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(PeopleInfoChangedEvent event) {
		try {
			if (event.getSource() instanceof SysUserService) return;
			int mode = event.getMode();
			if (mode == PeopleInfoChangedEvent.update) {
				User user = EntityUtil.entityCopyFrom(User.class, event.getPeople());
				if (EntityUtil.isEntityEmpty(user)) return;
				user.setId(event.getPeople().getUserNo());
				userDao.updateSelective(EntityUtil.mapFromEntity(user));
				user = userDao.select(user.getId());
			}
		} catch (Exception e) {}
	}

	@Override
	public  void beforeSave(User user0, User user, boolean updateSelective, HttpServletRequest request) throws IotequException {
		super.beforeSave(user0, user, updateSelective, request);
		if (!Util.hasRoleAdmin()) {
			String nm=user.getName();
			if (EntityUtil.entityEquals(nm,"admin")  || EntityUtil.entityEquals(nm,"guest"))
				throw new IotequException(IotequThrowable.NO_AUTHORITY,"不允许修改系统用户");
			String roles = user.getRoleList();
			Integer orgRoles = user.getOrgPrivilege();
			IotequException e = new IotequException(IotequThrowable.NO_AUTHORITY,"只有超级管理员才能赋权");
			IotequException oe = new IotequException(IotequThrowable.NO_AUTHORITY,"只有超级管理员才能部门赋权");
			IotequException se = new IotequException(IotequThrowable.NO_AUTHORITY,"只有超级管理员才能更改状态");
			IotequException pe = new IotequException(IotequThrowable.NO_AUTHORITY,"只有超级管理员才能更改密码");
			if (updateSelective) {
				if (!Util.isEmpty(roles)) throw e;
				if (!Util.isEmpty(user.getPassword())) throw pe;
				if (!Util.isEmpty(orgRoles)) throw oe;
				if (Objects.nonNull(user.getLocked()) || Objects.nonNull(user.getState()) || Objects.nonNull(user.getPasswordErrorTimes())
				    ||Objects.nonNull(user.getExpiredTime()) || Objects.nonNull(user.getRegTime())) throw oe;
			} else if (Objects.isNull(user0)) {
				if (!Util.isEmpty(roles)) throw e;
				if (!Util.isEmpty(orgRoles)) throw oe;
			} else {
				if (!EntityUtil.entityEquals(user0.getRoleList(), roles)) throw e;
				if (!EntityUtil.entityEquals(user0.getOrgPrivilege(), orgRoles)) throw e;
				if (!EntityUtil.entityEquals(user0.getLocked(),user.getLocked()) || !EntityUtil.entityEquals(user0.getState(),user.getState())
						|| !EntityUtil.entityEquals(user0.getPasswordErrorTimes(),user.getPasswordErrorTimes())
						|| !EntityUtil.entityEquals(user0.getExpiredTime(),user.getExpiredTime())
						|| !EntityUtil.entityEquals(user0.getRegTime(),user.getRegTime())) throw oe;
				if (!EntityUtil.entityEquals(user0.getPassword(), user.getPassword())) throw pe;

			}
		}
		if (Objects.isNull(user0) || !EntityUtil.entityEquals(user0.getPassword(), user.getPassword()))
			user.setPassword(StringUtil.encodePassword(Util.isEmpty(user.getPassword()) ? "123456" : user.getPassword()));
	}

	@Override
	public void setPrimaryKey(User user) throws IotequException {
		if (Util.isEmpty(user.getId())) {
			Integer t = user.getIdType();
			String no = user.getIdNumber();
			if (t==null || t==0 || Util.isEmpty(no)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"证件信息不全");
			Object svasBean = Util.getBean("svasService");
			String id=null;
			if (svasBean!=null) {
				id = StringUtil.toString(EntityUtil.runMethod(svasBean,"getUserNo",user.getIdType(),user.getIdNumber(),user.getRealName(),null,null));
				if (!Util.isEmpty(id)) {
					Boolean includePhoto = false;
					Object o = EntityUtil.runMethod(svasBean,"getUserAllInfo",id,includePhoto);
					if (o!=null && o instanceof PeopleInfoChangedEvent.People) {
						PeopleInfoChangedEvent.People p = (PeopleInfoChangedEvent.People)o;
						if (p.getRealName()!=null && Util.isEmpty(user.getRealName())) user.setRealName(p.getRealName());
						if (p.getWechatOpenid()!=null && Util.isEmpty(user.getWechatOpenid())) user.setWechatOpenid(p.getWechatOpenid());
						if (p.getEmail()!=null && Util.isEmpty(user.getEmail())) user.setEmail(p.getEmail());
						if (p.getMobilePhone()!=null && Util.isEmpty(user.getMobilePhone())) user.setMobilePhone(p.getMobilePhone());
						if (p.getSex()!=null && Util.isEmpty(user.getSex())) user.setSex(p.getSex());
						if (p.getBirthDate()!=null && Util.isEmpty(user.getBirthDate())) user.setBirthDate(p.getBirthDate());
					}
				}
			}
			if (id==null) id=user.getIdType() + "-" + user.getIdNumber();
			user.setId(id);
		}
	}

	@Override
	public void afterSave(User user0,User user, HttpServletRequest request, RestJson j)  throws IotequException{
		if (!Util.isEmpty(user)) {
			if (!"admin".equals(user.getName()) && !"guest".equals(user.getName())) {
				Util.publishEvent(PeopleInfoChangedEvent.createPeopleInfoChangedEvent(this,user0,user,"id"));
			}
		}
	}


	@Override
	public  void beforeExport( List<User>list,HttpServletRequest request) throws IotequException {
		if (list!=null)		{
			for (User u:list) u.setPassword(null);  // 禁止导出密码
		}
	}
	@Override
	public  void beforeImport(List<User>list,HttpServletRequest request) throws IotequException {
		if (list==null) return;
		String pass=StringUtil.encodePassword("123456");
		int count=list.size();
		for (int i=0;i<count;i++) {
			User u=list.get(i);
			if ("admin".equals(u.getName()) || "guest".equals(u.getName())) {
				list.remove(i);
				count--;
				i--;
			}
			else {
				u.setRoleList(SqlUtil.sqlQueryString("select role_list from sys_user where id=?", u.getId()));    // 禁止导入数据修改权限
				u.setPassword(Util.isEmpty(SqlUtil.sqlQueryString("select password from sys_user where id=?", u.getId()),pass));    // 禁止导入数据修改密码
			}
		}
	}

	@Override
	public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
		if ("resetPassword".equals(action)) {
			SqlUtil.sqlExecute("update sys_user set password = ? where id = ?", StringUtil.encodePassword("123456"),id);
		}
		return new RestJson();
	}
}
