package top.iotequ.svasclient;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.google.gson.Gson;

import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.security.oauth2.OAuth2Util;
import top.iotequ.framework.util.*;
import top.iotequ.svasclient.SvasTypes.*;
import top.iotequ.svasclient.db.dao.SvasUserNoDao;
import top.iotequ.svasclient.db.pojo.SvasUserNo;

/**
 * 这是调用svas服务的唯一入口<br>
 * 非bean类，如果通过autowired注入，请使用svasService<br>
 * @author Qinyoyo
 * @version 1.0
 */
@SuppressWarnings("unchecked")
class SvasClient  {
		static public final int      ALLOW_ALL = -1;
		static public final String SVAS = "svas/";
		static public final String getUserNo = "getUserNo";
	    static public final String getUserNoFromDict = "getUserNoFromDict";
	    static public final String setUserNoForDict = "setUserNoForDict";
		static public final String getUserInfo = "getUserInfo";
		static public final String getUserAllInfo = "getUserAllInfo";
		static public final String changeUserInfo = "changeUserInfo";
		static public final String changeUserInfoBy = "changeUserInfoBy";
		static public final String removeUserNo = "removeUserNo";
		static public final String getTemplates = "getTemplates";
		static public final String getFingerCount = "getFingerCount";
		static public final String getFingerInfo = "getFingerInfo";
		static public final String remove = "remove";
		static public final String update = "update";
	    static public final String set = "set";
		static public final String add = "add";
		static public final String auth = "auth";
		static public final String env = "env";
		static public final String PHOTO = "photo";
		static public final String version = "version";
		static public final String licence = "licence";
		static public final String trialDays = "trialDays";
		static public final int trialLicence = 100;

		private String svasUrl=null;
		private Object svasServer = null;
		private int maxLicence = 0 ;
		private SvasUserNoDao svasUserNoDao = null;
		private String clientId="svas";
		private String clientSecret="e10adc3949ba59abbe56e057f20f883e";
		private String scope="api";
		private OAuth2AccessToken oauth2Tokenoken=null;
		public void setLicence(int li) { maxLicence = li ; }
		public void setSvasUserNoDao(SvasUserNoDao dao) { svasUserNoDao = dao;}
		static public String db_prefix="";
		static public int min_length=6;
		static public String fill_digit="0";
		TimerTask timerTask=null;
		private OAuth2AccessToken getToken() throws IotequException {
			if (oauth2Tokenoken==null) {
				synchronized(this) {
					if (oauth2Tokenoken!=null) return oauth2Tokenoken;
					String s = HttpUtils.getHttpString(HttpUtils.doPost(OAuth2Util.getClientCredentialsTokenUrl(svasUrl, clientId, clientSecret, scope)));
					oauth2Tokenoken=OAuth2Util.getOAuth2AccessToken(s);
					if (oauth2Tokenoken!=null && oauth2Tokenoken.getExpiresIn()>0) {
							if (timerTask!=null) timerTask.cancel();
							timerTask=new TimerTask() {
								@Override
								public void run() {
									oauth2Tokenoken=null;
									try {
										getToken();
									} catch (Exception e) {
									}
								}									
							};
							new Timer().schedule(timerTask, oauth2Tokenoken.getExpiration().getTime());
					} else {
						System.out.println("*******************************************  getToken failure **************************************");
						System.out.println(s.toString());
					}
					return oauth2Tokenoken;
				}
			}
			return oauth2Tokenoken;
		}

		/**
		 * 获取剩余licence,仅用于本地数据库模式
		 * @return  剩余的licence许可
		 */
		private  int getLicenceLeft() {
			int left = -1;
			if (maxLicence > 0 ) {
				left = SqlUtil.checkTableLicenceLeft("dev_user_no", "id", maxLicence);
			}
			return left;
		}

		private  int MAXID = -2; //  -2 ： 需要重新计算 ,-1 : 所有id均可用， 0 ：所有id 不可用 >0 : <=MAXID的id可用
		/**
		 * 获得最大的max id，仅用于本地数据库模式
		 * @return 最大的id  -1 : 所有id均可用， 0 ：所有id 不可用 >0 : <=MAXID的id可用
		 */
		private int getMaxIdFromLicence() {
			if (true) {   // if (MAXID == -2) {
				if (maxLicence == 0) MAXID = 0;
				else {
					int licenceLeft = getLicenceLeft();;
					if (licenceLeft >= 0) {
						MAXID = ALLOW_ALL;
					}
					else {
						Object maxid=SqlUtil.maxIdInLicence("dev_user_no", "id", maxLicence);
						if (maxid!=null) MAXID=Integer.parseInt(maxid.toString());
					}
				}
			}
			return MAXID;
		}

		/**
		 * 判断 userNo的licence，仅用于本地数据库模式
		 * @param userNo  用户编号
		 * @return 返回0
		 * @throws IotequException 失败返回异常
		 */
		private int checkUserLicence(String userNo) throws IotequException {
			int maxId = getMaxIdFromLicence();
			if (maxId == ALLOW_ALL ) return SvasException.SUCCESS;
			else if (maxId == 0)  throw new SvasException(SvasException.ERR_LICENCE_OUT);
			String sql="select id from dev_user_no where user_no=?"; 
			try {
				int id=SqlUtil.sqlQueryInteger(false, sql, userNo);
				if (id <= maxId) return SvasException.SUCCESS;
				else  throw new SvasException(SvasException.ERR_LICENCE_OUT);
			} catch (IotequException e) {
				throw new SvasException(SvasException.ERR_NOT_FOUND);
			}
		}
		

