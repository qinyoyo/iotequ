package top.iotequ.reader.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.*;
import top.iotequ.reader.dao.DevReaderDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevPeopleMapping;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.pojo.DevReaderGroup;
import top.iotequ.reader.util.DevUtil;


import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class DevReaderService extends CgDevReaderService {
	@Autowired
	DevReaderDao devReaderDao;

	@Override
	public String listFilter(String path) {
		if (SqlUtil.getFilterOrg()) {
			String of = OrgUtil.getOrgPrivilege(DevReaderGroup.class);
			if (Util.isEmpty(of)) return null;
			else return "reader_group in (select id from dev_reader_group WHERE " + of + ")";
		} else return null;
	}

/*	@Override
	public  void beforeList(List<DevReader> list , HttpServletRequest request) {
		if (list!=null) {
			for (DevReader r : list) DevUtil.isReaderOnline(r);
		}
	}*/
	@Override
	public RestJson doAction(String action, String id, HttpServletRequest request)  {
		RestJson j = new RestJson();
		try {
			// 以下为单行选择
			DevReader reader=devReaderDao.select(id);
			/*if (reader==null) {
				j.setMessage(new IotequException(IotequThrowable.NULL_OBJECT,"reader"));
				return j;
			}*/
			if ("resetDevice".equals(action)) 	DevUtil.RestDevice(reader);
			else if ("queryTime".equals(action)) j.setMessage(DateUtil.date2String(DevUtil.queryTime(reader),null));
			else  if ("setDeviceTime".equals(action)) DevUtil.setDeviceTime(reader);
			else  if ("deleteAllUsers".equals(action)) {
				DevUtil.deleteAllUsers(reader);
				SqlUtil.sqlExecute("delete from dev_download_plan where reader_no=?", reader.getReaderNo());
				SqlUtil.sqlExecute("delete from dev_people_mapping where reader_no=?",reader.getReaderNo());
			}
			else if("selectParam".equals(action)) {
				if(DevUtil.isReaderOnline(reader)) {
					if(reader.getType().indexOf("D30")!=-1) {
						DevUtil.uploadD30Parameter(reader);
					}
					DevUtil.queryDevice(reader);   //  注册
					j.put("reader", reader);
				}
				j.put("status", DevUtil.isReaderOnline(reader));
			}else if("readerOnline".equals(action)) {
				List<DevReader> list=devReaderDao.listBy(null, null);
				if(list.size()>0) {
					for(DevReader d:list) {
						DevUtil.isReaderOnline(d);
					}
				}
			}else if("againDownload".equals(action)) {
				String readerId=request.getParameter("readerId");
				DevReader d=devReaderDao.select(readerId);
				List<DevPeopleMapping> listR=SqlUtil.sqlQuery(DevPeopleMapping.class, "select * from dev_people_mapping where reader_no=?", d.getReaderNo());
				List<DevPeople> listDp=new ArrayList<>();
				if(listR!=null&&listR.size()>0) {
					for(DevPeopleMapping l:listR) {
						DevPeople dp=SqlUtil.sqlQueryFirst(DevPeople.class, "select * from dev_people where user_no=?", l.getUserNo());
						listDp.add(dp);
					}
				}
				j.put("peopleList", listDp);
				j.put("readerNo", d.getReaderNo());
			}
		} catch (Exception e) {
			j.setMessage(e);
		}
		return j;
	}

	@Override
	public void beforeSave(DevReader obj0, DevReader devReader, boolean updateSelective, HttpServletRequest request) throws IotequException {
		if (Util.isEmpty(devReader.getId())) {  // 新加，需要注册
				if(devReader.getType().indexOf("C20")==-1) {
					DevUtil.RegistDevice(devReader);   //  注册
					DevUtil.queryDevice(devReader);   //  获得参数
					if(devReader.getType().indexOf("D30")!=-1) {
						DevUtil.uploadD30Parameter(devReader);//获取d30额外参数
					}
				}
				
		} else {   // 判断是否需要修改参数
			DevReader old=devReaderDao.select(devReader.getId()); 
			if (old.getAlignMethod() != devReader.getAlignMethod() ||  old.getBlacklightTime() != devReader.getBlacklightTime() || old.getVoiceprompt() != devReader.getVoiceprompt() || 
					old.getMenuTime() != devReader.getMenuTime() ||  old.getWengenform() != devReader.getWengenform() || old.getWengenOutput() != devReader.getWengenOutput() || 
					old.getWengenOutArea() != devReader.getWengenOutArea() ||  old.getRegfingerOutTime() != devReader.getRegfingerOutTime() || old.getAuthfingerOutTime() != devReader.getAuthfingerOutTime() ) {
				DevUtil.setDeviceParam(devReader);
				if(devReader.getAlignMethod()==4) {//模式改为服务器验证  清空设备与用户关联关系
					SqlUtil.sqlExecute("delete from dev_download_plan where reader_no=?", old.getReaderNo());
					SqlUtil.sqlExecute("delete from dev_people_mapping where reader_no=?",old.getReaderNo());
					SqlUtil.sqlExecute("delete from dev_reader_people where reader_no=?",old.getReaderNo());
				}
			}
			
			if(devReader.getType().indexOf("D30")!=-1&&(old.getWgOrder()!=devReader.getWgOrder() || old.getRelayTime()!=devReader.getRelayTime() ||
					(old.getFixedValue()!=null&&!old.getFixedValue().equals(devReader.getFixedValue())) || old.getAlarmEnable()!=devReader.getAlarmEnable()||
					old.getOpenEnable()!=devReader.getOpenEnable() || old.getDoorState()!=devReader.getDoorState()||
					old.getRelayEnable()!=devReader.getRelayEnable() || old.getDoorbellEnable()!=devReader.getDoorbellEnable()||
					(old.getWifiPsw()!=null&&!old.getWifiPsw().equals(devReader.getWifiPsw())) || (old.getWifiSsid()!=null&&!old.getWifiSsid().equals(devReader.getWifiSsid()))
					)) {
				DevUtil.setD30Parameter(devReader);
			}
			if (old.getIsTimeSync() != devReader.getIsTimeSync()) {
				DevUtil.setDeviceTime(devReader);
			}
		}
	}

	@Override
	public void afterSave(DevReader obj0, DevReader obj, HttpServletRequest request, RestJson j) throws IotequException {
		super.afterSave(obj0,obj,request,j);
		if (obj!=null) {
			SqlUtil.sqlExecute("delete from dev_new_device where reader_no = ?",obj.getReaderNo());
		}
	}

	@Override
	public  void beforeDelete(String id,HttpServletRequest request) throws IotequException {
		DevReader devReader=devReaderDao.select(id.toString());  // 删除，取消注册
		if (devReader!=null) DevUtil.unRegistDevice(devReader);
	}
	@Override
	public  void beforeUpdate(DevReader reader,HttpServletRequest request) throws IotequException {
		if (reader!=null) DevUtil.queryDevice(reader);
	}
}
