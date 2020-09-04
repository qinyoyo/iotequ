package top.iotequ.attendance.util;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;

import java.util.List;

import top.iotequ.attendance.adjust.dao.AdAdjustDao;
import top.iotequ.attendance.adjust.pojo.AdAdjust;
import top.iotequ.attendance.data.dao.AdDataDao;
import top.iotequ.attendance.data.pojo.AdData;
import top.iotequ.attendance.dayresult.dao.AdDayResultDao;
import top.iotequ.attendance.dayresult.pojo.AdDayResult;
import top.iotequ.attendance.employee.dao.AdEmployeeDao;
import top.iotequ.attendance.employee.pojo.AdEmployee;
import top.iotequ.attendance.exception.dao.AdExceptionDao;
import top.iotequ.attendance.exception.pojo.AdException;
import top.iotequ.attendance.org.dao.AdOrgDao;
import top.iotequ.attendance.org.pojo.AdOrg;
import top.iotequ.attendance.shift.pojo.AdShift;
import top.iotequ.attendance.shifttime.dao.AdShiftTimeDao;
import top.iotequ.attendance.shifttime.pojo.AdShiftTime;
import top.iotequ.attendance.specialshift.dao.AdSpecialShiftDao;
import top.iotequ.attendance.specialshift.pojo.AdSpecialShift;
import top.iotequ.attendance.specialshifttime.dao.AdSpecialShiftTimeDao;
import top.iotequ.attendance.specialshifttime.pojo.AdSpecialShiftTime;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.service.ICgService;
import top.iotequ.framework.util.*;


public class AdUtil {
	static public final int ad_work=1;                //出勤
	static public final int ad_absent = 2;           //旷工
	static public final int ad_free=3;                  //休息
	static public final int ad_late = 4;                //迟到
	static public final int ad_early_leave = 5;     //早退

	static public final int ad_add_data = 10;  //补登数据

	static public final int ad_business_local = 20;  //市内出差
	static public final int ad_business_out = 21;    //外地出差

	static public final int ad_leave_s = 30;     // 事假
	static public final int ad_leave_b = 31;    // 病假
	static public final int ad_leave_c = 40;    // 产假
	static public final int ad_leave_n = 41;    // 年假
	static public final int ad_leave_t = 42;    // 调休

	static public final int ad_overwork = 50; // 安排的加班
	static public final int ad_free_overwork = 51; // 自由加班

	static public final int st_waiting = 0;    // 等待
	static public final int st_doing = 1;    // 处理中
	static public final int st_passed =  2;    // 批准
	static public final int st_refused = 3;    // 拒绝

	static public final int md_attendance = 1;    // 考勤模式
	static public final int md_sign_in =  2;    // 签到模式
	
	public static final int workOn = 1;       // 时间点状态 上班
	public static final int workOff = 2;	  // 下班
	public static final int workUnknown = 0 ; //未知
	
	public static final int ss_free = 0;       // 特殊排班类型，休假
	public static final int ss_adjust_shift = 1;	  // 调整排班
	public static final int ss_overwork = 2 ; //安排加班
	
	public static String[] stateNameArray() {
		return new String[] {"✔","✘","迟","退","差","假","休","加"};
	}
	public static String stateName(int ad) {
		String [] r = stateNameArray();
		switch (ad) {
			case ad_absent : return r[1];
			case ad_late : return r[2];
			case ad_early_leave : return r[3];
			case ad_work: return r[0];
			case ad_free:
			case ad_leave_t : return r[6];
			case ad_business_local :
			case ad_business_out : return r[4];
			case ad_leave_s : 
			case ad_leave_b : 
			case ad_leave_c : 
			case ad_leave_n : return r[5];
			case ad_overwork :
			case ad_free_overwork : return r[7];		
		}
		return "?";
	}
	public static String stateFullName(int ad) {
		switch (ad) {
			case ad_absent : return "旷工";
			case ad_late : return "迟到";
			case ad_early_leave : return "早退";
			case ad_work: return "出勤";
			case ad_free: return "休息";
			case ad_leave_t : return "调休";
			case ad_business_local : return "本地出差";
			case ad_business_out : return "外地出差";
			case ad_leave_s : return "事假";
			case ad_leave_b : return "病假";
			case ad_leave_c : return "产假";
			case ad_leave_n : return "年假";
			case ad_overwork : return "加班";
			case ad_free_overwork : return "自由加班";
		}
		return "其他";
	}
	public static class SpecialShift {
	    Integer mode;		    //排班属性
        String[] orgCodes;		//影响的部门序列
        Integer sexProperty;		//性别要求
        Integer ageProperty0;		//年龄区间0
        Integer ageProperty1;		//年龄区间1
        Integer levelProperty0;		//职务级别区间0
        Integer levelProperty1;		//职务级别区间1
        Integer adMode;               //考勤模式
		ArrayList<AdTimeRange> range;
		public  SpecialShift(AdSpecialShift ss) {
			mode=ss.getShiftMode();
			orgCodes= ss.getOrgCodes()==null? null : ss.getOrgCodes().split(",");
			sexProperty=ss.getSexProperty();
			ageProperty0=ss.getAgeProperty0();
			ageProperty1=ss.getAgeProperty1();
			levelProperty0=ss.getLevelProperty0();
			levelProperty1=ss.getLevelProperty1();
			adMode=ss.getModeProperty();
			List<AdSpecialShiftTime> sstl=Util.getBean(AdSpecialShiftTimeDao.class).listBy("special_shift_id="+ss.getId(), "start_time");
			if (sstl!=null && sstl.size()>0) {
				range=new ArrayList<AdTimeRange>();
				for (AdSpecialShiftTime st:sstl) {
					try {
						range.add(new AdTimeRange(st));
					} catch (Exception e) {}
				}
				
			} else range=null;		
		}
	}
	
