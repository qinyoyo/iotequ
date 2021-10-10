package top.iotequ.attendance.util;

import java.awt.Color;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;

import com.google.gson.annotations.SerializedName;

import top.iotequ.attendance.dayresult.pojo.AdDayResult;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.util.DateUtil;
import top.iotequ.util.OrgUtil;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.Util;

public class AdExcelReport {
	public static String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	public static class DateReprot {
		public Date getAdDate() {
			return adDate;
		}
		public void setAdDate(Date adDate) {
			this.adDate = adDate;
		}
		public int getShiftId() {
			return shiftId;
		}
		public void setShiftId(int shiftId) {
			this.shiftId = shiftId;
		}
		public String getShiftName() {
			return shiftName;
		}
		public void setShiftName(String shiftName) {
			this.shiftName = shiftName;
		}
	    @SerializedName(value = "adDate", alternate = {"ad_date","AD_DATE"})
		Date adDate;
	    @SerializedName(value = "shiftId", alternate = {"shift_id","SHIFT_ID"})
		Integer shiftId;
	    @SerializedName(value = "shiftName", alternate = {"shift_name","SHIFT_NAME"})
		String shiftName;
		String weekName() {
			Calendar ca=Calendar.getInstance();
			ca.setTime(adDate);
			return weeks[ca.get(Calendar.DAY_OF_WEEK) - 1];
		}
	}
	int indexOf(Date dt,int shiftId) {
		String idt= DateUtil.date2String(dt, "yyyyMMdd");
		for (int i=0;i<dateList.size();i++) {
			if (idt.equals(DateUtil.date2String(dateList.get(i).adDate, "yyyyMMdd")) && shiftId==dateList.get(i).shiftId)  return i;
		}
		return -1;
	}
	int dayRows(List<DateReprot> list,Date dt) {
		int i0=-1,i1=-1;
		String idt=DateUtil.date2String(dt, "yyyyMMdd");
		for (int i=0;i<list.size();i++) {
			String idti=DateUtil.date2String(list.get(i).adDate, "yyyyMMdd");
			if (idt.equals(idti)  && i0 == -1)  i0=i;
			if (!idt.equals(idti) && i0 != -1)   i1=i;
			if (i0>-1 && i1>-1) return i1=i0;
		}
		if (i1==-1 && i0>-1) return list.size()-i0;
		else return 0;		
	}
	List<String> shiftStringList() {
		List<String> result=new ArrayList<String>();
		String sql="select distinct shift_name,shift_id from ad_day_result where "
				+orgWhere()+" and (ad_date >=? and ad_date <?)  order by shift_id";
		List<Map<String, Object>> list;
		try {
			list = SqlUtil.sqlQuery(sql,dateStart,dateEnd);
			if (list!=null && list.size()>0) {
				for (Map<String, Object> l:list) {
					result.add(l.get("shift_name").toString());
				}
			}
		} catch (Exception e) {	}

		return result;
	}
	String []  resultList() {
		return AdUtil.stateNameArray();
	}	
	String orgCode;
	String dateStart;
	String dateEnd;  // 不包括
	List<DateReprot>   dateList;
	List<AdDayResult>  rows;
	boolean includeChildrenOrg;
	String orgWhere() {
		String [] ww=orgCode.split(",");
		if (!includeChildrenOrg) {
			if (ww.length>1) return "org_code in ("+orgCode+")";	
			else return "org_code = "+orgCode;	
		}
		List<Integer> list=new ArrayList<Integer>();
		for (String w:ww)  {
				Integer[] oo= OrgUtil.getOrgPrivilegeCodeArray(Integer.parseInt(w));
				if (oo!=null) {
					for (int i : oo) {
						try {
							boolean found=false;
							for (int obj:list) {
								if (obj==i) {
									found=true;
									break;
								}
							}
							if (!found) list.add(i);
						} catch (Exception e) {}
					}
				}
		}
		return SqlUtil.sqlString("org_code = ?", list);	
	}
	public AdExcelReport(String orgCode,int year,int month,boolean includeChildrenOrg) {
		this.orgCode=orgCode;
		dateStart=String.format("%04d-%02d-01", year,month);
		dateEnd=String.format("%04d-%02d-01", month==12?year+1:year,month==12?1:month+1);
		this.includeChildrenOrg=includeChildrenOrg;
		
		String sql="select distinct ad_date,shift_id,shift_name from ad_day_result where "
				+orgWhere()+" and (ad_date >=? and ad_date <?)  order by ad_date,shift_id,shift_name";
		try {
			dateList = SqlUtil.sqlQuery(DateReprot.class, sql, dateStart,dateEnd);
		} catch (Exception e) {	}
		sql="select ad_day_result.* from ad_day_result left join ad_employee on  ad_day_result.employee = ad_employee.id where ad_employee.is_attendance = 1 and ad_day_result."
				+orgWhere()+" and (ad_day_result.ad_date >=? and ad_day_result.ad_date <?) "
				+"order by ad_day_result.org_code,ad_day_result.employee_no,ad_day_result.ad_date,ad_day_result.shift_id,ad_day_result.shift_name,ad_day_result.state";
		try {
			rows=SqlUtil.sqlQuery(AdDayResult.class, sql, dateStart,dateEnd);
		} catch (Exception e) {	}
	}
	public void doReport(HttpServletResponse response) throws IotequException {
		if (rows==null || rows.size()==0 || dateList==null || dateList.size()==0) throw new IotequException(IotequThrowable.NULL_OBJECT,"没有数据可以输出");
		List<Map<String, Object>> orgList=SqlUtil.sqlQuery("select name,parent from sys_org where org_code=?", Integer.parseInt(orgCode.split(",")[0]));
		String fullName="";
		Object parent=(orgList!=null && orgList.size()>0) ? orgList.get(0).get("parent") : null;
		Object  name=(orgList!=null && orgList.size()>0) ? orgList.get(0).get("name") : null;
		if (name!=null) fullName=name.toString();
		while (parent != null ) {
			orgList=SqlUtil.sqlQuery("select name,parent from sys_org where org_code=?", parent);
			parent=(orgList!=null && orgList.size()>0) ? orgList.get(0).get("parent") : null;
			name=(orgList!=null && orgList.size()>0) ? orgList.get(0).get("name") : null;
			if (name!=null) fullName=name.toString()+"-"+fullName;
		} 
		String 	title=fullName+"考勤统计表";
		String fileName="";
		if (dateStart.substring(8).equals("01") && dateEnd.substring(8).equals("01") 
			&& (Integer.parseInt(dateEnd.substring(5, 7))-Integer.parseInt(dateStart.substring(5, 7))==1 )) {
			title += "("+dateStart.substring(0,4)+"年"+dateStart.substring(5,7)+"月)";
			fileName=dateStart.substring(0,7)+".xlsx";
		} else {
			title += "("+dateStart+"至"+dateEnd+")";
			fileName=dateStart+"to"+dateEnd+".xlsx";
		}
		String sheetName=fileName.substring(0,fileName.length()-5);
		
		XSSFWorkbook wb = new XSSFWorkbook();
		List<String> shiftNames=shiftStringList();
		String [] resultNames= resultList();
		int columnTotal = 3 + dateList.size() + shiftNames.size() + resultNames.length;    //  总的列数
		int posHZ=3 + dateList.size(),posXJ=posHZ+shiftNames.size();
		int [] columnWidth = new  int [columnTotal];
		XSSFSheet sheet = wb.createSheet(sheetName);
		for (int i=0;i<columnTotal;i++)  {
//			sheet.autoSizeColumn(i);
			columnWidth[i]=(i==0 ? 4:i==1 || i==2 ? 9 :3);
//			sheet.setColumnWidth(i, columnWidth[i] * 256);
		}
		
		// 标题
		Font titleFont = wb.createFont();
		titleFont.setFontName("simsun");
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLACK.index);
		XSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		titleStyle.setFont(titleFont);
		titleStyle.setBorderTop(BorderStyle.THIN);
		titleStyle.setBorderLeft(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);
		titleStyle.setBorderBottom(BorderStyle.THIN);
		XSSFColor color = new XSSFColor(new Color(0, 0, 0),new DefaultIndexedColorMap());
		titleStyle.setBorderColor(BorderSide.TOP, color);
		titleStyle.setBorderColor(BorderSide.LEFT, color);
		titleStyle.setBorderColor(BorderSide.RIGHT, color);
		titleStyle.setBorderColor(BorderSide.BOTTOM, color);
		
