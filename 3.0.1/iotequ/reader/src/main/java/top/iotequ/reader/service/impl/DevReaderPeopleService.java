package top.iotequ.reader.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyna.bean.D10ServerBean.UploadUserNoAck;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.*;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.dao.DevReaderPeopleDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.pojo.DevReaderPeople;
import top.iotequ.reader.util.DevUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.iotequ.util.RestJson;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class DevReaderPeopleService extends CgDevReaderPeopleService {
	private static final Logger log = LoggerFactory.getLogger(DevReaderPeopleService.class);
	@Autowired
	DevPeopleDao devPeopleDao;

	@Override
	public void beforeSave(DevReaderPeople obj0, DevReaderPeople obj, boolean updateSelective,
			HttpServletRequest request) throws IotequException {
		// TODO Auto-generated method stub
		if(SqlUtil.sqlExist("select * from dev_reader_people where user_no=? and reader_no=?", obj.getUserNo(),obj.getReaderNo()))throw new IotequException("add_reader_people_error","该用户在此设备中已存在");
		if(obj!=null&& Util.isEmpty(obj.getId())) {
			obj.setType(0);
			obj.setStatus(1);
		}
		super.beforeSave(obj0, obj, updateSelective, request);
	}
	
	
	
	@Override
	public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
		RestJson j = new RestJson();
		 if(action.equals("reportPeople")) {
			Integer pageId=1;
			byte pageNum=30;
			String readerNo=request.getParameter("readerNo");
			DevReader reader=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", readerNo);
			if(reader!=null) {
				if(pageId==1) {
					SqlUtil.sqlExecute("delete from dev_reader_people where reader_no=? and status=0", reader.getReaderNo());
				}
				try {
					int all=0;
					Boolean isRturn=true;
					while(isRturn) {
						UploadUserNoAck u=DevUtil.UploadUserNos(reader, pageId-1, pageNum);
						if(u!=null&&u.getTotalNum()>0&&u.getUserNos().length>0) {
							if(pageId==1)all=u.getTotalNum();
							j.put("totalNum", u.getTotalNum());
						}else{
							isRturn=false;
						}
						if(u!=null&&u.getCurNum()>0) {
							all=all-pageNum;
							if(all<0)isRturn=false;
							String[] user=u.getUserNos();
							for (int i = 0; i < user.length; i++) {
								String temp=null;
								int g=user.length-1-i;
								if(i<=g){
								temp=user[i];
								user[i]=user[g];
								user[g]=temp;
								} 
							}
							int col=0;
							Integer orderNum=SqlUtil.sqlQueryInteger("select Max(order_num) from dev_reader_people where reader_no=?", reader.getReaderNo());
							if(!Util.isEmpty(orderNum))col=orderNum;
							for(int i=0;i<user.length;i++) {
								DevReaderPeople dr=new DevReaderPeople();
								dr.setUserNo(user[i]);
								col++;
								dr.setStatus(0);
								dr.setOrderNum(col);
								dr.setReaderNo(reader.getReaderNo());
								DevReaderPeopleDao drd= Util.getBean(DevReaderPeopleDao.class);
								if(!SqlUtil.sqlExist("select * from dev_reader_people where user_no=? and reader_no=?", dr.getUserNo(),dr.getReaderNo())) {
									drd.insert(dr);
								}
							}
						}
						pageId++;
					}
				} catch (IotequException e) {
					// TODO: handle exception
					if(!e.getAddtionalMessage().equals("成功")) {
						j.setSuccess(false);
						j.setMessage(e.getAddtionalMessage());
					}
				}
				request.getSession().setAttribute("readerNo", reader.getReaderNo());
			}
			
		}else if(action.equals("download")) {
			String readerNo=request.getParameter("readerNo");
			if(readerNo!=null) {
				DevReader reader=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", readerNo);
				if(reader!=null) {
					List<DevReaderPeople> listAdd=SqlUtil.sqlQuery(DevReaderPeople.class, false, "select * from dev_reader_people where reader_no=? and status=1 and type=0", readerNo);
					if(listAdd!=null && listAdd.size()>0) {
						List<DevPeople> list=new ArrayList<DevPeople>();
						for(DevReaderPeople p:listAdd) {
							DevPeople dp=SqlUtil.sqlQueryFirst(DevPeople.class, "select * from dev_people where user_no=?", p.getUserNo());
							list.add(dp);
						}
						DevUtil.downloadUsers(reader, list);
						for(DevReaderPeople p:listAdd) {
							SqlUtil.sqlExecute("delete from dev_reader_people where id=?", p.getId());
						}
					}
					
					List<DevReaderPeople> listDelete=SqlUtil.sqlQuery(DevReaderPeople.class,false, "select * from dev_reader_people where reader_no=? and status=1 and type=2", readerNo);
					if(listDelete!=null && listDelete.size()>0) {
						List<DevPeople> list=new ArrayList<DevPeople>();
						for(DevReaderPeople p:listDelete) {
							DevPeople dp=SqlUtil.sqlQueryFirst(DevPeople.class, "select * from dev_people where user_no=?", p.getUserNo());
							list.add(dp);
						}
						DevUtil.deleteSpecifyUser(reader, list);
						for(DevReaderPeople p:listDelete) {
							SqlUtil.sqlExecute("delete from dev_reader_people where id=?", p.getId());
						}
					}
				}
			}
		}else if(action.equals("copyDelete")) {
			String[] ids=id.split(",");
			for(String s:ids) {
				SqlUtil.sqlExecute("update dev_reader_people set status=1,type=2 where id=?", s);
			}
		}
		return j;
	}

}