	static List<AdOrg> orgList=null;

	public static void resetOrgList() {
		orgList=null;
	}
	public static class CalcParameter {
		int  shift;
		Date date;
		int adMode;
		int workTimePerDay;
		ArrayList<AdTimeRange>  workRange;
		ArrayList<SpecialShift> specialShift;
		// 根据时间获取特殊排班序列
		public boolean sameAs(int shift,Date dt) {
			if (this.shift==shift) {
				String s= DateUtil.date2String(date,"yyyy-MM-dd"),s1=DateUtil.date2String(dt,"yyyy-MM-dd");
				return EntityUtil.entityEquals(s, s1);
			} else return false;
		}
		private ArrayList<SpecialShift> getSpecialShift(Date date) {
			if (date==null) return null;
			String dt=DateUtil.date2String(date,"yyyy-MM-dd");
			String whe = String.format("start_date<='%s' and end_date>='%s'",dt,dt);
			List<AdSpecialShift> ssl=Util.getBean(AdSpecialShiftDao.class).listBy(whe,null);
			if (ssl!=null && ssl.size()>0) {
				ArrayList<SpecialShift> r=new ArrayList<SpecialShift>();
				for (AdSpecialShift ss:ssl) r.add(new SpecialShift(ss));
				return r;
			}
			else return null;
		}
		// 根据date和shiftid获得考勤区间
		private ArrayList<AdTimeRange> getWorkRange(int shift,Date date) {
			if (date==null) return null;
			int weekDay=-1;
			String dt=DateUtil.date2String(date,"yyyy-MM-dd");
			String whe = String.format("shift_id=%d and start_date<='%s' and end_date>='%s'",shift,dt,dt);
			//是否存在节假日以及调班安排
			List<AdException> el=Util.getBean(AdExceptionDao.class).listBy(whe,null);
			if (el!=null && el.size()>0) weekDay=el.get(0).getWeekDay();
			else 	weekDay=DateUtil.weekOf(date);
			String fis=SqlUtil.findInSet(String.valueOf(weekDay),true,false,"week_days",false);
			whe=String.format("shift_id=%d and %s", shift,fis);
			List<AdShiftTime> stList=Util.getBean(AdShiftTimeDao.class).listBy(whe, "start_work_time");
			if (stList!=null && stList.size()>0) {
				ArrayList<AdTimeRange> wr=new ArrayList<AdTimeRange>();
				for (AdShiftTime st:stList) {
						wr.add(new AdTimeRange(st));
				}
				return wr.isEmpty()?null:wr;
			}
			return null;
		}
		public CalcParameter(int shift,Date dt) {
			if (shift<=0 || dt==null) {
				shift=0;
				date=null;
				adMode=0;
				workTimePerDay=480;
				workRange=null;
				specialShift=null;
				return;
			};
			this.shift=shift;
			this.date=new Date(dt.getTime());
			try {
				AdShift s =SqlUtil.sqlQueryFirst(AdShift.class,"select * from ad_shift where id=?",shift);
				if (s!=null) {
					adMode=s.getAdMode();
					workTimePerDay=s.getMinutesPerDay();	
				}
			} catch (Exception e) {}
			workRange=getWorkRange(shift,dt);
			specialShift=getSpecialShift(dt);		
		}
	}

	// 根据id获得格式化的部门
	public static AdOrg findOrg(int id) {
		if (orgList==null) orgList=getAdOrgList();
		if (orgList==null) return null;
		for (AdOrg  p : orgList) {
			if (p.getOrgCode()==id) return p;
		}		
		return null;
	}
	
	//计算某个人的考勤数据
	public static CalcParameter calcEmployeeAttendance(String id,Date dt0,Date dt1,int startProgress, int endProgress) {
		if (id==null || (dt0==null && dt1==null) ) return null;
		if (dt0==null) return calcEmployeeAttendance(id,dt1);
		else if (dt1==null) return calcEmployeeAttendance(id,dt0);
		long d0=dt0.getTime();
		long d1=dt1.getTime();
		if (d0>d1) { long t=d1; d1=d0; d0=t; }
		CalcParameter cp=null;
		double progress = startProgress;
		double step = ((double)(endProgress - startProgress)) / (int)(1+(d1-d0) / (24*60*60*1000));
		while (d0<=d1) {
			Util.setProgress((int)progress);
			cp = calcEmployeeAttendance(id,new Date(d0));
			d0 += 24*60*60*1000;
			progress += step;
		}
		Util.setProgress(endProgress);
		return cp;
	}
	
	public static CalcParameter calcEmployeeAttendance(String id,Date date) {
		if (id==null || date==null) return null;
		return calcEmployeeAttendance(Util.getBean(AdEmployeeDao.class).select(id),date);
	}

	public static CalcParameter calcEmployeeAttendance(AdEmployee em,Date date) {
		if (em==null || date==null) return null;
		if (!Util.boolValue(em.getIsAttendance())) return null; // 不需要考勤
		Date date0=DateUtil.startOf(date, DateUtil.DAY);
		Date dt=em.getEnterDate();
		if (dt!=null && DateUtil.startOf(dt, DateUtil.DAY).getTime()>date0.getTime()) return null;
		dt=em.getLeaveDate();
		if (dt!=null && DateUtil.startOf(dt, DateUtil.DAY).getTime()<=date0.getTime()) return null;
		return calcEmployeeAttendance(em,findOrg(em.getOrgCode()),date,null,false);
	}
	public static void calcAllEmployeeAttendance(Integer code,Date dt0,Date dt1,boolean calcChildrenOrg, int startProgress, int endProgress) {
		long d0=dt0.getTime();
		long d1=dt1.getTime();
		if (d0>d1) { long t=d1; d1=d0; d0=t; }
		double progress = startProgress;
		double step = ((double)(endProgress - startProgress)) / (int)(1+(d1-d0) / (24*60*60*1000));
		while (d0<=d1) {
			Util.setProgress((int)progress);
			calcAllEmployeeAttendance( code,new Date(d0),calcChildrenOrg,null,false);
			d0 += 24*60*60*1000;
			progress += step;
		}
		Util.setProgress(endProgress);
	}
	
