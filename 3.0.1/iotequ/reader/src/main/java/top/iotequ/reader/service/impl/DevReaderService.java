package top.iotequ.reader.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.util.*;
import top.iotequ.reader.dao.DevReaderDao;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.pojo.DevReaderGroup;
import top.iotequ.reader.util.DevUtil;


import javax.servlet.http.HttpServletRequest;
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

	@Override
	public  void beforeList(List<DevReader> list , HttpServletRequest request) {
		if (list!=null) {
			for (DevReader r : list) DevUtil.isReaderOnline(r);
		}
	}
	@Override
	public RestJson doAction(String action, String id, HttpServletRequest request)  {
		RestJson j = new RestJson();
		try {
			// 以下为单行选择
			DevReader reader=devReaderDao.select(id);
			if (reader==null) {
				j.setMessage(new IotequException(IotequThrowable.NULL_OBJECT,"reader"));
				return j;
			}
			if ("resetDevice".equals(action)) 	DevUtil.RestDevice(reader);
			else if ("queryTime".equals(action)) j.setMessage(DateUtil.date2String(DevUtil.queryTime(reader),null));
			else  if ("setDeviceTime".equals(action)) DevUtil.setDeviceTime(reader);
			else  if ("deleteAllUsers".equals(action)) DevUtil.deleteAllUsers(reader);
			else if("selectParam".equals(action)) {
				if(DevUtil.isReaderOnline(reader)) {
					if(reader.getType().indexOf("D30")!=-1) {
						DevUtil.uploadD30Parameter(reader);
					}
					DevUtil.queryDevice(reader);   //  注册
					j.put("reader", reader);
				}
				j.put("status", DevUtil.isReaderOnline(reader));
			}
		} catch (Exception e) {
			j.setMessage(e);
		}
		return j;
	}

	@Override
	public void beforeSave(DevReader obj0, DevReader devReader, boolean updateSelective, HttpServletRequest request) throws IotequException {
		if (Util.isEmpty(devReader.getId())) {  // 新加，需要注册
			try {
				DevUtil.RegistDevice(devReader);   //  注册
				DevUtil.queryDevice(devReader);   //  获得参数
				if(devReader.getType().indexOf("D30")!=-1) {
					DevUtil.uploadD30Parameter(devReader);//获取d30额外参数
				}
			} catch (Exception e) {}
		} else {   // 判断是否需要修改参数
			DevReader old=devReaderDao.select(devReader.getId()); 
			if (old.getAlignMethod() != devReader.getAlignMethod() ||  old.getBlacklightTime() != devReader.getBlacklightTime() || old.getVoiceprompt() != devReader.getVoiceprompt() || 
					old.getMenuTime() != devReader.getMenuTime() ||  old.getWengenform() != devReader.getWengenform() || old.getWengenOutput() != devReader.getWengenOutput() || 
					old.getWengenOutArea() != devReader.getWengenOutArea() ||  old.getRegfingerOutTime() != devReader.getRegfingerOutTime() || old.getAuthfingerOutTime() != devReader.getAuthfingerOutTime() ) {
				DevUtil.setDeviceParam(devReader);
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
	public  void beforeDelete(String id,HttpServletRequest request) throws IotequException {
		DevReader devReader=devReaderDao.select(id.toString());  // 删除，取消注册
		if (devReader!=null) DevUtil.unRegistDevice(devReader);
	}
	@Override
	public  void beforeUpdate(DevReader reader,HttpServletRequest request) throws IotequException {
		if (reader!=null) DevUtil.queryDevice(reader);
	}
}
