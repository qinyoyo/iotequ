package top.iotequ.reader.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.util.DateUtil;
import top.iotequ.util.SqlUtil;
import top.iotequ.reader.dao.DevEventDao;
import top.iotequ.reader.pojo.DevEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class DevEventService extends CgDevEventService implements ApplicationListener<DeviceEvent>  {
	private static final Logger log = LoggerFactory.getLogger(DevEventService.class);
	@Autowired
	DevEventDao devEventDao;
	@Override
	public  void beforeList(List<DevEvent> list , HttpServletRequest request) {
		if (list!=null) {
			for (DevEvent e : list) {
				e.setDatDate(DateUtil.date2String(e.getTime(),"yyyy-MM-dd"));
				e.setDatTime(DateUtil.date2String(e.getTime(),"HH:mm"));
			}
		}
	}
	@Override
	public void onApplicationEvent(DeviceEvent event) {
		try {
			DevEvent devEvent = new DevEvent();
			devEvent.setDevType(event.getDeviceType());
			devEvent.setDevNo(event.getDeviceNo());
			devEvent.setUserNo(event.getUserNo());
			devEvent.setTime(event.getTime());

			// 0 passed 1 refused 2 warning
			Object o = event.get("status");
			if (o == null || !(o instanceof Integer)) {
				if (event.getWarning() != null && event.getWarning()) devEvent.setStatus(2);
				else devEvent.setStatus(0);
			} else devEvent.setStatus((Integer) o);

			o = event.get("orgCode");
			if (o != null && o instanceof Integer) devEvent.setOrgCode((Integer) o);

			o=event.get("AuditorUserNum");
			if(o!=null && o instanceof String)devEvent.setAuditorUserNum((String)o);
			
			o=event.get("auditorOrg");
			if(o!=null && o instanceof Integer)devEvent.setAuditorOrg((Integer)o);
			
			o=event.get("auditorAuthType");
			if(o!=null && o instanceof Byte)devEvent.setAuditorAuthType((byte)o);
			
			o=event.get("auditeeAuthType");
			if(o!=null && o instanceof Byte)devEvent.setAuditeeAuthType((byte)o);
			
			o=event.get("authType");
			if(o!=null && o instanceof Byte)devEvent.setAuthType((byte)o);
			devEventDao.insert(devEvent);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