	public static void attendanceTask() {
		Calendar ca=Calendar.getInstance();
		ca.add(Calendar.DATE,-1);
		calcAllEmployeeAttendance(null,ca.getTime(),true,null,true);
	}
	//计算部门的考勤，如果code为空，计算所有的.  calcChildrenOrg 为真同时将计算下级部门考勤
	public static CalcParameter calcAllEmployeeAttendance(Integer code,Date date,boolean calcChildrenOrg,CalcParameter calcParameter,boolean useSms) {
		if (date==null) return null;
		if (orgList==null) orgList=getAdOrgList();
		if (orgList==null) return null;
		if (Util.isEmpty(code)) {
			for (AdOrg o : orgList) {
				if (Util.isEmpty(o.getParent())) calcAllEmployeeAttendance(o.getOrgCode(),date,calcChildrenOrg,null,useSms);
			}
			return calcParameter;
		}
		for (AdOrg o : orgList) {
			if (code==o.getOrgCode() || (calcChildrenOrg && OrgUtil.isOrgChildren(o.getOrgCode(),code))) {
				ICgService adEmployeeService=(ICgService)Util.getBean("adEmployeeService");
				String filter=SqlUtil.licenceCondition("ad_employee", "id", adEmployeeService.getLicence());
				List<AdEmployee> users=Util.getBean(AdEmployeeDao.class).listBy(
						"org_code="+o.getOrgCode()+" and is_attendance=1"+(filter==null?"":" and "+filter),					
						"employee_no");
				for (AdEmployee e:users) {
					calcParameter=calcEmployeeAttendance(e,o,date,null,useSms);
				}
			}
		}
		return calcParameter;
	}		
	
	// 获得所有部门的考勤配置参数
	 private static List<AdOrg> getAdOrgList() {
		List<AdOrg> list=Util.getBean(AdOrgDao.class).listBy(null,"org_code"); 
		for (AdOrg me:list) {
			Integer pid=me.getParent(), shift=me.getShiftId(), deviation=me.getDeviation(),
					floatLimit=me.getFloatLimit(),absentLimit=me.getAbsentLimit(),freeWorkLimit=me.getFreeWorkLimit();
			while (pid!=null && pid!=0 && (shift==null || deviation==null || floatLimit==null ||
					absentLimit==null || floatLimit==null )) {
				AdOrg parent=null;
				for (AdOrg p : list) {
					if (p.getOrgCode()==pid) { parent=p; break; }
				}
				if (parent!=null) {
					pid=parent.getParent();
					if (shift==null) shift=parent.getShiftId();
					if (deviation==null) deviation=parent.getDeviation();
					if (floatLimit==null) floatLimit=parent.getFloatLimit();
					if (absentLimit==null) absentLimit=parent.getAbsentLimit();
					if (freeWorkLimit==null) freeWorkLimit=parent.getFreeWorkLimit();
				} else pid=null;
			}
			me.setShiftId(shift);
			me.setDeviation(deviation==null?0:deviation);
			me.setFloatLimit(floatLimit==null?0:floatLimit);
			me.setAbsentLimit(absentLimit==null?0:absentLimit);
			me.setFreeWorkLimit(freeWorkLimit==null?0:freeWorkLimit);
		}	
		return list;
	}

	 private static boolean matchOrg(String[] orgList,Integer code) {
		if (orgList==null) return true;
		for (String s:orgList) {
			if (OrgUtil.isOrgChildren(code,Integer.parseInt(s))) return true;
		}
		return false;
	}
	 
