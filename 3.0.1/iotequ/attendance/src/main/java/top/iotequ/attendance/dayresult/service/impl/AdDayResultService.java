package top.iotequ.attendance.dayresult.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.attendance.dayresult.dao.AdDayResultDao;
import top.iotequ.attendance.dayresult.pojo.AdDayResult;
import top.iotequ.attendance.employee.dao.AdEmployeeDao;
import top.iotequ.attendance.util.AdExcelReport;
import top.iotequ.attendance.util.AdUtil;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.service.utils.PageUtil;
import top.iotequ.framework.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdDayResultService extends CgAdDayResultService {
	@Autowired
	private AdDayResultDao adDayResultDao;
	@Autowired
	private AdEmployeeDao employeeDao;
	@Override
	public String orgCodePrivilege(HttpServletRequest request) {
		 return null; // 取消默认部门判断而采用 listFilter设置
	 }
	@Override
	public String listFilter(String path) {
		String c0= "ad_employee.is_attendance = 1";
		String c1= AdUtil.dataFilter(true,AdDayResult.class);
		if (Util.isEmpty(c1)) return c0;
		else return "("+c0+") AND ("+c1+")";
	}
	@Override
	public RestJson doAction(String action, String id, HttpServletRequest request) throws IotequException {
		RestJson j=new RestJson();
		if ("adjust".equals(action)) {
			if (id!=null) {
				AdDayResult o=adDayResultDao.select(Integer.parseInt(id));
				if (o==null) {
					j.setMessage("请在查询条件里设置一个日期，选择一个职员");
					return j;
				}
				Date dt=o.getAdDate();
				Integer org=o.getOrgCode();
				String eid=o.getEmployee();
				if (dt==null) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"请在查询条件里设置一个日期");
				if (Util.isEmpty(org) && Util.isEmpty(eid)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"请在查询条件里设置选择一个部门或职员");
				String column=request.getParameter("column");
				if ("0".equals(column)) AdUtil.calcAllEmployeeAttendance(org, dt, false, null,false);
				else	if (eid!=null)	AdUtil.calcEmployeeAttendance(o.getEmployee(), o.getAdDate());
				else AdUtil.calcAllEmployeeAttendance(org, dt, false, null,false);
			} else {
				String dt0=request.getParameter("adDate_start") , dt1=request.getParameter("adDate_end") ;
				if (Util.isEmpty(dt0) && Util.isEmpty(dt1)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"请在查询条件里设置一个日期");
				if (Util.isEmpty(dt0)) dt0=dt1 ;
				else if (Util.isEmpty(dt1)) dt1=dt0;
				String oo = request.getParameter("orgCode") , ee=request.getParameter("employee");
				if (Util.isEmpty(oo) && Util.isEmpty(ee)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"请在查询条件里选择一个部门或职员");
				Date date0= DateUtil.string2Date(dt0, null) , date1=DateUtil.string2Date(dt1, null);
				if (!Util.isEmpty(ee)) {
					String [] eids = ee.split(",");
					int step = 100 / eids.length;
					int start = 0;
					for (String eid : eids) {
						AdUtil.calcEmployeeAttendance(eid, date0,date1,start,start+step);
						start += step;
					}
					Util.setProgress(100);
				} else {
					String [] orgs = oo.split(",");
					int step = 100 / orgs.length;
					int start = 0;
					for (String code : orgs) {
						AdUtil.calcAllEmployeeAttendance(Integer.parseInt(code), date0,date1, false,start,start+step);
						start += step;
					}
					Util.setProgress(100);
				}
			}
		}
		return j;
	}	
	@Override
	public  boolean customExport(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) throws IotequException {
		String code = request.getParameter("orgCode"),
				date = request.getParameter("adDate_start");
		if (!Util.isEmpty(code) && !Util.isEmpty(date)) {
			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(5, 7));
			new AdExcelReport(code, year, month, true).doReport(response);
			return true;
		} else throw new IotequException(IotequThrowable.PARAMETER_ERROR, "请在查询条件里设置部门和月份");
	}
	@Override
	public RestJson sqlQuery(Map<String, Object> params) throws Exception {
		String action = StringUtil.toString(params.get("action"));
		String month = StringUtil.toString(params.get("month"));
		Date dt0 = DateUtil.string2Date(month+"-01 00:00:00");
		Date dt1 =DateUtil.dateAdd(dt0,1,DateUtil.MONTH);
		RestJson j=new RestJson();
		if ("statByOrg".equals(action)) {
			String sql = "SELECT distinct org_code FROM ad_day_result  where ad_date >= ? and ad_date < ?  order by org_code";
			List<Map<String,Object>> orgList = SqlUtil.sqlQuery(true,sql,dt0,dt1);
			if (!Util.isEmpty(orgList)) {
				for (Map<String,Object> m : orgList) {
					int org = Integer.parseInt(m.get("org_code").toString());
					m.put("orgName",OrgUtil.getOrgFullName(org));
				}
			}
			sql = "SELECT distinct state FROM ad_day_result  where ad_date >= ? and ad_date < ?  order by state";
			List<Map<String,Object>> stateList = SqlUtil.sqlQuery(false,sql,dt0,dt1);
			if (!Util.isEmpty(stateList)) {
				for (Map<String,Object> m : stateList) {
					int state = Integer.parseInt(m.get("state").toString());
					m.put("stateName",AdUtil.stateFullName(state));
				}
			}
			sql="SELECT sum(times) as times, org_code, state  FROM ad_day_result where ad_date >= ? and ad_date < ?  group by org_code, state order by org_code, state";
			List<Map<String,Object>> list = SqlUtil.sqlQuery(true,sql,dt0,dt1);
			Map<String,Object> data = new HashMap<>();
			data.put("orgList",orgList);
			data.put("stateList",stateList);
			data.put("data",list);
			j.data(data);
		} else if ("statRecTime".equals(action)) {
			String sql = "select a.employee,e.real_name,e.org_code,o.name as org_name,a.day,a.start_time,a.end_time from (select day,employee,min(start_time) as start_time, max(end_time) as end_time from \n" +
					"(SELECT e.id as employee, hour(d.rec_time)*100+minute(d.rec_time) as start_time, hour(d.rec_time)*100+minute(d.rec_time) as end_time, dayofmonth(rec_time) as day FROM ad_data d \n" +
					"join ad_employee e on d.employee_no = e.employee_no \n" +
					"where d.rec_time >= ? and d.rec_time< ? \n" +
					"union \n" +
					"SELECT employee, hour(start_time)*100+minute(start_time) as start_time, null as end_time, dayofmonth(start_time) as day FROM ad_adjust \n" +
					"where adjust_type=10 and  start_time >= ? and start_time< ? \n" +
					"union \n" +
					"SELECT employee, null as start_time, hour(end_time)*100+minute(end_time) as end_time, dayofmonth(end_time) as day FROM ad_adjust \n" +
					"where adjust_type=10 and  end_time >= ? and end_time < ? ) d \n" +
					"group by day, employee\n" +
					"order by day, employee) a\n" +
					"join sys_user e on a.employee=e.id\n" +
					"join sys_org o on o.org_code=e.org_code\n" +
					"where start_time != end_time\n" +
					"order by a.day, a.employee";
			List<Map<String,Object>> list = SqlUtil.sqlQuery(true,sql,dt0,dt1,dt0,dt1,dt0,dt1);
			j.data(list);
		}
		return j;
	}
}
