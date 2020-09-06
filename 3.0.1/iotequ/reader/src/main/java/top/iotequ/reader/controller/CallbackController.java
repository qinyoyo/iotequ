package top.iotequ.reader.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.sf.json.JSONObject;
import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.framework.util.DateUtil;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.reader.dao.DevEventDao;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.dao.DevReaderDao;
import top.iotequ.reader.pojo.DevEvent;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.service.impl.D10ClientService;
import top.iotequ.reader.service.impl.DevReaderService;
import top.iotequ.reader.util.DevUtil;

import com.dyna.bean.D10ClientBean.ServerAuthentication;
import top.iotequ.svasclient.SvasTypes.*;
import top.iotequ.svasclient.SvasService;

@RestController
public class CallbackController {
	private static final Logger log = LoggerFactory.getLogger(CallbackController.class);
	@Autowired
	private DevReaderDao readerDao;
	@Autowired
	private DevPeopleDao peopleDao;
	@Autowired
	private SvasService svasService;
	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	DevReaderService devReaderService;

	public boolean isLicenceAuthorized(DevReader reader) {
		if (reader==null) return false;
		Integer licence=devReaderService.getLicence();
		if (licence==null || licence==Integer.MAX_VALUE) return true;
		else return SqlUtil.getRowCount("dev_reader", "id", reader.getId()) <= licence ;
	}
	@RequestMapping(value = D10ClientService.Callback_Auth)
	public void AuthDevice(@RequestBody JSONObject body) {
		log.debug("Callback_Auth：{}",body);
		String dno=body.getString("devnum");
		DevReader reader=readerDao.selectByReaderNo(dno);
		if (reader==null) {
			log.debug("设备未注册：{}",dno);
			D10ClientService.getD10client().unRegistDevice(dno, "D10");
		}
		else if (!isLicenceAuthorized(reader)) {
			log.debug("设备未获得Licence许可：{}",dno);		
			D10ClientService.getD10client().unRegistDevice(dno, "D10");
		} else
			try {
				SqlUtil.sqlExecute("update dev_reader set is_online=1 where reader_no=?", body.getString("devnum"));
			} catch (Exception e) {}
	}
	@RequestMapping(value = D10ClientService.Callback_Disconnect)
	public void disconnect(@RequestBody JSONObject body) {
		log.debug("Callback_Disconnect：{}",body);
		try {
			SqlUtil.sqlExecute("update dev_reader set is_online=0 where reader_no=?", body.getString("devnum"));
		} catch (Exception e) {}
	}
	@RequestMapping(value = D10ClientService.Callback_Record)
	public void record(@RequestBody JSONObject body) {
		log.debug("Callback_Record：{}",body);
		String dno=body.getString("devnum");
		DevReader reader=readerDao.selectByReaderNo(dno);
		if (reader==null) log.info("设备不存在：{}",dno);
		else if (!isLicenceAuthorized(reader)) log.info("设备未获得Licence许可：{}",dno);
		else {
			Date dt= DateUtil.string2Date(body.getString("date"), "yyyyMMddHHmmss");
			if (dt!=null) {
				DeviceEvent event = new DeviceEvent(this);
				String uno=null;
				try { uno = body.getString("userNum");	} catch (Exception e) {};
				Integer status=null;
				try { status = body.getInt("status"); } catch (Exception e) {}
				Integer deviceOrgCode=0;
				try {
					deviceOrgCode = SqlUtil.sqlQueryInteger(false, "select org_code from dev_reader_group where id=?", reader.getReaderGroup());
				} catch (Exception e) {	}
				Boolean warning = null;
				try { warning = body.getBoolean("warning"); } catch (Exception e) {}

				event.setDeviceType(reader.getType());
				event.setDeviceNo(dno);
				event.setDeviceMode(reader.getDevMode());
				event.setTime(dt);
				event.setUserNo(uno);
				event.setWarning(warning);
				event.put("status",status);
				event.put("orgCode", deviceOrgCode);

				applicationContext.publishEvent(event);
			} else log.error("error date format of {}",body.getString("date"));
		}
	}
	