	//计算某个人的考勤数据，org部门的数据已经从父级部门更新过,否则出错,内部调用	
	private static CalcParameter calcEmployeeAttendance(AdEmployee em,AdOrg org,Date date,CalcParameter calcParameter,boolean useSms) {
		if (em==null || org==null || date==null || em.getEmployeeNo()==null) return calcParameter;
		//删除原有数据
		String dt=DateUtil.date2String(date,"yyyy-MM-dd");
		try {
			SqlUtil.sqlExecute("delete from ad_day_result where employee=? and ad_date=?", em.getId(),dt);
			SqlUtil.sqlExecute("update ad_data set is_used=0 where employee_no=? and rec_time like ?", em.getEmployeeNo(),dt+"%");
		} catch (Exception e) {}
		
		if (em.getEnterDate()!=null && DateUtil.startOf(date,DateUtil.DAY).getTime()<DateUtil.startOf(em.getEnterDate(),DateUtil.DAY).getTime())   return calcParameter;   // 入职前，不计算
		if (em.getLeaveDate()!=null && DateUtil.startOf(date,DateUtil.DAY).getTime()>=DateUtil.startOf(em.getLeaveDate(),DateUtil.DAY).getTime())  return calcParameter;  // 离职后，不计算

		Integer orgCode=em.getOrgCode();
		int age=DateUtil.age(em.getBirthDate()),level=em.getEmLevel(),sex=Integer.valueOf(em.getSex()),
			shift = (em.getShiftId()==null?org.getShiftId() : em.getShiftId()), 
			deviation=org.getDeviation(),floatLimit=org.getFloatLimit(),
			absentLimit=org.getAbsentLimit(),freeWorkLimit=org.getFreeWorkLimit();
		if (calcParameter==null || !calcParameter.sameAs(shift, date)) 
			calcParameter=new CalcParameter(shift,date);
		ArrayList<AdTimeRange> rgWork=null,rgOverWork=null,rgFree=null;
		boolean dayFree=false;  // 全天休假
		if (calcParameter.specialShift!=null && calcParameter.specialShift.size()>0) {
			for (SpecialShift ss:calcParameter.specialShift) {
				if (matchOrg(ss.orgCodes,orgCode) 
					&& (ss.adMode==null || ss.adMode == calcParameter.adMode)	
					&& (ss.sexProperty==null || ss.sexProperty==sex) 
					&& (ss.ageProperty0==null || ss.ageProperty0<=age)	
					&& (ss.ageProperty1==null || ss.ageProperty1>=age)	
					&& (ss.levelProperty0==null || ss.levelProperty0<=level)	
					&& (ss.levelProperty1==null || ss.ageProperty1>=level)	) {
					if (ss.mode==AdUtil.ss_free && ss.range==null) // 全天休假
						{ dayFree=true ; break; }
					else if (ss.mode==AdUtil.ss_free)  rgFree=ss.range;
					else if (ss.mode==AdUtil.ss_adjust_shift)  rgWork=ss.range;
					else if (ss.mode==AdUtil.ss_overwork)  rgOverWork=ss.range;
				}
			}
		}
		if (dayFree) {
			if (calcParameter.workRange!=null && calcParameter.workRange.size()>0) {
				for (AdTimeRange r:calcParameter.workRange) {
					AdResult result=new AdResult(ad_free,1,r.getMinutes() ,0);
					insertResult(em,org,date,result,r.getName(),useSms);					
				}
			}
			rgWork=null;   // 全休
		}
		else if (rgWork!=null){     // 以特殊排班为准
		} else rgWork=AdTimeRange.cloneList(calcParameter.workRange);
		if (rgWork!=null && rgFree!=null) {  //  减去调休时间
			for (AdTimeRange r:rgWork) {
				int len=0;
				for (AdTimeRange ff:rgFree) {
					AdTimeRange t=AdTimeRange.intersect(r, ff);
					if (t!=null) len+=t.getMinutes();
				}
				if (len>0) {
					AdResult result=new AdResult(ad_free,1,len ,0);
					insertResult(em,org,date,result,r.getName(),useSms);										
				}
			}
			for (AdTimeRange ff:rgFree)
				rgWork=AdTimeRange.rangeSubtraction(rgWork, ff);
		}
		if (rgWork!=null && AdTimeRange.timeLong(rgWork)<=deviation) {   //  没有工作时间
			rgWork=null;
		}
		if (rgOverWork!=null && AdTimeRange.timeLong(rgOverWork)<=deviation) {   //  没有加班时间
			rgOverWork=null;
		}
		String where = "ad_adjust.state="+st_passed+" and ad_adjust.employee=? and " +
				"( (ad_adjust.end_time >= ? and ad_adjust.start_time<=?) || " +
				"  (ad_adjust.start_time is null and ad_adjust.end_time>=? and ad_adjust.end_time<=?) ||" +
				"  (ad_adjust.end_time is null and ad_adjust.start_time>=? and ad_adjust.start_time<=?)  )";
		String dt0=DateUtil.date2String(date, "yyyy-MM-dd")+" 00:00:00",dt1=DateUtil.date2String(date, "yyyy-MM-dd")+" 23:59:59";
		List<AdAdjust> adjusts=null;
		try {
			adjusts = Util.getBean(AdAdjustDao.class).listBy(SqlUtil.sqlString(where,em.getId(),dt0,dt1,dt0,dt1,dt0,dt1),null);
		} catch (Exception e) {	}
		where="ad_data.employee_no=? and  ad_data.rec_time >= ? and ad_data.rec_time <=?";
		List<AdData> datas=null;
		try {
			datas = Util.getBean(AdDataDao.class).listBy(SqlUtil.sqlString( where,em.getEmployeeNo(),dt0,dt1),null);
		} catch (Exception e) {	}
		if (datas==null) datas=new ArrayList<AdData>();
		if (adjusts!=null && adjusts.size()>0) {
			for (AdAdjust a:adjusts) {
				switch (a.getAdjustType()) {
					case ad_add_data :   //插入调整数据
						if (a.getStartTime()!=null) {
							AdData d=new AdData();
							d.setEmployeeNo(em.getEmployeeNo());
							d.setOrgCode(org.getOrgCode());
							d.setRecTime(a.getStartTime());
							d.setRecType(1);
							datas.add(d);
						}
						if (a.getEndTime()!=null) {
							AdData d=new AdData();
							d.setEmployeeNo(em.getId());
							d.setOrgCode(org.getOrgCode());
							d.setRecTime(a.getEndTime());
							d.setRecType(2);
							datas.add(d);
						};
						break;
					case ad_overwork :   // 加班,加班以天为单位登记，可以直接取时间计算
						if (rgOverWork==null) rgOverWork=new ArrayList<AdTimeRange>();
						rgOverWork.add(new AdTimeRange(new AdTime(a.getStartTime(),1),new AdTime(a.getEndTime(),2),""));
						break;
					default:
						if (rgWork!=null) {   //  出差，休假，休息区间的出差休假不计算
							Date d0=DateUtil.string2Date(dt0,null),d1=DateUtil.string2Date(dt1,null);    //  获得当天的区间
							if (d0.getTime()<a.getStartTime().getTime()) d0=a.getStartTime();    //  开始时间在今天之后，以开始时间计算
							if (d1.getTime()>a.getEndTime().getTime())  d1=a.getEndTime();       //  结束时间在今天之前，以结束时间为准
							AdTimeRange rg=new AdTimeRange(new AdTime(d0,1),new AdTime(d1,2),"?");
							businessAndLeave(calcParameter.adMode,em,org,date,rgWork,rg,a.getAdjustType(),useSms);							
						}
				}
			}
		}
		//将加班区间与上班区间的交叉部分减去
		if (rgOverWork!=null && rgWork!=null) {
			for (AdTimeRange r:rgWork) {
				rgOverWork=AdTimeRange.rangeSubtraction(rgOverWork, r);
				if (AdTimeRange.isEmpty(rgOverWork)) break;
			}
		}
		ArrayList<AdTime> adTimes=new ArrayList<AdTime>();
		for (AdData d:datas) {
			AdTime t=new AdTime(d.getRecTime(),d.getRecType(),d.getId());
			adTimes.add(t);
		}
		AdTime.AdTimeSort(adTimes);
		AdTime.processAdTimes(adTimes); // 修改未知状态的时间点
		AdTime workOn=AdTime.firstMatchTime(adTimes, AdTime.workOn);   // 第一个上班时间
		if (floatLimit>0 && !AdTimeRange.isEmpty(rgWork) && workOn!=null)  {
			int delta=workOn.getValue() - rgWork.get(0).getStart().getValue();  //  晚到时间，小于0为早到
			if (delta!=0) {
				if (delta>floatLimit) delta=floatLimit;   //  晚到不能超过范围
				else if (delta<-floatLimit) delta=-floatLimit;  // 早到范围不能太早
				floatLimit=0; // 后续不再处理浮动
				rgWork.get(0).getStart().setValue(rgWork.get(0).getStart().getValue() + delta);
				rgWork.get(rgWork.size()-1).getEnd().setValue(rgWork.get(rgWork.size()-1).getEnd().getValue() + delta);	  // 出差、请假等导致多个上班区间，此时浮动可能导致区间为负，删除负区间
				if (rgWork.get(0).getStart().getValue() >=rgWork.get(0).getEnd().getValue()) rgWork.remove(0);
				if (rgWork.size()>0) {
					if (rgWork.get(rgWork.size()-1).getStart().getValue() >=rgWork.get(rgWork.size()-1).getEnd().getValue()) rgWork.remove(rgWork.size()-1);
				}
			}
		}		
		if (!AdTimeRange.isEmpty(rgWork)) {			
			workAttendance(calcParameter.adMode,rgWork,adTimes,em,org,date,deviation,freeWorkLimit,absentLimit,useSms);
		}
		//计算加班时间，加班不考虑误差和浮动
		if (!AdTimeRange.isEmpty(rgOverWork)) {
			if (calcParameter.adMode==AdUtil.md_attendance &&  adTimes.size()>=2) {
				ArrayList<AdTimeRange> onoffList=new ArrayList<AdTimeRange>();
				for (int i=0;i<adTimes.size()/2;i++) {
					AdTimeRange adR = new AdTimeRange(adTimes.get(2*i),adTimes.get(2*i+1),"");
					onoffList.add(adR);
				}
				AdTimeRange.rangeSort(rgOverWork);
				for (AdTimeRange r:rgOverWork) {
					int rl = AdTimeRange.timeLong(AdTimeRange.intersect(onoffList,r));
					if (rl>0) {
					setName2Overwork(shift,r);
					AdResult result=new AdResult(ad_overwork,1,rl ,rl);
					insertResult(em,org,date,result,r.getName(),useSms);
					}
				}
			} else if (calcParameter.adMode==AdUtil.md_sign_in  && adTimes.size()>0) {
				AdTimeRange.rangeSort(rgOverWork);
				for (AdTimeRange r:rgOverWork) {
					for (AdTime adt : adTimes) {
						if (adt.getValue()>=r.getStart().getValue() && adt.getValue()<=r.getEnd().getValue()) {
							setName2Overwork(shift,r);
							AdResult result=new AdResult(ad_overwork,1,r.getMinutes() ,r.getMinutes());
							insertResult(em,org,date,result,r.getName(),useSms);
						}
					}
				}
			}
		}
		return calcParameter;
	}
	

