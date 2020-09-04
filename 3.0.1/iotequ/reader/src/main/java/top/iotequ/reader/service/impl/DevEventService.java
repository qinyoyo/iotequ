package top.iotequ.reader.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.framework.util.DateUtil;
import top.iotequ.reader.pojo.DevEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class DevEventService extends CgDevEventService {
	@Override
	public  void beforeList(List<DevEvent> list , HttpServletRequest request) {
		if (list!=null) {
			for (DevEvent e : list) {
				e.setDatDate(DateUtil.date2String(e.getTime(),"yyyy-MM-dd"));
				e.setDatTime(DateUtil.date2String(e.getTime(),"HH:mm"));
			}
		}
	}}
