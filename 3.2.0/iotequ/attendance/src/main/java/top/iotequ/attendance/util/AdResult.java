package top.iotequ.attendance.util;

import java.util.ArrayList;


public class AdResult {
	int state; // 考勤结果
	int times; // 次数
	int minutes; // 附加时长
	int workMinutes; // 工作时长

	public AdResult() {
		state = AdUtil.ad_work;
		times = 1;
		minutes = 0;
		workMinutes=0;
	}
	public AdResult(int state) {
		this.state = state;
		times = 1;
		minutes = 0;
		workMinutes=0;
	}
	public AdResult(int st,int tm,int tl,int wt) {
		state = st;
		times = tm;
		minutes = tl;
		workMinutes=wt;
	}
	
	public int getState() {
		return state;
	}
	public int getTimes() {
		return times;
	}
	public int getMinutes() {
		return minutes;
	}

	public int getWorkMinutes() {
		return workMinutes;
	}

	@Override
	public String toString() {
		String rs = AdUtil.stateName(state);
		return String.format("%s :  次数 : %d     时长 : %d小时零%d分    出勤时长 :  %d小时零%d分", 
				rs, times, minutes / 60, minutes % 60 , workMinutes / 60, workMinutes % 60);
	}

	// 考核计算，可调用函数
	// workRange :单个考核区间 
	// deviation：允许考核浮动误差，大于0有效
	// adTimes ： 所有考勤时间点数据 
	// freeWorkLimit : 自由加班门限，超过此值的时间才统计
	// absentLimit : 超过此值的迟到或早退按旷工处理
	// sorted：数据是否已经排好序
	public static ArrayList<AdResult> adCalc(AdTimeRange rgWork,	ArrayList<AdTime> adTimes,int deviation,int freeWorkLimit, int absentLimit)  {
		if (rgWork==null ) return null;
		ArrayList<AdResult> resultList=new ArrayList<AdResult>();
		AdTime.processAdTimes(adTimes); // 修改未知状态的时间点
		if (adTimes==null || adTimes.size()<2) {  // 考勤记录小于2，按旷工计算
			AdResult adResult = new AdResult();
			adResult.state = AdUtil.ad_absent;
			adResult.times = 1;   
			adResult.minutes = 0;
			adResult.minutes = rgWork.getMinutes();
			resultList.add(adResult);
			if (adTimes.size()==1) adTimes.get(0).setUsedSign();
			return resultList;			
		}
		AdTimeRange workRange=new AdTimeRange(rgWork);
		AdTime workOn = workRange.getStart(), workOff = workRange.getEnd();
		AdTime onTime = null,offTime=null;
		for (AdTime t:adTimes) {
			if (onTime==null && (t.getState()==AdTime.workOn || t.getState()==AdTime.workUnknown)) {
				onTime=t;
				if (offTime!=null)  offTime=null;
			}
			if (onTime!=null && offTime!=null && offTime.getValue()>=workOff.getValue()-deviation) break;
			if (t.getState()==AdTime.workOff || t.getState()==AdTime.workUnknown)
				offTime=t;
		}
		if (onTime!=null) onTime.setUsedSign();
		if (offTime!=null) offTime.setUsedSign();
		
		if (offTime!=null) {   // 删除处理后的时间点
			for (int i=0;i<adTimes.size();i++) {
				AdTime t=adTimes.get(i);
				if (t.getValue()<offTime.getValue()) {   // 删除下班时间之前的数据
					adTimes.remove(t);
					i--;
				}
				else if (t.getValue()==offTime.getValue()) { 
					if (t.getValue()>workOff.getValue()) {   //  此次计算的下班时间在排班的下班时间后，补位，为下一个排班插入数据
						AdTime nt=new AdTime(workOff.getValue(),AdTime.workOn);
						offTime=new AdTime(workOff.getValue(),AdTime.workOff);
						adTimes.add(0,nt);
						i++;
					} else {
						adTimes.remove(t);
						i--;
					}
					break;
				}
			}
		}
		if (onTime == null || offTime == null || offTime.getValue()<workOn.getValue()-deviation ||  onTime.getValue()>workOff.getValue()+deviation) {  // 考核时间不完整，时间不在考核区间内，按旷工处理
			AdResult adResult = new AdResult();
			adResult.state = AdUtil.ad_absent;
			adResult.times = 1;
			adResult.minutes = workOff.getValue() - workOn.getValue();
			adResult.workMinutes = 0;
			resultList.add(adResult);
			return resultList;
		} else {
			AdResult late = null, early_leave = null;
			int lateTime = onTime.getValue() - workOn.getValue();
			int leaveTime = workOff.getValue() - offTime.getValue();
			if (lateTime>0 && lateTime<=deviation) lateTime=0;
			if (leaveTime>0 && leaveTime<=deviation) leaveTime=0;
			if (lateTime > 0) {
				late = new AdResult();
				late.state = AdUtil.ad_late;
				late.times = 1;
				late.minutes = lateTime;
			}
			if (leaveTime > 0) {
				early_leave = new AdResult();
				early_leave.state = AdUtil.ad_early_leave;
				early_leave.times = 1;
				early_leave.minutes = leaveTime;
			}
			
			int workTimeLong = workRange.getMinutes();
			if (late == null && early_leave == null) {
				AdResult adResult = new AdResult();
				adResult.state = AdUtil.ad_work;
				adResult.times = 1;
				adResult.workMinutes = adResult.minutes = workTimeLong;
				resultList.add(adResult);
				return resultList;
			} else if (late == null) {
				if (absentLimit>0 && early_leave.minutes>=absentLimit) {
					early_leave.state = AdUtil.ad_absent;
				}
				early_leave.workMinutes = workTimeLong - early_leave.minutes;
				if (early_leave.workMinutes<0) {
					early_leave.workMinutes=0;
					early_leave.minutes=workTimeLong;
				}
				resultList.add(early_leave);
				return resultList;
			}
			else if (early_leave == null) {
				if (absentLimit>0 && late.minutes>=absentLimit) {
					late.state = AdUtil.ad_absent;
				}
				late.workMinutes = workTimeLong - late.minutes;
				if (late.workMinutes<0) {
					late.workMinutes=0;
					late.minutes=workTimeLong;
				}
				resultList.add(late);
				return resultList;
			}
			else {
				if (absentLimit>0 && (late.minutes>=absentLimit || early_leave.minutes>=absentLimit) ){
					late.state=AdUtil.ad_absent;
					late.minutes += early_leave.minutes;
					late.workMinutes = workTimeLong - late.minutes;
					resultList.add(late);
					return resultList;
				}
				late.workMinutes = workTimeLong / 2 - late.minutes;
				early_leave.workMinutes = workTimeLong - workTimeLong / 2 - early_leave.minutes;
				resultList.add(late);
				resultList.add(early_leave);
				return resultList;
			}
		}
	}
}