	//计算某个人的出勤有效时间范围，提供给外部程序使用
	public static Map<String,Date> calcEmployeeAttendanceTime(String userNo,Date date) {
		if (userNo==null || date==null) return null;
		AdEmployee em=null;
		try {
			em = Util.getBean(AdEmployeeDao.class).select(userNo);
		} catch (Exception e) {	}
		if (em==null) return null;
		if (Util.isEmpty(em.getEmployeeNo())) return null;
		if (em.getEnterDate()!=null && date.getTime()<em.getEnterDate().getTime())   return null;  // 入职前
		if (em.getLeaveDate()!=null && date.getTime()>=em.getLeaveDate().getTime())  return null;  // 离职后
		Map<String,Date> ret = new HashMap<String,Date>();
		if (!Util.boolValue(em.getIsAttendance())) {  // 不需要考勤的，全天有效
			ret.put("start", DateUtil.startOf(date,DateUtil.DAY));
			ret.put("end", DateUtil.endOf(date,DateUtil.DAY));
			return ret;
		}
		Integer orgCode=em.getOrgCode();
		AdOrg org = findOrg(orgCode);
		if (org==null) return null;

		int age=DateUtil.age(em.getBirthDate()),level=em.getEmLevel(),sex=Integer.valueOf(em.getSex()),
			shift=org.getShiftId(), deviation=org.getDeviation(),floatLimit=org.getFloatLimit();
		CalcParameter	calcParameter=new CalcParameter(shift,date);
		ArrayList<AdTimeRange> rgWork=null,rgOverWork=null,rgFree=null;
		boolean dayFree=false;  // 全天休假
		if (calcParameter.specialShift!=null && calcParameter.specialShift.size()>0) {
			for (SpecialShift ss:calcParameter.specialShift) {
				if (matchOrg(ss.orgCodes,orgCode) 
					&& (ss.adMode==null || ss.adMode == calcParameter.adMode)	
					&& (ss.sexProperty==null || ss.sexProperty==sex) 
					&& (ss.ageProperty0==null || ss.ageProperty0<=age)	
					&& (ss.ageProperty1==null || ss.ageProperty1>=age)	
					&& (ss.levelProperty0==null || ss.levelProperty0<=level)	
					&& (ss.levelProperty1==null || ss.ageProperty1>=level)	) {
					if (ss.mode==AdUtil.ss_free && ss.range==null) // 全天休假
						{ dayFree=true ; break; }
					else if (ss.mode==AdUtil.ss_free)  rgFree=ss.range;
					else if (ss.mode==AdUtil.ss_adjust_shift)  rgWork=ss.range;
					else if (ss.mode==AdUtil.ss_overwork)  rgOverWork=ss.range;
				}
			}
		}
		if (dayFree) {
			return null;
		}
		else if (rgWork!=null){     // 以特殊排班为准
		} else rgWork=AdTimeRange.cloneList(calcParameter.workRange);
		
		if (rgWork!=null && rgFree!=null) {  //  减去调休时间
			for (AdTimeRange ff:rgFree)
				rgWork=AdTimeRange.rangeSubtraction(rgWork, ff);
		}
		if (rgWork==null && rgOverWork==null) return null;
		int start=24*60,end=0;
		if (rgWork!=null) {
			for (AdTimeRange r : rgWork) {
				if (r.getStart().getValue()<start) start=r.getStart().getValue();
				if (r.getEnd().getValue()>end) end=r.getEnd().getValue();
			}
		}
		if (rgOverWork!=null) {
			for (AdTimeRange r : rgOverWork) {
				if (r.getStart().getValue()<start) start=r.getStart().getValue();
				if (r.getEnd().getValue()>end) end=r.getEnd().getValue();
			}
		}
		start -= (floatLimit+deviation);
		if (start<0) start=0;
		end += (floatLimit+deviation);
		if (end>24*60-1) end = 24*60-1;
		String dt=DateUtil.date2String(date,"yyyy-MM-dd");
		ret.put("start",DateUtil.string2Date(String.format("%s %02d:%02d:00", dt,start/60,start%60)));
		ret.put("end",DateUtil.string2Date(String.format("%s %02d:%02d:59", dt,end/60,end%60)));
		return ret;
	}

