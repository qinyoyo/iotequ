package top.iotequ.checkIn.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;
//import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.util.StringUtil;

import net.sf.json.JSONObject;

import top.iotequ.checkIn.dao.CkRegisterDao;
import top.iotequ.checkIn.pojo.CkRegister;
import top.iotequ.framework.event.DeviceEvent;
import top.iotequ.framework.pojo.Org;
import top.iotequ.reader.dao.DevPeopleDao;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.svasclient.SvasService;
import top.iotequ.svasclient.SvasTypes;
import top.iotequ.util.DateUtil;
import top.iotequ.util.SqlUtil;
import top.iotequ.util.Util;


@Controller
public class CofferInterfaceController {

	/*
	 * @Autowired private OrgDao sysOrgDao;
	 * 
	 * @Autowired private DevPeopleDao devPeopleDao;
	 * 
	 * @Autowired private DevVeinInfoDao devVeinInfoDao;
	 */
	
	@Autowired private DevPeopleDao devPeopleDao;
	@Autowired
	private CkRegisterDao ckRegisterDao;

	@Autowired
	private SvasService userNoService;
	private Pattern idNumberP = Pattern.compile("^[a-zA-Z0-9]{0,32}$");
	private Pattern templatesP = Pattern.compile("^[a-zA-Z0-9]{1728}$");
	private Pattern fingerNoP = Pattern.compile("^[0-9]{1}$");
	private Pattern idTypeP = Pattern.compile("^-?[1-9]\\d*$");
	
	private Random random = new Random();
	
