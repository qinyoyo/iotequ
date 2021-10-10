package top.iotequ.reader.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.framework.event.PeopleInfoChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.*;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.util.DownloadPlan;
import top.iotequ.svasclient.SvasTypes.*;
import top.iotequ.svasclient.SvasService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DevPeopleService extends CgDevPeopleService implements ApplicationListener<PeopleInfoChangedEvent> {
	@Autowired
	private SvasService svasService;
	@Autowired
	DevPeopleDao devPeopleDao;
	@Autowired
    private Environment env;

	void saveImage2File(DevPeople people) {
		if (people!=null) {
			String userNo = people.getUserNo();
			if (Util.isEmpty(userNo)) return;
			if (!Util.isEmpty(people.getPhoto()) && people.getPhoto().indexOf("data:")==0) {
				people.setPhoto(Util.saveBase64Image(new File(FileUtil.uploadFileDir(getGeneratorName()),FileUtil.uploadFilename("photo",userNo,"p")).getAbsolutePath(), people.getPhoto()));
			}
		}
	}
	@Override
	public void onApplicationEvent(PeopleInfoChangedEvent event) {
		try {
			if (event.getSource() instanceof DevPeopleService) return;
			DevPeople people= EntityUtil.entityCopyFrom(DevPeople.class,event.getPeople());
			if (people!=null) {
				String userNo = people.getUserNo();
				if (Util.isEmpty(userNo)) return;
				DevPeople people0 = devPeopleDao.select(userNo);
				if (people0!=null) {
					saveImage2File(people);
					devPeopleDao.update(people);
				} else {
					devPeopleDao.insert(people);
				}
			}
		} catch (Exception e) {}
	}
	@Override
	public void beforeSave(DevPeople p0, DevPeople p, boolean updateSelective, HttpServletRequest request) throws IotequException {
		if (p==null) return;
		if (p0==null) p.setRegisterType(1);  // PC 注册
		if (Util.isEmpty(p.getUserNo())) {
			String uno = svasService.getUserNo(p.getIdType(), p.getIdNumber(), p.getRealName(), null, null);
			if (Util.isEmpty(uno)) throw new IotequException(IotequThrowable.FAILURE, "无法获得用户号！");
			p.setUserNo(uno);
			if (!"admin".equals(Util.getUser().getName()) && p.getUserType()==1) {
				throw new IotequException(IotequThrowable.NO_AUTHORITY,"只有超级管理员才能创建管理员");
			}
			if (!"admin".equals(Util.getUser().getName())) p.setDevPassword(null);
		} else {
			if (!"admin".equals(Util.getUser().getName()) && p0.getUserType()!=1 && p.getUserType()==1) throw new IotequException(IotequThrowable.NO_AUTHORITY,"只有超级管理员赋权管理员");
			if (!"admin".equals(Util.getUser().getName())) p.setDevPassword(p0.getDevPassword());
		}
		if (p0==null) { // 新建时获取数据
			try {
				Boolean includePhoto = true;
				PeopleInfoChangedEvent.People pl = svasService.getUserAllInfo(p.getUserNo(), includePhoto);
				if (pl != null) {
					if (pl.getRealName() != null && Util.isEmpty(p.getRealName())) p.setRealName(pl.getRealName());

					if (pl.getEmail() != null && Util.isEmpty(p.getEmail())) p.setEmail(pl.getEmail());
					if (pl.getMobilePhone() != null && Util.isEmpty(p.getMobilePhone()))
						p.setMobilePhone(pl.getMobilePhone());
					if (pl.getSex() != null && Util.isEmpty(p.getSex())) p.setSex(pl.getSex());
					if (pl.getBirthDate() != null && Util.isEmpty(p.getBirthDate())) p.setBirthDate(pl.getBirthDate());

					if (pl.getIdNation() != null && Util.isEmpty(p.getIdNation())) p.setIdNation(pl.getIdNation());
					if (pl.getExpiredDate() != null && Util.isEmpty(p.getExpiredDate()))
						p.setExpiredDate(pl.getExpiredDate());
					if (pl.getValidDate() != null && Util.isEmpty(p.getValidDate())) p.setValidDate(pl.getValidDate());
					if (pl.getHomeAddr() != null && Util.isEmpty(p.getHomeAddr())) p.setHomeAddr(pl.getHomeAddr());

					if (pl.getPhoto() != null && Util.isEmpty(p.getPhoto())) p.setPhoto(pl.getPhoto());
					p.setRegFingers(pl.getRegFingers() == null ? 0 : pl.getRegFingers());
				}
			} catch (Exception e) {
			}
		}else{//修改设备中用户状态
			SqlUtil.sqlExecute("update dev_people_mapping set status=? where user_no=?","1",p.getUserNo());
		}
		saveImage2File(p);
	}
	@Override
	public void afterSave(DevPeople p0, DevPeople p, HttpServletRequest request, RestJson j) {
		Util.publishEvent(PeopleInfoChangedEvent.createPeopleInfoChangedEvent(this,p0,p,"userNo"));
		j.put("userNo", p.getUserNo());
		if(p0!=null&&p0.getUserNo()!=null) {
			DownloadPlan.download(p.getUserNo(), 1,true);
		}
	}

	private void sendFingerRegisteredInfo(String userNo,RestJson j) {
		try {
			List<SvasTemplates> list = svasService.getFingerInfo(userNo);
			Integer type1=null,type2=null,count=0;
			Boolean warning1=false, warning2=false;
			if (!Util.isEmpty(list)) {
				count = list.size();
				for (SvasTemplates temp : list) {
					if (temp.fingerNo == 1) {
						type1 = temp.fingerType;
						warning1=temp.warning;
					}
					else if (temp.fingerNo == 2) {
						type2 = temp.fingerType;
						warning2 = temp.warning;
					}
				};
				for (SvasTemplates temp : list) {

				}
			}
			Map<String,Object> m=new HashMap<String, Object>();
			m.put("fingerNo1", type1);
			m.put("fingerNo2", type2);
			SqlUtil.sqlExecute("update dev_people set reg_fingers=? where user_no=?", count, userNo);
			m.put( "regFingers", count);
			m.put( "warning1", warning1);
			m.put( "warning2", warning2);
			j.parameter("record", m);
		} catch (Exception e) {}
	}
	@Override
	public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
		RestJson j = new RestJson();
		String userNo = request.getParameter("userNo");
		if ("getRegistered".equals(action)) {
			sendFingerRegisteredInfo(userNo,j);
		}
		else if ("removeFinger".equals(action)) {
			int fingerIndex=Integer.parseInt(request.getParameter("fingerIndex"));
			if (!svasService.removeTemplate(userNo, fingerIndex)) 
				throw new IotequException(IotequThrowable.FAILURE,"未能删除指定手指或没有注册该手指信息");
				SqlUtil.sqlExecute("update dev_people_mapping set status=? where user_no=?", "1",userNo);
				DownloadPlan.download(userNo, 1,true);
		}
		else if ("registerFinger".equals(action)) {
			int fingerIndex=Integer.parseInt(request.getParameter("fingerIndex"));
			int fingerType=Integer.parseInt(request.getParameter("fingerType"));
			String template=request.getParameter("template");
			boolean warning=Util.boolValue(request.getParameter("isWarning"));
			boolean update=Util.boolValue(request.getParameter("update"));
			if (update) {
				if (!svasService.updateTemplate(userNo, fingerIndex, fingerType,template))
					throw new IotequException(IotequThrowable.FAILURE,"未能修改成功");
			} else	if (!svasService.addTemplate(userNo, fingerIndex, fingerType,template, warning))
				throw new IotequException(IotequThrowable.FAILURE,"未能注册成功");
			SqlUtil.sqlExecute("update dev_people_mapping set status=? where user_no=?", "1",userNo);
			DownloadPlan.download(userNo, 1,true);
		}
		else if ("verifyFinger".equals(action)) {
			int fingerIndex=Integer.parseInt(request.getParameter("fingerIndex"));
			String template=svasService.getTemplate(userNo, fingerIndex);
			if (!Util.isEmpty(template)) {
				j.put("template", template);
			} else {
				throw new IotequException(IotequThrowable.FAILURE,"未找到注册手指，请检查参数");
			}
		}
		else if ("matchFinger".equals(action)) {
			String template=request.getParameter("template");
			String thresh=request.getParameter("thresh");
			SvasMatched matchInfo = svasService.auth(template, thresh == null ? 0 : Integer.parseInt(thresh));
			if (!Util.isEmpty(matchInfo)) {
				j.put("matchInfo", matchInfo);
			} else {
				throw new IotequException(IotequThrowable.FAILURE,"未找到注册手指，请检查参数");
			}
		}
		else if ("syncRegFingers".equals(action)) {
			if (!"admin".equals(Util.getUser().getName())) throw new IotequException(IotequThrowable.NO_AUTHORITY,"仅超级管理员才可执行该操作");
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<Map<String, Object>> list=null;
					try {
						list = SqlUtil.sqlQuery("select user_no AS U from dev_people");
					} catch (Exception e1) {}
					if (list!=null) {
						int total=list.size(),sync=0;
						for (Map<String, Object> map : list) {
							String userNo=map.get("U").toString();
							try {
								int c=svasService.getFingerCount(userNo);
								SqlUtil.sqlExecute("update dev_people set reg_fingers=? where user_no=?", c,userNo);
								sync++;
							} catch (Exception e) {}
						}
					}
				}				
			}).start();
			j.setMessage("已提交后台执行，稍后请手动刷新页面");
		}
		if ("removeFinger".equals(action) || "registerFinger".equals(action)) {
			sendFingerRegisteredInfo(userNo,j);
		}
		return j;
	}
	@Override
	public  void beforeExport(List<DevPeople>list,HttpServletRequest request) throws IotequException {
		if (list!=null)
			for (DevPeople u:list) u.setDevPassword(null);  // 禁止导出密码
	}
	@Override
	public  void beforeImport(List<DevPeople>list,HttpServletRequest request) throws IotequException {
			for (DevPeople u:list) {
				Integer typ=SqlUtil.sqlQueryInteger("select user_type from dev_people where user_no=?", u.getUserNo());
				if (typ==null) typ=2;
				u.setUserType(typ);  // 禁止改变用户属性或导入新的管理员
				u.setDevPassword(Util.isEmpty(SqlUtil.sqlQueryString("select dev_password from dev_people where user_no=?", u.getUserNo()),u.getDevPassword()));    // 禁止导入数据修改密码
				try {  // 添加 dev_user_no
					String uno=svasService.getUserNo(u.getIdType(), u.getIdNumber(),u.getRealName(),u.getUserNo(),null);
					if (!Util.isEmpty(uno)) u.setUserNo(uno);
					u.setRegFingers(svasService.getFingerCount(u.getUserNo()));
				} catch (Exception e) {}
			}
	}
	@Override
	public  void beforeUpdate(DevPeople devPeople,HttpServletRequest request) throws IotequException {
		Integer [] warningInfos=new Integer[] {2,2};
		if (devPeople!=null && devPeople.getUserNo()!=null) {
			try {
				List<SvasTemplates> fis=svasService.getFingerInfo(devPeople.getUserNo());
				if (fis!=null) {
					for (SvasTemplates  t : fis) {
						if (t.fingerNo==1) warningInfos[0]=(t.warning?1:0);
						else 	if (t.fingerNo==2) warningInfos[1]=(t.warning?1:0);
					}
					devPeople.setRegFingers(fis.size());
				}
			} catch (Exception e) {}
		}
		request.setAttribute("warningInfos", warningInfos);
		String u53=env.getProperty("U53.connectUrl");
		request.setAttribute("U53Url", u53==null?"localhost:9000":u53);
	}
	
	@Override
	public void afterDelete(String ids, HttpServletRequest request, RestJson j) throws IotequException {
		// TODO Auto-generated method stub
		String[] users=ids.split(",");
		for(String u:users) {
			DownloadPlan.download(u, 2,true);
		}
		super.afterDelete(ids, request, j);
		
	}
}
