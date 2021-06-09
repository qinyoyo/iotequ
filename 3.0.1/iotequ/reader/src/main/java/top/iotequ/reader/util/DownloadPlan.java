package top.iotequ.reader.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyna.bean.D10ClientBean.SpecifyUser;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.SqlUtil;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevPeopleMapping;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.service.impl.D10ClientService;

public class DownloadPlan {
	static final boolean DEBUG=false;
	private static final Logger log = LoggerFactory.getLogger(DownloadPlan.class);
	public static void  download(String userNo,int status,Boolean isUpdate) {
		try {
				List<DevPeopleMapping> list=SqlUtil.sqlQuery(DevPeopleMapping.class,"select * from dev_people_mapping where user_no=?", userNo);
				if(list!=null&&list.size()>0) {//查询这个用户下发到哪些设备上
					for(DevPeopleMapping d:list) {
						DevReader reader=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", d.getReaderNo());
						if(reader==null)continue;
						boolean b=false;
						if(status==1) {
							List<DevPeople> listPeople=SqlUtil.sqlQuery(DevPeople.class, "select * from dev_people where user_no=?", userNo);
							b=DevDownloadUtil.downloadUsers(reader, listPeople, true, true, true,isUpdate);
						}else if(status==2) {
							List<DevPeople> listPeople=new ArrayList<>();
							DevPeople dp=new DevPeople();
							dp.setUserNo(userNo);
							listPeople.add(dp);
							b=deleteSpecifyUser(reader, listPeople);
						}
						if(!b) {
							if(!SqlUtil.sqlExist("select * from dev_download_plan where user_no=? and reader_no=?", userNo,reader.getReaderNo())) {
								SqlUtil.sqlExecute("insert into dev_download_plan (user_no,reader_no,type,time) values(?,?,?,?)", userNo,reader.getReaderNo(),status,new Date());
							}else {
								SqlUtil.sqlExecute("update dev_download_plan set type=? where user_no=? and reader_no=?", status,userNo,reader.getReaderNo());
							}
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	
	// 删除指定用户
		static public boolean deleteSpecifyUser(DevReader reader,List<DevPeople>list) throws IotequException {
			boolean isSucc=false;
			String deviceId = reader.getReaderNo() ; 
			String deviceModel = reader.getType() ;
			SpecifyUser users = new SpecifyUser();
			users.setDeviceId(deviceId);
			users.setDeviceModel(deviceModel);
			String [] userList = new String[list.size()];
			for (int i=0;i<list.size();i++) userList[i]=list.get(i).getUserNo();
			users.setDelusersNo(userList); // 要删除的设备的用户编号数组
			if (DEBUG) {
				System.out.printf("%s remove %d users\n",deviceId,userList.length);
			} else {
				 isSucc = D10ClientService.getD10client().DeleteSpecifyUser(users);
				log.debug("DeleteSpecifyUser({})={}",deviceId,isSucc?"true":"false");
			}
			return isSucc;
		}

}