		private void init(Object server,String url,String prefix,String clientId,String clientSecret,String scope)  {
			if (server!=null && server.getClass().getName().equals("top.iotequ.svas.service.SvasServer")) {
				svasServer=server;
				svasUrl = null;
			} else {
				svasServer=null;
				svasUrl = url;				
			}
			if (svasUrl!=null) {
				if (!Util.isEmpty(clientId))  this.clientId=clientId;
				if (!Util.isEmpty(clientSecret))  this.clientSecret=clientSecret;
				if (!Util.isEmpty(scope))  this.scope=scope;
				svasUrl=svasUrl.trim();
				if (svasUrl.isEmpty()) svasUrl=null;
				else if (!svasUrl.endsWith("/")) svasUrl = svasUrl+"/";
			} 
			if (prefix!=null) db_prefix=prefix;
			if (svasServer==null && svasUrl==null) {
				List<Map<String, Object>> pp=null;
				try {
					pp = SqlUtil.sqlQuery("select * from dev_user_no_params");
				} catch (IotequException e) {}
				if (pp==null || pp.size()==0) return ;
				db_prefix=pp.get(0).get("prefix").toString();
				min_length=Integer.parseInt(pp.get(0).get("min_length").toString());
				fill_digit=pp.get(0).get("fill_digit").toString();
			}
		}
		/**
		 * 构造函数。server优先于url。<br>
		 * 如果server，url均无效，使用本地数据库服务（离线认证）
		 * @param server	非空SvasServer对象有效，其他则判断url
		 * @param url	指定远程的svas服务器地址，server无效起作用。如 http://www.iotequ.top:81
		 * @param prefix  默认的userNo前缀
		 * @param clientId 获得token的clientid
		 * @param clientSecret 获得token的clientSecret
		 * @param scope scope，默认 api
		 */
		public SvasClient(Object server,String url,String prefix,String clientId,String clientSecret,String scope)  {
			init(server,url,prefix,clientId,clientSecret,scope);
		}
		
		public SvasClient(String url,String prefix,String clientId,String clientSecret,String scope)  {
			init(null,url,prefix, clientId, clientSecret, scope);
		}
		public SvasClient(String url,String clientId,String clientSecret,String scope)  {
			init(null,url,"",clientId, clientSecret, scope);
		}
		private <T> T getFromHttp(String url,Map<String, Object> parameters, Class<T> clazz) throws IotequException {
			Map<String, Object> headers=OAuth2Util.getTokenHeader(getToken());
			Map<String, Object> req=new HashMap<>();
			String s = HttpUtils.getHttpString(HttpUtils.doPost(url, headers, req, parameters));
			if (clazz.equals(String.class)) return (T)s;
			if (Util.isEmpty(s)) throw new IotequException(IotequThrowable.NO_ANSWER,"没有获得结果");
			Gson gson = Util.getGson(); 
			SvasErrorInfo e=gson.fromJson(s, SvasErrorInfo.class);
			if (e!=null) {
				if (e.success) {
					if (clazz.equals(Boolean.class)) {
						Boolean b=true;
						return (T) b;
					}
					if (clazz.equals(Map.class)) return (T) Util.mapFromJson(s);
					else {
						T r = gson.fromJson(s, clazz);
						return r;
					}
				} else {
					if (e.error!=0) throw new SvasException(e.error);
					else if (e.message!=null) throw new IotequException(IotequThrowable.ERROR,e.message);
				}
			} 
			throw new IotequException(IotequThrowable.UNKNOWN,"未知的错误");
		}
		private <T> T getFromMap(Map<String, Object> r, Class<T> clazz) throws IotequException {
			if (r==null) return null;
			SvasErrorInfo e= EntityUtil.entityFromMap(r, SvasErrorInfo.class);
			if (e!=null) {
				if (r.get("success")==null) { // 从数据库读取的数据没有定义success，
					e.success=true;
				}
				if (e.success) {
					if (clazz.equals(Boolean.class)) {
						Boolean b=true;
						return (T) b;
					}
					T t = EntityUtil.entityFromMap(r,clazz);
					return t;
				} else {
					if (e.error!=0) throw new SvasException(e.error);
					else if (e.message!=null) throw new IotequException(IotequThrowable.ERROR,e.message);
				}
			} 
			throw new IotequException(IotequThrowable.UNKNOWN,"未知的错误");
		}
		/**
		 * 获得svas版本号
		 * @return 版本号
		 * @throws IotequException 异常
		 */
		public String getVersion() throws IotequException {
			if (svasServer!=null) {
				Object o=EntityUtil.runMethod(svasServer, "svein_getVersion");
				if (o!=null) {		
					Map<String, Object> map=(Map<String, Object>)o;
					Object v=map.get("version");
					if(v!=null) return v.toString();
				} 
				return "3.0.0";
			}
			else if (svasUrl==null ) {
				return "3.0.0";
			} else {
				String url=svasUrl+SVAS+version;
				Map<String, Object> req= new HashMap<String, Object>();
				String s = getFromHttp(url,req,String.class);	
				if (Util.isEmpty(s)) throw new IotequException(IotequThrowable.NO_ANSWER,"没有获得结果");
				Gson gson =Util.getGson(); 
				Map<String,Object> r=gson.fromJson(s.trim(), Map.class);
				if (r!=null) {
					Object v=r.get("version");
					if(v!=null) return v.toString();
				}
				return "3.0.0";
			}
		}
		/**
		 * 获得授权的licence数
		 * @return licence。licence表示做多可以支持的指静脉模板数量
		 * @throws IotequException 异常
		 */
		public int getLicence() throws IotequException {
			if (svasServer!=null) {
				Object o=EntityUtil.runMethod(svasServer, "svein_getLicence");
				if (o!=null) {		
					Map<String, Object> map=(Map<String, Object>)o;
					Object v=map.get("licence");
					if(v!=null) return Integer.parseInt(v.toString());
				} 
				return 0;
			}
			else if (svasUrl==null ) {
				Environment env=Util.getBean(Environment.class);
				String sn=env.getProperty("svas.sn");
				if (sn!=null && !sn.isEmpty()) {
					int licence = Util.getLicence(sn);
					if (licence>0) return licence;
				}
				return trialLicence;
			} else {
				String url=svasUrl+SVAS+licence;
				Map<String, Object> req= new HashMap<String, Object>();
				String s = getFromHttp(url,req,String.class);	
				if (Util.isEmpty(s)) throw new IotequException(IotequThrowable.NO_ANSWER,"没有获得结果");
				Gson gson = Util.getGson(); 
				Map<String,Object> r=gson.fromJson(s, Map.class);
				if (r!=null) {
					Object v=r.get("licence");
					if(v!=null) return Integer.parseInt(v.toString());
				}
				return 0;
			}
		}

