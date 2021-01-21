package top.iotequ.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import lombok.NonNull;
import org.springframework.core.env.Environment;

import org.springframework.dao.*;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.bean.SpringContext;
import top.iotequ.framework.pojo.User;
import top.iotequ.framework.service.impl.SqlService;

/***************          数据库相关操作             ************************/

public class SqlUtil extends SqlStringUtil {
	/**
	 * 转换数据库异常报错信息,不再使用，有前端转换
	 * @param e  异常
	 * @return 转换后的异常
	*/
	static public IotequException changeException(DataAccessException e) {
		String cause =  e.getCause() != null ? e.getCause().getMessage() : null;
		if (e instanceof DataIntegrityViolationException) {
			String s=cause.toLowerCase();
			Pattern p = Pattern.compile("duplicate\\s*entry\\s*('[^']*')");
			Matcher m = p.matcher(s);
			if (m.find()) {
				String entry=m.group(1);
				return new IotequException(IotequThrowable.DB_ERROR_DUPLICATE_ENTRY,entry);
			} 
			p = Pattern.compile("column\\s*'([^']*)'\\s*cannot\\s*be\\s*null");
			m = p.matcher(s);
			if (m.find()) {
				return new IotequException(IotequThrowable.DB_ERROR_NULL,m.group(1));
			}
			p = Pattern.compile("too long for column\\s*'([^']*)'");
			m = p.matcher(s);
			if (m.find()) {
				return new IotequException(IotequThrowable.DB_ERROR_TOO_LONG,m.group(1));
			} 
			return new IotequException("DataIntegrityViolationException",e.getMessage());
		} else if (e instanceof TypeMismatchDataAccessException) return new IotequException("TypeMismatchDataAccessException", e.getMessage());
		else if (e instanceof DataAccessResourceFailureException) return new IotequException("DataAccessResourceFailureException", e.getMessage());
		else if (e instanceof DeadlockLoserDataAccessException) return new IotequException("DeadlockLoserDataAccessException", e.getMessage());
		else if (e instanceof OptimisticLockingFailureException) return new IotequException("OptimisticLockingFailureException", e.getMessage());
		else return new IotequException(IotequThrowable.DB_ERROR, e.getClass().getSimpleName()+":"+e.getMessage());
	}
	/**
	 * 用一个request对象的值格式化一个sql语句
	 * @param sql  语句,通过 ${entityName} 来引用 request的属性 entityName。根据clazz的同名字段属性自动加引号以及进行 in 集合转换，缺省为字符串类型
	 * @param clazz  类型参考类
	 * @param request HttpServletRequest对象，从http请求传值
	 * @return 格式替换后的sql语句。
	 */
	public static String sqlStringUseRequest(String sql, Class<?> clazz,HttpServletRequest request) {
		if (sql == null || request==null)	return sql;
		String regex = "\\$\\{([^{}]+?)\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sql);
		int lenChanged=0;
		String nsql=sql;
		int len=sql.length();
		while (matcher.find()) {
			int s=lenChanged+matcher.start(),e=lenChanged+matcher.end();
			Object [] pp = getValueAndQuotationWithSystemParameter(matcher.group(1),null,clazz,request);
			Object v=pp[0];
			char quotation=(Boolean)pp[1] ? '\'' : 0;
			nsql=replaceWithValue(nsql, s, e, v, pp.length>2?pp[2]:null,pp.length>3?pp[3]:null,pp.length>4?pp[4]:null,quotation);
			lenChanged = nsql.length() - len;
		}
		return nsql;
	}

	/**
	 * 用一个实体类对象的值格式化一个sql语句
	 * @param sql  语句,通过 ${entityName} 来引用 pojoObject的属性 entityName。根据类型自动加引号以及进行 in 集合转换
	 * @param pojoObject  实体对象
	 * @return 格式替换后的sql语句。
	 */
	public static String sqlStringUsePojo(String sql, Object pojoObject) {
		if (sql == null)	return sql;
		String regex = "\\$\\{([^{}]+?)\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sql);
		int lenChanged=0;
		String nsql=sql;
		int len=sql.length();
		while (matcher.find()) {
			int s=lenChanged+matcher.start(),e=lenChanged+matcher.end();
			Object [] pp = getValueAndQuotationWithSystemParameter(matcher.group(1),pojoObject,null,null);
			Object v=pp[0];
			char quotation=(Boolean)pp[1] ? '\'' : 0;
			nsql=replaceWithValue(nsql, s, e, v,pp.length>2?pp[2]:null,pp.length>3?pp[3]:null,pp.length>4?pp[4]:null, quotation);
			lenChanged = nsql.length() - len;
		}
		return nsql;
	}

