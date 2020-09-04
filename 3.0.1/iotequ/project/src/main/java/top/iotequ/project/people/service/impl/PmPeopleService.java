package top.iotequ.project.people.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.util.RestJson;
import top.iotequ.framework.util.Util;
import top.iotequ.project.people.dao.PmPeopleDao;
import top.iotequ.project.people.pojo.PmPeople;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class PmPeopleService extends CgPmPeopleService {
	@Autowired
	private PmPeopleDao pmPeopleDao;
	@Override
	public RestJson doAction(String action, String ids, HttpServletRequest request) throws IotequException {
		RestJson j=new RestJson();
		if ("insertBatch".equals(action)) {
			String s=request.getParameter("groupId");
			if (Util.isEmpty(s)) return j;
			Integer groupId = Integer.parseInt(s);
			if (Util.isEmpty(ids)) {
				j.setSuccess(false);
				j.setErrorCode(IotequThrowable.NULL_OBJECT,"");
			} else {
				int total=0;
				for (String id : ids.split(",")) {
					PmPeople p=new PmPeople();
					p.setGroupId(groupId);
					p.setUserId(id);
					try {
						pmPeopleDao.insert(p);
						total++;
					} catch (Exception e) {
					}
				}
				j.setMessage(String.format("插入 %d 条记录",total));
			}
		}
		return j;
	}
}
