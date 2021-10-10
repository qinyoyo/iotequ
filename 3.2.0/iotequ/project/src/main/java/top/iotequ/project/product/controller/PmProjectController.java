package top.iotequ.project.product.controller;
import top.iotequ.project.product.pojo.PmProject;
import top.iotequ.project.product.dao.PmProjectDao;
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
import org.springframework.web.multipart.MultipartFile;
import top.iotequ.framework.service.ICgService;
import top.iotequ.util.*;
import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/project/product/pmProject")
public class PmProjectController  {
	private static final Logger log = LoggerFactory.getLogger(PmProjectController.class);
	@Autowired
	ICgService<PmProject> cgService;
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
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, PmProject pmProject,HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(cgService.getDictionary(pmProject,true, requestDynaFields)).toResponse();
			else {
				pmProject = cgService.getDefaultObject(pmProject);
                return new RestJson().data(pmProject)
					.data(IFlowService.flowNextOperator, cgService.getFlowService()==null ? null: cgService.getFlowService().defaultNextOperator(pmProject,null,null))
					.data(IFlowService.flowCopyToList, cgService.getFlowService()==null ? null: cgService.getFlowService().defaultCopyToList(pmProject,null,null))
                    .dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? cgService.getDictionary(pmProject,true,null) : null).toResponse();
			}
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, PmProject pmProject, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
				if (cgService.getFlowService()!=null) {
					final PmProject obj = pmProject;
					final IFlowService flowService = cgService.getFlowService();
					final String operation = request.getParameter(IFlowService.flowAction);
					final String selection = request.getParameter(IFlowService.flowSelection);
					j.parameter("record", new HashMap<String, Object>() {{
						put(IFlowService.flowNextOperator, Util.isEmpty(operation) ? flowService.getNextOperator(obj) : flowService.defaultNextOperator(obj,operation,selection));
						put(IFlowService.flowCopyToList, Util.isEmpty(operation) ? flowService.getCopyToList(obj) : flowService.defaultCopyToList(obj,operation,selection));
					}});
				}
				return j.dictionary(cgService.getDictionary(pmProject,true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(pmProject,true,null));
			if (!Util.isEmpty(id)) pmProject=cgService.getRecord(id,request);
			j.data(pmProject);
			j.data(IFlowService.flowNextOperator, cgService.getFlowService()==null ? null: cgService.getFlowService().getNextOperator(pmProject));
			j.data(IFlowService.flowCopyToList, cgService.getFlowService()==null ? null: cgService.getFlowService().getCopyToList(pmProject));
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
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, PmProject pmProject, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(idSaved) || Util.isEmpty(pmProject.getId()));
			return 	cgService.doSave(isNew, flowCode,total_filepart, pmProject, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<Map<String,Object>> pmProjectList, HttpServletRequest request, HttpServletResponse response) {
		try {
			return cgService.updateSelective(pmProjectList).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/f_assess",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flowAssess(Integer total_filepart, PmProject pmProject, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "assess",total_filepart, pmProject, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/f_decision",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flowDecision(Integer total_filepart, PmProject pmProject, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "decision",total_filepart, pmProject, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/f_develop",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flowDevelop(Integer total_filepart, PmProject pmProject, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "develop",total_filepart, pmProject, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/f_publish",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flowPublish(Integer total_filepart, PmProject pmProject, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "publish",total_filepart, pmProject, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/f_review",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flowReview(Integer total_filepart, PmProject pmProject, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "review",total_filepart, pmProject, idSaved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/f_test",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flowTest(Integer total_filepart, PmProject pmProject, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "test",total_filepart, pmProject, idSaved, request).toResponse();
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
			cgService.export(null,0,100, null, "pmProject.xlsx", "pmProject", sort, order, search, request, response);
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