	/*
	 * @RequestMapping(value={"/common/people/add"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject addPerson(@Validated({DevPeople.newG.class})
	 * DevPeople devPeople, BindingResult pbr) { JSONObject data = new JSONObject();
	 * 
	 * data.put("status", 200); data.put("success", false);
	 * 
	 * try { if (pbr.hasErrors()) { List<FieldError> el = pbr.getFieldErrors();
	 * StringBuffer sb = new StringBuffer("参数错误："); for (FieldError e : el) {
	 * sb.append(e.getField()).append("，").append(e.getDefaultMessage()).append("；")
	 * ; } data.put("message", sb.toString()); } else { Org org =
	 * sysOrgDao.select(devPeople.getOrgId()); if (org == null) {
	 * data.put("message", "参数错误：orgId，组织机构不存在"); return data; } DevPeople p0 =
	 * devPeopleDao.selectByIdNumber(devPeople.getIdNumber()); if (p0 != null) {
	 * data.put("message", "该身份证已被注册"); return data; } userNoService.getUserNo(1,
	 * devPeople.getIdNumber(), null, devPeople.getRealName(), ""); if
	 * (Util.isEmpty(uno)) { data.put("message", "无法创建用户编号"); return data; }
	 * devPeople.setUserNo(uno); devPeople.setRegTime(new Date()); int insert =
	 * devPeopleDao.insertSelective(devPeople); data.put("success", insert > 0);
	 * data.put("message", insert > 0 ? "新增成功" : "新增失败"); } } catch (Exception e) {
	 * e.printStackTrace(); data.put("success", false); data.put("message",
	 * e.getMessage()); }
	 * 
	 * return data; }
	 * 
	 * @RequestMapping(value={"/common/people/update"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject
	 * updatePerson(@Validated({DevPeople.newGU.class}) DevPeople devPeople,
	 * BindingResult pbr) { JSONObject result = new JSONObject();
	 * 
	 * try { result.put("status", 200); result.put("success", false); if
	 * (pbr.hasErrors()) { List<FieldError> el = pbr.getFieldErrors(); StringBuffer
	 * sb = new StringBuffer("参数错误："); for (FieldError e : el) {
	 * sb.append(e.getField()).append("，").append(e.getDefaultMessage()).append("；")
	 * ; } result.put("message", sb.toString()); } else { DevPeople p =
	 * devPeopleDao.selectByIdNumber(devPeople.getIdNumber()); if (p == null) {
	 * result.put("message", "更新失败，未找到指定用户"); return result; } Org o =
	 * sysOrgDao.select(devPeople.getOrgId()); if (o == null) {
	 * result.put("message", "参数错误：orgId，组织机构不存在"); return result; } if
	 * (devPeople.getMobilePhone() != null) { DevPeople mp =
	 * devPeopleDao.selectByMobilePhone(devPeople.getMobilePhone()); if ((mp !=
	 * null) && (!mp.getIdNumber().equals(devPeople.getIdNumber()))) {
	 * result.put("message", "参数错误：mobilePhone，该号码已被注册"); return result; } }
	 * devPeople.setEmployeeNo(null); devPeople.setRegFingers(null);
	 * devPeople.setRegTime(null); devPeople.setUserNo(p.getUserNo()); int res =
	 * devPeopleDao.updateSelective(devPeople); result.put("success", res > 0);
	 * result.put("message", res > 0 ? "更新成功" : "更新失败"); } } catch (Exception e) {
	 * e.printStackTrace(); result.put("status", 500); result.put("message",
	 * e.getMessage()); }
	 * 
	 * return result; }
	 * 
	 * @RequestMapping(value={"/common/people/delete"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject deletePerson(String idNumber) { JSONObject
	 * result = new JSONObject(); try { result.put("status", 200);
	 * result.put("success", false); if (StringUtils.isEmpty(idNumber)) {
	 * result.put("message", "参数错误：idNumber，身份证号码不能为空"); return result; } String
	 * userNo = userNoService.getUserNo(1, idNumber, null, StringUtils.EMPTY, null);
	 * if (userNo != null) { List<SvasClient.Templates> fs =
	 * userNoService.getFingerInfo(userNo); for (SvasClient.Templates t : fs) {
	 * userNoService.removeTemplate(userNo, t.fingerNo); } }
	 * devVeinInfoDao.deleteBy(userNo); int res =
	 * devPeopleDao.deleteByIdNumber(idNumber); result.put("success", res > 0);
	 * result.put("message", res > 0 ? "删除成功" : "用户不存在"); } catch (Exception e) {
	 * e.printStackTrace(); result.put("status", 500); result.put("message",
	 * e.getMessage()); }
	 * 
	 * return result; }
	 * 
	 * @RequestMapping(value={"/common/people/get"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject getPerson(String idNumber) { JSONObject
	 * result = new JSONObject(); try { result.put("status", 200);
	 * result.put("success", false); Map<String, Object> datas = new HashMap(); if
	 * (StringUtils.isEmpty(idNumber)) { result.put("message",
	 * "参数错误：idNumber，身份证号码不能为空"); return result; } DevPeople p =
	 * devPeopleDao.selectByIdNumber(idNumber); if (p != null) { p.setCardNo(null);
	 * p.setDevPassword(null); p.setEmail(null); p.setEmployeeNo(null);
	 * p.setIcon(null); p.setIdType(null); p.setRegisterType(null);
	 * p.setUserNo(null); p.setUserType(null);
	 * 
	 * JSONObject pj = JSONObject.fromObject(p);
	 * 
	 * pj.remove("cardNo"); pj.remove("devPassword"); pj.remove("email");
	 * pj.remove("employeeNo"); pj.remove("icon"); pj.remove("idType");
	 * pj.remove("registerType"); pj.remove("userNo"); pj.remove("userType");
	 * pj.remove("idValidDate"); datas.put("people", pj); if (pj.get("birthDay") ==
	 * null) pj.remove("birthDate"); String userNo = userNoService.getUserNo(1,
	 * idNumber, null, StringUtils.EMPTY, null); if (userNo != null) {
	 * List<SvasClient.Templates> fs = userNoService.getFingerInfo(userNo); if
	 * (fs.size() > 0) datas.put("veins",
	 * com.alibaba.fastjson.JSONArray.toJSON(fs)); else datas.put("veins", new
	 * JSONObject[0]); result.put("data", datas); result.put("success", true);
	 * result.put("message", "查询成功"); } else { result.put("message", "获取用户信息失败"); }
	 * } else { result.put("message", "用户不存在"); } } catch (Exception e) {
	 * e.printStackTrace(); result.put("status", 500); result.put("message",
	 * e.getMessage()); }
	 * 
	 * return result; }
	 * 
	 * @RequestMapping(value={"/common/org/list"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject queryOrgs() { JSONObject result = new
	 * JSONObject(); result.put("status", 200); result.put("success", false); try {
	 * List<Org> pos = sysOrgDao.listBy(" parent is NULL ", null);
	 * net.sf.json.JSONArray posa = net.sf.json.JSONArray.fromObject(pos); for
	 * (Object e : posa) { ((JSONObject) e).remove("address"); ((JSONObject)
	 * e).remove("code"); ((JSONObject) e).remove("fax"); ((JSONObject)
	 * e).remove("phone"); ((JSONObject) e).remove("roleList");
	 * groupSubOrgs((JSONObject)e, ((JSONObject)e).getInt("id")); }
	 * result.put("data", posa); result.put("success", true); } catch (Exception e)
	 * { result.put("message", e.getMessage()); } return result; }
	 * 
	 * @RequestMapping(value={"/common/org/add"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject addOrg(@Validated({Org.newO.class}) Org org,
	 * BindingResult obr) { JSONObject data = new JSONObject();
	 * 
	 * data.put("status", 200); data.put("success", false);
	 * 
	 * try { if (obr.hasErrors()) { List<FieldError> el = obr.getFieldErrors();
	 * StringBuffer sb = new StringBuffer("参数错误："); for (FieldError e : el) {
	 * sb.append(e.getField()).append("，").append(e.getDefaultMessage()).append("；")
	 * ; } data.put("message", sb.toString()); return data; } else { if
	 * (org.getParent() != null) { Org query = sysOrgDao.select(org.getParent()); if
	 * (query == null) { data.put("message", "参数错误：parent，父级组织机构不存在"); return data;
	 * } } List<Org> ql = sysOrgDao.listBy(" name = '" + org.getName() + "'", null);
	 * if (ql.size() > 0) { data.put("message", "参数错误：name，该名称已被使用"); return data; }
	 * org.setCode(getOrgCode(org.getParent())); org.setId(null); int insert =
	 * sysOrgDao.insertSelective(org); data.put("success", insert > 0);
	 * data.put("message", insert > 0 ? sysOrgDao.selectLastInsertId() : "新增失败"); }
	 * } catch (Exception e) { e.printStackTrace(); data.put("success", false);
	 * data.put("message", e.getMessage()); }
	 * 
	 * return data; }
	 * 
	 * @RequestMapping(value={"/common/org/update"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject updateOrg(@Validated({Org.newO.class}) Org
	 * org, BindingResult obr) { JSONObject data = new JSONObject();
	 * 
	 * data.put("status", 200); data.put("success", false);
	 * 
	 * try { if (obr.hasErrors()) { List<FieldError> el = obr.getFieldErrors();
	 * StringBuffer sb = new StringBuffer("参数错误："); for (FieldError e : el) {
	 * sb.append(e.getField()).append("，").append(e.getDefaultMessage()).append("；")
	 * ; } data.put("message", sb.toString()); return data; } else { if (org.getId()
	 * == null) { data.put("message", "参数错误：id，组织机构ID不能为空"); return data; } Org o0 =
	 * sysOrgDao.select(org.getId()); if (o0 == null) { data.put("message",
	 * "更新失败，未找到指定组织机构"); return data; } if (org.getParent() != null) { Org o1 =
	 * sysOrgDao.select(org.getParent()); if(null == o1) { data.put("message",
	 * "参数错误：parent，父级组织机构不存在"); return data; } } if
	 * (!o0.getName().equals(org.getName())) { List<Org> ql =
	 * sysOrgDao.listBy(" name = '" + org.getName() + "'", null); if (ql.size() > 0)
	 * { data.put("message", "参数错误：name，该名称已被使用"); return data; } }
	 * org.setCode(null); org.setRoleList(null); int update =
	 * sysOrgDao.updateSelective(org); data.put("success", Boolean.valueOf(update >
	 * 0)); data.put("message", update > 0 ? "更新成功" : "更新失败"); } } catch (Exception
	 * e) { e.printStackTrace(); data.put("success", false); data.put("message",
	 * e.getMessage()); }
	 * 
	 * return data; }
	 * 
	 * @RequestMapping(value={"/common/org/delete"}, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ResponseBody public JSONObject deleteOrg(String id) { JSONObject result =
	 * new JSONObject();
	 * 
	 * result.put("status", 200); result.put("success", false); try { if ((id ==
	 * null) || ("".equals(id))) { result.put("message", "参数错误：id，组织机构ID不能为空");
	 * return result; } Integer nid = null; try { nid =
	 * Integer.valueOf(Integer.parseInt(id)); } catch (Exception localException1) {}
	 * 
	 * if (nid == null) { result.put("message", "参数错误：id，请输入正确的组织机构ID"); return
	 * result; } List<DevPeople> ql = devPeopleDao.listBy(" org_id = " + id, null);
	 * if (ql.size() > 0) { result.put("message", "该组织机构下存在人员信息，请先删除人员信息"); return
	 * result; } List<Integer> subs = new ArrayList(); getSubOrgs(subs, nid); if
	 * (subs.size() > 0) { result.put("message", "该组织机构下存在子机构，请先删除子机构"); return
	 * result; } int res = sysOrgDao.delete(nid); if (res > 0) {
	 * result.put("success", true); result.put("message", "删除成功"); } else {
	 * result.put("message", "组织机构不存在"); } } catch (Exception e) {
	 * e.printStackTrace(); result.put("status", 500); result.put("message",
	 * e.getMessage()); }
	 * 
	 * return result; }
	 */

