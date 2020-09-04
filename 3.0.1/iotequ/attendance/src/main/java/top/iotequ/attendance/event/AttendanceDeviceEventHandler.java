package top.iotequ.attendance.event;

import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.util.Util;
@Service
public class AttendanceDeviceEventHandler implements ApplicationListener<DeviceEvent> {
	@Override
	public void onApplicationEvent(DeviceEvent event) {
		try {
			if ("D10".equals(event.getDeviceType())) {
				Map<String,Object> map=event.getData();
				if (map!=null) {
					int adMode=-1;
					String mode=(String) map.get("mode");
					if (Util.isEmpty(mode))	adMode=-1;
					else if (mode.contains("ON") && mode.contains("OFF")) adMode=0;
					else if (mode.contains("ON")) adMode=1;
					else if (mode.contains("OFF")) adMode=2;
					if (adMode >= 0) {
						String uno=StringUtil.toString(map.get("userNo"));
						String eno=StringUtil.toString(map.get("employeeNo"));
						if (!Util.isEmpty(uno) || !Util.isEmpty(eno)) {
							try {
								if (Util.isEmpty(eno)) eno=SqlUtil.sqlQueryString("select employee_no from ad_employee where id=?", uno);
								String sql="insert into ad_data (id,employee_no,rec_time,rec_type,rec_source,is_used) value ("
										+ "'" + StringUtil.uuid() + "'" + ",?,?,?,?,?,0)";
								SqlUtil.sqlExecute(sql, eno,map.get("time"),adMode,map.get("deviceNo"));
							} catch (Exception e) {}
						}
					}
				}
			}
		} catch (Exception e) {}
	}
}