	static public Object[] getValueAndQuotationWithSystemParameter(String fieldName,Object pojoObject,Class<?> clazz,HttpServletRequest request) {
		if (fieldName.toLowerCase().startsWith("system.user.")) return getValueAndQuotation(fieldName.substring(12), Util.getUser(), User.class,null);
		else if (fieldName.equalsIgnoreCase("System.orgListWithSuborg")) return new Object[] { OrgUtil.getOrgAndChildrenOrgList(Util.getUser()==null?null: Util.getUser().getOrgCode()),false };
		else if (fieldName.equalsIgnoreCase("System.orgPrivilegeList")) return new Object[] { OrgUtil.getOrgAndChildrenOrgList(null),false };
		else if (fieldName.equalsIgnoreCase("System.clientIp")) return new Object[] { Util.getIpAddr(null),true };
		else return getValueAndQuotation(fieldName,pojoObject,clazz,request);
	}
	static public Object[] getValueAndQuotation(String fieldName,Object pojoObject,Class<?> clazz,HttpServletRequest request) {
		Object v=null,v0=null,v1=null,v2=null;
		if (Util.isEmpty(fieldName)) return new Object[] {null,false};
		try {
			if (pojoObject!=null) {
				v = ObjectUtil.getPrivateField(pojoObject,fieldName);
				clazz=pojoObject.getClass();
			}
			else if (request!=null) {
				v=request.getParameter(fieldName);
				if (v==null) {
					v0=request.getParameter(fieldName+"_start");
					v1=request.getParameter(fieldName+"_end");
					v2=request.getParameter(fieldName+"_exclusive_end");
				}
			}
		} catch (Exception e1) {}
		Field field;
		Boolean quotation=true;
		try {
			field = clazz.getDeclaredField(fieldName);
			String type=field.getType().getName();
			if (type.endsWith("Boolean")) {
				if (v!=null) 	v=((Boolean)v?"1":"0");
				quotation=false;
			}
			else if (type.endsWith("String") || type.endsWith("Date")) {}
			else if (type.endsWith("Integer") || type.endsWith("Short") || type.endsWith("Long") || type.endsWith("Byte")
					|| type.endsWith("Float") || type.endsWith("Double") || type.endsWith("Decimal"))
				quotation=false;
		} catch (Exception excep) {}
		return new Object[] {v,quotation,v0,v1,v2};
	}


	static public String sqlAddOrgCondition(String sql) {
		int pSelect=wordIndexOf(sql,"select",true,true),
			pFrom=wordIndexOf(sql,"from",true,true),
			pOrgCode =wordIndexOf(sql,"org_code",true,true);
		if (pSelect >=0 && pFrom > pSelect && pOrgCode > pSelect && pOrgCode < pFrom) {
			return sqlAddCondition(sql,OrgUtil.getOrgPrivilege("org_code"));
		} else return sql;
	}
	static public String findInSet(String a,boolean aIsValue,boolean aIsStringType,String b,boolean bIsValue) {
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.findInSet(a,aIsValue,aIsStringType,b,bIsValue);	
	}
	static public String subString(String a,String start,String len) {
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.subString(a,start,len);	
	}