	private static void workAttendance(int adMode,ArrayList<AdTimeRange> workRange,ArrayList<AdTime> adTimes,AdEmployee em,AdOrg org,Date date,
			int deviation,int freeWorkLimit, int absentLimit,boolean useSms) {
		if (adMode==AdUtil.md_sign_in) // 签到考勤,
		{
			if (adTimes==null || adTimes.size()==0) {
				for (AdTimeRange r:workRange) {
					AdResult result=new AdResult(ad_absent,1,r.getEnd().getValue()-r.getStart().getValue(),0);
					insertResult(em,org,date,result,r.getName(),useSms);
				}
				return;
			}
			for (AdTimeRange r:workRange) {
				boolean absent=true;
				for (AdTime t:adTimes) {
					if (r.getStart().getValue()-deviation <= t.getValue() && t.getValue()<=r.getEnd().getValue()+deviation) {
						AdResult result=new AdResult(ad_work,1,r.getEnd().getValue()-r.getStart().getValue(),r.getEnd().getValue()-r.getStart().getValue());
						insertResult(em,org,date,result,r.getName(),useSms);
						t.setUsedSign();
						absent=false;
						break;
					}
				}
				if (absent) {
					AdResult result=new AdResult(ad_absent,1,r.getEnd().getValue()-r.getStart().getValue(),r.getEnd().getValue()-r.getStart().getValue());
					insertResult(em,org,date,result,r.getName(),useSms);
				}
			}
		} else if (adMode==AdUtil.md_attendance) {
			if (adTimes==null || adTimes.size()<2) {
				for (AdTimeRange r:workRange) {
					AdResult result=new AdResult(ad_absent,1,r.getEnd().getValue()-r.getStart().getValue(),0);
					insertResult(em,org,date,result,r.getName(),useSms);
				}
				return;
			}
			for (AdTimeRange r:workRange) {
				List<AdResult> resultList=AdResult.adCalc(r, adTimes, deviation, freeWorkLimit, absentLimit);
				if (resultList!=null && resultList.size()>0) {
					for (AdResult res : resultList) {
						insertResult(em,org,date,res,r.getName(),useSms);
					}
				}
			}			
		}
	}
	
	private static void businessAndLeave(int adMode,AdEmployee em,AdOrg org,Date date,ArrayList<AdTimeRange> rgWork,AdTimeRange rg,int state,boolean useSms) {
		if (adMode==AdUtil.md_attendance) { //有交叉记录一次出差或请假，然后减去该区间
			ArrayList<AdTimeRange> newRgWork=new ArrayList<AdTimeRange>();
			for (AdTimeRange r:rgWork) {
				AdTimeRange t=AdTimeRange.intersect(r, rg);
				if (t!=null && t.getMinutes()>0) {
					AdResult result=new AdResult(state,1,t.getMinutes(),0);
					insertResult(em,org,date,result,r.getName(),useSms);
					newRgWork.addAll(AdTimeRange.subtract(r, rg));
				}else newRgWork.add(r);
			}
			AdTimeRange.rangeSort(newRgWork);
			rgWork.clear();
			for (AdTimeRange r:newRgWork) rgWork.add(r);
			//rgWork=newRgWork; 这种写法无法改变rgWork
		}else if (adMode==AdUtil.md_sign_in) { // 签到考勤，只要在区间有交叉，就认为整个区间为改事件
			for (int i=0;i<rgWork.size();i++) {
				AdTimeRange r=rgWork.get(i);
				AdTimeRange t=AdTimeRange.intersect(r, rg);
				if (t!=null && t.getMinutes()>0) {
					AdResult result=new AdResult(state);
					insertResult(em,org,date,result,r.getName(),useSms);
					rgWork.remove(r);
					i--;
				}
			}		
		}
		
	}
	
