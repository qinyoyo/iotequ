package top.iotequ.attendance.adjust.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import top.iotequ.attendance.adjust.dao.AdAdjustDao;
import top.iotequ.attendance.adjust.pojo.AdAdjust;
import org.springframework.stereotype.Service;
import top.iotequ.attendance.approvelist.dao.AdApproveListDao;
import top.iotequ.attendance.org.pojo.AdOrg;
import top.iotequ.attendance.util.AdUtil;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.*;
import top.iotequ.util.EntityUtil;
import top.iotequ.util.OrgUtil;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdAdjustService extends CgAdAdjustService {
	@Autowired
	private AdAdjustDao adAdjustDao;
	@Autowired
	private AdApproveListDao adApproveListDao;
	@Override
	public String listFilter(String path) {
		String id= Util.getUser().getId();
		if (Util.isEmpty(id)) return "1=2";
		else {
			String orgSet=null;
			try {
				List<AdOrg> orgList= SqlUtil.sqlQuery(AdOrg.class,false,"select * from ad_org where hr = ? or manager = ?",id,id);
				if (orgList!=null && orgList.size()>0) {
					List<Integer> orgs = new ArrayList<>();
					for (AdOrg org : orgList) {
						int orgCode = org.getOrgCode();
						Integer[] oo= OrgUtil.getOrgPrivilegeCodeArray(orgCode);
						for (Integer i : oo ) {
							if (!orgs.contains(i)) orgs.add(i);
						}
					}
					if (orgs.size()>0) {
						orgSet = orgs.get(0).toString();
						for (int i=1;i<orgs.size();i++) orgSet = orgSet+"," + orgs.get(i).toString();
					}
				}
			} catch (Exception e) {				
			}
			if (orgSet==null)
				return String.format("ad_adjust.employee='%s' or ad_adjust.id in (SELECT adjust_id FROM ad_approve_list where approver='%s')",id,id);
			else 
				return String.format("ad_adjust.employee='%s' or ad_adjust.org_code in (%s) or ad_adjust.id in (SELECT adjust_id FROM ad_approve_list where approver='%s')",id,orgSet,id);
		}
	}
	@Override
	public  void beforeUpdate(AdAdjust obj, HttpServletRequest request) throws IotequException {
		if ((obj.getState()!= AdUtil.st_waiting && obj.getState()!=AdUtil.st_doing) || (!EntityUtil.entityEquals(obj.getApprover(), Util.getUser().getId()))) {
			request.setAttribute("_load","detail");
		}
	}
	// 审批前
	@Override
	public  void beforeSave(AdAdjust o0,AdAdjust o,boolean updateSelective,HttpServletRequest request) throws IotequException {
		if ((AdUtil.ad_add_data==o.getAdjustType() && o.getStartTime()==null && o.getEndTime() == null)
		    || (AdUtil.ad_add_data!=o.getAdjustType() && (o.getStartTime()==null || o.getEndTime() == null)) )
			throw new IotequException(IotequThrowable.PARAMETER_ERROR,"时间参数不正确");
		if (Objects.isNull(o0)) {  // 新建
			String id = Util.getUser().getId();
			o.setEmployee(id);
			o.setOrgCode(Util.getUser().getOrgCode());
			o.setRegisterTime(new Date());
			o.setState(AdUtil.st_waiting);
			String manager = AdUtil.getManager(id);
			if (Util.isEmpty(manager)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"找不到审批人,请检查工号,部门等职员配置");
			o.setApprover(manager);
			o.setApproveOrg(AdUtil.getManageOrg(id));
			o.setHr(AdUtil.getHr(id));
		}
	};
	public  void beforeDelete(String id,HttpServletRequest request) throws IotequException {
		AdAdjust data=adAdjustDao.select(id);
		if (data!=null && data.getState()!=AdUtil.st_waiting) new Exception("不允许删除");
	}
}
