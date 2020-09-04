<#ftl strip_whitespace=true>
<#assign D = "$" />
<#assign J = "#" />
<#assign useEntity = (table.entity?? && table.entity?trim!="") >
<#assign USEDB = (table.name?? && table.name?trim!="") >
<#assign ACTIONPAGE = false >
<#assign ACTIONAJAX = false >
<#assign SYSACTIONS = ",_add,_edit,_view,_delete,_batdel,_import,_export,_flow,_refresh,_reset,_toolbar,_switch,_column,_search,_doSearch," />
<#if buttons??>
<#list buttons as btn>
<#if SYSACTIONS?index_of(btn.action) lt 0 && (btn.actionProperty=="go" || btn.actionProperty=="pg")>
<#assign ACTIONPAGE = true />
</#if>
<#if SYSACTIONS?index_of(btn.action) lt 0 && btn.actionProperty=="aj">
<#assign ACTIONAJAX = true />
</#if>
</#list>
</#if>
<#assign USEFILE = false />
<#assign needSetDefault = false />
<#list fields as f>
<#if pk?? && (f.showType=='file' || f.showType=='image')>
<#assign USEFILE = true />
</#if>
<#if f.defaultValue?? && f.defaultValue?trim!="" && !f.isNull>
<#assign needSetDefault = true />
</#if>
</#list>
package ${basePackage}.${CONTROLLER};
<#assign FASTEXPORT = false />
<#if useEntity>
import ${basePackage}.${POJO}.${table.entity};
<#if USEDB>
import ${basePackage}.${DAO}.${table.entity}${DAO?cap_first};
<#if sons?? && table.actionList ?? && table.actionList?index_of(",export,") gte 0>
<#list sons as son>
<#if son.fastExport?? && son.fastExport!="">
<#assign FASTEXPORT = true />
import ${son.fastExport}.${POJO}.${son.entity};
import ${son.fastExport}.${DAO}.${son.entity}${DAO?cap_first};
</#if>
</#list>
</#if>
</#if>
<#if flowList ??>
import top.iotequ.framework.flow.*;
</#if>
</#if>
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
<#if table.actionList ?? && table.actionList?index_of(",import,") gte 0>
import org.springframework.web.multipart.MultipartFile;
</#if>
import top.iotequ.framework.service.ICgService;
import top.iotequ.framework.util.*;
<#if table.imports?? && table.imports?trim !="">
<#assign imports = table.imports?trim?split(",") >
<#list imports as i>
import ${i};
</#list>
</#if>
import java.util.*;
<#assign hasDynamicQuery = false >
<#list fields as f>
<#if f.showType=='select' && f.dictTable?? && f.dynaCondition?? && f.dynaCondition?trim!=''>
<#assign hasDynamicQuery = true >
<#break>
</#if>
</#list>

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}")
public class ${generatorName?cap_first}${CONTROLLER?cap_first} <#if table.controllerExtends?? && table.controllerExtends?trim != ''>${table.controllerExtends?trim}</#if> {
	private static final Logger log = LoggerFactory.getLogger(${generatorName?cap_first}${CONTROLLER?cap_first}.class);
	@Autowired
	ICgService<${table.entity}> cgService;
	<#if rightJoinPK>
	@Autowired
	private ${table.entity}${DAO?cap_first} ${table.entity?uncap_first}${DAO?cap_first};
	</#if>
	<#if table.actionList ?? && table.actionList?index_of(",list,") gte 0 >
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean queryFlowProcess, Boolean needLoadDictionary,String resortFirstField, Integer pageSize,Integer pageNumber,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		try {
			if (Objects.nonNull(queryFlowProcess) && queryFlowProcess) return cgService.getFlowProcessData(request.getParameter("flowId")).toResponse();
			else return cgService.getListPageData(needLoadDictionary,resortFirstField, pageSize, pageNumber, sort, order, search, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	</#if>
	<#if table.actionList ?? && table.actionList?index_of(",add,") gte 0 >
	@RequestMapping(value = "/default",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, ${table.entity} ${table.entity?uncap_first},HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(cgService.getDictionary(${table.entity?uncap_first},true, requestDynaFields)).toResponse();
			else {
				${table.entity?uncap_first} = cgService.getDefaultObject(${table.entity?uncap_first});
                return new RestJson().data(${table.entity?uncap_first})
				<#if table.actionList ?? && table.actionList?index_of(",flow,") gte 0 >
					.data(IFlowService.flowNextOperator, cgService.getFlowService()==null ? null: cgService.getFlowService().defaultNextOperator(${table.entity?uncap_first},null,null))
					.data(IFlowService.flowCopyToList, cgService.getFlowService()==null ? null: cgService.getFlowService().defaultCopyToList(${table.entity?uncap_first},null,null))
				</#if>
                    .dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? cgService.getDictionary(${table.entity?uncap_first},true,null) : null).toResponse();
			}
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	</#if>
	<#if table.actionList ?? && (table.actionList?index_of(",add,") gte 0 || table.actionList?index_of(",edit,") gte 0 || table.actionList?index_of(",view,") gte 0) >
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, Boolean loadDictionaryOnly, String requestDynaFields, String id, ${table.entity} ${table.entity?uncap_first}, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			RestJson j = new RestJson();
			if (!Util.isEmpty(requestDynaFields) || (loadDictionaryOnly!=null && loadDictionaryOnly)) {
			<#if table.actionList ?? && table.actionList?index_of(",flow,") gte 0 >
				if (cgService.getFlowService()!=null) {
					final ${table.entity} obj = ${table.entity?uncap_first};
					final IFlowService flowService = cgService.getFlowService();
					final String operation = request.getParameter(IFlowService.flowAction);
					final String selection = request.getParameter(IFlowService.flowSelection);
					j.parameter("record", new HashMap<String, Object>() {{
						put(IFlowService.flowNextOperator, Util.isEmpty(operation) ? flowService.getNextOperator(obj) : flowService.defaultNextOperator(obj,operation,selection));
						put(IFlowService.flowCopyToList, Util.isEmpty(operation) ? flowService.getCopyToList(obj) : flowService.defaultCopyToList(obj,operation,selection));
					}});
				}
			</#if>
				return j.dictionary(cgService.getDictionary(${table.entity?uncap_first},true, requestDynaFields)).toResponse();
			}
			if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(cgService.getDictionary(${table.entity?uncap_first},true,null));
			if (!Util.isEmpty(id)) ${table.entity?uncap_first}=cgService.getRecord(id,request);
			j.data(${table.entity?uncap_first});
		<#if table.actionList ?? && table.actionList?index_of(",flow,") gte 0 >
			j.data(IFlowService.flowNextOperator, cgService.getFlowService()==null ? null: cgService.getFlowService().getNextOperator(${table.entity?uncap_first}));
			j.data(IFlowService.flowCopyToList, cgService.getFlowService()==null ? null: cgService.getFlowService().getCopyToList(${table.entity?uncap_first}));
		</#if>
			return j.toResponse();
		} catch (Exception e) {
			return new RestJson().setError(e).toResponse();
		}
	}
	</#if>
	<#if table.actionList ?? && (table.actionList?index_of(",edit,") gte 0 || table.actionList?index_of(",add,") gte 0) >
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
	public ResponseEntity<Map<String, Object>> save(String flowCode, Integer total_filepart, ${table.entity} ${table.entity?uncap_first}, String ${pk.entityName}Saved, HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean isNew = (Util.isEmpty(${pk.entityName}Saved)<#if pk?? && (pk.keyType=='1' || pk.keyType=='2')> || Util.isEmpty(${table.entity?uncap_first}.get${pk.entityName?cap_first}())</#if>);
      <#if rightJoinPK>
			if (!isNew) {
				${table.entity} obj = ${table.entity?uncap_first}${DAO?cap_first}.realSelect(${pk.type}.valueOf(${pk.entityName}Saved));
				isNew = (obj==null);
			}
      </#if>
			return 	cgService.doSave(isNew, flowCode,total_filepart, ${table.entity?uncap_first}, ${pk.entityName}Saved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@RequestMapping(value = "/updateSelective",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> updateSelective(@RequestBody List<${table.entity}> ${table.entity?uncap_first}List, HttpServletRequest request, HttpServletResponse response) {
		try {
      <#if rightJoinPK>
			List<Boolean> isNewList=new ArrayList<>();
			for (${table.entity} obj : ${table.entity?uncap_first}List) {
				isNewList.add(${table.entity?uncap_first}${DAO?cap_first}.realSelect(obj.get${pk.entityName?cap_first}()) == null);
			}
			return cgService.updateSelective(${table.entity?uncap_first}List, isNewList).toResponse();
		  <#else>
			return cgService.updateSelective(${table.entity?uncap_first}List).toResponse();
      </#if>
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
  </#if>
	<#if USEDB && flowList ??>
	<#list flowList as flow>
	<#list flow.path?split(",") as path>
	@RequestMapping(value = "/f_${path}",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> flow${path?cap_first}(Integer total_filepart, ${table.entity} ${table.entity?uncap_first}, String ${pk.entityName}Saved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return 	cgService.doSave(false, "${path}",total_filepart, ${table.entity?uncap_first}, ${pk.entityName}Saved, request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	</#list>
	</#list>
	</#if>

	<#if table.actionList ?? && table.actionList?index_of(",delete,") gte 0 >
	@RequestMapping(value = "/delete",method = {RequestMethod.DELETE})
	public ResponseEntity<Map<String, Object>> delete(String id,HttpServletRequest request, HttpServletResponse response) {
		try {
      <#if sons??>
	      List<Map<String,String>> sons = new ArrayList<Map<String,String>>() {{
		      <#list sons as son>
		      <#if son.name?? && son.name!="">
	        add(new HashMap<String,String>() {{
	          put("table","${son.name}");
	          put("parent","${son.fk_field}");
	        }});
          </#if>
          </#list>
	      }};
		  </#if>
			return cgService.doDelete(id,<#if sons??>sons<#else>null</#if>,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	</#if>
	<#if table.actionList ?? && table.actionList?index_of(",batdel,") gte 0>
	@RequestMapping(value = "/batdel",method = {RequestMethod.DELETE})
	public ResponseEntity<Map<String, Object>> deleteBatch(String ids,HttpServletRequest request, HttpServletResponse response) {
		try {
      <#if sons??>
			List<Map<String,String>> sons = new ArrayList<Map<String,String>>() {{
        <#list sons as son>
            <#if son.name?? && son.name!="">
					add(new HashMap<String,String>() {{
					put("table","${son.name}");
					put("parent","${son.fk_field}");
	      }});
            </#if>
        </#list>
			}};
      </#if>
			return cgService.doBatchDelete(ids,<#if sons??>sons<#else>null</#if>,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	</#if>
	<#if table.actionList ?? && table.actionList?index_of(",export,") gte 0 >
	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			cgService.export(null,0,100, null, "${table.entity?uncap_first}.xlsx", "${table.entity?uncap_first}", sort, order, search, request, response);
			return null;
		} catch (Exception e) {
			response.reset();
			response.setContentType("text/json; charset=UTF-8");
			return new RestJson().setMessage(e).toResponse();
		} finally {
			Util.setProgress(100);
		}
	}
	</#if>
	<#if table.actionList ?? && table.actionList?index_of(",import,") gte 0 >
	@RequestMapping(value = "/import",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> importFile(MultipartFile file,HttpServletRequest request) {
		try {
			return cgService.doImport(file,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	</#if>
	<#if table.actionList ?? && table.actionList?index_of(",query,") gte 0 && USEDB>
	@RequestMapping(value = "/query",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> sqlQuery(HttpServletRequest request) {
		try {
			return cgService.sqlQuery(EntityUtil.mapFromBody(request)).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	</#if>
	<#if buttons??>
	<#list buttons as btn>
	<#if SYSACTIONS?index_of(btn.action) lt 0 && (btn.actionProperty=="aj" || btn.actionProperty=="pg")>
	@RequestMapping(value = "/action/${btn.action}")
	public ResponseEntity<Map<String, Object>> action${btn.action?cap_first}(String id,HttpServletRequest request, HttpServletResponse response){
		try {
		  return cgService.doAction("${btn.action}",id,request).toResponse();
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	</#if>
	</#list>
	</#if>
}