	/**
	 * 执行sql语句
	 * @param sql  sql语句，参数用 ? 代表
	 * @param params  参数
	 * @return 结果为影响的行数
	 * @throws IotequException 异常
	 */
	public static Integer sqlExecute(String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.execute(sql, params);
	}
	/**
	 * 查询sql语句，得到指定类的对象list
	 * @param clazz      对象类
	 * @param filterOrg  是否判断部门权限
	 * @param sql  sql语句，参数用 ? 代表
	 * @param params  参数
	 * @param <T> 泛型类型
	 * @return 对象list
	 * @throws IotequException 异常
	 */
	public static <T> List<T> sqlQuery(Class<T> clazz, boolean filterOrg,String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.query(clazz, filterOrg,sql, params);
	}
	/**
	 * 获得默认的部门权限过滤参数，通过 _condition_filter_org_ 参数控制
	 * @return 是否需要部门权限控制
	 */
	public static boolean getFilterOrg() {
		HttpServletRequest request= Util.getRequest();
		if(request==null) return true;
		boolean filterOrg=true;
		if (request.getAttribute(Util.ORG_FILTER_CONDITION)!=null || request.getParameter(Util.ORG_FILTER_CONDITION)!=null ) {
			if (request.getAttribute(Util.ORG_FILTER_CONDITION)!=null)  filterOrg=(Boolean)request.getAttribute(Util.ORG_FILTER_CONDITION);
			else filterOrg= Util.boolValue(request.getParameter(Util.ORG_FILTER_CONDITION));
		}
		return filterOrg;
	}
	/**
	 * 查询sql语句，得到指定类的对象list。默认判断部门权限
	 * @param clazz      对象类
	 * @param sql  sql语句，参数用 ? 代表
	 * @param params  参数
	 * @param <T> 泛型类型
	 * @return 对象list
	 * @throws IotequException 异常
	 */
	public static <T> List<T> sqlQuery(Class<T> clazz, String sql, Object... params) throws IotequException {
		return sqlQuery(clazz,getFilterOrg(), sql, params);
	}

