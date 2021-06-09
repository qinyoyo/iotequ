package top.iotequ.reader.util;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyna.bean.D10ClientBean.D10ClinetUserInfo;
import com.dyna.bean.D10ClientBean.UserInfo;
import com.dyna.bean.D10ClientBean.UserInfo.FingerInfo;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.DateUtil;
import top.iotequ.util.EntityUtil;
import top.iotequ.util.OrgUtil;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.Util;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.service.impl.D10ClientService;
import top.iotequ.svasclient.SvasService;
import top.iotequ.svasclient.SvasTypes.SvasTemplates;



public class DevDownloadUtil {
	static final boolean DEBUG=false;
	private static final Logger log = LoggerFactory.getLogger(DevDownloadUtil.class);
	static public boolean downloadUsers(DevReader reader,List<DevPeople>list,boolean covered,Boolean busyOccupy,Boolean freeOccupy,Boolean isUpdate) throws IotequException {
		boolean isSucc=false;
		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		D10ClinetUserInfo d10ClinetUserInfors = new D10ClinetUserInfo();
		d10ClinetUserInfors.setDeviceId(deviceId);
		d10ClinetUserInfors.setDeviceModel(deviceModel);
		d10ClinetUserInfors.setCovered(covered);// 是否覆盖已经存在的用户 true--覆盖 false--不覆盖

		List<UserInfo> userInfos = new ArrayList<>();

		for (DevPeople em : list) {
			UserInfo user = new UserInfo();
			user.setUsernum(em.getUserNo());// 用户编号
			user.setUsername(em.getRealName());// 用户姓名
			user.setDepartmenno(em.getOrgCode());// 部门编码
			user.setPhonenum(em.getMobilePhone());// 手机号码
			user.setInductiontime(em.getValidDate()==null?"20180101": DateUtil.date2String(em.getValidDate(),"yyyyMMdd"));// 入职时间
			user.setLeavedate(em.getExpiredDate()==null?"21001231":DateUtil.date2String(em.getExpiredDate(),"yyyyMMdd"));// 离职时间
			user.setUserpsw(em.getDevPassword());// 设备端密码
			int emtype=em.getUserType();
			user.setUsertype((byte) emtype);// 1 -管理员 2-普通用户
			user.setDatasources((byte) 2);// 从服务器同步获得 这里应该固定为2
			user.setUserregtime(DateUtil.date2String(em.getRegTime(),"yyyyMMddHHmmss"));// 用户登记时间
			user.uerpermissions = (byte)4;//
			user.dayStartTime="000000";
			user.dayEndTime="235959";
			if ("D10-DG".equals(reader.getType())) {
				user.setEmail(em.getDutyRank());
				user.setUsermark(em.getOrgCode()==null ? "" : OrgUtil.getOrgFullName(em.getOrgCode()));
				String eno = SqlUtil.sqlQueryString(false,"select employee_no from ad_employee where id=?",em.getUserNo());
				user.setUsercardno(eno);
			} else {
				user.setEmail(em.getEmail());// email地址
				user.setUsermark(em.getNote());// 用户备注
				user.setUsercardno(em.getCardNo());// 用户卡号
			}
			try {
				Object o=EntityUtil.runMethod(Util.getBean(SvasService.class),"getFingerInfo", em.getUserNo());
				if (o!=null) {
					@SuppressWarnings("unchecked")
					List<SvasTemplates> infos=(List<SvasTemplates>)o;
					int fingernum =0 ;
					if (infos!=null && infos.size()>0) {
						 fingernum = infos.size() ;// 手指数量最大为2
						if (fingernum>2) fingernum=2;
						user.setFingernum((byte) fingernum);// 用户手指数量
						FingerInfo[] fingerInfos = new FingerInfo[fingernum];
						for (int i = 0; i < fingernum; i++) { // 手指信息
							FingerInfo finger = new FingerInfo();
							SvasTemplates map=infos.get(i);
							finger.setFignerIndex((byte)(map.fingerNo));
							finger.setWarningfinger(map.warning ? (byte) 1 : (byte)0);// 0：非胁迫手指 1：胁迫手指
							finger.setTemplates(map.templates);
							fingerInfos[i] = finger;
						}
						user.setFingers(fingerInfos);	
					}
				}
			} catch(Exception e) {}
			Map<String,String> params = new HashMap<>();
			user.uerpermissions=DevUtil.getPermission(em,deviceId,params);
			if (params.get("start")!=null) user.dayStartTime=params.get("start");
			if (params.get("end")!=null)   user.dayEndTime=params.get("end");
			if(!isUpdate) {
				if (user.uerpermissions >0) userInfos.add(user);
			}else {
				userInfos.add(user);
			}
		}
		if (userInfos.size() > 0) {
			UserInfo[] userArray = new UserInfo[userInfos.size()];
			userInfos.toArray(userArray);
			d10ClinetUserInfors.setUserInfos(userArray);// 数组输入
			if (DEBUG) {
				System.out.printf("Download %d users to %s\n", userInfos.size(), deviceId);
			} else {
				if(busyOccupy)D10ClientService.getD10client().setModeBusy(deviceId, deviceModel);
				isSucc = D10ClientService.getD10client().DownloadUsersProgress(d10ClinetUserInfors);
				log.debug("DownloadUsers({})={}", deviceId, isSucc ? "true" : "false");
				if(freeOccupy) {
					D10ClientService.getD10client().setModeFree(deviceId, deviceModel);
				}
				
			}
		} else log.debug("No user need download to {}", deviceId);
		return  isSucc;
	}

	
	