    public void sendServerAuthResult(ServerAuthentication oServerAuthenticationRsp) {
		boolean isSucc = D10ClientService.getD10client().SendServerAuthRsp(oServerAuthenticationRsp);
		log.debug("SendServerAuthRsp：{}",oServerAuthenticationRsp);
		if (!isSucc) {
			log.error("error msg=" + D10ClientService.getD10client().getLastMessage());
		} 
	}

	private static final byte rc_fail = 0;
	private static final byte rc_ok = 1;
	private static final byte rc_multi = 2;
	private static final byte rc_warning = 3;
	
	
	@RequestMapping(value = D10ClientService.Callback_ServerAuth)
	public void ServerAuthentication(@RequestBody JSONObject body) {
		String devnum = body.getString("devnum");
		String template = body.getString("template");
		int sid = Integer.parseInt(body.getString("sid"));
		log.debug("Callback_ServerAuth：{}",body);
		SvasMatched map=null;
		ServerAuthentication oServerAuthenticationRsp = new ServerAuthentication();
		oServerAuthenticationRsp.setDeviceId(devnum); // 设备号
		oServerAuthenticationRsp.setDeviceModel("D10");// 设备型号
		oServerAuthenticationRsp.setSid(sid);// 设置当前会话号
		oServerAuthenticationRsp.setAuthRetCode(rc_fail);// 认证结果 2：包含多人，请求按第二指以交叉验证 1：认证成功 0：认证失 
		DevReader reader=readerDao.selectByReaderNo(devnum);
		if (reader==null) {
			log.error("设备不存在：{}",devnum);
		}
		else if (!isLicenceAuthorized(reader)) {
			log.error("设备未获得Licence许可：{}",devnum);
		}
		else { 
			try {
				map = svasService.auth(template,0);
				log.debug("Auth match result:",map);
				if (map!=null && map.count>0 ) {
					Integer match=(Integer) map.count;
					if (match>1) {
						int same=0;
						for (int i=1;i<match;i++) {
							if (map.list.get(0).userNo.equals(map.list.get(i).userNo)) {
								log.info("同一个人手指多重认证，取第一个结果");
								same++;
							}
						}
						match -= same;
					}
					if (match==1) {
						SvasMatchInfo first= map.list.get(0);
						oServerAuthenticationRsp.setAuthRetCode(first.warning ? rc_warning : rc_ok);
						String userNo= StringUtil.toString(first.userNo);
						oServerAuthenticationRsp.setUserNo(StringUtil.toString(first.userNo));// 用户编号
						oServerAuthenticationRsp.setUserName(StringUtil.toString(first.name));
						DevPeople peo=SqlUtil.sqlQueryFirst(DevPeople.class,false,"select * from dev_people where user_no=?", userNo);
						if (peo!=null) {
							if (first.warning) log.debug("Auth warning : {}",userNo);
							/*
							if (first.warning) {  // 记录胁迫事件
								try {
									DevEvent event=new DevEvent();
									event.setDevNo(devnum);
									event.setUserNo(userNo);
									event.setTime(new Date());
									event.setStatus(2);   // 胁迫
									event.setOrgCode(peo.getOrgCode());
									eventDao.insert(event);
									log.debug("Insert warning event : {}",userNo);
								} catch (Exception e) {
									log.error(e.getMessage());
								}
							}
							*/
							oServerAuthenticationRsp.setUserName(peo.getRealName());	
							oServerAuthenticationRsp.setUserCardNo(peo.getCardNo());// 用户卡号
							oServerAuthenticationRsp.setUserType(peo.getUserType()==1 ? (byte)1 : (byte)2); // 1 管理员 2 普通用户
							byte perm = DevUtil.getPermission(peo,devnum,null);
							oServerAuthenticationRsp.setUserPermission(perm); // 用户权限 0：无权限 1：需要审核 2：审核权限 4：直接开门
						} else 
							oServerAuthenticationRsp.setUserPermission((byte) 0x01); // 非注册用户，无开门权限
					} else oServerAuthenticationRsp.setAuthRetCode( match>1 ? rc_multi : rc_fail);
				} 
			} catch (Exception e) {
				log.error(e.getMessage());
				oServerAuthenticationRsp.setAuthRetCode((byte) 0);// 认证结果 2：包含多人，请求按第二指以交叉验证 1：认证成功 0：认证失
			}
		}
		sendServerAuthResult(oServerAuthenticationRsp);// 发送验证结果
	}

}