	/**
	 * 查询sql语句，得到指定类的第一个对象
	 * @param clazz   对象类
	 * @param filterOrg  是否判断部门权限 
	 * @param sql    sql语句，参数用 ? 代表
	 * @param params   参数
	 * @param <T> 泛型类型
	 * @return 第一个对象
	 * @throws IotequException 异常
	 */
	public static <T> T sqlQueryFirst(Class<T> clazz, boolean filterOrg,String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		List<T> list = sqlService.query(clazz, filterOrg,sql, params);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	/**
	 * 查询sql语句，得到指定类的第一个对象。过滤部门权限
	 * @param clazz   对象类
	 * @param sql    sql语句，参数用 ? 代表
	 * @param params   参数
	 * @param <T> 泛型类型
	 * @return 第一个对象
	 * @throws IotequException 异常
	 */
	public static <T> T sqlQueryFirst(Class<T> clazz, String sql, Object... params) throws IotequException {
		return sqlQueryFirst(clazz, getFilterOrg(), sql, params) ;
	}

	/**
	 * 查询sql语句，得到指定类的第一个对象
	 * @param filterOrg  是否判断部门权限 
	 * @param sql    sql语句，参数用 ? 代表
	 * @param params   参数
	 * @return 第一个对象
	 * @throws IotequException 异常
	 */
	public static Map<String,Object> sqlQueryFirst(boolean filterOrg,String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		List<Map<String,Object>> list = sqlService.query(filterOrg,sql, params);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	/**
	 * 查询sql语句，得到指定类的第一个对象。过滤部门权限
	 * @param sql    sql语句，参数用 ? 代表
	 * @param params   参数
	 * @return 第一个对象
	 * @throws IotequException 异常
	 */
	public static Map<String,Object>  sqlQueryFirst(String sql, Object... params) throws IotequException {
		return sqlQueryFirst(getFilterOrg(), sql, params) ;
	}
	
	/**
	 * 查询sql语句，得到一个Map的list
	 * @param filterOrg  是否判断部门权限 
	 * @param sql   sql语句，参数用 ? 代表
	 * @param params   参数
	 * @return Map list
	 * @throws IotequException 异常
	 */
	public static List<Map<String, Object>> sqlQuery(boolean filterOrg,String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.query(filterOrg,sql, params);
	}
	/**
	 * 查询sql语句，得到一个Map的list.判断部门权限 
	 * @param sql   sql语句，参数用 ? 代表
	 * @param params   参数
	 * @return Map list
	 * @throws IotequException 异常
	 */
	public static List<Map<String, Object>> sqlQuery(String sql, Object... params) throws IotequException {
		return sqlQuery(getFilterOrg(),sql,params);
	}

	/**
	 * 使用一个对象传递参数进行查询
	 * @param filterOrg  是否判断部门权限 
	 * @param sql  sql语句，里面包含有 ${字段名}
	 * @param pojoObject 用于替换 ${}的对象
	 * @param params 参数
	 * @return 将sql语句的${}替换为pojoObject对象的属性值，然后进行查询返回查询结果
	 * @throws IotequException 异常
	 */
	public static List<Map<String, Object>> sqlQueryUsePojo(boolean filterOrg,String sql, Object pojoObject,Object... params) throws IotequException {
		if (sql == null || pojoObject==null)	return null;
		sql=sqlStringUsePojo(sql,pojoObject);
		return  sqlQuery(filterOrg, sql,params);
	}

	/**
	 * 查询sql得到一个字段
	 * @param filterOrg  是否判断部门权限 
	 * @param sql sql语句，参数用 ? 代表
	 * @param params  参数
	 * @return 只返回第一行的第一个字段
	 * @throws IotequException 异常
	 */
	public static Object sqlQueryField(boolean filterOrg,String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.queryField(filterOrg,sql, params);
	}
	

	/**
	 * 查询sql得到一个字段.判断部门权限 
	 * @param sql sql语句，参数用 ? 代表
	 * @param params  参数
	 * @return 只返回第一行的第一个字段
	 * @throws IotequException 异常
	 */
	public static Object sqlQueryField(String sql, Object... params) throws IotequException {
		return sqlQueryField(getFilterOrg(),sql, params);
	}
	/**
	 * 查询sql得到一个字符串字段
	 * @param filterOrg  是否判断部门权限 
	 * @param sql sql语句，参数用 ? 代表
	 * @param params    参数
	 * @return 只返回第一行的第一个字段
	 * @throws IotequException 异常
	 */
	public static String sqlQueryString(boolean filterOrg,String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		Object o = sqlService.queryField(filterOrg,sql, params);
		if (o == null)
			return null;
		else
			return o.toString();
	}

	/**
	 * 查询sql得到一个字符串字段.判断部门权限 
	 * @param sql sql语句，参数用 ? 代表
	 * @param params    参数
	 * @return 只返回第一行的第一个字段
	 * @throws IotequException 异常
	 */
	public static String sqlQueryString(String sql, Object... params) throws IotequException {
		return sqlQueryString(getFilterOrg(), sql,params) ;
	}
	
	/**
	 * 查询sql得到一个整数字段
	 * @param filterOrg  是否判断部门权限 
	 * @param sql  sql语句，参数用 ? 代表
	 * @param params   参数
	 * @return 只返回第一行的第一个字段
	 * @throws IotequException 异常
	 */
	public static Integer sqlQueryInteger(boolean filterOrg,String sql, Object... params) throws IotequException {
		if (sql == null)
			return null;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		Object o = sqlService.queryField(filterOrg,sql, params);
		if (o == null)
			return null;
		else if (o instanceof Boolean) return ((Boolean) o) ? 1 : 0;
		else return Integer.parseInt(o.toString());
	}
	/**
	 * 获得记录行号
	 * @param table  表
	 * @param pkField 主键
	 * @param id 主键值
	 * @return 行号
	 */
	public static int getRowCount(@NonNull String table,@NonNull String pkField,Object  id) {
		if (id==null) return 0;
		else {
			SqlService sqlService = SpringContext.getBean(SqlService.class);
			return sqlService.getRowCount(table, pkField, id);
		}
	}
	/**
	 * 查询sql得到一个整数字段.判断部门权限 
	 * @param sql  sql语句，参数用 ? 代表
	 * @param params   参数
	 * @return 只返回第一行的第一个字段
	 * @throws IotequException 异常
	 */
	public static Integer sqlQueryInteger(String sql, Object... params) throws IotequException {
		return sqlQueryInteger(getFilterOrg(),sql,params);
	}
	/**
	 * 判断sql是否存在结果集
	 * @param filterOrg  是否判断部门权限 
	 * @param sql  sql语句，参数用 ? 代表
	 * @param params     参数
	 * @return 是否存在结果集
	 */
	public static boolean sqlExist(boolean filterOrg,String sql, Object... params) {
		if (sql == null)
			return false;
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.exist(filterOrg,sql, params);
	}
	/**
	 * 判断sql是否存在结果集.判断部门权限 
	 * @param sql  sql语句，参数用 ? 代表
	 * @param params     参数
	 * @return 是否存在结果集
	 */
	public static boolean sqlExist(String sql, Object... params) {
		return sqlExist(getFilterOrg(),sql, params);
	}	

	/**
	 * 检查表的licence
	 * @param table   表名
	 * @param pkField 主键字段
	 * @param licence licence值
	 * @return 剩余licence数 
	 */
	static public int checkTableLicenceLeft(@NonNull String table,@NonNull String pkField,int licence) {
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.checkTableLicenceLeft(table,pkField,licence);
	}
	static public String licenceCondition(@NonNull String table,@NonNull String pkField,int licence) {
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.licenceCondition(table,pkField,licence);
	}
	static public Object maxIdInLicence(@NonNull String table,@NonNull String pkField,int licence) {
		SqlService sqlService = SpringContext.getBean(SqlService.class);
		return sqlService.maxIdInLicence(table,pkField,licence);
	}
	static public Map<String,String> getDatabaseSetting(Environment env) throws IotequException {
		Map<String,String> map=new HashMap<>();
		String datasourceUrl= env.getProperty("spring.datasource.url");  
		if(datasourceUrl==null) throw new IotequException (IotequThrowable.CONFIG_ERROR,"数据库配置有错: spring.datasource.url") {
			private static final long serialVersionUID = 1L;} ;
		String s=env.getProperty("spring.datasource.username");
		if (s==null) throw new IotequException (IotequThrowable.CONFIG_ERROR,"数据库配置有错: spring.datasource.username") {
			private static final long serialVersionUID = 1L;} ;
		map.put("user",s);
		s=env.getProperty("spring.datasource.password");
		if (s==null) map.put("password","");
		else map.put("password",s);
		if (datasourceUrl.toLowerCase().contains("jdbc:mysql")) {
			map.put("databaseId","MySql");
			datasourceUrl=datasourceUrl.split("[?]")[0];
			map.put("database",datasourceUrl.substring(datasourceUrl.lastIndexOf("/")+1));
			String addr=datasourceUrl.substring(datasourceUrl.indexOf("//")+2,datasourceUrl.lastIndexOf("/"));
			String [] ap=addr.split(":");
			map.put("host", ap[0]);
			map.put("port",ap.length==2 ? ap[1] : "3306");
		} else if (datasourceUrl.toLowerCase().contains("jdbc:sqlserver")) {
			map.put("databaseId","SQLServer");
			int pos=datasourceUrl.toLowerCase().indexOf("databasename=");
			int pos1=datasourceUrl.toLowerCase().indexOf(";",pos);
			if (pos1>0) map.put("database",datasourceUrl.substring(pos+13, pos1));
			else map.put("database",datasourceUrl.substring(pos+13));
			String addr=datasourceUrl.substring(datasourceUrl.indexOf("//")+2,datasourceUrl.lastIndexOf(";"));
			String [] ap=addr.split(":");
			map.put("host", ap[0]);
			map.put("port",ap.length==2 ? ap[1] : "1433");		
		} else if (datasourceUrl.toLowerCase().contains("jdbc:oracle")) {
			//jdbc:oracle:thin:@127.0.0.1:1521:iotequ
			map.put("databaseId","Oracle");
			int pos=datasourceUrl.toLowerCase().indexOf("@");
			if (pos<0)  throw new IotequException (IotequThrowable.CONFIG_ERROR,"数据库配置有错: spring.datasource.url") {
				private static final long serialVersionUID = 1L;} ;
			String [] ap=datasourceUrl.substring(pos).split(":");
			map.put("host", ap[0]);
			map.put("port",ap.length>2 ? ap[1] : "1521");	
			map.put("database",ap.length>2?ap[2]:ap[1]);
		}   
		return map;
	}
}