	static public D10ClinetUserInfo downloadUsers(DevReader reader,List<DevPeople>list,Boolean isUpdate) throws IotequException {

		String deviceId = reader.getReaderNo() ; 
		String deviceModel = reader.getType() ;
		D10ClinetUserInfo d10ClinetUserInfors = new D10ClinetUserInfo();
		d10ClinetUserInfors.setDeviceId(deviceId);
		d10ClinetUserInfors.setDeviceModel(deviceModel);
		d10ClinetUserInfors.setCovered(true);// 是否覆盖已经存在的用户 true--覆盖 false--不覆盖

		List<UserInfo> userInfos = new ArrayList<>();

		for (DevPeople em : list) {
			UserInfo user = new UserInfo();
			user.setUsernum(em.getUserNo());// 用户编号
			user.setUsername(em.getRealName());// 用户姓名
			user.setDepartmenno(em.getOrgCode());// 部门编码
			user.setPhonenum(em.getMobilePhone());// 手机号码
			user.setInductiontime(em.getValidDate()==null?"20180101": DateUtil.date2String(em.getValidDate(),"yyyyMMdd"));// 入职时间
			user.setLeavedate(em.getExpiredDate()==null?"21001231":DateUtil.date2String(em.getExpiredDate(),"yyyyMMdd"));// 离职时间
			user.setUserpsw(em.getDevPassword());// 设备端密码
			int emtype=em.getUserType();
			user.setUsertype((byte) emtype);// 1 -管理员 2-普通用户
			user.setDatasources((byte) 2);// 从服务器同步获得 这里应该固定为2
			user.setUserregtime(DateUtil.date2String(em.getRegTime(),"yyyyMMddHHmmss"));// 用户登记时间
			user.uerpermissions = (byte)4;//
			user.dayStartTime="000000";
			user.dayEndTime="235959";
			if ("D10-DG".equals(reader.getType())) {
				user.setEmail(em.getDutyRank());
				user.setUsermark(em.getOrgCode()==null ? "" : OrgUtil.getOrgFullName(em.getOrgCode()));
				String eno = SqlUtil.sqlQueryString(false,"select employee_no from ad_employee where id=?",em.getUserNo());
				user.setUsercardno(eno);
			} else {
				user.setEmail(em.getEmail());// email地址
				user.setUsermark(em.getNote());// 用户备注
				user.setUsercardno(em.getCardNo());// 用户卡号
			}
			try {
				Object o=EntityUtil.runMethod(Util.getBean(SvasService.class),"getFingerInfo", em.getUserNo());
				if (o!=null) {
					@SuppressWarnings("unchecked")
					List<SvasTemplates> infos=(List<SvasTemplates>)o;
					int fingernum =0 ;
					if (infos!=null && infos.size()>0) {
						 fingernum = infos.size() ;// 手指数量最大为2
						if (fingernum>2) fingernum=2;
						user.setFingernum((byte) fingernum);// 用户手指数量
						FingerInfo[] fingerInfos = new FingerInfo[fingernum];
						for (int i = 0; i < fingernum; i++) { // 手指信息
							FingerInfo finger = new FingerInfo();
							SvasTemplates map=infos.get(i);
							finger.setFignerIndex((byte)(map.fingerNo));
							finger.setWarningfinger(map.warning ? (byte) 1 : (byte)0);// 0：非胁迫手指 1：胁迫手指
							finger.setTemplates(map.templates);
							fingerInfos[i] = finger;
						}
						user.setFingers(fingerInfos);	
					}
				}
			} catch(Exception e) {}
			Map<String,String> params = new HashMap<>();
			user.uerpermissions=DevUtil.getPermission(em,deviceId,params);
			if (params.get("start")!=null) user.dayStartTime=params.get("start");
			if (params.get("end")!=null)   user.dayEndTime=params.get("end");
			if(!isUpdate) {
				if (user.uerpermissions >0) userInfos.add(user);
			}else {
				userInfos.add(user);
			}
		}
		if (userInfos.size() > 0) {
			UserInfo[] userArray = new UserInfo[userInfos.size()];
			userInfos.toArray(userArray);
			d10ClinetUserInfors.setUserInfos(userArray);// 数组输入
		} else log.debug("No user need download to {}", deviceId);
		return d10ClinetUserInfors;
	}
	
	public static boolean download(D10ClinetUserInfo d) {
		boolean isSucc = D10ClientService.getD10client().DownloadUsersProgress(d);
		return isSucc;
	}
	
}
