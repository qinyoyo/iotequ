package top.iotequ.svas.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.iotequ.svas.service.SvasServer;
import top.iotequ.svas.service.SvasTypes;


@RestController
public class CofferInterfaceController {

	@Autowired
	SvasServer svasServer;

	int getIntFromMap(Map<String,Object> m,String key) {
		if (m==null) return 0;
		Object o = m.get(key);
		if (o==null) return 0;
		try {
			return (Integer)o;
		} catch (Exception e) {
			return 0;
		}
	}
	boolean getBoolFromMap(Map<String,Object> m,String key) {
		if (m==null) return false;
		Object o = m.get(key);
		if (o==null) return false;
		try {
			return (Boolean)o;
		} catch (Exception e) {
			return false;
		}
	}
	String getStringFromMap(Map<String,Object> m,String key) {
		if (m==null) return "";
		Object o = m.get(key);
		if (o==null) return "";
		try {
			return (String)o;
		} catch (Exception e) {
			return "";
		}
	}
    @RequestMapping(value = { "/vein/add" })
    public Map<String,Object> registDyna(String templates,Integer fingerNo,String idNumber,
										 String name) {
		Map<String,Object> result = new HashMap<>();
        result.put("status", 200);
        result.put("success", false);
        try {
        	StringBuffer errs = new StringBuffer();
        	if (templates==null || templates.isEmpty() || templates.length() != 1728) {
            	errs.append("templates，请输入正确的指静脉模板数据；");
            }
            if (fingerNo==null || (fingerNo != 1 && fingerNo != 2)) {
            	errs.append("fingerNo，请输入正确的手指编号；");
            }
            if (idNumber==null || idNumber.isEmpty() || idNumber.length() >32) {
            	errs.append("idNumber，请输入正确的唯一编号；");
            }
            if(name==null || name.length() >20) {
            	errs.append("name，请输入正确的姓名");
            }
            if(errs.length() > 0) {
            	errs.insert(0, "参数错误：");
            	result.put("message", errs.toString());
            	return result;
            }
			Map<String, Object> e = svasServer.svein_matchFinger(templates, null);
			boolean success = getBoolFromMap(e,"success");
			int count = getIntFromMap(e,"count");

            if (success && count == 0) {
				e = getUserNo(idNumber,name);
				success = getBoolFromMap(e,"success");
				if (!success) return e;
				String userNo = getStringFromMap(e,"message");
				e = svasServer.svein_addFinger(userNo, fingerNo, 11, templates, false);;
				success = getBoolFromMap(e,"success");
				if (success)  {
                    result.put("success", true);
                    result.put("message", "新增成功");
                }
                else {
                    result.put("message", "新增失败");
                }
            }
            else {
                result.put("message", "该指静脉已被注册");
            }
        }
        catch (Exception e3) {
            result.put("status", 500);
            result.put("message", e3.getMessage());
            e3.printStackTrace();
        }
        return result;
    }
    
    @RequestMapping(value = "/vein/delete")
    public Map<String,Object>  delDyna(Integer fingerNo,String idNumber) {
		Map<String,Object> result = new HashMap<>();
        result.put("status", 200);
        result.put("success", false);
        try {
        	StringBuffer errs = new StringBuffer();
        	if (fingerNo==null || (fingerNo != 1 && fingerNo != 2)) {
				errs.append("fingerNo，请输入正确的手指编号；");
			}
			if (idNumber==null || idNumber.isEmpty() || idNumber.length() >32) {
				errs.append("idNumber，请输入正确的唯一编号；");
			}
            if(errs.length() > 0) {
            	errs.insert(0, "参数错误：");
            	result.put("message", errs.toString());
            	return result;
            }
			Map<String, Object> e = svasServer.svein_queryUserNo(1, idNumber);
			boolean success = getBoolFromMap(e,"success");
			if (!success) {
				result.put("message", "无法获取用户编号");
				return result;
			}
            String userNo = getStringFromMap(e,"userNo");
            if (userNo == null || userNo.isEmpty()) {
                result.put("message", "无法获取用户编号");
                return result;
            }
			e  = svasServer.svein_removeFinger(userNo, fingerNo);
			success = getBoolFromMap(e,"success");
            if (success) {
                result.put("success", true);
                result.put("message", "删除成功");
            }
            else {
                result.put("message", "删除失败");
            }
        }
        catch (Exception e2) {
            result.put("status", 500);
            result.put("message", e2.getMessage());
            e2.printStackTrace();
        }
        return result;
    }
    
    @RequestMapping(value = "/vein/auth")
    public Map<String,Object> authVein(String templates) {
		Map<String,Object> result = new HashMap<>();
		result.put("status", 200);
		result.put("success", false);
        try {
        	StringBuffer errs = new StringBuffer();
			if (templates==null || templates.isEmpty() || templates.length() <576) {
				errs.append("templates，请输入正确的指静脉模板数据；");
            }
            if(errs.length() > 0) {
            	errs.insert(0, "参数错误：");
            	result.put("message", errs.toString());
            	return result;
            }
			Map<String, Object> e = svasServer.svein_matchFinger(templates, null);
			boolean success = getBoolFromMap(e,"success");
			int count = getIntFromMap(e,"count");
			if (success && count==1) {
				List<SvasTypes.SvasMatchInfo> list = (List<SvasTypes.SvasMatchInfo>)e.get("list");
				result.put("message", list.get(0).idNo);
				result.put("name", list.get(0).name);
				result.put("success", true);
			} else if (count==0){
				result.put("message", "认证失败，没有合适的匹配");
			} else if (count>1){
				result.put("message", "认证失败，多重匹配");
			} else {
				result.put("message", "认证失败");
			}
        }
        catch (Exception e2) {
            result.put("status", 500);
            result.put("message", e2.getMessage());
            e2.printStackTrace();
        }
        return result;
    }
 	@RequestMapping(value = "/vein/getUserNo")
	public Map<String,Object> getUserNo(String idNumber,String name) {
		Map<String,Object> result = new HashMap<>();
		result.put("status", 200);
		result.put("success", false);
		try {
			StringBuffer errs = new StringBuffer();
			if (idNumber==null || idNumber.isEmpty() || idNumber.length() >32) {
				errs.append("idNumber，请输入正确的唯一编号；");
			}
			if(name==null || name.length() >20) {
				errs.append("name，请输入正确的姓名");
			}
			if(errs.length() > 0) {
				errs.insert(0, "参数错误：");
				result.put("message", errs.toString());
				return result;
			}
			Map<String, Object> e = svasServer.svein_getUserNo(1, idNumber, name, null, null);
			boolean success = getBoolFromMap(e,"success");
			if (!success) {
				result.put("message", "无法获取用户编号");
				return result;
			}
			String userNo = getStringFromMap(e,"userNo");
			if (userNo == null || userNo.isEmpty()) {
				result.put("message", "无法获取用户编号");
				return result;
			}
			result.put("success", true);
			result.put("message", userNo);
		}
		catch (Exception e3) {
			result.put("status", 500);
			result.put("message", e3.getMessage());
			e3.printStackTrace();
		}
		return result;
	}
}