	/*
	 * @RequestMapping(value = "/common/index") public ModelAndView index() { return
	 * new ModelAndView("/demo"); }
	 */

    @RequestMapping(value = { "/vein/add" }, produces = { "application/json" })
    @ResponseBody
    public JSONObject registDyna(@RequestBody String params) {
        JSONObject result = new JSONObject();
        result.put("status", 200);
        result.put("success", false);
        try {
        	StringBuffer errs = new StringBuffer();
        	JSONObject paramsJson = JSONObject.fromObject(params);
        	if (!paramsJson.containsKey("templates") || StringUtil.isEmpty(paramsJson.getString("templates")) || paramsJson.getString("templates").length() != 1728) {
            	errs.append("templates，请输入正确的指静脉模板数据；");
            }
            if (!paramsJson.containsKey("fingerNo") || (paramsJson.getInt("fingerNo") != 1 && paramsJson.getInt("fingerNo") != 2)) {
            	errs.append("fingerNo，请输入正确的手指编号；");
            }
            if (!paramsJson.containsKey("idNumber") || paramsJson.getString("idNumber").length() >32) {
            	errs.append("idNumber，请输入正确的唯一编号；");
            }
            if(!paramsJson.containsKey("realName") || paramsJson.getString("realName").length() >20) {
            	errs.append("realName，请输入正确的姓名");
            }
            if(errs.length() > 0) {
            	errs.insert(0, "参数错误：");
            	result.put("message", errs.toString());
            	return result;
            }
            SvasTypes.SvasMatched e2 = userNoService.auth(paramsJson.getString("templates"),0);
            if (e2.count == 0) {
                String userNo = this.userNoService.getUserNo(Integer.valueOf(1), paramsJson.getString("idNumber"), StringUtils.EMPTY, StringUtils.EMPTY, "0100");
                if (userNo == null) {
                    result.put("message", "无法获取用户编号");
                    return result;
                }
                boolean addTemplate = userNoService.addTemplate(userNo, paramsJson.getInt("fingerNo"), paramsJson.getInt("fingerNo"), paramsJson.getString("templates"), false);
                // 增加dev_people人员信息
                boolean hasP = SqlUtil.sqlExist(false, "select * from dev_people where id_number = ? and user_no = ?", paramsJson.getString("idNumber"), userNo);
                if(!hasP) {
                	DevPeople person = new DevPeople();
                	person.setRealName(paramsJson.getString("realName"));
                	person.setRealName("默认用户");
                	person.setIdType(1);
                	person.setIdNumber(paramsJson.getString("idNumber"));
                	person.setUserNo(userNo);
                	person.setUserType(2);
                	person.setOrgCode(-1);
                	person.setRegisterType(1);
                	person.setRegTime(new Date());
                	devPeopleDao.insert(person);
                }
                userNoService.changeUserInfo(userNo, null, null, paramsJson.getString("realName"));
                int fingerCount = userNoService.getFingerCount(userNo);
                SqlUtil.sqlExecute("update dev_people set reg_fingers = ?,real_name = ? where user_no = ?", fingerCount, paramsJson.getString("realName"), userNo);
                if (addTemplate) {
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
    
    @RequestMapping(value = { "/vein/delete" }, produces = { "application/json" })
    @ResponseBody
    public JSONObject delDyna(@RequestBody String params) {
        JSONObject result = new JSONObject();
        result.put("status", 200);
        result.put("success", false);
        try {
        	StringBuffer errs = new StringBuffer();
        	JSONObject paramsJson = JSONObject.fromObject(params);
            if (!paramsJson.containsKey("fingerNo") || (paramsJson.getInt("fingerNo") != 1 && paramsJson.getInt("fingerNo") != 2)) {
            	errs.append("fingerNo，请输入正确的手指编号；");
            }
            if (!paramsJson.containsKey("idNumber") || paramsJson.getString("idNumber").length() >32) {
            	errs.append("idNumber，请输入正确的唯一编号；");
            }
            if(errs.length() > 0) {
            	errs.insert(0, "参数错误：");
            	result.put("message", errs.toString());
            	return result;
            }
            String userNo = userNoService.getUserNo(1, paramsJson.getString("idNumber"), StringUtils.EMPTY, StringUtils.EMPTY, "0100");
            if (userNo == null) {
                result.put("message", "无法获取用户编号");
                return result;
            }
            String template = null;
            try {
                template = userNoService.getTemplate(userNo, paramsJson.getInt("fingerNo"));
            }
            catch (Exception ex) {}
            if (StringUtils.isEmpty(template)) {
                result.put("message", "该用户没有指定指静脉数据");
                return result;
            }
            boolean removed = userNoService.removeTemplate(userNo, paramsJson.getInt("fingerNo"));
            int fingerCount = userNoService.getFingerCount(userNo);
            SqlUtil.sqlExecute("update dev_people set reg_fingers = ? where user_no = ?", fingerCount, userNo);
            if (removed) {
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
    
    @RequestMapping(value = { "/vein/auth" }, produces = { "application/json" })
    @ResponseBody
    public JSONObject authVein(@RequestBody String params) {
        JSONObject result = new JSONObject();
        result.put("status", 200);
        result.put("success", false);
        try {
        	StringBuffer errs = new StringBuffer();
        	JSONObject paramsJson = JSONObject.fromObject(params);
        	if (!paramsJson.containsKey("templates") || StringUtil.isEmpty(paramsJson.getString("templates")) || paramsJson.getString("templates").length() != 576) {
            	errs.append("templates，请输入正确的指静脉模板数据；");
            }
        	if(!paramsJson.containsKey("devNo") || StringUtil.isEmpty(paramsJson.getString("devNo")) || paramsJson.getString("devNo").length() <= 0){
        		errs.append("devNo，请输入正确的设备编号；");
        	}
            if(errs.length() > 0) {
            	errs.insert(0, "参数错误：");
            	result.put("message", errs.toString());
            	return result;
            }
			SvasTypes.SvasMatched e = userNoService.auth(paramsJson.getString("templates"), 0);
            if (e.count == 0) {
                result.put("message", "未找到匹配的用户信息");
                return result;
            }
			SvasTypes.SvasMatchInfo mi = e.list.get(0);
			SvasTypes.SvasUserInfo ui = userNoService.getUserInfo(mi.userNo);

			Integer orgCode = SqlUtil.sqlQueryInteger(false,"select org_code from dev_reader_group g, dev_reader r where r.reader_group = g.id and r.reader_no = ?",paramsJson.getString("devNo"));
			if (orgCode==null) throw new Exception("设备配置不正确");
			DevPeople people = devPeopleDao.select(ui.userNo);
			if (people==null) throw new Exception("人员配置不存在");;

			Date now = new Date();
			CkRegister rec = ckRegisterDao.selectByUserNoOrgCodeInDate(people.getUserNo(),orgCode,DateUtil.startOf(now,DateUtil.DAY));
			if (rec!=null) {
				Date lastTime =  (rec.getOffTime()==null ? rec.getOnTime() : rec.getOffTime());
				int lt = DateUtil.get(lastTime,DateUtil.HOUR) * 60 + DateUtil.get(lastTime,DateUtil.MINUTE);
				int nw = DateUtil.get(now,DateUtil.HOUR) * 60 + DateUtil.get(now,DateUtil.MINUTE);
				if (nw - lt < 5) {
					result.put("name", rec.getName());
					throw new Exception("请勿频繁打卡");
				}
			}

			DeviceEvent event = new DeviceEvent(this);
			event.setDeviceType("C20");
			event.setDeviceNo(paramsJson.getString("devNo"));
			event.setDeviceMode("AD");
			event.setTime(new Date());
			event.setUserNo(ui.userNo);
			event.setWarning(false);
			event.setTemplate(paramsJson.getString("templates"));
			if (paramsJson.containsKey("image") && !StringUtil.isEmpty(paramsJson.getString("image")))
				event.setImage(paramsJson.getString("image"));
			event.put("authType", (byte)0);
			event.put("auditeeAuthType", (byte)0);
			Util.getApplicationContext().publishEvent(event);

			result.put("message", ui.idNo);
            result.put("name", ui.name);
            result.put("success", true);
        }
        catch (Exception e2) {
            result.put("status", 500);
            result.put("message", e2.getMessage());
        }
        return result;
    }
    
    public Integer getRandomType() {
    	
    	Integer res = 0;
    	
    	int nextInt = random.nextInt(100);
    	// 挂号
    	if(0 <= nextInt && nextInt < 29) {
    		res = 1;
		// 就诊
    	} else if(29 <= nextInt && nextInt < 52) {
    		res = 2;
		// 缴费
    	} else if(52 <= nextInt && nextInt < 80) {
    		res = 3;
		// 取药
    	} else if(80 <= nextInt && nextInt < 98) {
    		res = 4;
		// 入院
    	} else if(98 <= nextInt && nextInt < 100) {
	    	res = 5;
	    }
    	
    	return res;
    }
//	@RequestMapping(value = "/common/vein/update", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public JSONObject update(@RequestBody JSONObject json, String idNumber, Integer fingerNo, String templates,
//			HttpServletResponse response) {
//		JSONObject result = new JSONObject();
//		result.put("status", 200);
//		result.put("success", false);
//		if (json.containsKey("idNumber"))
//			idNumber = json.getString("idNumber");
//		if (json.containsKey("templates"))
//			templates = json.getString("templates");
//		if (json.containsKey("fingerNo"))
//			fingerNo = json.getInt("fingerNo");
//		if (StringUtils.isEmpty(idNumber)) {
//			result.put("message", "参数错误：idNumber，ID号码不能为空");
//			return result;
//		}
//
//		if (!this.idNumberP.matcher(idNumber).matches()) {
//			result.put("message", "参数错误：idNumber，请输入正确的ID号码（1~32位数字）");
//			return result;
//		}
//
//		if (!this.templatesP.matcher(templates).matches()) {
//			result.put("message", "参数错误：templates，请输入正确的指静脉模板（1728位字符）");
//			return result;
//		}
//
//		if (null == fingerNo || !(fingerNo >= 0 && fingerNo <= 9)) {
//			result.put("message", "参数错误：fingerNo，手指编号错误");
//			return result;
//		}
//		try {
//			String un = userNoService.getUserNo(1, idNumber, StringUtils.EMPTY, StringUtils.EMPTY, "0100");
//			boolean updateTemplate = userNoService.updateTemplate(un, fingerNo, templates);
//			if (updateTemplate) {
//				result.put("success", true);
//				result.put("message", "更新成功");
//			} else {
//				result.put("message", "更新失败");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			result.put("message", e.getMessage());
//		}
//		return result;
//	}
//
	@RequestMapping(value = "/vein/getUserNo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject getUserNo(@RequestBody String params, HttpServletResponse response) {

		JSONObject result = new JSONObject();
		result.put("status", 200);
		result.put("success", false);

		try {
			StringBuffer errs = new StringBuffer();
        	JSONObject paramsJson = JSONObject.fromObject(params);
            if (!paramsJson.containsKey("name") || paramsJson.getString("name").length() > 10) {
            	errs.append("name，请输入正确的姓名；");
            }
            if (!paramsJson.containsKey("idNumber") || paramsJson.getString("idNumber").length() >32) {
            	errs.append("idNumber，请输入正确的唯一编号；");
            }
            if(errs.length() > 0) {
            	errs.insert(0, "参数错误：");
            	result.put("message", errs.toString());
            	return result;
            }

			String userNo = userNoService.getUserNo(1, paramsJson.getString("idNumber"), paramsJson.getString("name"), StringUtils.EMPTY, "0100");
			if (userNo == null) {
				result.put("message", "获取用户编号失败");
				return result;
			} else {
				result.put("success", true);
				result.put("message", userNo);
			}

		} catch (Exception e) {
			result.put("status", 500);
			result.put("message", e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private class Matched {
	}

//	@RequestMapping(value = { "/vein/querybatch" }, produces = { "application/json" })
//    @ResponseBody
//    public JSONObject getVein(@RequestBody String params) {
//        JSONObject result = new JSONObject();
//        result.put("status", 200);
//        result.put("success", false);
//        try {
//        	StringBuffer errs = new StringBuffer();
//        	JSONObject paramsJson = JSONObject.fromObject(params);
//        	if (!paramsJson.containsKey("idNumber") || paramsJson.getString("idNumber").length() >32) {
//        		errs.append("idNumber，请输入正确的唯一编号；");
//        	}
//            if(errs.length() > 0) {
//            	errs.insert(0, "参数错误：");
//            	result.put("message", errs.toString());
//            	return result;
//            }
//            String userNo = userNoService.getUserNo(1, paramsJson.getString("idNumber"), StringUtils.EMPTY, StringUtils.EMPTY, "0100");
//            if (userNo == null) {
//                result.put("message", "无法获取用户编号");
//                return result;
//            } else {
//            	List<Templates> templates = userNoService.getFingerInfo(userNo);
//            	JSONArray arr = new JSONArray();
//                if (templates.size() > 0) {
//                	for(Templates t : templates) {
//                		JSONObject each = new JSONObject();
//                		each.put("fingerNo", t.fingerNo);
//                		each.put("templates", t.templates);
//                		each.put("userNo", userNo);
//                		arr.add(each);
//                	}
//                }
//                result.put("message", arr);
//                result.put("success", true);
//            }
//        }
//        catch (Exception e3) {
//            result.put("status", 500);
//            result.put("message", e3.getMessage());
//            e3.printStackTrace();
//        }
//        return result;
//    }

	/*
	 * String getOrgCode(Integer parent) throws Exception { List<Org> list =
	 * sysOrgDao.listBy("parent=" + parent, "code desc"); if ((list != null) &&
	 * (!list.isEmpty())) { String maxCode = ((Org)list.get(0)).getCode(); int n =
	 * Integer.parseInt(maxCode.substring(maxCode.length() - 2)) + 1; return
	 * maxCode.substring(0, maxCode.length() - 2) + String.format("%02d", new
	 * Object[] { Integer.valueOf(n) }); } if ((parent == null) ||
	 * (parent.intValue() == 0)) { return "A01"; } Org p = sysOrgDao.select(parent);
	 * int c = p.getCode().charAt(p.getCode().length() - 3); c++; char cc = (char)c;
	 * return p.getCode() + cc + "01"; }
	 * 
	 * void getSubOrgs(List<Integer> ids, Integer pid) { List<Org> rgl =
	 * sysOrgDao.listBy(" parent = " + pid, null); if (rgl.size() > 0) { for (Org g
	 * : rgl) { ids.add(g.getId()); getSubOrgs(ids, g.getId()); } } }
	 * 
	 * void groupSubOrgs(JSONObject cs, Integer pid) { List<Org> l =
	 * sysOrgDao.listBy(" parent = " + pid, null); net.sf.json.JSONArray jList =
	 * net.sf.json.JSONArray.fromObject(l); cs.put("children", jList); for (Object j
	 * : jList) { ((JSONObject) j).remove("address"); ((JSONObject)
	 * j).remove("code"); ((JSONObject) j).remove("fax"); ((JSONObject)
	 * j).remove("phone"); ((JSONObject) j).remove("roleList");
	 * groupSubOrgs((JSONObject)j, Integer.valueOf(((JSONObject)j).getInt("id"))); }
	 * }
	 */

}