		int rowIndex = 0;
		Row titleRow = sheet.createRow(rowIndex++);
		titleRow.setHeightInPoints(27);
		for (int i = 0; i < columnTotal; i++)
              {
			Cell cell=titleRow.createCell(i);
			if (i==0) {
				cell.setCellValue(title);
				cell.setCellStyle(titleStyle);
			}
              }
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnTotal-1));  // 合并标题栏

		// 表格内容
		Font textFont = wb.createFont();
		textFont.setFontName("simsun");
		textFont.setBold(false);
		textFont.setFontHeightInPoints((short) 9);
		textFont.setColor(IndexedColors.BLACK.index);
		
		XSSFCellStyle textStyle = wb.createCellStyle();
		textStyle.setAlignment(HorizontalAlignment.CENTER);
		textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		textStyle.setFont(textFont);
		textStyle.setBorderTop(BorderStyle.THIN);
		textStyle.setBorderLeft(BorderStyle.THIN);
		textStyle.setBorderRight(BorderStyle.THIN);
		textStyle.setBorderBottom(BorderStyle.THIN);
		textStyle.setBorderColor(BorderSide.TOP, color);
		textStyle.setBorderColor(BorderSide.LEFT, color);
		textStyle.setBorderColor(BorderSide.RIGHT, color);
		textStyle.setBorderColor(BorderSide.BOTTOM, color);

		Row dateRow = sheet.createRow(rowIndex++),weekRow=sheet.createRow(rowIndex++),shiftRow= sheet.createRow(rowIndex++);
		dateRow.setHeightInPoints(17);
		weekRow.setHeightInPoints(17);
		shiftRow.setHeightInPoints(17);
		for (int i = 0; i < columnTotal; i++) {
			Cell celld = dateRow.createCell(i);
			Cell cellw = weekRow.createCell(i);
			Cell cells = shiftRow.createCell(i);
			celld.setCellValue(i==0?"序号":i==1?"部门":i==2?"姓名":"");
			celld.setCellStyle(textStyle);
			cellw.setCellStyle(textStyle);
			cells.setCellStyle(textStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(1, 3, 0, 0));  // 合并序号
		sheet.addMergedRegion(new CellRangeAddress(1, 3, 1, 1));  // 合并部门
		sheet.addMergedRegion(new CellRangeAddress(1, 3, 2, 2));  // 合并姓名
		String dtMerg="";
		int cellMerg=-1;
		for (int i=0;i<dateList.size();i++) {
			shiftRow.getCell(i+3).setCellValue(dateList.get(i).shiftName);
			weekRow.getCell(i+3).setCellValue(dateList.get(i).weekName());
			String dtName=DateUtil.date2String(dateList.get(i).adDate, "M月d");
			dateRow.getCell(i+3).setCellValue(dtName);
			if (cellMerg==-1) {
				dtMerg=dtName;
				cellMerg=i;
			} else if (!dtName.equals(dtMerg)) {
				if(cellMerg < i-1) {
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 3+cellMerg, 3+i-1));  // 合并日期
					sheet.addMergedRegion(new CellRangeAddress(2, 2, 3+cellMerg, 3+i-1));  // 合并星期
				} else if (cellMerg == i-1) {  // 日期只有一列，调宽
					columnWidth[3+i-1]=6;
				}
				dtMerg=dtName;
				cellMerg=i;					
			}
		}
		if (cellMerg>=0 && cellMerg < dateList.size()-1) {
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3+cellMerg, 3+dateList.size()-1));  // 合并日期
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 3+cellMerg, 3+dateList.size()-1));  // 合并星期					
		} else if (cellMerg == dateList.size()-1) {  // 日期只有一列，调宽
			columnWidth[3+cellMerg]=6;
		}
		dateRow.getCell(posHZ).setCellValue("出勤\n汇总");
		if (shiftNames.size()-1>0) sheet.addMergedRegion(new CellRangeAddress(1, 2, posHZ, posHZ+shiftNames.size()-1));  // 合并汇总
		for (int i=0;i<shiftNames.size();i++) {
			shiftRow.getCell(i+posHZ).setCellValue(shiftNames.get(i));
		}
		dateRow.getCell(posXJ).setCellValue("分类\n小计");
		if (resultNames.length-1>0) sheet.addMergedRegion(new CellRangeAddress(1, 2, posXJ, posXJ+resultNames.length-1));  // 合并分类小计
		for (int i=0;i<resultNames.length;i++) {
			shiftRow.getCell(i+posXJ).setCellValue(resultNames[i]);
		}	
		
		
		int [] hz = new int[shiftNames.size()];
		int [] xj = new int[resultNames.length];
		Integer code=null;
		String eid=null;
		int rowBegin=-1;
		Row row = null;
		for (AdDayResult r:rows) {
			if (eid==null) 	eid=r.getEmployee();

			if (!eid.equals(r.getEmployee()) || row==null) {
				if (row!=null) {  //  写入汇总
					for (int i=0;i<hz.length;i++)
						if (hz[i]>0) row.getCell(posHZ+i).setCellValue(hz[i]);
					for (int i=0;i<xj.length;i++)
						if (xj[i]>0) row.getCell(posXJ+i).setCellValue(xj[i]);						
				}
				row = sheet.createRow(rowIndex++);
				for (int i = 0; i < columnTotal; i++) {
					Cell cell = row.createCell(i);
					if (i==0) cell.setCellValue(rowIndex-4);
					cell.setCellStyle(textStyle);
				}			
				for (int i=0;i<hz.length;i++)  hz[i]=0;
				for (int i=0;i<xj.length;i++)    xj[i]=0;	;	
				eid=r.getEmployee();
			}
			row.getCell(1).setCellValue(r.getOrgName());
			row.getCell(2).setCellValue(r.getRealName());
			for (int i=0;i<hz.length;i++) {
				String sid=shiftNames.get(i);
				if (r.getShiftName().equals(sid) && r.getState()==AdUtil.ad_work) {
					hz[i]++;
					break;
				}
			}
			for (int i=0;i<xj.length;i++) {
				String sid=resultNames[i];
				if (r.getStateName().equals(sid)) {
					xj[i]++;
					break;
				}
			}
			int pos=3+indexOf(r.getAdDate(),r.getShiftId());
			if (pos>=3) {
				Cell cell=row.getCell(pos);
				String ov=cell.getStringCellValue();
				if (Util.isEmpty(ov)) cell.setCellValue(r.getStateName());
				else {
					cell.setCellValue(ov+r.getStateName());
					int w=columnWidth[pos];
					if (w<3*(ov+r.getStateName()).length()) columnWidth[pos] = 3*(ov+r.getStateName()).length();
				}
			}
			if (code==null) {
				code=r.getOrgCode();
				rowBegin=rowIndex-1;
			}
			if (!code.equals(r.getOrgCode())) {
				if (rowIndex-2>rowBegin) sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowIndex-2, 1, 1));  // 合并部门
				code=r.getOrgCode();
				rowBegin=rowIndex-1;
			}
		}
		if (row!=null) {  //  写入汇总
			for (int i=0;i<hz.length;i++)
				if (hz[i]>0) row.getCell(posHZ+i).setCellValue(hz[i]);
			for (int i=0;i<xj.length;i++)
				if (xj[i]>0) row.getCell(posXJ+i).setCellValue(xj[i]);						
		}
		if (rowBegin>=0 && rowBegin<rowIndex-1) {
			if (rowIndex-1>rowBegin) sheet.addMergedRegion(new CellRangeAddress(rowBegin, rowIndex-1, 1, 1));  // 合并部门
		}
		sheet.setDefaultRowHeight((short) (2 * 256)); //设置默认行高，表示2个字符的高度
		sheet.setDefaultColumnWidth(18*256);
		for (int i=0;i<columnTotal;i++)  {
			sheet.autoSizeColumn(i);
//			sheet.setDefaultColumnWidth(17);
//			sheet.setColumnWidth(i, columnWidth[i] * 256);
		}
		try {
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
			wb.close();
		} catch (Exception e) {
			throw IotequException.newInstance(e);
		}
	}
}
