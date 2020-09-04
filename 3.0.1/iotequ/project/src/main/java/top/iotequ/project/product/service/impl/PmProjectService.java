package top.iotequ.project.product.service.impl;

import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.project.ConstData;
import top.iotequ.project.product.pojo.PmProject;

import javax.servlet.http.HttpServletRequest;

@Service
public class PmProjectService extends CgPmProjectService {
	@Override
	public void initial() {	
		super.initial();
		ConstData.initialDataDict();
	}
	@Override
	public void beforeSave(PmProject obj0,PmProject obj,boolean updateSelective, HttpServletRequest request) throws IotequException {
		if (obj!=null && obj.getType()==6) obj.setFlowState(ConstData.st_published);
	}
}