	//获得员工的审批信息
	public static Object getManageInfo(String id,String field,boolean getOrg) {
		Integer orgCode=null;
		try {
			orgCode = SqlUtil.sqlQueryInteger(false,"select org_code as org from sys_user where id=?",id);
		} catch (Exception e) {	}
		while (orgCode!=null && orgCode!=0) {
			String sql="SELECT ad_org.manager,ad_org.org_code as org,sys_org.parent,ad_org.hr FROM ad_org JOIN sys_org ON ad_org.org_code=sys_org.org_code where ad_org.org_code=?";
			List<Map<String, Object>> list=null;
			try {
				list = SqlUtil.sqlQuery(false,sql, orgCode);
			} catch (Exception e) {	}
			if (list!=null && list.size()>0) {
				Object o=list.get(0).get(field);
				if (!Util.isEmpty(o) && ("hr".equals(field) || !o.toString().equals(id))) {    //  manager排除本人,hr不排除本人
					if (getOrg)
						return (Integer)list.get(0).get("org");
					else
						return o.toString();
				}
				orgCode = (Integer)list.get(0).get("parent");
			} else break;
		}
		return null;
	}

	//获得员工的管理员。本部门未定义，则查询上级部门的
	public static String getManager(String id) {
		Object o = getManageInfo(id,"manager",false);
		if (o!=null) return (String)o;
		else return null;
	}
	//获得员工的审批部门。本部门未定义，则查询上级部门
	public static Integer getManageOrg(String id) {
		Object o = getManageInfo(id,"manager",true);
		if (o!=null) return (Integer)o;
		else return null;
	}

	//获得员工的人事管理员。本部门未定义，则查询上级部门的
	public static String getHr(String id) {
		Object o = getManageInfo(id,"hr",false);
		if (o!=null) return (String)o;
		else return null;
	}
	public static Integer approveLimit(Integer org,String aid) throws IotequException {
		if (Util.isEmpty(org)) throw new IotequException(IotequThrowable.NULL_OBJECT,"部门不存在");
		if (Util.isEmpty(aid)) throw new IotequException(IotequThrowable.NULL_OBJECT,"审批人不存在");

		String sql="SELECT * FROM ad_org where org_code=? and manager=?";
		List<AdOrg> list=SqlUtil.sqlQuery(AdOrg.class,sql,org,aid);
		if (list==null || list.size()==0) throw new IotequException(IotequThrowable.NO_AUTHORITY,"没有权限审批");
		return list.get(0).getManageLimit();
	}

