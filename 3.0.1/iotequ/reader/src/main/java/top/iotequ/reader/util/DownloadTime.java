package top.iotequ.reader.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.dyna.bean.D10ClientBean.D10ClinetUserInfo;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.util.SqlUtil;
import top.iotequ.reader.pojo.DevDownloadPlan;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.pojo.DevReader;
import top.iotequ.reader.service.impl.D10ClientService;
@Configuration
@EnableScheduling
public class DownloadTime {
	@Scheduled(cron = "0 0/10 * * * ?") // 每10分钟执行一次
	public static void download() {
		try {
			List<String> list=SqlUtil.sqlQuery(String.class, "select * from dev_download_plan group by reader_no");
			if(list!=null&&list.size()>0) {
				for(String s:list) {
					DevReader dr=SqlUtil.sqlQueryFirst(DevReader.class, "select * from dev_reader where reader_no=?", s);
					boolean isOnline=DevUtil.isReaderOnline(dr);
					if(isOnline) {
						List<DevDownloadPlan> listDP=SqlUtil.sqlQuery(DevDownloadPlan.class, "select * from dev_download_plan where reader_no=?", s);
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
									D10ClinetUserInfo di=DevDownloadUtil.downloadUsers(dr, listAdd,true);
									if(D10ClientService.getD10client().setModeBusy(di.getDeviceId(), di.getDeviceModel())) {
										boolean isTrue=DevDownloadUtil.download(di);
										D10ClientService.getD10client().setModeFree(di.getDeviceId(), di.getDeviceModel());
										for(DevPeople l:listAdd) {
											if(!isTrue) {
												SqlUtil.sqlExecute("update dev_download_plan set download_num=download_num-1 where user_no=? and reader_no=?", l.getUserNo(),dr.getReaderNo());
											}else {
												SqlUtil.sqlExecute("delete from dev_download_plan where user_no=? and reader_no=?", l.getUserNo(),dr.getReaderNo());
											}
										}
									}
								}
							if(listDelete.size()>0) {
								boolean isTrue=DownloadPlan.deleteSpecifyUser(dr, listDelete);
								for(DevPeople l:listDelete) {
									if(!isTrue) {
										SqlUtil.sqlExecute("update dev_download_plan set download_num=download_num-1 where user_no=? and reader_no=?", l.getUserNo(),dr.getReaderNo());
									}else {
										SqlUtil.sqlExecute("delete from dev_download_plan where user_no=? and reader_no=?", l.getUserNo(),dr.getReaderNo());
									}
								}
							}
						}
					}
				}
			}
		} catch (IotequException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
