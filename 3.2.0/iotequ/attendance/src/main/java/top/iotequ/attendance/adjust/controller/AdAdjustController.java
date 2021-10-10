package top.iotequ.attendance.adjust.controller;
import top.iotequ.attendance.adjust.pojo.AdAdjust;
import top.iotequ.attendance.adjust.dao.AdAdjustDao;
import top.iotequ.framework.flow.*;
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
import top.iotequ.attendance.util.AdUtil;
import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/attendance/adjust/adAdjust")
public class AdAdjustController  {
	private static final Logger log = LoggerFactory.getLogger(AdAdjustController.class);
	@Autowired
	ICgService<AdAdjust> cgService;
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
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, AdAdjust adAdjust,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(cgService.getDictionary(adAdjust,true, requestDynaFields)).toResponse();
			else {
				adAdjust = cgService.getDefaultObject(adAdjust);
                return new RestJson().data(adAdjust)
					.data(IFlowService.flowNextOperator, cgService.getFlowService()==null ? null: cgService.getFlowService().defaultNextOperator(adAdjust,null,null))
					.data(IFlowService.flowCopyToList, cgService.getFlowService()==null ? null: cgService.getFlowService().defaultCopyToList(adAdjust,null,null))
                    .dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? cgService.getDictionary(adAdjust,true,null) : null).toResponse();
			}
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, AdAdjust adAdjust, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				if (cgService.getFlowService()!=null) {
					final AdAdjust obj = adAdjust;
					final IFlowService flowService = cgService.getFlowService();
					final String operation = request.getParameter(IFlowService.flowAction);
					final String selection = request.getParameter(IFlowService.flowSelection);
					j.parameter("record", new HashMap<String, Object>() {{
						put(IFlowService.flowNextOperator, Util.isEmpty(operation) ? flowService.getNextOperator(obj) : flowService.defaultNextOperator(obj,operation,selection));
						put(IFlowService.flowCopyToList, Util.isEmpty(operation) ? flowService.getCopyToList(obj) : flowService.defaultCopyToList(obj,operation,selection));
					}});
				}
				return j.dictionary(cgService.getDictionary(adAdjust,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(adAdjust,true,null));
			if (!Util.isEmpty(id)) adAdjust=cgService.getRecord(id,request);
			j.data(adAdjust);
			j.data(IFlowService.flowNextOperator, cgService.getFlowService()==null ? null: cgService.getFlowService().getNextOperator(adAdjust));
			j.data(IFlowService.flowCopyToList, cgService.getFlowService()==null ? null: cgService.getFlowService().getCopyToList(adAdjust));
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
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, AdAdjust adAdjust, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(idSaved) || Util.isEmpty(adAdjust.getId()));
			return 	cgService.doSave(isNew, flowCode,total_filepart, adAdjust, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<Map<String,Object>> adAdjustList, HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.updateSelective(adAdjustList).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/f_approve",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flowApprove(Integer total_filepart, AdAdjust adAdjust, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "approve",total_filepart, adAdjust, idSaved, request).toResponse();
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
	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			cgService.export(null,0,100, null, "adAdjust.xlsx", "adAdjust", sort, order, search, request, response);
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