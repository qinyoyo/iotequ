package top.iotequ.attendance.org.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.attendance.org.pojo.AdOrg;
import top.iotequ.attendance.util.AdUtil;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.util.RestJson;

import javax.servlet.http.HttpServletRequest;

@Service
public class AdOrgService extends CgAdOrgService {
	@Override
	public  void afterSave(AdOrg o0, AdOrg o, HttpServletRequest request, RestJson j)  throws IotequException {
		AdUtil.resetOrgList();
	}
}
