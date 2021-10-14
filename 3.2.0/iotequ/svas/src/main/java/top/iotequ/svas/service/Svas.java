package top.iotequ.svas.service;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import top.iotequ.svas.service.SvasTypes.*;
import top.iotequ.svas.util.SvasUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

// 性能考虑，不再使用jna
class Svas
{
	public static final String dllMode = "JNI";
	public static final Logger logger = LoggerFactory.getLogger("jni.svas");
	public static Svas instance=new Svas();
	public native  static int  svein_getVersion(JniStringReturn ver);
	public native  static int  svein_getLicence();
	public native  static int  svein_getLicenceAvailable();
	public native  static int  svein_getTrialDays();
	public native  static int  svein_initial(SvasSetting settings);
	public native  static int  svein_getUserNo(int idType, String idNo ,String name, String def,String prefix, JniStringReturn userNo);
	public native  static int  svein_queryUserNo(int idType, String idNo, JniStringReturn userNo);
	public native  static int  svein_getUserNoFromDict(String temp, JniStringReturn userNo);
	public native  static int  svein_setUserNoForDict(String temp, String userNo, JniStringReturn newTemp);
	public native  static int  svein_getUserInfo(String userNo, SvasUserInfo info);
	public native  static int  svein_changeUserInfo(String userNo, int idType, String idNo, String name);
	public native  static int  svein_changeUserInfoByMap(HashMap<String,String> people);
	public native  static int  svein_getUserAllInfo(String userNo, int includePhoto, Map<String,Object> info);
	public native  static int  svein_removeUserNo(String userNo);  
	public native  static int  svein_removeFinger(String userNo, int fingerNo);
	public native  static int  svein_updateFinger(String userNo, int fingerNo, int fingerType, String temp);
	public native  static int  svein_getFingerCount(String userNo,int onlyCountNeed, SvasFingerInfo fingers);
	public native  static int  svein_getTemplates(String userNo, int fingerNo, SvasTemplates temp);
	public native  static int  svein_matchFinger(String temp, int thresh, SvasMatched matched);
	public native  static int  svein_addFinger(String userNo, int fingerNo, int fingerType,String temp, int warning);
	public native  static int  svein_setFingers(String userNo, int type1,int warning1, String hexTemp1,int type2,int warning2,String hexTemp2);
	public native  static int  svein_setPhoto(String userNo, String photo);
	public native  static int  svein_getEnvProperties(JniStringReturn result);
	public static  int initial(SvasSetting settings) throws BeansException {
		if (logger instanceof ch.qos.logback.classic.Logger) {
			((ch.qos.logback.classic.Logger)logger).setLevel(Level.ALL);
		}
		String dll = SvasUtil.dllFile();
		logger.info("Load native libary {} ...",dll);
		System.load(dll);
        int r = svein_initial(settings);
        //apiTest();
        return r;
	}
	public static void logInfo(String s) {
		logger.info(s);
	}
	public static void logDebug(String s) {
		logger.debug(s);
	}
	public static void logError(String s) {
		logger.error(s);
	}
	private static void apiTest() {
/*
		int  r = svein_getVersion(null);
		JniStringReturn ver=new JniStringReturn();
		svein_getVersion(ver);
		r=svein_getLicence();
		r=svein_getLicenceAvailable();
		r=svein_getTrialDays();
		r=svein_initial(null);
		SvasSetting settings=new SvasSetting();
		r=svein_initial(settings);

		JniStringReturn userNo=new JniStringReturn();
		r=svein_getUserNo(0, null ,null, null,null, userNo);
		r=svein_getUserNo(1, "1234" ,null, null,null, userNo);
		r=svein_getUserNo(67, "5678" ,"", null,null, userNo);
		r=svein_getUserNo(5, "56789" ,"ddd", "dd",null, userNo);
		r=svein_getUserNo(5, "566789" ,"ddd", "dd","02dhg", userNo);
		r=svein_getUserNo(1, "56678dsfklgjsdfkgjsdfklgjsdklfgjsdklfgjsdkfgjsdfgsdfgsdfgsdfgdfgdgfsdfgdsfgsdfgsdfgsdfgsdg9" ,
				"精神抖擞的房间卡士大夫爱神的箭饭卡上的房间阿克苏的房间爱神的箭饭卡上单反安居客水电费阿斯蒂芬阿斯蒂芬dd",
				"快递费关键时刻东方闪电反光镜水电费改水电费敢打的沙发斯蒂芬阿斯顿发斯蒂芬撒旦法阿斯蒂芬阿斯蒂芬阿斯蒂芬",
				"时代峰峻阿考虑是否啊撒旦法撒旦法飞阿斯顿发送到发斯蒂芬", userNo);

		r=svein_getUserNoFromDict(null, null);
		r=svein_getUserNoFromDict("null", null);
		userNo=new JniStringReturn();
		r=svein_getUserNoFromDict("null", userNo);
		r=svein_setUserNoForDict(null, null, null);
		r=svein_setUserNoForDict(null, "22", null);
		r=svein_setUserNoForDict("333", "22", new JniStringReturn());

		r=svein_getUserInfo(null, null);
		r=svein_getUserInfo(null, new SvasUserInfo());
		r=svein_getUserInfo("0100000005", new SvasUserInfo());

		r=svein_changeUserInfo(null, 0, null, null);
		r=svein_changeUserInfo("dd", 0, null, null);
		r=svein_changeUserInfo("0100000005", 0, null, null);
		r=svein_changeUserInfo("0100000005", 2, null, null);
		r=svein_changeUserInfo("0100000005", 1, "2222222", null);
		r=svein_changeUserInfo("0100000005", 0, "44444", "changed");



		r=svein_changeUserInfoByMap(null);
		r=svein_changeUserInfoByMap(new HashMap<String,String>());
		r=svein_changeUserInfoByMap(new HashMap<String,String>() {{ put("name","map");}});
		r=svein_changeUserInfoByMap(new HashMap<String,String>() {{ put("name","map"); put("userNo","0100000005");}});



		r=svein_getUserAllInfo(null, 0, null);
		r=svein_getUserAllInfo(null, 0, new HashMap<String,Object>());
		r=svein_getUserAllInfo("0100000005", 0, new HashMap<String,Object>());
		r=svein_getUserAllInfo("0100000005", 1, new HashMap<String,Object>());
		r=svein_getUserAllInfo("ddd", 0, new HashMap<String,Object>());

		r=svein_removeUserNo(null);
		r=svein_removeUserNo("dd");
		r=svein_removeUserNo("0100000005");

		r=svein_removeFinger(null, 0);
		r=svein_removeFinger("ddd", 0);
		r=svein_removeFinger("0100000005", 1);
		r=svein_removeFinger("0100000006", 2);

		r=svein_updateFinger(null, 0, 1, null);
		r=svein_updateFinger(null, 0, 1, null);
		r=svein_updateFinger("ddd", 0, 1, null);
		r=svein_updateFinger("0100000006", 0, 11, null);
		r=svein_updateFinger("0100000006", 1, 11, null);
		r=svein_updateFinger("0100000006", 1, 11, "dddddd");

		r=svein_getFingerCount(null,1, null);
		r=svein_getFingerCount("0100000006",1, null);
		r=svein_getFingerCount("0100000006",1, new SvasFingerInfo());

		r=svein_getTemplates(null, 0, null);
		r=svein_getTemplates("0100000006", 1, null);
		r=svein_getTemplates("0100000006", 1, new SvasTemplates());

		r=svein_matchFinger(null, 0, null);
		r=svein_matchFinger("ddd", 2000, null);
		r=svein_matchFinger("ddd", 2000, new SvasMatched());

		r=svein_addFinger(null, 1, 1,null, 0);
		r=svein_addFinger("0100000006", 1, 1,null, 0);
		r=svein_addFinger("0100000006", 1, 1,"ddddd", 0);
		r=svein_setFingers(null, 1,1, null,2,2,null);
		r=svein_setFingers("0100000006", 1,1, null,2,2,null);
		r=svein_setFingers("0100000006", 1,1, "d",2,2,"d");

		r=svein_setPhoto(null, null);
		r=svein_setPhoto("0100000006", null);
		r=svein_setPhoto("0100000006", "ddd");

		r=svein_getEnvProperties(null);
		r=svein_getEnvProperties(new JniStringReturn());
*/
	}
}






