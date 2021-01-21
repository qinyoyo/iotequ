package top.iotequ.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

/***************          Sql语句相关操作             ************************/

public class SqlStringUtil {
	/**
	 * sql写法
	 * @param obj
	 * @return 对象在sql语句里面的写法
	 */
	private static String sqlValue(Object obj) {
		if (obj == null)
			return "null";
		else if (obj instanceof Integer || obj instanceof Long || obj instanceof Short || obj instanceof Double
				|| obj instanceof Float)
			return obj.toString();
		else if (obj instanceof Boolean)
			return (Boolean) obj ? "1" : "0";
		else if (obj instanceof Date)
			return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) obj) + "'";
		else if (obj.getClass().isArray()) {
			Object[] a = (Object[]) obj;
			String s = "(";
			boolean first = true;
			for (Object o : a) {
				if (first) {
					s += sqlValue(o);
					first = false;
				} else
					s += "," + sqlValue(o);
			}
			s += ")";
			return s;
		} else if (obj instanceof Collection) {
			String s = "(";
			boolean first = true;
			for (Object o : (Collection<?>) obj) {
				if (first) {
					s += sqlValue(o);
					first = false;
				} else
					s += "," + sqlValue(o);
			}
			s += ")";
			return s;
		} else if (obj instanceof String && ((String)obj).split(",").length>1) {
			String[] a = ((String)obj).split(",");
			String s = "(";
			boolean first = true;
			for (String o : a) {
				if (first) {
					s += sqlValue(o);
					first = false;
				} else
					s += "," + sqlValue(o);
			}
			s += ")";
			return s;
		} else {
			String s = obj.toString();
			if ((s.startsWith("'") && s.endsWith("'")) || (s.startsWith("\"") && s.endsWith("\""))) {
				s=s.substring(1, s.length()-2).replace("'", "\\'");
				return s;
			} else {
				s=s.replace("'", "\\'");
			}
			return "'" + s + "'";
		}
	}

	/**
	 * 将逗号分隔的字符串序列修改为（）集合串
	 * @param set	逗号分隔的序列串或Array或List
	 * @param isStringSet	是否字符序列，字符序列需要加引号
	 * @return	  转换后的()集合字符串
	 */
	public static String sqlSet(Object set,boolean isStringSet) {
		if (Utils.isEmpty(set)) return "()";
		StringBuilder sb=new StringBuilder();
		sb.append("(");
		String YH=(isStringSet?"'":"");
		if (set.getClass().isArray() || set instanceof Collection ) {
			Object[] oo=(set.getClass().isArray()? (Object[]) set  : ((Collection<?>) set).toArray());
			for (int i=0;i<oo.length;i++) {
				if (i>0) sb.append(",");
				sb.append(YH+oo[i].toString()+YH);
			}
		} else if (isStringSet) {
				String [] oo=set.toString().split(",");
				for (int i=0;i<oo.length;i++) {
					if (i>0) sb.append(",");
					sb.append(YH+oo[i].toString()+YH);
				}
		} else {
				sb.append(set.toString());
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 将sql语句中的?用参数替换 。
	 * @param sql  sql语句
	 * @param params 参数。
	 * @return 替换后的sql语句，如果参数为逗号分隔的序列，将转换为集合。并且将 =改为 in ,!=改为 not in 。?必须紧跟在=或!=之后
	 */
	static public String sqlString(String sql, Object... params) {
		if (params == null || params.length == 0)
			return sql;
		else if (sql != null) {
			int pos = sql.indexOf("?");
			if (pos<0) return sql;
			StringBuffer result = new StringBuffer(sql);
			if (result.indexOf("?",pos+1)>=0) {  // 多个参数
				for (Object obj : params) {
					replaceOneParameter(result, obj);
				}
			} else {
				if (params.length==1) replaceOneParameter(result, params[0]);  // 单个参数，如果为数组等，params长度可能 >1
				else if (params.length>1) replaceOneParameter(result, params);
				else replaceOneParameter(result, null);
			}
			String r=result.toString();
			return r;
		} else
			return null;
	}

	/**
	 * 修改 sql语句的 字段列表，不支持嵌套的sql语句
	 * @param sql 原sql语句
	 * @param newFields 替换的型字段列表
	 * @return 修改后的sql语句
	 */
	static public String sqlReplaceSelectFields(String sql,String newFields ) {
		int pSelect=wordIndexOf(sql,"select",true,true),
		    pFrom=wordIndexOf(sql,"from",true,true);
		if (pSelect >=0 && pFrom > pSelect) {
			return sql.substring(0,pSelect+6) + " " + newFields + " " + sql.substring(pFrom);
		} else return sql;
	}


	/**
	 * 给sql语句添加一个条件
	 * @param sql  sql语句(不支持where条件含有order having group子句的子查询)
	 * @param condition 需要添加的条件
	 * @return 新的sql语句
	 */
	static public String sqlAddCondition(String sql,String condition) {
		if (Utils.isEmpty(condition) || Utils.isEmpty(sql)) return sql;
		int pWhere=sqlConditionStart(sql,"where",true,true),
			 pGroup=sqlConditionStart(sql,"group",false,true),
			 pHaving=sqlConditionStart(sql,"having",false,true),
			 pOrder=sqlConditionStart(sql,"order",false,true);
		int pEnd=(pGroup==-1? ( pHaving==-1? pOrder : pHaving ) : pGroup);
		if (pWhere==-1) {
			if (pEnd==-1) return sql + " WHERE " + condition;
			else return sql.substring(0, pEnd) + " WHERE " + condition +" " + sql.substring(pEnd);
		} else {
			String where =  (pEnd==-1? sql.substring(pWhere+5) : sql.substring(pWhere+5,pEnd)) ;
			where = " ("+where.trim()+") AND ("+condition + ") ";
			return sql.substring(0,pWhere+5) +where + (pEnd==-1?"" : sql.substring(pEnd));
		}
	}
	/**
	 * 给sql语句添加一个排序条件
	 * @param sql  sql语句(不支持where条件含有order子句的子查询)
	 * @param order 需要添加的条件
	 * @return 新的sql语句
	 */
	static public String sqlAddOrderCondition(String sql,String order) {
		if (Utils.isEmpty(order) || Utils.isEmpty(sql)) return sql;
		int pOrder=wordIndexOf(sql,"order",false,true);
		if (pOrder==-1) {
			return sql + " ORDER BY " + order;
		} else {
			String right=sql.substring(pOrder);
			 String reg="(?!)order\\s*by";
			 Pattern p = Pattern.compile(reg);
		     Matcher m = p.matcher(right);
		     if(m.find()) {
		    	 right=right.substring(m.end());
		      }
			return sql + " ORDER BY " + order + ","+right;
		}
	}
	/**
	 * 指定位置替换
	 * @param sql SQL语句
	 * @param s 开始位置
	 * @param e 结束为止
	 * @param v 值,=null时判断 v0,v1,v2
	 * @param v0 取值区间start值
	 * @param v1 取值区间end 值（包含）
	 * @param v2 取值区间不包含的 end 值
	 * @param quotation 引号字符，=0时不需要引号
	 * @return 替换后的sql字符串
	 */
	public static String replaceWithValue(String sql,int s,int e,Object v,Object v0,Object v1,Object v2,char quotation) {
		if (v==null) {
			if (v0==null && v1==null && v2==null)
				sql = sql.substring(0, s) + "null" + sql.substring(e);
			else if (v0!=null && v1!=null){
				String left=sql.substring(0, s );
				String newLeft=replaceEndEqualWith(left,"BETWEEN");
				sql = newLeft + (quotation==0?"":String.valueOf(quotation)) + v0.toString() + (quotation==0?"":String.valueOf(quotation))+ " AND "
						+ (quotation==0?"":String.valueOf(quotation)) + v1.toString() + (quotation==0?"":String.valueOf(quotation)) + sql.substring(e );		
			} else {
				String left=sql.substring(0, s );
				Pattern p=Pattern.compile("[\\s\\(\\)]([^\\s\\(\\)]+)\\s*(\\<\\>|\\!=)\\s*$");
				Matcher m=p.matcher(left);
				StringBuilder sb=new StringBuilder();
				if (m.find()) {
					String field = m.group(1);
					if (v0!=null)  sb=sb.append(field).append(" < ").append(quotation==0?"":String.valueOf(quotation)).append(v0.toString()).append(quotation==0?"":String.valueOf(quotation));
					if (v1!=null)  sb=sb.append(sb.length()>0?" OR ":"").append(field).append(" > ").append(quotation==0?"":String.valueOf(quotation)).append(v1.toString()).append(quotation==0?"":String.valueOf(quotation));
					if (v2!=null)  sb=sb.append(sb.length()>0?" OR ":"").append(field).append(" >= ").append(quotation==0?"":String.valueOf(quotation)).append(v2.toString()).append(quotation==0?"":String.valueOf(quotation));
					int sp=m.start();
					if (m.group(0).charAt(0)=='(' || m.group(0).charAt(0)==')' ) sp++;
					left = left.substring(0,sp) + " (" + sb.toString() +") ";
				} else {
					p=Pattern.compile("[\\s\\(\\)]([^\\s\\(\\)]+)\\s*(=)\\s*$");
					m=p.matcher(left);
					if (m.find()) {
						String field = m.group(1);
						if (v0!=null)  sb=sb.append(field).append(" >= ").append(quotation==0?"":String.valueOf(quotation)).append(v0.toString()).append(quotation==0?"":String.valueOf(quotation));
						if (v1!=null)  sb=sb.append(sb.length()>0?" AND ":"").append(field).append(" <= ").append(quotation==0?"":String.valueOf(quotation)).append(v1.toString()).append(quotation==0?"":String.valueOf(quotation));
						if (v2!=null)  sb=sb.append(sb.length()>0?" AND ":"").append(field).append(" < ").append(quotation==0?"":String.valueOf(quotation)).append(v2.toString()).append(quotation==0?"":String.valueOf(quotation));						
						int sp=m.start();
						if (m.group(0).charAt(0)=='(' || m.group(0).charAt(0)==')' ) sp++;
						left = left.substring(0,sp) + " (" + sb.toString() +") ";
					}
				}
				sql = left + sql.substring(e );
			}
		} else if (v.toString().isEmpty()) {
			sql = sql.substring(0, s) +  (quotation==0?"null":String.valueOf(quotation)) +  (quotation==0?"":String.valueOf(quotation)) + sql.substring(e);						
		} else if (v.getClass().isArray() ||  v instanceof List<?> || v.toString().contains(",")) {
			String left=sql.substring(0, s );
			String newLeft=replaceEndEqualWith(left,"IN");
			if (getMatched("\\s+(?i)in\\s*$", newLeft).size()>0) {
				String set=sqlSet(v,quotation!=0);	
				sql=newLeft + set + sql.substring(e);
			} else sql = sql.substring(0, s ) + (quotation==0?"":String.valueOf(quotation)) + v.toString() + (quotation==0?"":String.valueOf(quotation))+ sql.substring(e );			
		} else {
			sql = sql.substring(0, s ) + (quotation==0?"":String.valueOf(quotation)) + v.toString() + (quotation==0?"":String.valueOf(quotation))+ sql.substring(e );
		}
		return sql;
	}
	/**
	 * 查找第一个匹配的单词，不包括引号内的。用于sql语法分析
	 * @param str   字符串
	 * @param word 需要查找的单词
	 * @param findFirstMatched 返回第一个匹配的，否则返回最后一个匹配的
	 * @param ignoreCase 忽略大小写
	 * @return 找到的位置，未找到返回-1
	 */
	protected static int wordIndexOf(String str, String word, boolean findFirstMatched, boolean ignoreCase) {
		final String regSY = "\\\"(\\\\.|[^\\\\\"])*\\\"";  // 匹配双引号内容
		final String regDY = "\\\'(\\\\.|[^\\\\\"])*\\\'";  //  匹配单引号内容
		final String reg="\\b"+(ignoreCase?"(?i)":"")+word+"\\b";
		List<Integer> inStr=getMatched(regSY,str);
		inStr.addAll(getMatched(regDY,str));
		List<Integer> matched=getMatched(reg,str);
		if (matched.size()>=2) {
			int total = matched.size()/2;
			for (int i=(findFirstMatched?0 : total-1) ; (findFirstMatched && i<total) || (!findFirstMatched && i>=0); i += (findFirstMatched?1:-1)) {
				int s=matched.get(2*i),e=matched.get(2*i+1);
				boolean inYH=false;
				for (int j=0;j<inStr.size()/2;j+=2) {
					if ((s>=inStr.get(j) && s<inStr.get(j+1)) || (e>=inStr.get(j) && e<inStr.get(j+1))) {
						inYH=true;
						break;
					}
				}
				if (!inYH) return s;
			}
		}
		return-1;
	}
	private static void replaceOneParameter(StringBuffer result,Object obj) {
		int pos = result.indexOf("?");
		if (pos >= 0) {
			if (obj == null) {
				result.replace(pos, pos + 1, "null");
			} else if (obj.getClass().isArray() || obj instanceof Collection || (obj instanceof String && ((String)obj).split(",").length>1) ) { // 序列将=或 != 换成 in 或 not in
				String s = sqlValue(obj);
				result.replace(pos, pos + 1, s);
				String left = result.substring(0, pos);
				String newLeft=replaceEndEqualWith(left,"IN");
				if (!left.equals(newLeft)) {
					result.replace(0, pos, newLeft);
				}
			} else {
				String s = sqlValue(obj);
				result.replace(pos, pos + 1, s);
			}
		}
	}
	static private int sqlConditionStart(String sql,String word,boolean findFirstMatched,boolean ignoreCase) {
		int p=0,len=sql.length();
		while (p<len) {
			String sub = sql.substring(p);
			int pos = wordIndexOf(sub,word,findFirstMatched,ignoreCase);
			if (pos<0) return pos;
			pos += p;
			int kh=0;
			int i=0;
			while (i<pos) {
				char ch = sql.charAt(i);
				if (ch == '(') kh++;
				else if (ch == ')') kh--;
				i++;
			}
			if (kh==0) return pos;
			p=pos + word.length();
		}
		return -1;
	}
	static private List<Integer> getMatched(String reg,String s) {
		List<Integer> inStr=new ArrayList<Integer>();
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(s); // 获取 matcher 对象
		while(m.find()) {
			inStr.add(m.start());
			inStr.add(m.end());
		}
		return inStr;
	}
	private static String replaceEndEqualWith(String exp,String inOrLike) {
		if (inOrLike==null) inOrLike="IN";
		exp=exp.replaceFirst("\\s*(\\<\\>|\\!=)\\s*$", " NOT "+inOrLike+" ");
		exp=exp.replaceFirst("\\s*=\\s*$", " "+inOrLike+" ");
		return exp;
	}

}