	/**
	 * 获得授权的licence数
	 * @return licence。licence表示做多可以支持的指静脉模板数量
	 * @throws IotequException 异常
	 */
	public int getLicenceAvailable() throws IotequException {
		if (svasServer!=null) {
			Object o=EntityUtil.runMethod(svasServer, "svein_getLicenceAvailable");
			if (o!=null) {
				Map<String, Object> map=(Map<String, Object>)o;
				Object v=map.get("licence");
				if(v!=null) return Integer.parseInt(v.toString());
			}
			return 0;
		}
		else if (svasUrl==null ) {
			return getLicenceLeft();
		} else {
			String url=svasUrl+SVAS+licence+"?available=true";
			Map<String, Object> req= new HashMap<String, Object>();
			String s = getFromHttp(url,req,String.class);
			if (Util.isEmpty(s)) throw new IotequException(IotequThrowable.NO_ANSWER,"没有获得结果");
			Gson gson = Util.getGson();
			Map<String,Object> r=gson.fromJson(s, Map.class);
			if (r!=null) {
				Object v=r.get("licence");
				if(v!=null) return Integer.parseInt(v.toString());
			}
			return 0;
		}
	}
	/**
		 * 获得软件使用时间
		 * @return 试用剩余天数。如果为正式版本，返回一个 大于3650的数字
		 * @throws IotequException 异常
		 */
		public int getTrialDays() throws IotequException {
			if (svasServer!=null) {
				Object o=EntityUtil.runMethod(svasServer, "svein_getTrialDays");
				if (o!=null) {		
					Map<String, Object> map=(Map<String, Object>)o;
					Object v=map.get("trialDays");
					if(v!=null) return Integer.parseInt(v.toString());
				} 
				return 0;
			}
			else if (svasUrl==null ) {
				Environment env=Util.getBean(Environment.class);
				String sn=env.getProperty("svas.sn");
				if (sn!=null && !sn.isEmpty()) {
					int licence = Util.getLicence(sn);
					if (licence>0) return 36500;
				}
				Date dt=Util.getVersionBuildTime("svas-client");
				int ds=(int) ((new Date().getTime() - dt.getTime())/1000/3600/24);
				if (ds>=90) return 0;
				else return 90-ds; 
			} else {
				String url=svasUrl+SVAS+trialDays;
				Map<String, Object> req= new HashMap<String, Object>();
				String s = getFromHttp(url,req,String.class);	
				if (Util.isEmpty(s)) throw new IotequException(IotequThrowable.NO_ANSWER,"没有获得结果");
				Gson gson = Util.getGson(); 
				Map<String,Object> r=gson.fromJson(s, Map.class);
				if (r!=null) {
					Object v=r.get("trialDays");
					if(v!=null) return Integer.parseInt(v.toString());
				}
				return 0;
			}
		}

