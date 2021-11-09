package top.iotequ.svas.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.iotequ.svas.service.SvasServer;
import top.iotequ.svas.IotequModule;
@SuppressWarnings("static-access")
@RestController
@RequestMapping("/svas")
public class SvasController {
	@Autowired
	SvasServer svasServer;
	@Autowired
	IotequModule version;

	@RequestMapping(value = "/version")
	public Map<String,Object> version(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = svasServer.svein_getVersion();
		if (map!=null) {
			map.put("module", version.getName());
			map.put("groupId", version.getGroupId());
			map.put("moduleVersion", version.getVersion());
			map.put("buildTime", version.getBuildTime());
		}
		return map;
	}
	@RequestMapping(value = "/licence")
	public Map<String,Object> licence(Boolean available, HttpServletRequest request, HttpServletResponse response) {
		if (available!=null && available) return svasServer.svein_getLicenceAvailable();
		else return svasServer.svein_getLicence();
	}	
	@RequestMapping(value = "/trialDays")
	public Map<String,Object> trialDays(HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getTrialDays();
	}		
	@RequestMapping(value = "/setThresh")
	public Map<String,Object> setThresh(Integer thresh,HttpServletRequest request, HttpServletResponse response) {
		if (thresh!=null && thresh>0 && thresh<=1000) svasServer.THRESH=thresh;
		Map<String,Object> map=new HashMap<>();
		map.put("thresh",svasServer.THRESH);
		return map;
	}
	@RequestMapping(value = "/getUserNo")
	public Map<String,Object> getUserNo(String idNo,Integer idType,String name,String def,String prefix,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getUserNo(idType, idNo, name,def,prefix);
	}
	@RequestMapping(value = "/queryUserNo")
	public Map<String,Object> queryUserNo(String idNo,Integer idType,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_queryUserNo(idType, idNo);
	}
	@RequestMapping(value = "/getUserNoFromDict")
	public Map<String,Object> getUserNoFromDict(String template,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getUserNoFromDict(template);
	}

	@RequestMapping(value = "/setUserNoForDict")
	public Map<String,Object> setUserNoForDict(String template,String userNo, HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_setUserNoForDict(template,userNo);
	}

	@RequestMapping(value = "/getUserInfo")
	public Map<String,Object> getUserInfo(String userNo,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getUserInfo(userNo);
	}
	@RequestMapping(value = "/getUserAllInfo")
	public Map<String,Object> getUserAllInfo(String userNo,Boolean includePhoto, HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getUserAllInfo(userNo,includePhoto==null ? false : includePhoto);
	}
	@RequestMapping(value = "/changeUserInfo")
	public Map<String,Object> modify(String userNo,String idNo,Integer idType,String name,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_changeUserInfo(userNo, idType, idNo, name);
	}
	@RequestMapping(value = "/changeUserInfoBy")
	public Map<String,Object> modifyBy(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,String> map = new HashMap<>();
		String v = request.getParameter("userNo");
		if (v!=null) map.put("userNo",v);

		v = request.getParameter("name");
		if (v!=null) map.put("name",v);

		v = request.getParameter("sex");
		if (v!=null) map.put("sex",v);

		v = request.getParameter("birthDate");
		if (v!=null) map.put("birthDate",v);

		v = request.getParameter("mobilePhone");
		if (v!=null) map.put("mobilePhone",v);

		v = request.getParameter("email");
		if (v!=null) map.put("email",v);

		v = request.getParameter("wechatOpenid");
		if (v!=null) map.put("wechatOpenid",v);

		v = request.getParameter("idType");
		if (v!=null) map.put("idType",v);

		v = request.getParameter("idType");
		if (v!=null) map.put("idType",v);

		v = request.getParameter("idType");
		if (v!=null) map.put("idType",v);

		v = request.getParameter("idType");
		if (v!=null) map.put("idType",v);

		v = request.getParameter("idNo");
		if (v!=null) map.put("idNo",v);

		v = request.getParameter("idNation");
		if (v!=null) map.put("idNation",v);

		v = request.getParameter("homeAddr");
		if (v!=null) map.put("homeAddr",v);

		return svasServer.svein_changeUserInfoByMap(map);
	}
	@RequestMapping(value = "/removeUserNo")
	public Map<String,Object> removeUserNo(String userNo,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_removeUserNo(userNo);
	}
	
	@RequestMapping(value = "/getTemplates")
	public Map<String,Object> getTemplates(String userNo,Integer fingerNo,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getTemplates(userNo,fingerNo);
	}
	@RequestMapping(value = "/getFingerCount")
	public Map<String,Object> getFingerCount(String userNo,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getFingerCount(userNo);
	}
	@RequestMapping(value = "/getFingerInfo")
	public Map<String,Object> svein_getFingerInfo(String userNo,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getFingerInfo(userNo);
	}
	@RequestMapping(value = "/remove")
	public Map<String,Object> remove(String userNo,Integer fingerNo,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_removeFinger(userNo, fingerNo);
	}
	@RequestMapping(value = "/update")
	public Map<String,Object> update(String userNo,Integer fingerNo,Integer fingerType,String fingerName, String templates,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_updateFinger(userNo, fingerNo,
				fingerName==null?(fingerType==null?"":fingerType.toString()) : fingerName,   // 保留fingerType 兼容以前版本
				templates);
	}
	@RequestMapping(value = "/add")
	public Map<String,Object> add(String userNo,Integer fingerNo,String fingerType,String fingerName, String templates,Boolean warning,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_addFinger(userNo, fingerNo,
				fingerName==null?(fingerType==null?"":fingerType.toString()) : fingerName,   // 保留fingerType 兼容以前版本
				templates, warning);
	}
	@RequestMapping(value = "/set")
	public Map<String,Object> set(String userNo,String fingerType1, String fingerName1, Boolean warning1, String templates1, String fingerType2, String fingerName2, Boolean warning2, String templates2,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_setFingers(userNo,
				fingerName1==null?(fingerType1==null?"":fingerType1.toString()) : fingerName1,   // 保留fingerType 兼容以前版本
				warning1,templates1,
				fingerName2==null?(fingerType2==null?"":fingerType2.toString()) : fingerName2,   // 保留fingerType 兼容以前版本
				warning2,templates2);
	}
	@RequestMapping(value = "/photo")
	public Map<String,Object> photo(String userNo,String photo,HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_setPhoto(userNo, photo);
	}
	@RequestMapping(value = "/auth")
	public Map<String,Object> auth(String template,Integer thresh,HttpServletRequest request, HttpServletResponse response) {
		if (thresh==null || thresh<=0 || thresh>1000) thresh=svasServer.THRESH;   //  使用默认阈值
		return svasServer.svein_matchFinger(template,thresh);
	}
	@RequestMapping(value = "/env")
	public Map<String,Object> env(HttpServletRequest request, HttpServletResponse response) {
		return svasServer.svein_getEnvProperties();
	}

	@RequestMapping(value = "/init")
	public Map<String,Object> init(String password,HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map=new HashMap<>();
		int r = ("123456".equals(password)) ? svasServer.svas_initial() : 9;
		if (r==0) map.put("success", true);
		else {
			map.put("success", false);
			map.put("error", r);
		}
		return map;
	}
}
