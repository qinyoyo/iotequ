package top.iotequ.attendance.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import top.iotequ.attendance.shifttime.pojo.AdShiftTime;
import top.iotequ.attendance.specialshifttime.pojo.AdSpecialShiftTime;

public class AdTimeRange {
	private String name;
	private AdTime start;
	private AdTime end;
	
	public void setStart(AdTime start) {
		this.start = start;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AdTime getStart() {
		return start;
	}
	public AdTime getEnd() {
		return end;
	}
	public int getMinutes() {
		return end.getValue() - start.getValue();
	}
	public AdTimeRange(AdShiftTime st) {
		start=new AdTime(st.getStartWorkTime(),AdTime.workOn);
		end=	new AdTime(st.getEndWorkTime(),AdTime.workOff);
		name=st.getName();
	}
	public AdTimeRange(AdSpecialShiftTime st) {
		start=new AdTime(st.getStartTime(),AdTime.workOn);
		end=	new AdTime(st.getEndTime(),AdTime.workOff);
		name=st.getName();
	}
	public AdTimeRange(AdTimeRange src) {
		this.start=new AdTime(src.getStart());
		this.end=new AdTime(src.getEnd());
		this.name=src.name;
	}

	public AdTimeRange(AdTime start, AdTime end,String name) {
		if (start.getValue() <= end.getValue()) {
			this.start = start;
			this.end = end;
		} else {
			this.start = end;
			this.end = start;
		}
		this.start.setState(AdTime.workOn);
		this.end.setState(AdTime.workOff);
		this.name=name;
	}

	private void setEnd(AdTime end) {
		this.end = end;
		this.end.setState(AdTime.workOff);
	}

	@Override
	public String toString() {
		return String.format("[%02d:%02d,%02d:%02d]", start.getValue() / 60, start.getValue() % 60,
		            end.getValue() / 60, end.getValue() % 60);
	}
	public static ArrayList<AdTimeRange> cloneList(ArrayList<AdTimeRange> src) {
		if (src==null) return null;
		ArrayList<AdTimeRange> dest=new ArrayList<AdTimeRange>();
		for (AdTimeRange r : src) {
			dest.add(new AdTimeRange(r));
		}
		return dest;
	}
	public static void rangeSort(ArrayList<AdTimeRange> adRanges) { // 排序与去重复
		if (adRanges == null || adRanges.size() == 0)
			return;
		Collections.sort(adRanges, new Comparator<AdTimeRange>() {
			@Override
			public int compare(AdTimeRange ad1, AdTimeRange ad2) {
				if (ad1.getStart().getValue() == ad2.getStart().getValue())
					return ad1.getEnd().getValue() - ad2.getEnd().getValue();
				else
					return ad1.getStart().getValue() - ad2.getStart().getValue();
			}
		});
		int len = adRanges.size();
		for (int i = 1; i < len; i++) {
			if (adRanges.get(i).getStart().getValue() <= adRanges.get(i - 1).getEnd().getValue()) { // 区间重叠，消除
				if (adRanges.get(i - 1).getEnd().getValue() < adRanges.get(i).getEnd().getValue()) // 区间合并，删除
					adRanges.get(i - 1).setEnd(adRanges.get(i).getEnd());
				adRanges.remove(i);
				len--;
				i--;
			}
		}
		for (int i = 0; i < len; i++) {
			if (adRanges.get(i).getStart().getValue() == adRanges.get(i).getEnd().getValue()) { // 删除空区间
				adRanges.remove(i);
				len--;
				i--;
			}
		}
		if (adRanges.size()==0) adRanges=null;
	}
	public static boolean isEmpty(ArrayList<AdTimeRange> source) {
		if (source==null || source.size()==0) return true;
		else return timeLong(source)==0;
	}

	// 从考核范围区间序列减去一个区间，得到新的考核区间序列
	public static ArrayList<AdTimeRange> rangeSubtraction(ArrayList<AdTimeRange> source, AdTimeRange sub) {
		if (source==null) return null;
		ArrayList<AdTimeRange> src=cloneList(source);
		if (sub == null)
			return src;
		rangeSort(src);
		for (int i = 0; i < src.size(); i++) {
			if (src.get(i).getStart().getValue() <= sub.getStart().getValue()
			            && src.get(i).getEnd().getValue() > sub.getStart().getValue()) {
				src.add(i + 1, new AdTimeRange(sub.getStart(), src.get(i).getEnd(),src.get(i).name));
				src.get(i).setEnd(sub.getStart());
				break;
			}
		}
		for (int i = 0; i < src.size(); i++) {
			if (src.get(i).getStart().getValue() <= sub.getEnd().getValue()
			            && src.get(i).getEnd().getValue() > sub.getEnd().getValue()) {
				src.add(i + 1, new AdTimeRange(sub.getEnd(), src.get(i).getEnd(),src.get(i).name));
				src.get(i).setEnd(sub.getEnd());
				break;
			}
		}
		int len = src.size();
		for (int i = 0; i < len; i++) {
			if (src.get(i).getStart().getValue() >= sub.getStart().getValue()
			            && src.get(i).getEnd().getValue() <= sub.getEnd().getValue()) {
				src.remove(i);
				len--;
				i--;
			}
		}
		rangeSort(src);
		return src;
	}

	// 从区间序列减去一个区间，得到新的区间序列
	public static ArrayList<AdTimeRange> subtract(AdTimeRange source, AdTimeRange sub) {
		if (source==null) return null;
		ArrayList<AdTimeRange> srcList=new ArrayList<AdTimeRange>();
		srcList.add(source);
		return rangeSubtraction(srcList,sub);
	}
	// 交叉范围
	public static AdTimeRange intersect(AdTimeRange src, AdTimeRange ins) {
		if (src==null || ins==null) return null;
		int insStart=ins.getStart().getValue();
		int insEnd=ins.getEnd().getValue();
		int start=src.getStart().getValue();
		int end=src.getEnd().getValue();
		if (insStart >=start  && insStart <= end) {
			return new AdTimeRange(new AdTime(insStart, 0), new AdTime(Math.min(end, insEnd), 0),src.name);
		} else if (insEnd >= start   && insEnd <= end) {
			return new AdTimeRange( new AdTime(Math.max(start,start), 0), new AdTime(insEnd, 0),src.name);
		} else if (insStart <=start && insEnd>=end) {
			return new AdTimeRange( new AdTime(start, 0), new AdTime(end, 0),src.name);
		} else return null;

	}

	// 得到 ins 与 src 的交叉区间，不改变src
	public static ArrayList<AdTimeRange> intersect(ArrayList<AdTimeRange> src, AdTimeRange ins) {
		if (src == null || ins == null || src.size() == 0)
			return null;
		ArrayList<AdTimeRange> ret = new ArrayList<AdTimeRange>();
		for (int i = 0; i < src.size(); i++) {
			AdTimeRange t=intersect(src.get(i),ins);
			if (t!=null) ret.add(t);
		}
		rangeSort(ret);
		return ret;
	}

	// range序列时长
	public static int timeLong(ArrayList<AdTimeRange> src) {
		if (src == null)
			return 0;
		int ret = 0;
		for (AdTimeRange rg : src) {
			ret += (rg.getEnd().getValue() - rg.getStart().getValue());
		}
		return ret;
	}
}
