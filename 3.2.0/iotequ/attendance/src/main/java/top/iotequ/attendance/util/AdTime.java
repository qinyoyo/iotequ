package top.iotequ.attendance.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import top.iotequ.util.SqlUtil;

public class AdTime {
	public static final int workOn = AdUtil.workOn;      // 时间点状态 上班
	public static final int workOff = AdUtil.workOff;	 // 下班
	public static final int workUnknown = AdUtil.workUnknown ;
	private String dataId;
	private int  value;   //  每天0:00开始的分钟数
	private int  state;
	public String getDataId() { return dataId; }
	public   int  getValue() { return value; }
	public   void  setValue(int value) { this.value=value; }
	public   int  getState()  { return state;  } 
	public void setUsedSign() {
		if (dataId!=null)
			try {
				SqlUtil.sqlExecute("update ad_data set is_used=1 where id=?", dataId);
			} catch (Exception e) {	}
	}
	public   void setState(int st) {
		if (st==workOn) state=workOn;
		else if (st==workOff) state=workOff;
		else state=workUnknown;
	}
	public AdTime(int v,int st) {
		value=v % (23*60+59);
		setState(st);
		dataId=null;
	}
	public AdTime(AdTime src) {
		value=src.getValue();
		state=src.getState();
		dataId=src.dataId;
	}
	public AdTime(int h,int m,int st) {
		value=(h % 24)*60 + (m % 60);
		setState(st);
		dataId=null;
	}
	@Override public String toString() {
		return String.format("%02d:%02d %s",value/60,value%60,state==workOn?"上班":(state==workOff?"下班":"未知"));
	}
	public AdTime(String h_m,int st)  throws Exception {      //  h_m  like 8:30
		if (h_m==null || h_m.isEmpty())  throw new Exception("Can not use a null string");
		setState(st);
		String[] hm=h_m.split(":");
		if (hm.length<2) throw new Exception("Please use format like hh:mm");
		int h=Integer.parseInt(hm[0]);
		int m=Integer.parseInt(hm[1]);
		value=(h % 24)*60 + (m % 60);
		dataId=null;
	}
	public AdTime(Date tm,int st)  {
		if (tm==null)  value=0; 
		else {
			Calendar ca=Calendar.getInstance();
			ca.setTime(tm);
			int h=ca.get(Calendar.HOUR_OF_DAY);	
			int m=ca.get(Calendar.MINUTE);
			value=(h % 24)*60 + (m % 60);
		}
		setState(st);
		dataId=null;
	}

	public AdTime(Date tm,int st,String id)  {
		if (tm==null)  value=0; 
		else {
			Calendar ca=Calendar.getInstance();
			ca.setTime(tm);
			int h=ca.get(Calendar.HOUR_OF_DAY);	
			int m=ca.get(Calendar.MINUTE);
			value=(h % 24)*60 + (m % 60);
		}
		setState(st);
		dataId=id;
	}

	public static void AdTimeSort(ArrayList<AdTime> adTimes) {
		Collections.sort(adTimes,new  Comparator<AdTime> () 
                  {
                         @Override public int compare(AdTime ad1, AdTime ad2) {
                                        if  (ad1.getValue() == ad2.getValue())
                                      	  return ad1.getState() - ad2.getState();   //  上班排在下班前
                                        else return ad1.getValue() - ad2.getValue();
                          }
                  }
              );
	}
	// 修改未知状态的时间点为上下班数据，规则 ：
	// 第一个未知的数据修改为上班，最后一个未知的数据修改为下班
	// 上班之后的未知数据修改为下班，下班之后的未知数据修改为上班
	// 状态确定的数据不修改
	public static void processAdTimes(ArrayList<AdTime> adTimes) {
		boolean prevIsOff = true; // 第一个修改为上班的判断初始条件
		if (adTimes==null || adTimes.size()==0) return;
		if (adTimes.get(0).getState()==AdTime.workUnknown)  adTimes.get(0).setState(AdTime.workOn);
		if (adTimes.get(adTimes.size()-1).getState()==AdTime.workUnknown)  adTimes.get(adTimes.size()-1).setState(AdTime.workOff);
		for (int i = 0; i < adTimes.size(); i++) {
			if (adTimes.get(i).getState() == AdTime.workUnknown) {
				adTimes.get(i).setState(prevIsOff ? AdTime.workOn : AdTime.workOff);
				prevIsOff = !prevIsOff;
			} else {
				if (prevIsOff && adTimes.get(i).getState() == AdTime.workOff && i> 0 ) {  // 连续两个下班，删除前一个
					adTimes.remove(i-1);
					i--;
				} else if (!prevIsOff && adTimes.get(i).getState() == AdTime.workOn && i > 0) { // 连续两个上班，删除后一个
					adTimes.remove(i);
					i--;
				} else
					prevIsOff = (adTimes.get(i).getState() == AdTime.workOff);
			}
		}
	}
	public static AdTime firstMatchTime (ArrayList<AdTime> adTimes,int state) {
		if (adTimes==null) return null;
		for (int i=0;i<adTimes.size();i++)
			if (adTimes.get(i).getState()==state) return adTimes.get(i);
		return null;
	}
	public static AdTime lastMatchTime (ArrayList<AdTime> adTimes,int state) {
		if (adTimes==null) return null;
		for (int i=adTimes.size()-1;i>=0;i--)
			if (adTimes.get(i).getState()==state) return adTimes.get(i);
		return null;
	}
	public static int firstMatchTimeIndex (ArrayList<AdTime> adTimes,int state) {
		if (adTimes==null) return -1;
		for (int i=0;i<adTimes.size();i++)
			if (adTimes.get(i).getState()==state) return i;
		return -1;
	}
	public static int lastMatchTimeIndex (ArrayList<AdTime> adTimes,int state) {
		if (adTimes==null) return -1;
		for (int i=adTimes.size()-1;i>=0;i--)
			if (adTimes.get(i).getState()==state) return i;
		return -1;
	}		
}