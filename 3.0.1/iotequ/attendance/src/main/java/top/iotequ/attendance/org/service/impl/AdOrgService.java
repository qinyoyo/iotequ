package top.iotequ.attendance.org.service.impl;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import top.iotequ.attendance.org.pojo.AdOrg;
import top.iotequ.attendance.util.AdUtil;
import top.iotequ.framework.event.SystemParameterChangedEvent;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.RestJson;
import top.iotequ.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

@Service
public class AdOrgService extends CgAdOrgService implements ApplicationListener<SystemParameterChangedEvent> {
	@Override
	public  void afterSave(AdOrg o0, AdOrg o, HttpServletRequest request, RestJson j)  throws IotequException {
		AdUtil.resetOrgList();
	}

	@Override
	public void onApplicationEvent(SystemParameterChangedEvent systemParameterChangedEvent) {
		if (SystemParameterChangedEvent.SP_ORG_RELOAD.equals(StringUtil.toString(systemParameterChangedEvent.getValue()))) {
			AdUtil.resetOrgList();
		}
	}
}