	public static AdOrg parentApproveOrg(Integer org) {
		try {
			org = SqlUtil.sqlQueryInteger("select parent from sys_org where org_code=?",org);
			String sql="SELECT * FROM sys_org where org_code=?";
			while (org!=null && org!=0) {
				List<AdOrg> list=SqlUtil.sqlQuery(AdOrg.class,sql, org);
				if (list!=null && list.size()>0) {
					AdOrg o=list.get(0);
					if (!Util.isEmpty(o.getManager()) )	return o;
					org = o.getParent();
				} else return null;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void sendMessage(AdAdjust o,String title,String content,boolean useSms) {
		if (o==null) return;
		String url="/adAdjust/update?id="+o.getId();
		if (title==null) {
			try {
				title=SqlUtil.sqlQueryString("select real_name from sys_user where id=?", o.getEmployee());
			} catch (Exception e) {	}
			if (title==null) title=""; else title+=":";
			int [] dictAdjustTypeValue= {AdUtil.ad_add_data,
					AdUtil.ad_business_local,AdUtil.ad_business_out,  
					AdUtil.ad_leave_s,AdUtil.ad_leave_b,
					AdUtil.ad_leave_c,  AdUtil.ad_leave_n,AdUtil.ad_leave_t,
					AdUtil.ad_overwork};
			//数据补登,市内出差,外地出差,事假,病假,产假,年假,调休,加班
			//数据补登,市内出差,外地出差,事假,病假,产假,年假,调休,加班
			String [] dictAdjustTypeText= {
					"adAdjust.field.adjustType_0",
					"adAdjust.field.adjustType_1",
					"adAdjust.field.adjustType_2",
					"adAdjust.field.adjustType_3",
					"adAdjust.field.adjustType_4",
					"adAdjust.field.adjustType_5",
					"adAdjust.field.adjustType_6",
					"adAdjust.field.adjustType_7",
					"adAdjust.field.adjustType_8"
				};

			for (int i=0;i<dictAdjustTypeValue.length;i++)  {
				if  (dictAdjustTypeValue[i]==o.getAdjustType()) { 
					title += dictAdjustTypeText[i];
					break;
				}
			}
		}
		if (Util.isEmpty(title)) title="人事审批单";
		if (Util.isEmpty(content)) content=o.getDescription();
		String sendTo=o.getApprover();
		if (!Util.isEmpty(sendTo))  {
			Integer msgid=Util.sendMessage(sendTo,title , content, url,o.getId());
			if (useSms) {
				String smsModules=Util.getBean(Environment.class).getProperty("sms.modules");
				if (smsModules!=null && smsModules.toLowerCase().contains("attendance")) {
					try {
							String mobile=SqlUtil.sqlQueryString(false, "select mobile_phone from sys_user where id=?",sendTo);
							if (!Util.isEmpty(mobile)) {
								Map<String,Object> map = new HashMap<>();
								map.put("name",SqlUtil.sqlQueryString(false, "select real_name from sys_user where id=?",sendTo));
								map.put("eventname",content);		
								map.put("eventtype","人事审批单");
								map.put("operation","审批");
								map.put("eventtime", new Date());
								if (msgid!=null)  map.put("messageid","m/"+msgid);
								Util.sendTemplateSmsToMobile(mobile, null,map);
							}
					} catch (Exception e) {
						e.printStackTrace();
					}		
				}
			}
		}
		if (!Util.isEmpty(o.getHr()) && !EntityUtil.entityEquals(o.getHr(), o.getApprover()))  Util.sendMessage(o.getHr(), title,content, url+"&_load=detail",o.getId());
		if (!Util.getUser().getId().equals(o.getEmployee())) Util.sendMessage(o.getEmployee(), title,content, url+"&_load=detail",o.getId());
	}
	
	public static void sendMessage(AdResult o,String shiftName,AdEmployee em,String hr,Date dt,String eid,boolean useSms) {
		if (o==null || em==null) return;
		String title="";
		switch (o.getState()) {
			case ad_absent : title="旷工"; break;
			case ad_late : title="迟到"; break;
			case ad_early_leave : title="早退"; break;
			default:
				return;
		}
		String smsTitle=title;
		title=String.format("%s : %s[%s]", em.getRealName(),title,(shiftName==null ? "" : shiftName));
		String content=DateUtil.date2String(dt,"yyyy-MM-dd : " ) + o.toString();
		Integer msgid=Util.sendMessage(em.getId(),null,title , content, null,eid);
		if (useSms) {
			String smsModules=Util.getBean(Environment.class).getProperty("sms.modules");
			if (smsModules!=null && smsModules.toLowerCase().contains("attendance")) {
				try {
						String mobile=SqlUtil.sqlQueryString(false, "select mobile_phone from sys_user where id=?",em.getId());
						if (!Util.isEmpty(mobile)) {
							Map<String,Object> map = new HashMap<>();
							map.put("name",em.getRealName());
							map.put("eventname",smsTitle);
							map.put("eventtype","考勤");
							map.put("operation","自查");
							map.put("eventtime", dt);
							if (msgid!=null)  map.put("messageid","m/"+msgid);
							Util.sendTemplateSmsToMobile(mobile, null,map);
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (o.getState() == ad_absent  && !Util.isEmpty(hr) && !EntityUtil.entityEquals(hr, em.getId())) {
			Util.sendMessage(hr,null,title , content, null,eid);
		}
	}

	private static void insertResult(AdEmployee em,AdOrg org,Date date,AdResult result,String shiftName,boolean useSms) {
		if (em==null || org==null || date==null || result==null || shiftName==null || em.getEmployeeNo()==null) return;
		List<Map<String,Object>> shiftList = Util.getDataDict("ad_shift_name");
		Integer shiftId=10000;
		for (Map<String,Object> m:shiftList) {
			if (m.get("value").toString().equals(shiftName)) {
				shiftId=(Integer)m.get("order_num");
				break;
			}
		}
		if (shiftId==null) shiftId=10000;
		AdDayResultDao dao=Util.getBean(AdDayResultDao.class);
		AdDayResult r=new AdDayResult();
		r.setAdDate(date);
		r.setEmployee(em.getId());
		r.setEmployeeNo(em.getEmployeeNo());
		r.setMinutes(result.getMinutes());
		r.setOrgCode(em.getOrgCode());
		r.setOrgName(org.getName());
		r.setRealName(em.getRealName());
		r.setShiftName(shiftName);
		r.setShiftId(shiftId);
		r.setStateName(AdUtil.stateName(result.getState()));
		r.setState(result.getState());
		r.setTimes(result.getTimes());
		r.setWorkMinutes(result.getWorkMinutes());
		dao.insert(r);
		sendMessage(result,shiftName,em,org.getHr(),date,(r.getId()==null?null:r.getId().toString()),useSms);
	}

	public static String dataFilter(boolean useNo,Class<?> clazz) {
		String id=Util.getUser().getId();
		if (Util.isEmpty(id)) return "1=2";
		String employeeNo= null;
		try {
			employeeNo = SqlUtil.sqlQueryString(false,"select employee_no from ad_employee where id=?",id);
		} catch (IotequException e) {
			employeeNo = null;
		}
		String field = EntityUtil.getDBFieldNameFrom(clazz, useNo?"employeeNo" : "employee");
		String w1=String.format("%s='%s'",field,employeeNo);  // 可以看本人的
		AdOrg ol1=null;
		try {
			ol1 = SqlUtil.sqlQueryFirst(AdOrg.class, false,"SELECT * FROM ad_org where manager=? or hr=?",id,id);
		} catch (Exception e) {}
		if (ol1==null) return w1;  // 仅可看本人的
		 //  可以看那些部门的	
		String w2=OrgUtil.getOrgAndChildrenOrgList(ol1.getOrgCode());
		w2 = EntityUtil.getDBFieldNameFrom(clazz, "orgCode") + " in (" + w2 +")";
		return  w2;
	}
	private static void setName2Overwork(int shift,AdTimeRange oRange ) {
		List<AdTimeRange> rgWork=new ArrayList<AdTimeRange>();
		int week=1;
		while (week<6 && rgWork.size()==0) {
			String whe=String.format("shift_id=%d and week_days=%d", shift,week);
			List<AdShiftTime> stList=Util.getBean(AdShiftTimeDao.class).listBy(whe, "start_work_time");
			if (stList!=null && stList.size()>0) {
				for (AdShiftTime st:stList) {
					rgWork.add(new AdTimeRange(st));
				}
			}
			week++;
		}
		for (int i=0;i<rgWork.size();i++) {
			AdTimeRange r = rgWork.get(i);
			if (oRange.getEnd().getValue()<=r.getStart().getValue()) {
				oRange.setName(r.getName());
				return;
			} else if (i==rgWork.size()-1 && oRange.getStart().getValue()>=r.getEnd().getValue()) {
				oRange.setName(r.getName());
				return;				
			}
			AdTimeRange t=AdTimeRange.intersect(r, oRange);
			if (t!=null && t.getMinutes()>0) {
				oRange.setName(r.getName());
				return;
			} 
		}
	}
}
