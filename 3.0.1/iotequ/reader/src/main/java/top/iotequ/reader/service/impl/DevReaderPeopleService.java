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
import top.iotequ.reader.util.DownloadPlan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DevReaderPeopleService extends CgDevReaderPeopleService {
	private static final Logger log = LoggerFactory.getLogger(DevReaderPeopleService.class);
	@Autowired
	DevPeopleDao devPeopleDao;

	@Override
	public void beforeSave(DevReaderPeople obj0, DevReaderPeople obj, boolean updateSelective,
			HttpServletRequest request) throws IotequException {
		// TODO Auto-generated method stub
		Map<String,String> params = new HashMap<>();
		DevPeople d=SqlUtil.sqlQueryFirst(DevPeople.class, "select * from dev_people where user_no=?", obj.getUserNo());
		if(DevUtil.getPermission(d, obj.getReaderNo(), params)<=0)throw new IotequException("add_reader_people_error_permission","该用户与此设备权限不符合，添加失败");
		if(SqlUtil.sqlExist("select * from dev_reader_people where user_no=? and reader_no=?", obj.getUserNo(),obj.getReaderNo()))throw new IotequException("add_reader_people_error","该用户在此设备中已存在");
		if(obj!=null&&Util.isEmpty(obj.getId())) {
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
					List<String> listS=new ArrayList<>();
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
								if(!SqlUtil.sqlExist("select * from dev_people where user_no=?", user[i])) {
									listS.add(user[i]);
									continue;
								}
								dr.setUserNo(user[i]);
								col++;
								dr.setStatus(0);
								dr.setOrderNum(col);
								dr.setReaderNo(reader.getReaderNo());
								DevReaderPeopleDao drd=Util.getBean(DevReaderPeopleDao.class);
								if(!SqlUtil.sqlExist("select * from dev_reader_people where user_no=? and reader_no=?", dr.getUserNo(),dr.getReaderNo())) {
									drd.insert(dr);
								}
							}
						}
						pageId++;
						System.out.println(pageId);
					}
					if(listS!=null&&listS.size()>0) {
						for(String s:listS) {
							DownloadPlan.download(s, 2,false);
						}
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
				String msg=null;
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
						if(list.size()>0) {
							for(DevReaderPeople p:listAdd) {
								SqlUtil.sqlExecute("update dev_reader_people set status=0,type=null where  id=?", p.getId());
								if(!SqlUtil.sqlExist("select * from dev_people_mapping where user_no=? and reader_no=?", p.getUserNo(),p.getReaderNo())) {
									SqlUtil.sqlExecute("insert into dev_people_mapping(reader_no,user_no,status) values(?,?,?)", p.getReaderNo(),p.getUserNo(),"0");
								}
							}
							msg=String.format("下发 %d 人;",list.size());
						}
						
					}
					
					List<DevReaderPeople> listDelete=SqlUtil.sqlQuery(DevReaderPeople.class,false, "select * from dev_reader_people where reader_no=? and status=1 and type=2", readerNo);
					if(listDelete!=null && listDelete.size()>0) {
						List<DevPeople> list=new ArrayList<DevPeople>();
						for(DevReaderPeople p:listDelete) {
							DevPeople dp=SqlUtil.sqlQueryFirst(DevPeople.class, "select * from dev_people where user_no=?", p.getUserNo());
							list.add(dp);
						}
						boolean b=DownloadPlan.deleteSpecifyUser(reader, list);
						if(b) {
							msg=msg==null?String.format("删除 %d 人;",list.size()):msg+String.format("删除 %d 人;",list.size());
							for(DevReaderPeople p:listDelete) {
								SqlUtil.sqlExecute("delete from dev_reader_people where id=?", p.getId());
								SqlUtil.sqlExecute("delete from dev_people_mapping where user_no=? and reader_no=?", p.getUserNo(),p.getReaderNo());
							}
						}
					}
					j.setMessage(msg);
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
