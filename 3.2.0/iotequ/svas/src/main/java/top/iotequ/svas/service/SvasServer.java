package top.iotequ.svas.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import top.iotequ.svas.util.SvasUtil;
import top.iotequ.svas.service.SvasTypes.*;

/**
 *svas服务bean<br>
 * 一般不直接使用，请使用svasService或svasClient<br>
 * 本模块使用 maven package -Pspring可以打包成单独的微服务 jar包<br>
 * 本模块单独运行需要配置的参数<br>
 * svas.host,svas.port,svas.user,svas.password,svas.database,svas.thresh
 * @author Qinyoyo
 * @version 1.0
 */
@SuppressWarnings("static-access")
@Component
public class SvasServer  implements ApplicationContextAware {
	@Autowired
    private Environment env;
	public static int THRESH = 448;
	public static final int SUCCESS  = 0;
	public static final int ERR_NO_MEMERY	=		1;
	public static final int ERR_TEMPLATE_FORMAT =	2;
	public static final int ERR_TEMPLATE_LENGTH	=	3;
	public static final int ERR_NOT_FOUND	=		4;
	public static final int ERR_TEMPLATE_EXISTS	=	5;
	public static final int ERR_MULTI_FOUND	=		6;
	public static final int ERR_TIMEOUT		=		7;
	public static final int ERR_MUTEX			=	8;
	public static final int ERR_PARAMETER		=	9;
	public static final int ERR_THREAD_START	 =	10 ;
	public static final int  ERR_NOT_CHANGED  =	11;
	public static final int  ERR_LOW_TEMPLATES  =		12;
	public static final int   ERR_NOT_ACTIVITED	  =  13;
	public static final int   ERR_LICENCE_OUT		=	14;
	public static final int   ERR_CONFIG_LOST		=	15;
	public static final int  ERR_NAME_MISMATCH      = 16 ;
	public static final int TRIAL_EXPIRED		=	99;
	public static final int ERR_DB_NOT_CONNECTED =	101;
	public static final int ERR_NULL_IDNO		=	102;
	private static final Logger log = LoggerFactory.getLogger(SvasServer.class);
	private int validDay =0;
	public static String  getError(int errorno) {
		String err;
		switch (errorno) {
			case ERR_NO_MEMERY : err = "内存申请错误"; break;
			case ERR_TEMPLATE_FORMAT : err = "指静脉数据格式错误"; break;
			case ERR_TEMPLATE_LENGTH : err = "指静脉数据长度不正确"; break;
			case ERR_NOT_FOUND : err = "未找到匹配数据"; break;
			case ERR_TEMPLATE_EXISTS : err = "指静脉数据已经存在"; break;
			case ERR_MULTI_FOUND : err = "匹配到多个值"; break;
			case ERR_TIMEOUT : err = "请求超时"; break;
			case ERR_MUTEX : err = "数据锁失败"; break;
			case ERR_PARAMETER : err = "参数不全或不正确"; break;
			case ERR_THREAD_START: err = "线程启动失败"; break;
			case  ERR_NOT_CHANGED : err = "没有任何更改"; break;
			case  ERR_LOW_TEMPLATES: err = "模板之间不匹配"; break;
			case  ERR_NOT_ACTIVITED : err = "svas没有正确激活"; break;
			case  ERR_LICENCE_OUT: err = "Licence许可数量用完"; break;
			case ERR_DB_NOT_CONNECTED : err = "数据库未连接"; break;
			case ERR_NULL_IDNO : err = "错误的空编号"; break;
			case ERR_CONFIG_LOST : err= "没有找到 svas.ini 或无权限" ;break;
			case TRIAL_EXPIRED : err= "软件已过试用期" ;break;
			case ERR_NAME_MISMATCH: err="证件名称不匹配"; break;
			default : if (errorno>=1000) err="Mysql数据操作错误"; else err="其他错误";
		};
		return String.format("Error %d : %s", errorno,err);
	}
	@Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
    	int r = svas_initial();
        if (r!=0) throw new BeansException (Svas.dllMode + " Svas 初始化错误(" + getError(r)	+ ")") {
			private static final long serialVersionUID = 1L;} ;
		Integer v = Svas.instance.svein_getTrialDays();
		validDay =v;
		if (validDay >0 && validDay <3650) {
			Timer timer = new Timer();
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, validDay);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					validDay =0;
				}
			}, ca.getTime());
		} else {
			Integer al = Svas.instance.svein_getLicenceAvailable();
			if (al>0) validDay = 36500;
		}
    }
	private String getProperty(String firstSession ,String secondSession, String id) {
		String p = (firstSession==null ? null : env.getProperty(firstSession+"."+id));
		if (p==null) return env.getProperty((secondSession+"."+id));
		else return p;
	}
    private int getIntProperty(String firstSession, String secondSession, String key) {
		String v = getProperty(firstSession,secondSession,key);
		if (v==null || v.trim().isEmpty()) return 0;
		try {
			v=v.trim();
			if (v.startsWith("T") || v.startsWith("t")) return 1;
			else if (v.startsWith("F") || v.startsWith("f")) return 0;
			else return Integer.parseInt(v);
		} catch (Exception e) {
			return 0;
		}
	}
	static Map<String,String> getDatabaseSetting(Environment env) {
		Map<String,String> map=new HashMap<>();
		String datasourceUrl= env.getProperty("spring.datasource.url");
		if(datasourceUrl==null) return null;
		String s=env.getProperty("spring.datasource.username");
		if (s==null) return null;
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
			if (pos<0)  return null;
			String [] ap=datasourceUrl.substring(pos).split(":");
			map.put("host", ap[0]);
			map.put("port",ap.length>2 ? ap[1] : "1521");
			map.put("database",ap.length>2?ap[2]:ap[1]);
		}
		return map;
	}

    public int svas_initial(){
		String sc = null;
		try {
			sc = SvasUtil.getSetupCode();
		} catch (Exception e) {}
		System.setProperty("jna.encoding", "GBK");
		SvasSetting settings=new SvasSetting();

		settings.debug = log.isDebugEnabled() ? 1: 0;
		settings.matchTrace = getIntProperty(sc,"svas","matchTrace");

		THRESH = getIntProperty(sc,"svas","thresh");
		if (THRESH<=0 || THRESH>=1000) THRESH=448;
		settings.thresh = THRESH;

		settings.userNoPrefix = getProperty(sc,"svas","userNoPrefix");
		settings.userNoMminLength = getIntProperty(sc,"svas","userNoMminLength");
		settings.sn = getProperty(sc,"svas","sn");
		settings.host = getProperty(sc,"svas","datasource.host");
		if (settings.host==null || settings.host.trim().isEmpty()) {
			Map<String,String> m = getDatabaseSetting(env);
			if (m!=null) {
				settings.user = m.get("user");
				settings.password = m.get("password");
				settings.schema = m.get("database");
				settings.port = Integer.parseInt(m.get("port"));
			}
		} else {
			settings.user = getProperty(sc,"svas","datasource.user");
			settings.password = getProperty(sc,"svas","datasource.password");
			settings.schema = getProperty(sc,"svas","datasource.schema");
			settings.port = getIntProperty(sc,"svas","datasource.port");
		}

		settings.autoUpdateTemplate = getIntProperty(sc,"svas","adjust.autoUpdateTemplate");
		settings.templatesMustMatch = getIntProperty(sc,"svas","adjust.templatesMustMatch");
		settings.multiMatchedWeight = getIntProperty(sc,"svas","adjust.multiMatchedWeight");
		settings.maxMatchedWeight = getIntProperty(sc,"svas","adjust.maxMatchedWeight");
		settings.maxMatchedCondition = getIntProperty(sc,"svas","adjust.maxMatchedCondition");

        return Svas.initial(settings);
    }


	public Map<String,Object>  svein_getVersion()  {
		JniStringReturn ver=new JniStringReturn();
		Integer v = Svas.instance.svein_getVersion(ver);
		Map<String, Object> map = new HashMap<String, Object>();
		if (v==SUCCESS) {
			log.debug("svein_getVersion() success. version={}", ver.string);
			map.put("version", ver.string);
			map.put("success", true);
		} else {
			log.debug("svein_getVersion() failed with code {}", v);
			map.put("success", false);
			map.put("error", v);
		}
		return map;
	}
	public Map<String,Object>  svein_getLicence()  {
		Integer v = Svas.instance.svein_getLicence();
        log.debug("svein_getLicence()={}", v);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("licence", v);
		return map;
	}
	public Map<String,Object>  svein_getLicenceAvailable()  {
		Integer v = Svas.instance.svein_getLicenceAvailable();
		log.debug("svein_getLicenceAvailable()={}", v);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("licence", v);
		return map;
	}
	public Map<String,Object>  svein_getTrialDays()  {
		Integer v = Svas.instance.svein_getTrialDays();
        log.debug("svein_getTrialDays()={}", v);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("trialDays", v);
		return map;
	}
	public Map<String,Object>  svein_getUserNo(Integer idType, String idNo ,String name,String def,String prefix)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(idNo) || idType==null || idType==0) {
			map.put("success", false);
			map.put("message","please input"+(SvasUtil.isEmpty(idNo)?" <idNo>":"") + (idType==null || idType==0 ? " <idType>":""));
			map.put("error", ERR_PARAMETER);
		} else {
			if (SvasUtil.isEmpty(name)) name="";
			if (prefix==null) prefix=env.getProperty("svas.user-no-prefix");
			JniStringReturn userNo=new JniStringReturn();
			int r=Svas.instance.svein_getUserNo(idType, idNo, name,def,prefix,userNo);
			if (r==SUCCESS) {
				map.put("success", true);
				map.put("userNo", userNo.string);
				log.debug("svein_getUserNo({},{},{},{},{})={} ,userNo={}",idType, idNo, name,def,prefix,r,userNo);
			} else {
				map.put("success", false);
				map.put("error",r);
				log.debug("svein_getUserNo({},{},{},{},{})={}",idType, idNo, name,def,prefix,r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_queryUserNo(Integer idType, String idNo)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(idNo) || idType==null || idType==0) {
			map.put("success", false);
			map.put("message","please input"+(SvasUtil.isEmpty(idNo)?" <idNo>":"") + (idType==null || idType==0 ? " <idType>":""));
			map.put("error", ERR_PARAMETER);
		} else {
			JniStringReturn userNo=new JniStringReturn();
			int r=Svas.instance.svein_queryUserNo(idType, idNo, userNo);
			if (r==SUCCESS) {
				map.put("success", true);
				map.put("userNo", userNo.string);
				log.debug("svein_queryUserNo({},{})={} ,userNo={}",idType, idNo,r,userNo);
			} else {
				map.put("success", false);
				map.put("error",r);
				log.debug("svein_getUserNo({},{})={}",idType, idNo, r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_getUserNoFromDict(String temp)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(temp)) {
			map.put("success", false);
			map.put("message","please input <template>");
			map.put("error", ERR_PARAMETER);
		} else {
			JniStringReturn userNo=new JniStringReturn();
			int r=Svas.instance.svein_getUserNoFromDict(temp,userNo);
			if (r==SUCCESS) {
				map.put("success", true);
				map.put("userNo", userNo.string);
				log.debug("svein_getUserNoFromDict(...)={} ,userNo={}",r,userNo);
			} else {
				map.put("success", false);
				map.put("error",r);
				log.debug("svein_getUserNo(...)={}",r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_setUserNoForDict(String temp,String userNo)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(temp) || SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input" + (SvasUtil.isEmpty(userNo)?" <userNo>":"") + (SvasUtil.isEmpty(temp)?" <template>":""));
			map.put("error", ERR_PARAMETER);
		} else {
			JniStringReturn newTemp=new JniStringReturn();
			int r=Svas.instance.svein_setUserNoForDict(temp,userNo,newTemp);
			if (r==SUCCESS) {
				map.put("success", true);
				map.put("templates", newTemp.string);
				log.debug("svein_setUserNoForDict(...{})={}",userNo,r);
			} else {
				map.put("success", false);
				map.put("error",r);
				log.debug("svein_setUserNoForDict(...{})={}",userNo,r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_getUserInfo(String userNo)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		} else {
			SvasUserInfo u = new SvasUserInfo();
			u.userNo = userNo;
			int r=Svas.instance.svein_getUserInfo(userNo,u);
			if (r==SUCCESS) {
				map.put("success", true);
				map.put("idType", u.idType);
				map.put("idNo", u.idNo);
				map.put("name", u.name);
				map.put("userNo",u.userNo);
				log.debug("svein_getUserInfo({})={} : {} {} {}",userNo,r,u.idType,u.idNo,u.name);
			} else {
				map.put("success", false);
				map.put("error",r);
				log.debug("svein_getUserInfo({})={}",userNo,r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_changeUserInfo(String userNo, Integer idType, String idNo, String name)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (idType==null) idType=0;
		if (SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		}else if (idType==0 && SvasUtil.isEmpty(idNo) && SvasUtil.isEmpty(name)) {
			map.put("success", false);
			map.put("message","please input <idType> or <idNo> or <name>");
			map.put("error", ERR_PARAMETER);
		}
		else {
			int r=Svas.instance.svein_changeUserInfo(userNo,idType, idNo, name);
	        log.debug("svein_changeUserInfo({},{},{},{})={}",userNo,idType, idNo, name,r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("error",r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_getUserAllInfo(String userNo, Boolean includePhoto)  {
		HashMap<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		} else {
			int r=Svas.instance.svein_getUserAllInfo(userNo,includePhoto!=null &&  includePhoto ? 1: 0, map);
			if (r!=SUCCESS) {
				map.put("success", false);
				map.put("error",r);
				log.debug("svein_getUserAllInfo({})={}",userNo,r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_changeUserInfoByMap(HashMap<String,String> mapPeople)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (mapPeople==null || SvasUtil.isEmpty(mapPeople.get("userNo"))) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		} else {
			int r=Svas.instance.svein_changeUserInfoByMap(mapPeople);
			log.debug("svein_changeUserInfoByMap()={}",r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("error",r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_removeUserNo(String userNo)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		} else {
			int r=Svas.instance.svein_removeUserNo(userNo);
	        log.debug("svein_removeUserNo({})={}",userNo,r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("error",r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_removeFinger(String userNo, Integer fingerNo) {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo) || fingerNo==null ) {
			map.put("success", false);
			map.put("message","please input"+(SvasUtil.isEmpty(userNo)?" <userNo>":"")+(fingerNo==null?" <fingerNo>":""));
			map.put("error", ERR_PARAMETER);
		}	else {
			int r=Svas.instance.svein_removeFinger(userNo,fingerNo);
	        log.debug("svein_removeFinger({},{})={}",userNo,fingerNo,r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("error",r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_updateFinger(String userNo, Integer fingerNo, String fingerName, String templates)   {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo) || fingerNo==null) {
			map.put("success", false);
			map.put("message","please input"+(SvasUtil.isEmpty(userNo)?" <userNo>":"")+(fingerNo==null?" <fingerNo>":""));
			map.put("error", ERR_PARAMETER);
		} else if (SvasUtil.isEmpty(templates) ) {
			map.put("success", false);
			map.put("message","please templates");
		}
		else  {
			if (fingerName==null) fingerName="";
			int r=Svas.instance.svein_updateFinger(userNo,fingerNo,fingerName,templates);
	        log.debug("svein_updateFinger({},{},...)={}",userNo,fingerNo,r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				if (r==ERR_TEMPLATE_EXISTS)  map.put("exists",true);
				map.put("error",r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_addFinger(String userNo, Integer fingerNo, String fingerName, String templates, Boolean warning)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo) || fingerNo==null) {
			map.put("success", false);
			map.put("message","please input"+(SvasUtil.isEmpty(userNo)?" <userNo>":"")+(fingerNo==null?" <fingerNo>":""));
			map.put("error", ERR_PARAMETER);
		} else if (SvasUtil.isEmpty(templates) ) {
			map.put("success", false);
			map.put("message","please templates");
		}
		else  {
			int r=Svas.instance.svein_addFinger(userNo,fingerNo,(fingerName==null?"":fingerName),templates,(warning!=null && warning)?1:0);
	        log.debug("svein_addFinger({},{},...,{})={}",userNo,fingerNo,(warning!=null && warning)?1:0,r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				if (r==ERR_TEMPLATE_EXISTS)  map.put("exists",true);
				map.put("error",r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_setFingers(String userNo, String type1, Boolean warning1, String templates1, String type2, Boolean warning2, String templates2)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		} else if (SvasUtil.isEmpty(templates1) && SvasUtil.isEmpty(templates2)) {
			map.put("success", false);
			map.put("message","please input <templates1> or <templates2>");
			map.put("error", ERR_PARAMETER);
		} else  {
			int r=Svas.instance.svein_setFingers(userNo,(type1==null?"":type1),(warning1!=null && warning1 ? 1 : 0) , templates1 ,
					(type2==null?"":type2),(warning2!=null && warning2 ? 1 : 0),templates2);
			log.debug("svein_setFingers(...)={}",r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				if (r==ERR_TEMPLATE_EXISTS)  map.put("exists",true);
				map.put("error",r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_setPhoto(String userNo, String photo)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		}
		else  {
			int r=Svas.instance.svein_setPhoto(userNo,photo);
			log.debug("svein_setPhoto(...)={}",r);
			if (r==SUCCESS) {
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("error",r);
			}
		}
		return map;
	}
	public Map<String,Object>  svein_getTemplates(String userNo,Integer fingerNo)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo) || fingerNo==null) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		} else {
			SvasTemplates templates = new SvasTemplates();
			templates.fingerNo = fingerNo;
			int r=Svas.instance.svein_getTemplates(userNo,fingerNo,templates);
			log.debug("svein_getTemplates({},{})={}",userNo,fingerNo,r);
			if (r==SUCCESS) {
				map.put("success", true);
				map.put("templates",templates.templates) ;
				map.put("fingerNo",templates.fingerNo) ;
				map.put("fingerName",templates.fingerName) ;
				map.put("warning",templates.warning) ;
			} else {
				map.put("success", false);
				map.put("error",r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_getFingerCount(String userNo)  {
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(userNo)) {
			map.put("success", false);
			map.put("message","please input <userNo>");
			map.put("error", ERR_PARAMETER);
		} else {
			SvasFingerInfo info = new SvasFingerInfo();
			int r=Svas.instance.svein_getFingerCount(userNo,1, info);
			log.debug("svein_getFingerCount({},1)={}",userNo,r);
			if (r==SUCCESS) {
				map.put("count",info.count);
				map.put("success", true);
			}else {
				map.put("success", false);
				map.put("error",r);
			}
		}
		return map;
	}

	public Map<String,Object>  svein_getFingerInfo(String userNo)  {
		Map<String,Object> ret=new HashMap<String,Object>();
		if (SvasUtil.isEmpty(userNo)) {
			ret.put("success", false);
			ret.put("message","please input <userNo>");
			ret.put("error", ERR_PARAMETER);
		} else {
			if (validDay <=0) {
				ret.put("success", false);
				ret.put("message","version expired");
				ret.put("error", TRIAL_EXPIRED);
				return ret;
			}
			SvasFingerInfo info = new SvasFingerInfo();
			int r=Svas.instance.svein_getFingerCount(userNo,0, info);
			log.debug("svein_getFingerCount({},0)={}",userNo,r);
			if (r==SUCCESS) {
				ret.put("count", info.count);
				ret.put("success", true);
				if (info.count > 0) ret.put("list",info.list);
			} else if (r==ERR_NOT_FOUND) {
				ret.put("count", 0);
				ret.put("success", true);
				ret.put("list",null);
			} else  {
				ret.put("success", false);
				ret.put("error",r);
			}
		}
		return ret;
	}

	public static void matchedCallback(SvasMatched matched) // svas.dll中调用
	{
		synchronized (matched) {
			matched.notify();
		}
	}
	public Map<String,Object>  svein_matchFinger(String template,Integer thresh)  { // 结果通过result返回。result为调用者负责分配内存，1024字节。返回格式为 id,fingerNo,userNo,name,warning,... 最多返回4个结果
		Map<String,Object> map=new HashMap<String,Object>();
		if (validDay <=0) {
			map.put("success", false);
			map.put("message","version expired");
			map.put("error", TRIAL_EXPIRED);
			return map;
		}
		if (SvasUtil.isEmpty(template) ) {
			map.put("success", false);
			map.put("message","please input <template>");
			map.put("error", ERR_PARAMETER);
		}
		else  {
			SvasMatched m=new SvasMatched();
			int r=Svas.instance.svein_matchFinger(template,thresh!=null && thresh>0 ? thresh : THRESH,m);
			if (r==SUCCESS) {
				synchronized (m) {
					try {
						m.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				map.put("success", m.state == SUCCESS || m.state == ERR_NOT_FOUND);
				map.put("count",m.count);
				map.put("dictSize",m.dictSize);
				map.put("usUsed",m.usUsed);
				map.put("matchUsed",m.matchUsed);
				if (m.state!=SUCCESS) map.put("error",m.state);
				if (m.count>0) map.put("list", m.list);
				log.debug("svein_matchFinger(...,{})={} found={}",thresh!=null && thresh>0 ? thresh : THRESH,r,m.count);
			}
			else if (r==ERR_NOT_FOUND) {
				map.put("success", true);
				map.put("count",0);
				log.debug("svein_matchFinger({},{})={}",template,thresh!=null && thresh>0 ? thresh : THRESH,r);
			} else {
				map.put("success", false);
				map.put("error",r);
				log.debug("svein_matchFinger({},{})={}",template,thresh!=null && thresh>0 ? thresh : THRESH,r);
			}
		}
		return map;
	}


	public Map<String,Object>  svein_getEnvProperties()  {
		Map<String,Object> ret=new HashMap<String,Object>();
		JniStringReturn result=new JniStringReturn();
		int r=Svas.instance.svein_getEnvProperties(result);
		if (r==SUCCESS) {
			log.debug(result.string);
			Map<String,Object> m = SvasUtil.fromJson(result.string);
			ret.putAll(m);
			ret.put("success", true);
		} else {
			ret.put("success", false);
			ret.put("error",r);
		}
		return ret;
	}
}