	/**
	 *
	 * 获得唯一识别的用户号userNo。如果已经存在，返回存在的用户号
	 * @param idType	证件类别编号
	 * @param idNo	证件号码
	 * @param name	姓名
	 * @param prefix   指定用户号前缀而不是用默认值
	 * @return  用户号
	 * @throws IotequException svas失败时返回的错误及代码
	 */
	public String getUserNo(Integer idType,String idNo,String name,String def, String prefix) throws IotequException {
		if (Util.isEmpty(idNo) || idType==null || idType<=0) return null;
		if (svasServer!=null) {
			// svein_getUserNo(Integer idType, String idNo ,String name,String def,String prefix)
			Object o=EntityUtil.runMethod(svasServer, "svein_getUserNo",idType,idNo,name,def,prefix);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				SvasUserInfo user=getFromMap(map, SvasUserInfo.class);
				if (user!=null) return user.userNo;
				else return null;
			} else return null;
		}
		else if (svasUrl==null ) {
			if (fill_digit==null) {
				List<Map<String, Object>> pp=SqlUtil.sqlQuery("select * from dev_user_no_params");
				if (pp==null || pp.size()==0) return null;
				db_prefix=pp.get(0).get("prefix").toString();
				min_length=Integer.parseInt(pp.get(0).get("min_length").toString());
				fill_digit=pp.get(0).get("fill_digit").toString();
			}
			if (idNo==null) return null;
			if (db_prefix!=null) prefix=db_prefix;
			String uno=SqlUtil.sqlQueryString("select user_no from dev_user_no where id_type=? and id_no=?", idType,idNo);
			if (Util.isEmpty(uno)) {
				int licenceLeft=getLicenceLeft();;
				if (licenceLeft<=0) throw new SvasException(SvasException.ERR_LICENCE_OUT);
				if (!Util.isEmpty(def) && !SqlUtil.sqlExist("select * from dev_user_no where user_no=?", def))  {
					SqlSessionTemplate sqlSessionTemplate=(SqlSessionTemplate) Util.getBean("sqlSessionTemplate");
					if ("Oracle".equals(sqlSessionTemplate.getConfiguration().getDatabaseId())) {
						SqlUtil.sqlExecute("insert into dev_user_no(id,id_type,id_no,user_no,name) values(SEQUENCE_DEV_USER_NO.nextval,?,?,?,?)", idType,idNo,def,name);
					}
					else {
						SqlUtil.sqlExecute("insert into dev_user_no (id_type,id_no,user_no,name) values(?,?,?,?)", idType,idNo,def,name);
					}
					return def;
				} else {
					SqlSessionTemplate sqlSessionTemplate=(SqlSessionTemplate) Util.getBean("sqlSessionTemplate");
					if ("Oracle".equals(sqlSessionTemplate.getConfiguration().getDatabaseId())) {
						Integer uid = SqlUtil.sqlQueryInteger("select SEQUENCE_DEV_USER_NO.nextval from dual");
						uno=uid.toString();
						SqlUtil.sqlExecute("insert into dev_user_no(id,id_type,id_no,user_no,name) values(?,?,?,?,?)", uid,idType,idNo,uid.toString(),name);
					}
					else {
						SqlUtil.sqlExecute("insert into dev_user_no (id_type,id_no,user_no,name) values(?,?,'',?)", idType,idNo,name);
						uno=SqlUtil.sqlQueryString("select id from dev_user_no where id_type=? and id_no=?", idType,idNo);
					}
					if (uno==null) return null;
					while (uno.length()<min_length-prefix.length()) uno=fill_digit+uno;
					uno=prefix+uno;
					SqlUtil.sqlExecute("update dev_user_no set user_no=? where id_type=? and id_no=?", uno,idType,idNo);
				}
			} else if (!Util.isEmpty(name)) {
				int maxId = getMaxIdFromLicence();
				int id=SqlUtil.sqlQueryInteger("select id from dev_user_no where user_no=?", uno);
				if (maxId==ALLOW_ALL || id<=maxId)
					SqlUtil.sqlExecute("update dev_user_no set name=? where user_no=?", name,uno);
				else
					throw new SvasException(SvasException.ERR_LICENCE_OUT);
			}
			return uno;
		} else {
			String url=svasUrl+SVAS+getUserNo;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("idType", idType);
			req.put("idNo", idNo);
			req.put("name", name);
			req.put("def", def);
			req.put("prefix", prefix);
			SvasUserInfo u=getFromHttp(url,req, SvasUserInfo.class);
			return u.userNo;
		}
	}

	/**
	 * 从模板读取uid信息
	 * @param templates 模板，最多三个词典
	 * @return 最多三个userNo
	 * @throws IotequException 错误
	 */
	public String[] getUserNoFromDict(String templates) throws IotequException {
		if (Util.isEmpty(templates)) return null;
		if (svasServer!=null) {
			// svein_getUserNo(Integer idType, String idNo ,String name,String def,String prefix)
			Object o=EntityUtil.runMethod(svasServer, "svein_getUserNoFromDict",templates);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				Boolean b = getFromMap(map,Boolean.class);
				if (b)  return StringUtil.toString(map.get("templates")).split(",");
				else	return null;
			} else return null;
		}
		else if (svasUrl==null ) {
			throw new IotequException(IotequThrowable.NOT_FOUND,"Svas server not exists");
		} else {
			String url=svasUrl+SVAS+getUserNoFromDict;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("templates", templates);
			SvasUserInfo u=getFromHttp(url,req, SvasUserInfo.class);
			return Util.isEmpty(u.userNo)?null:u.userNo.split(",");
		}
	}

	/**
	 * 修改模板的uid信息
	 * @param templates 模板
	 * @param userNo 新的userno
	 * @return 新的模板
	 * @throws IotequException 错误
	 */
	public String setUserNoForDict(String templates,String userNo) throws IotequException {
		if (Util.isEmpty(templates) || Util.isEmpty(userNo)) return null;
		if (svasServer!=null) {
			Object o=EntityUtil.runMethod(svasServer, "svein_setUserNoForDict",templates, userNo);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				Boolean b = getFromMap(map,Boolean.class);
				if (b)  return StringUtil.toString(map.get("templates"));
				else	return null;
			} else return null;
		}
		else if (svasUrl==null ) {
			throw new IotequException(IotequThrowable.NOT_FOUND,"Svas server not exists");
		} else {
			String url=svasUrl+SVAS+setUserNoForDict;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("templates", templates);
			req.put("userNo", userNo);
			SvasTemplates u=getFromHttp(url,req, SvasTemplates.class);
			return u.templates;
		}
	}
		/**
		 * 获得用户信息
		 * @param userNo		用户号
		 * @return					用户信息
		 * @throws IotequException	 svas失败时返回错误
		 */
		public SvasUserInfo getUserInfo(String userNo) throws IotequException {
			if (Util.isEmpty(userNo)) return null;
			else if (svasServer!=null) {
				// svein_getUserInfo(String userNo)
				Object o=EntityUtil.runMethod(svasServer, "svein_getUserInfo",userNo);
				if (o!=null) {
					Map<String,Object> map=(Map<String,Object>)o;
					return getFromMap(map, SvasUserInfo.class);
				} else return null;
			}
			else if (svasUrl==null ) {
				checkUserLicence(userNo);
				List<Map<String, Object>> idInfo=SqlUtil.sqlQuery("select id_type as \"idType\",id_no as \"idNo\",name as \"name\" from dev_user_no where user_no=?", userNo);
				if (idInfo==null || idInfo.size()==0)  {  // 通过userNo自建立dev_user_no表，仅仅适用于终端验证
					return null;
				}
				return getFromMap(idInfo.get(0), SvasUserInfo.class);
			} else {
				String url=svasUrl+SVAS+getUserInfo;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("userNo", userNo);
				return getFromHttp(url,req, SvasUserInfo.class);
			}
		}


		/**
		 * 修改user-No对应的证件号码及类别，user-No不变
		 * @param userNo 用户号
		 * @param idType 新的证件类别，为null时，保持不变
		 * @param idNo  新的证件号码，为null时，保持不变
		 * @param name 新的姓名，为null时，保持不变
		 * @return 用户号
		 * @throws IotequException svas失败时返回的错误及代码
		 */

		public String changeUserInfo(String userNo, Integer idType, String idNo, String name) throws IotequException {
			if (Util.isEmpty(userNo) || ( (idType==null || idType<=0) && Util.isEmpty(idNo) && Util.isEmpty(name))) return userNo;
			else if (svasServer!=null) {
				// svein_changeUserInfo(String userNo, Integer idType, String idNo, String name)
				Object o=EntityUtil.runMethod(svasServer, "svein_changeUserInfo",userNo,idType,idNo,name);
				if (o!=null) {
					Map<String,Object> map=(Map<String,Object>)o;
					Boolean b = getFromMap(map,Boolean.class);
					if (b)  return userNo;
					else return null;
				} else return null;
			} else if (svasUrl==null) {
				checkUserLicence(userNo);
				String set="";
				if (idType!=null && idType!=0) {
					if (set.isEmpty()) set="id_type="+idType;
					else set=set+" , " + "id_type="+idType;
				}
				if (!Util.isEmpty(idNo)) {
					if (set.isEmpty()) set="id_no='"+idNo+"'";
					else set=set+" , " + "id_no='"+idNo+"'";
				}
				if (!Util.isEmpty(name)) {
					if (set.isEmpty()) set="name='"+name+"'";
					else set=set+" , " + "name='"+name+"'";
				}
				if (set.isEmpty()) return null;
				try {
					SqlUtil.sqlExecute("update dev_user_no set  "+set+" where user_no='"+userNo+"'" );							
				} catch (IotequException e) {
					e.printStackTrace();
				}
				return userNo;
			} else {
				String url=svasUrl+SVAS+changeUserInfo;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("userNo", userNo);
				req.put("idType", idType);
				req.put("idNo", idNo);
				req.put("name", name);
				SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
				if (e.success)  return userNo;
				else return null;
			}			
		}
	/**
	 * 获得用户信息
	 * @param userNo		用户号
	 * @param includePhoto		是否包含photo字段
	 * @return					用户信息
	 * @throws IotequException	 svas失败时返回错误
	 */
	public SvasUserNo getUserAllInfo(String userNo,Boolean includePhoto) throws IotequException {
		if (Util.isEmpty(userNo)) return null;
		else if (svasServer!=null) {
			// svein_getUserInfo(String userNo)
			Object o=EntityUtil.runMethod(svasServer, "svein_getUserAllInfo",userNo,includePhoto);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				return getFromMap(map, SvasUserNo.class);
			} else return null;
		}
		else if (svasUrl==null ) {
			checkUserLicence(userNo);
			return  svasUserNoDao.selectByUserNo(userNo);
		} else {
			String url=svasUrl+SVAS+getUserAllInfo;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("userNo",userNo);
			req.put("includePhoto",includePhoto);
			return getFromHttp(url,req, SvasUserNo.class);
		}
	}
	/**
	 * 修改用户信息
	 * @param people 用户信息
	 * @return userNo
	 * @throws IotequException 错误
	 */
		public String changeUserInfo(SvasUserNo people) throws IotequException {
			if (people==null) throw new IotequException(IotequThrowable.NULL_OBJECT,"people");
			String userNo = people.getUserNo();
			if (Util.isEmpty(userNo)) throw new IotequException(IotequThrowable.NULL_OBJECT,"userNo");
			if (svasServer!=null) {


				HashMap<String,String> mapPeople = new HashMap<>();
				if (people.getUserNo()!=null) mapPeople.put("userNo",people.getUserNo());
				else return null;

				if (people.getBirthDate()!=null) mapPeople.put("birthDate",DateUtil.date2String(people.getBirthDate(),"yyyy-MM-dd"));
				if (people.getValidDate()!=null) mapPeople.put("validDate",DateUtil.date2String(people.getValidDate(),"yyyy-MM-dd"));
				if (people.getExpiredDate()!=null) mapPeople.put("expiredDate",DateUtil.date2String(people.getExpiredDate(),"yyyy-MM-dd"));

				if (people.getName()!=null) mapPeople.put("name",people.getName());
				if (people.getSex()!=null) mapPeople.put("sex",people.getSex());
				if (people.getIdType()!=null) mapPeople.put("idType",people.getIdType().toString());
				if (people.getIdNo()!=null) mapPeople.put("idNo",people.getIdNo());
				if (people.getEmail()!=null) mapPeople.put("email",people.getEmail());
				if (people.getMobilePhone()!=null) mapPeople.put("mobilePhone",people.getMobilePhone());
				if (people.getWechatOpenid()!=null) mapPeople.put("wechatOpenid",people.getWechatOpenid());
				if (people.getHomeAddr()!=null) mapPeople.put("homeAddr",people.getHomeAddr());
				if (people.getIdNation()!=null) mapPeople.put("idNation",people.getIdNation());
				if (people.getPhoto()!=null) mapPeople.put("photo",people.getPhoto());

				Object o=EntityUtil.runMethod(svasServer, "svein_changeUserInfoByMap",mapPeople);
				if (o!=null) {
					Map<String,Object> map=(Map<String,Object>)o;
					Boolean b = getFromMap(map,Boolean.class);
					if (b)  return userNo;
					else return null;
				} else return null;
			} else if (svasUrl==null) {
				checkUserLicence(userNo);
				svasUserNoDao.updateSelective(people);
				return userNo;
			} else {
				String url=svasUrl+SVAS+changeUserInfoBy;
				Map<String, Object> req= EntityUtil.mapFromEntity(people);
				SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
				if (e.success)  return userNo;
				else return null;
			}
		}
		/**
		 * 删除用户号以及用户号对应注册的指静脉信息
		 * @param userNo		用户号
		 * @return					删除的用户号
		 * @throws IotequException svas服务调用失败时返回的错误及代码
		 */
		public String removeUserNo(String userNo) throws IotequException {
			if (Util.isEmpty(userNo)) return null;
			else if (svasServer!=null) {
				//svein_removeUserNo(String userNo)
				Object o=EntityUtil.runMethod(svasServer, "svein_removeUserNo",userNo);
				if (o!=null) {
					Map<String,Object> map=(Map<String,Object>)o;
					Boolean b = getFromMap(map,Boolean.class);
					if (b)  return userNo;
					else return null;
				} else return null;
			}
			else if (svasUrl==null ) {
				SqlUtil.sqlExecute("delete from dev_vein_info where user_no=?",userNo);
				SqlUtil.sqlExecute("delete from dev_user_no where user_no=?",userNo);
				return userNo;
			} else {
				String url=svasUrl+SVAS+removeUserNo;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("userNo", userNo);
				SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
				if (e.success)  return userNo;
				else return null;
			}			
		}
	/**
	 * 删除指定的指静脉信息
	 * @param userNo  用户号
	 * @param fingerNo 手指编号,0表示全部删除
	 * @return 是否成功
	 * @throws IotequException svas服务调用失败时返回的错误及代码
	 */
	public boolean removeTemplate(String userNo, Integer fingerNo) throws IotequException {
		if (Util.isEmpty(userNo) || fingerNo==null) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"参数不正确");
		else if (svasServer!=null) {
			// svein_removeFinger(String userNo, Integer fingerNo)
			Object o=EntityUtil.runMethod(svasServer, "svein_removeFinger",userNo,fingerNo);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				Boolean b = getFromMap(map,Boolean.class);
				return b;
			} else return false;
		} else if (svasUrl==null) {
			int i;
			checkUserLicence(userNo);
			if (fingerNo>0) i=SqlUtil.sqlExecute("delete from dev_vein_info where user_no=? and finger_no=?", userNo,fingerNo);
			else i=SqlUtil.sqlExecute("delete from dev_vein_info where user_no=?", userNo);
			if (i==0) {
				throw new IotequException(IotequThrowable.FAILURE,"未能删除指定手指或没有注册该手指信息");
			}
			return true;
		} else {
			String url=svasUrl+SVAS+remove;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("userNo", userNo);
			req.put("fingerNo", fingerNo);
			SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
			return e.success;
		}
	}

	/**
	 * 更新用户的指静脉词典。如果该注册信息不存在，调用addTemplate操作。默认warning=false<br>
	 * 该操作会删除已经存在的与本词典匹配的注册指静脉信息
	 * @param userNo		用户号
	 * @param fingerNo	手指编号
	 * @param fingerType	手指
	 * @param templates	指静脉词典，可包含1,2,3个辞书。一个注册手指最多三个辞书
	 * @return 是否修改成功
	 * @throws IotequException svas服务调用失败时返回的错误及代码
	 */
	public boolean updateTemplate(String userNo, Integer fingerNo, Integer fingerType, String templates) throws IotequException {
		if (Util.isEmpty(userNo) || fingerNo==null || Util.isEmpty(templates)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"参数不正确");
		else if (svasServer!=null) {
			// svein_updateFinger(String userNo, Integer fingerNo, Integer fingerType, String templates)
			Object o=EntityUtil.runMethod(svasServer, "svein_updateFinger",userNo,fingerNo,fingerType,templates);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				Boolean b = getFromMap(map,Boolean.class);
				return b;
			} else return false;
		} else if (svasUrl==null) {
			checkUserLicence(userNo);
			int licenceUsed = SqlUtil.sqlQueryInteger("select count(*) from dev_vein_info");
			if (licenceUsed>=maxLicence) throw new SvasException(SvasException.ERR_LICENCE_OUT);
			if (SqlUtil.sqlExist("select * from dev_vein_info where user_no=? and finger_no=?", userNo,fingerNo)) {
				int i=0;
				if (fingerType!=null && fingerType!=0)
					i=SqlUtil.sqlExecute("update dev_vein_info set templates = ? , finger_type = ? where user_no=? and finger_no=?", templates,fingerType,userNo,fingerNo);
				else
					i=SqlUtil.sqlExecute("update dev_vein_info set templates = ? where user_no=? and finger_no=?", templates,userNo,fingerNo);
				if (i!=1) throw new IotequException(IotequThrowable.FAILURE,"未能注册成功");
				else return true;
			} 	else throw new IotequException(IotequThrowable.USER_NOT_EXIST,"不存在的用户信息");
		} else {
			String url=svasUrl+SVAS+update;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("userNo", userNo);
			req.put("fingerNo", fingerNo);
			req.put("fingerType", fingerType);
			req.put("templates", templates);
			SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
			if (e.exists) throw new SvasException(SvasException.ERR_TEMPLATE_EXISTS);
			else return e.success;
		}
	}

	/**
		 * 增加指静脉<br>
		 * 该操作会删除已经存在的与本词典匹配的注册指静脉信息
		 * @param userNo			用户号
		 * @param fingerNo		手指编号，1或2
		 * @param fingerType	手指类别标识 
		 * @param templates		指静脉词典，可包含1,2,3个辞书。一个注册手指最多三个辞书
		 * @param warning		是否胁迫
		 * @return						是否注册成功
		 * @throws IotequException    svas服务调用失败时返回的错误及代码
		 */
		public boolean addTemplate(String userNo, Integer fingerNo, Integer fingerType,String templates, Boolean warning) throws IotequException {
			if (Util.isEmpty(userNo) || fingerNo==null || Util.isEmpty(templates)) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"参数不正确");
			else if (svasServer!=null) {
				// svein_addFinger(String userNo, Integer fingerNo, Integer fingerType, String templates, Boolean warning)
				Object o=EntityUtil.runMethod(svasServer, "svein_addFinger",userNo,fingerNo,fingerType,templates,warning);
				if (o!=null) {
					Map<String,Object> map=(Map<String,Object>)o;
					Boolean b = getFromMap(map,Boolean.class);
					if (b)  return true;
					return false;
				} else return false;
			} else if (svasUrl==null) {
				checkUserLicence(userNo);
				if (SqlUtil.sqlExist("select * from dev_vein_info where user_no=? and finger_no=?", userNo,fingerNo)) {
					int i=SqlUtil.sqlExecute("update dev_vein_info set templates = ? , warning = ? where user_no=? and finger_no=?", 
							templates,(warning!=null && warning? 1 : 0),userNo,fingerNo);
					if (i!=1) {
						throw new IotequException(IotequThrowable.FAILURE,"未能注册成功");
					} 
				} else {
					SqlSessionTemplate sqlSessionTemplate=(SqlSessionTemplate) Util.getBean("sqlSessionTemplate");
					int i=0;
					if ("Oracle".equals(sqlSessionTemplate.getConfiguration().getDatabaseId())) {
						i=SqlUtil.sqlExecute("insert into dev_vein_info (id,templates,warning,user_no,finger_no,finger_type) values(SEQUENCE_DEV_VEIN_INFO.nextval,?,?,?,?,?)", 
								templates,(warning!=null && warning? 1 : 0),userNo,fingerNo,fingerType==null?0:fingerType);
					} else i=SqlUtil.sqlExecute("insert into dev_vein_info (templates,warning,user_no,finger_no,finger_type) values(?,?,?,?,?)", 
							templates,(warning!=null && warning? 1 : 0),userNo,fingerNo,fingerType==null?0:fingerType);
					if (i!=1) {
						throw new IotequException(IotequThrowable.FAILURE,"未能注册成功");
					}	
				}
				return true;
			} else {
				String url=svasUrl+SVAS+add;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("userNo", userNo);
				req.put("fingerNo", fingerNo);
				req.put("fingerType", fingerType);
				req.put("templates", templates);
				req.put("warning", warning);
				SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
				if (e.exists) throw new SvasException(SvasException.ERR_TEMPLATE_EXISTS);
				else return e.success;
			}			
		}

	/**
	 * 设置1-2个指静脉
	 * @param userNo 用户号
	 * @param fingerType1 第一个类别
	 * @param warning1 第一个是否胁迫
	 * @param templates1 第一个模板
	 * @param fingerType2 第二个类别
	 * @param warning2 第二个是否胁迫
	 * @param templates2 第二个模板
	 * @return 是否成功
	 * @throws IotequException 错误
	 */
	public boolean setTemplates(String userNo, Integer fingerType1, Boolean warning1,String templates1,Integer fingerType2, Boolean warning2,String templates2) throws IotequException {
		if (Util.isEmpty(userNo) || (Util.isEmpty(templates1) && Util.isEmpty(templates2))) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"参数不正确");
		else if (svasServer!=null) {
			// svein_setFingers(String userNo, Integer type1, Boolean warning1, String templates1, Integer type2, Boolean warning2, String templates2)
			Object o=EntityUtil.runMethod(svasServer, "svein_setFingers",userNo,fingerType1,warning1,templates1,fingerType2,warning2,templates2);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				Boolean b = getFromMap(map,Boolean.class);
				return b;
			} else return false;
		} else if (svasUrl==null) {
			checkUserLicence(userNo);
			int licenceUsed = SqlUtil.sqlQueryInteger("select count(*) from dev_vein_info");
			if (licenceUsed>=maxLicence) throw new SvasException(SvasException.ERR_LICENCE_OUT);
			if (!Util.isEmpty(templates1)) {
				int i=0;
				if (SqlUtil.sqlExist("select * from dev_vein_info where user_no=? and finger_no=1", userNo)) {
					i=SqlUtil.sqlExecute("update dev_vein_info set templates = ? , finger_type = ? , warning = ? where user_no=? and finger_no=1",
							templates1,(fingerType1!=null?fingerType1:0),(warning1!=null && warning1 ? 1: 0),userNo);
				} else {
					i=SqlUtil.sqlExecute("insert into dev_vein_info (templates,warning,user_no,finger_no,finger_type) values(?,?,?,?,?)",
							templates1,(warning1!=null && warning1? 1 : 0),userNo,1,fingerType1==null?0:fingerType1);
				}
				if (i!=1) throw new IotequException(IotequThrowable.FAILURE,"未能注册成功");
			}
			if (!Util.isEmpty(templates2)) {
				int i=0;
				if (SqlUtil.sqlExist("select * from dev_vein_info where user_no=? and finger_no=2", userNo)) {
					i=SqlUtil.sqlExecute("update dev_vein_info set templates = ? , finger_type = ? , warning = ? where user_no=? and finger_no=1",
							templates2,(fingerType2!=null?fingerType2:0),(warning2!=null && warning2 ? 1: 0),userNo);
				} else {
					i=SqlUtil.sqlExecute("insert into dev_vein_info (templates,warning,user_no,finger_no,finger_type) values(?,?,?,?,?)",
							templates2,(warning2!=null && warning2? 1 : 0),userNo,1,fingerType2==null?0:fingerType2);
				}
				if (i!=1) throw new IotequException(IotequThrowable.FAILURE,"未能注册成功");
			}
			return true;
		} else {
			String url=svasUrl+SVAS+set;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("userNo", userNo);
			req.put("fingerType1", fingerType1);
			req.put("warning1", warning1);
			req.put("templates1", templates1);
			req.put("fingerType2", fingerType2);
			req.put("warning2", warning2);
			req.put("templates2", templates2);
			SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
			if (e.exists) throw new SvasException(SvasException.ERR_TEMPLATE_EXISTS);
			else return e.success;
		}
	}

	public boolean  setPhoto(String userNo,String photo) throws IotequException {
		if (Util.isEmpty(userNo)) throw new SvasException(SvasException.ERR_PARAMETER);
		else if (svasServer!=null) {
			Object o=EntityUtil.runMethod(svasServer, "svein_setPhoto",userNo,photo);
			if (o!=null) {
				Map<String,Object> map=(Map<String,Object>)o;
				Boolean b = getFromMap(map,Boolean.class);
				if (b)  return true;
				return false;
			} else return false;
		} else if (svasUrl==null) {
			checkUserLicence(userNo);
			if (Util.isEmpty(photo)) SqlUtil.sqlExecute("update dev_user_no set photo=? where user_no=?", photo,userNo);
			else SqlUtil.sqlExecute("update dev_user_no set photo=null where user_no=?", userNo);
			return true;
		} else {
			String url=svasUrl+SVAS+PHOTO;
			Map<String, Object> req= new HashMap<String, Object>();
			req.put("userNo", userNo);
			req.put("photo",photo);
			SvasErrorInfo e=getFromHttp(url,req, SvasErrorInfo.class);
			return e.success;
		}
	}
		
		/**
		 * 获取指静脉词典
		 * @param userNo 用户号
		 * @param fingerNo 手指编号
		 * @return 词典
		 * @throws IotequException svas服务调用失败时返回的错误及代码
		 */
		public String getTemplate(String userNo, Integer fingerNo) throws IotequException {
			if (Util.isEmpty(userNo) || fingerNo==null) throw new SvasException(SvasException.ERR_PARAMETER);
			else if (svasServer!=null) {
				// svein_getTemplates(String userNo,Integer fingerNo)
				Object o=EntityUtil.runMethod(svasServer, "svein_getTemplates",userNo,fingerNo);
				if (o!=null) {
					Map<String,Object> map=(Map<String,Object>)o;
					Boolean b = getFromMap(map,Boolean.class);
					if (b)  return map.get("templates").toString();
					else	return null;
				} else return null;
			} else if (svasUrl==null) {
				checkUserLicence(userNo);
				String template=SqlUtil.sqlQueryString("select templates from dev_vein_info where user_no=? and finger_no=?", userNo,fingerNo);
				if (!Util.isEmpty(template)) {
					return template;
				} else {
					throw new IotequException(IotequThrowable.NOT_FOUND,"未找到注册手指，请检查参数");
				}
			} else {
				String url=svasUrl+SVAS+getTemplates;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("userNo", userNo);
				req.put("fingerNo", fingerNo);
				SvasTemplates map=getFromHttp(url,req, SvasTemplates.class);
				return map.templates;
			}			
		}
		/**
		 * 获得用户注册的指静脉手指数
		 * @param userNo  用户号
		 * @return 注册的手指数
		 * @throws IotequException svas服务调用失败时返回的错误及代码
		 */
		public int getFingerCount(String userNo) throws IotequException {
			if (Util.isEmpty(userNo)) throw new SvasException(SvasException.ERR_PARAMETER);
			else if (svasServer!=null) {
				// svein_getFingerCount(String userNo)
				Object o=EntityUtil.runMethod(svasServer, "svein_getFingerCount",userNo);
				if (o!=null) {
					Map<String,Object> map=(Map<String,Object>)o;
					Boolean b = getFromMap(map,Boolean.class);
					if (b)  return (Integer)map.get("count");
					else	return 0;
				} else return 0;
			} else if (svasUrl==null) {
				checkUserLicence(userNo);
				int rs=SqlUtil.sqlQueryInteger("select count(*) from dev_vein_info where user_no=?", userNo);
				return rs;
			} else {
				String url=svasUrl+SVAS+getFingerCount;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("userNo", userNo);
				SvasFingerInfo c=getFromHttp(url,req, SvasFingerInfo.class);
				return c.count;
			}			
		}			
		
		/**
		 * 获得用户注册的指静脉数据详细信息
		 * @param userNo 用户号
		 * @return 注册指静脉数据列表
		 * @throws IotequException svas服务调用失败时返回的错误及代码
		 */
		public List<SvasTemplates>  getFingerInfo(String userNo) throws IotequException  {
			if (Util.isEmpty(userNo)) throw new SvasException(SvasException.ERR_PARAMETER);
			else if (svasServer!=null) {
				// svein_getFingerInfo(String userNo)
				Object o=EntityUtil.runMethod(svasServer, "svein_getFingerInfo",userNo);
				if (o!=null) {
					SvasFingerInfo map=getFromMap((Map<String, Object>)o, SvasFingerInfo.class);
					return map.list;
				} else return null;
			} else if (svasUrl==null) {	
				checkUserLicence(userNo);
				String sql="select finger_no,finger_type,templates,warning from dev_vein_info where user_no=?";
				List<SvasTemplates> infos=SqlUtil.sqlQuery(SvasTemplates.class,sql, userNo);
				return infos;
			} else {
				String url=svasUrl+SVAS+getFingerInfo;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("userNo", userNo);
				SvasFingerInfo map=getFromHttp(url,req, SvasFingerInfo.class);
				return map.list;
			}			
		}	
		/**
		 * 指静脉数据认证
		 * @param template		一个辞书
		 * @param thresh  指定阈值，0或空 表示使用系统默认值
		 * @return						认证结果
		 * @throws IotequException svas服务调用失败时返回的错误及代码
		 */
		public SvasMatched auth(String template, Integer thresh) throws IotequException {
			if (Util.isEmpty(template)) throw new SvasException(SvasException.ERR_PARAMETER);
			else if (svasServer!=null) {
				// svein_matchFinger(String template,Integer thresh);
				Object o=EntityUtil.runMethod(svasServer, "svein_matchFinger",template, thresh);
				if (o!=null) {
					Map<String,Object> map=(Map<String, Object>)o;
					return 	getFromMap(map, SvasMatched.class);
				} else return null;
			} else if (svasUrl==null) {
				throw new IotequException(IotequThrowable.NOT_FOUND,"Svas server not exists");
			} else {
				String url=svasUrl+SVAS+auth;
				if (thresh!=null && thresh>0 && thresh<1000) url=url+"?thresh="+thresh;
				Map<String, Object> req= new HashMap<String, Object>();
				req.put("template", template);
				SvasMatched map=getFromHttp(url,req, SvasMatched.class);
				return map;
			}					
		}

	public Map<String,Object>  getEnvProperties() throws IotequException {
		if (svasServer!=null) {
			// svein_getEnvProperties()
			Object o=EntityUtil.runMethod(svasServer, "svein_getEnvProperties");
			if (o!=null) {
				return (Map<String,Object>)o;
			} else return null;
		} else if (svasUrl==null) {
			return null;
		} else {
			String url=svasUrl+SVAS+env;
			Map<String,Object> m=getFromHttp(url,null,Map.class);
			return m;
		}
	}
}
