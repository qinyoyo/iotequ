package top.iotequ.attendance.employee.controller;
import top.iotequ.attendance.employee.pojo.AdEmployee;
import top.iotequ.attendance.employee.dao.AdEmployeeDao;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.iotequ.framework.service.ICgService;
import top.iotequ.framework.util.*;
import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/attendance/employee/adEmployee")
public class AdEmployeeController  {
	private static final Logger log = LoggerFactory.getLogger(AdEmployeeController.class);
	@Autowired
	ICgService<AdEmployee> cgService;
	@Autowired
	private AdEmployeeDao adEmployeeDao;
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean queryFlowProcess, Boolean needLoadDictionary,String resortFirstField, Integer pageSize,Integer pageNumber,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		try {
			if (Objects.nonNull(queryFlowProcess) && queryFlowProcess) return cgService.getFlowProcessData(request.getParameter("flowId")).toResponse();
			else return cgService.getListPageData(needLoadDictionary,resortFirstField, pageSize, pageNumber, sort, order, search, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, AdEmployee adEmployee, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				return j.dictionary(cgService.getDictionary(adEmployee,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(adEmployee,true,null));
			if (!Util.isEmpty(id)) adEmployee=cgService.getRecord(id,request);
			j.data(adEmployee);
			return j.toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/download",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> download(String id,String field,String fileName,HttpServletResponse response) throws Exception {
		try {
			cgService.download(field,id,fileName,response);
			return null;
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/save",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, AdEmployee adEmployee, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(idSaved));
			if (!isNew) {
				AdEmployee obj = adEmployeeDao.realSelect(String.valueOf(idSaved));
				isNew = (obj==null);
			}
			return 	cgService.doSave(isNew, flowCode,total_filepart, adEmployee, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<AdEmployee> adEmployeeList, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Boolean> isNewList=new ArrayList<>();
			for (AdEmployee obj : adEmployeeList) {
				isNewList.add(adEmployeeDao.realSelect(obj.getId()) == null);
			}
			return cgService.updateSelective(adEmployeeList, isNewList).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}

	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			cgService.export(null,0,100, null, "adEmployee.xlsx", "adEmployee", sort, order, search, request, response);
			return null;
		} catch (Exception e) {
			response.reset();
			response.setContentType("text/json; charset=UTF-8");
			return new RestJson().setMessage(e).toResponse();
		} finally {
			Util.setProgress(100);
		}
	}
}