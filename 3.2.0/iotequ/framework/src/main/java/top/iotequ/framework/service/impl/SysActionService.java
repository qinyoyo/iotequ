package top.iotequ.framework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.framework.event.SystemParameterChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.security.service.SecurityService;
import top.iotequ.util.RestJson;

import javax.servlet.http.HttpServletRequest;

@Service
public class SysActionService extends CgSysActionService {
	@Autowired
	private SecurityService securityService;
	@Override
	public RestJson doAction(String action, String ids, HttpServletRequest request) throws IotequException {
		if ("initial".equals(action)) {
			securityService.initialPermission();
			SystemParameterChangedEvent.sendPeemissionInitialEvent(this,null);
		}
		return new RestJson();
	}
}
