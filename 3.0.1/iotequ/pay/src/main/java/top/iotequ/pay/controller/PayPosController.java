package top.iotequ.pay.controller;
import top.iotequ.pay.pojo.PayPos;
import top.iotequ.pay.dao.PayPosDao;
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
import org.springframework.web.multipart.MultipartFile;
import top.iotequ.framework.service.ICgService;
import top.iotequ.framework.util.*;
import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/pay/payPos")
public class PayPosController  {
	private static final Logger log = LoggerFactory.getLogger(PayPosController.class);
	@Autowired
	ICgService<PayPos> cgService;
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
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, PayPos payPos,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(cgService.getDictionary(payPos,true, requestDynaFields)).toResponse();
			else {
				payPos = cgService.getDefaultObject(payPos);
                return new RestJson().data(payPos)
                    .dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? cgService.getDictionary(payPos,true,null) : null).toResponse();
			}
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, PayPos payPos, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				return j.dictionary(cgService.getDictionary(payPos,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(payPos,true,null));
			if (!Util.isEmpty(id)) payPos=cgService.getRecord(id,request);
			j.data(payPos);
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
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, PayPos payPos, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(idSaved) || Util.isEmpty(payPos.getId()));
			return 	cgService.doSave(isNew, flowCode,total_filepart, payPos, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<PayPos> payPosList, HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.updateSelective(payPosList).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}

	@RequestMapping(value = "/delete",method = {RequestMethod.DELETE})
	public ResponseEntity<Map<String, Object>> delete(String id,HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.doDelete(id,null,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/batdel",method = {RequestMethod.DELETE})
	public ResponseEntity<Map<String, Object>> deleteBatch(String ids,HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.doBatchDelete(ids,null,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			cgService.export(null,0,100, null, "payPos.xlsx", "payPos", sort, order, search, request, response);
			return null;
		} catch (Exception e) {
			response.reset();
			response.setContentType("text/json; charset=UTF-8");
			return new RestJson().setMessage(e).toResponse();
		} finally {
			Util.setProgress(100);
		}
	}
	@RequestMapping(value = "/import",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> importFile(MultipartFile file,HttpServletRequest request) {
		try {
			return cgService.doImport(file,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
}