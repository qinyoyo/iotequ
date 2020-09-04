package top.iotequ.attendance.controller;
import top.iotequ.attendance.pojo.AdTest;
import top.iotequ.attendance.dao.AdTestDao;
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
@RequestMapping("/attendance/adTest")
public class AdTestController  {
	private static final Logger log = LoggerFactory.getLogger(AdTestController.class);
	@Autowired
	ICgService<AdTest> cgService;
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean queryFlowProcess, Boolean needLoadDictionary,String resortFirstField, Integer pageSize,Integer pageNumber,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		try {
			if (Objects.nonNull(queryFlowProcess) && queryFlowProcess) return cgService.getFlowProcessData(request.getParameter("flowId")).toResponse();
			else return cgService.getListPageData(needLoadDictionary,resortFirstField, pageSize, pageNumber, sort, order, search, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/default",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, AdTest adTest,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(cgService.getDictionary(adTest,true, requestDynaFields)).toResponse();
			else {
				adTest = cgService.getDefaultObject(adTest);
                return new RestJson().data(adTest)
                    .dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? cgService.getDictionary(adTest,true,null) : null).toResponse();
			}
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, AdTest adTest, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				return j.dictionary(cgService.getDictionary(adTest,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(adTest,true,null));
			if (!Util.isEmpty(id)) adTest=cgService.getRecord(id,request);
			j.data(adTest);
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
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, AdTest adTest, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(idSaved) || Util.isEmpty(adTest.getId()));
			return 	cgService.doSave(isNew, flowCode,total_filepart, adTest, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<AdTest> adTestList, HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.updateSelective(adTestList).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}

}