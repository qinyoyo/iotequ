package top.iotequ.checkIn.controller;
import top.iotequ.checkIn.pojo.CkRegister;
import top.iotequ.checkIn.dao.CkRegisterDao;
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
import top.iotequ.util.*;
import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/check-in/ckRegister")
public class CkRegisterController  {
	private static final Logger log = LoggerFactory.getLogger(CkRegisterController.class);
	@Autowired
	ICgService<CkRegister> cgService;
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
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, CkRegister ckRegister,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(cgService.getDictionary(ckRegister,true, requestDynaFields)).toResponse();
			else {
				ckRegister = cgService.getDefaultObject(ckRegister);
                return new RestJson().data(ckRegister)
                    .dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? cgService.getDictionary(ckRegister,true,null) : null).toResponse();
			}
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, CkRegister ckRegister, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				return j.dictionary(cgService.getDictionary(ckRegister,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(ckRegister,true,null));
			if (!Util.isEmpty(id)) ckRegister=cgService.getRecord(id,request);
			j.data(ckRegister);
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
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, CkRegister ckRegister, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(idSaved) || Util.isEmpty(ckRegister.getId()));
			return 	cgService.doSave(isNew, flowCode,total_filepart, ckRegister, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<Map<String,Object>> ckRegisterList, HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.updateSelective(ckRegisterList).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}

	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			cgService.export(null,0,100, null, "ckRegister.xlsx", "ckRegister", sort, order, search, request, response);
			return null;
		} catch (Exception e) {
			response.reset();
			response.setContentType("text/json; charset=UTF-8");
			return new RestJson().setMessage(e).toResponse();
		} finally {
			Util.setProgress(100);
		}
	}
	@RequestMapping(value = "/action/register")
	public ResponseEntity<Map<String, Object>> actionRegister(String id,HttpServletRequest request, HttpServletResponse response){
		try {
		  return cgService.doAction("register",id,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
}