package top.iotequ.reader.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.SqlUtil;
import top.iotequ.reader.pojo.DevDownloadPlan;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevReader;

public class UtilThread extends Thread {
	//private static final Logger log = LoggerFactory.getLogger(UtilThread.class);
	
    private DevReader reader;
 
    public UtilThread(DevReader reader) {
    	this.reader=reader;
    }
    
    @Override
	public void run() {
		try {
			Thread.sleep(10000);
			SqlUtil.sqlExist("delete from dev_download_plan where download_num=0");
			List<DevDownloadPlan> listDP=SqlUtil.sqlQuery(DevDownloadPlan.class, "select * from dev_download_plan where reader_no=?", reader.getReaderNo());
			if(listDP!=null&&listDP.size()>0) {
				List<DevPeople> listAdd=new ArrayList<>();
				List<DevPeople> listDelete=new ArrayList<>();
				for(DevDownloadPlan d:listDP) {
					if(d.getType()==1) {
						DevPeople dp=SqlUtil.sqlQueryFirst(DevPeople.class, "select * from dev_people where user_no=?", d.getUserNo());
						listAdd.add(dp);
					}else {
						DevPeople dp=new DevPeople();
						dp.setUserNo(d.getUserNo());
						listDelete.add(dp);
					}
				}
				if(listAdd.size()>0) {
					boolean b=false;
					try {
						DevUtil.downloadUsers(reader, listAdd);
						b=true;
					} catch (IotequException e) {
					}
					// TODO: handle exception
					for(DevPeople l:listAdd) {
						if(!b) {
							SqlUtil.sqlExecute("update dev_download_plan set download_num=download_num-1 where user_no=? and reader_no=?", l.getUserNo(),reader.getReaderNo());
						}else {
							SqlUtil.sqlExecute("delete from dev_download_plan where user_no=? and reader_no=?", l.getUserNo(),reader.getReaderNo());
						}
					}
				}
				if(listDelete.size()>0) {
					DevUtil.deleteSpecifyUser(reader, listDelete);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
