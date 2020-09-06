package top.iotequ.attendance.event;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.iotequ.attendance.data.dao.AdDataDao;
import top.iotequ.attendance.data.pojo.AdData;
import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.Util;
@Service
public class AttendanceDeviceEventHandler implements ApplicationListener<DeviceEvent> {
	private static final Logger log = LoggerFactory.getLogger(AttendanceDeviceEventHandler.class);
	@Autowired
	AdDataDao adDataDao;
	@Override
	public void onApplicationEvent(DeviceEvent event) {
		try {
			int adMode = -1;
			String mode=event.getDeviceMode().toLowerCase();
			if (Util.isEmpty(mode))	{
				log.error("device properties is null");
				return;
			}
			mode=","+mode+",";
			if (mode.contains(",ad,") || (mode.contains(",on,") && mode.contains(",off,"))) adMode=0;
			else if (mode.contains(",on,")) adMode=1;
			else if (mode.contains(",off,")) adMode=2;
			if (adMode >= 0) {
				String uno=event.getUserNo();
				if (Util.isEmpty(uno)) {
					log.error("user no is null");
					return;
				}
				Date time = event.getTime();
				if (time==null) {
					log.error("time is null");
					return;
				}
				AdData dat = new AdData();
				String eno=SqlUtil.sqlQueryString("select employee_no from ad_employee where id=?", uno);
				if (Util.isEmpty(eno)) {
					log.error("employee no not found");
					return;
				}
				dat.setEmployeeNo(eno);
				dat.setRecTime(time);
				dat.setRecType(adMode);
				dat.setRecSourceType(event.getDeviceType());
				dat.setRecSource(event.getDeviceNo());
				dat.setIsUsed(false);
				adDataDao.insert(dat);
			} else log.debug("event has not properties for attendance");
		} catch (Exception e) {}
	}
}
