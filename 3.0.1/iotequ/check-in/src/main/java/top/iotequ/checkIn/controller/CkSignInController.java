package top.iotequ.checkIn.controller;
import top.iotequ.checkIn.pojo.CkSignIn;
import top.iotequ.checkIn.dao.CkSignInDao;
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
@RequestMapping("/check-in/ckSignIn")
public class CkSignInController  {
	private static final Logger log = LoggerFactory.getLogger(CkSignInController.class);
	@Autowired
	ICgService<CkSignIn> cgService;
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
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, CkSignIn ckSignIn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				return j.dictionary(cgService.getDictionary(ckSignIn,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(ckSignIn,true,null));
			if (!Util.isEmpty(id)) ckSignIn=cgService.getRecord(id,request);
			j.data(ckSignIn);
			return j.toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}

	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			cgService.export(null,0,100, null, "ckSignIn.xlsx", "ckSignIn", sort, order, search, request, response);
			return null;
		} catch (Exception e) {
			response.reset();
			response.setContentType("text/json; charset=UTF-8");
			return new RestJson().setMessage(e).toResponse();
		} finally {
			Util.setProgress(100);
		}
	}
	@RequestMapping(value = "/query",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> sqlQuery(HttpServletRequest request) {
		try {
			return cgService.sqlQuery(EntityUtil.mapFromBody(request)).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
}