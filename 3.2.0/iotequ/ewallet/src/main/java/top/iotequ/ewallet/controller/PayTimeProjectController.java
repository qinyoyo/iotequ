package top.iotequ.ewallet.controller;
import top.iotequ.ewallet.pojo.EwTimeProject;
import top.iotequ.ewallet.dao.EwTimeProjectDao;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.context.SmartLifecycle;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.mybatis.spring.SqlSessionTemplate;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import top.iotequ.framework.service.ICgService;
import top.iotequ.framework.service.*;
import top.iotequ.framework.controller.BaseController;
import top.iotequ.framework.util.*;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import java.util.*;

/**************************************************
    Create by iotequ codegenerator 3.0.0
    Author : Qinyoyo
"**************************************************/
@SuppressWarnings("unused")
@RestController
@RequestMapping("/ewallet/payTimeProject")
public class PayTimeProjectController extends BaseController<EwTimeProject> implements SmartLifecycle {
	private static final Logger log = LoggerFactory.getLogger(PayTimeProjectController.class);
	@Autowired
	private EwTimeProjectDao ewTimeProjectDao;
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	IIotequService<EwTimeProject> service = null;
	public Class getEntityClass() {
		return EwTimeProject.class;
	}
	public Map<String,Object> getDictionary(HttpServletRequest request, Boolean useTree, String dynaFields) {
		Map<String,Object> map=new HashMap<>();
		return map;
	}
	private String getListCondition(String search,HttpServletRequest request) {
		String entities = request.getParameter("queryEntities");
		String whereString = getWhereString(EwTimeProject.class,service.orgCodePrivilege(request,EwTimeProject.class),
			Util.isEmpty(entities) ? null : search, Util.isEmpty(entities) ? null : entities.split(","), request);
		String filter=service.listFilter(request.getParameter("pathNameOfQuery"));
		if (!Util.isEmpty(filter)) {
			if (Util.isEmpty(whereString)) whereString=filter;
			else whereString="("+filter+") and (" +whereString+")";
		}
		return whereString;
	}
	public List<EwTimeProject> getDataList(Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request) {
		List<EwTimeProject> data=null;
		String defaultOrder=request.getParameter("defaultOrder");
		String orderString=getOrderString(EwTimeProject.class,sort,order,null,new String[]{""},defaultOrder);
		String whereString=getListCondition(search,request);
		String sql=request.getParameter(Util._addtional_condition);
		if (sql==null || !sql.trim().toLowerCase().startsWith("select ")) {
			log.debug("Dao.listBy({},{})",whereString,orderString);
			startPageForList(limit,offset);
			data=ewTimeProjectDao.listBy(whereString,orderString);
		} else {
			sql = SqlUtil.sqlAddCondition(sql, whereString);
			sql = SqlUtil.sqlAddOrderCondition(sql, orderString);
			log.debug("List by : {}",sql);
			try {
				startPageForList(limit,offset);
				data=SqlUtil.sqlQuery(EwTimeProject.class,getFilterOrg(request),sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (data==null) return new ArrayList<EwTimeProject>();
		return data;
	}
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean needLoadDictionary,String resortFirstField, Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		if (!Util.isEmpty(resortFirstField)) {
			try {
				service.dragSort(EwTimeProject.class, resortFirstField, "id", getDataList(null, null, resortFirstField, "asc", null, request), request);
			} catch (Exception e) {
				return new RestJson().setMessage(e).toResponse();
			}
		}
		List<EwTimeProject> data=getDataList(limit,offset,sort,order, search,request);
		service.beforeList(data,request);
		PageInfo<EwTimeProject> pageInfo = new PageInfo<>(data);
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) {
			Map<String, Object> obj = getPagedTableData(pageInfo);
			RestJson j = new RestJson();
			j.put("data", obj);
			j.dictionary(getDictionary(request,true,null));
			return j.toResponse();
		} else return getTableDataEntity(pageInfo);
	}
	@RequestMapping(value = "/default",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, EwTimeProject ewTimeProject,HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(getDictionary(request,true, requestDynaFields)).toResponse();
		if (ewTimeProject==null)  ewTimeProject=new EwTimeProject();
		else ewTimeProject.setId(null);  // 禁止绕开update修改数据
		//在这里设置服务器端的默认值
		service.customAdd(ewTimeProject, request);
		return new RestJson()
			.data(ewTimeProject)
			.dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? getDictionary(request,true,null) : null)
			.toResponse();
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, String requestDynaFields, String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(getDictionary(request,true, requestDynaFields)).toResponse();
		RestJson j = new RestJson();
		EwTimeProject ewTimeProject=null;
		if (Util.isEmpty(id)) {
			ewTimeProject=EntityUtil.createEntity(EwTimeProject.class,request);
			if (ewTimeProject==null) ewTimeProject=new EwTimeProject();
			else {
				List<EwTimeProject> list=ewTimeProjectDao.list(ewTimeProject);
				if (list!=null && list.size()>0)	ewTimeProject=list.get(0);
			}
		} else {
			Integer iid=Integer.valueOf(id);
			ewTimeProject=ewTimeProjectDao.select(iid);
			if (ewTimeProject==null)	ewTimeProject=new EwTimeProject();
		} 
		//在这里附加字段值
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(getDictionary(request,true,null));
		service.beforeUpdate(ewTimeProject,request);
		j.data(ewTimeProject);
		return j.toResponse();		
	}
	@RequestMapping(value = "/download",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> download(String id,String field,String fileName,HttpServletResponse response) throws Exception {
		log.debug("download file from /upload-files/payTimeProject/{}_{}_",field,id);
		downloadFile("payTimeProject",field,id,fileName,response);
		return null;
	}
	public void changeEmpty2Null(EwTimeProject ewTimeProject) {
		if (ewTimeProject!=null) {
		}
	}
	@RequestMapping(value = "/save",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> save(Boolean updateSelective, Integer total_filepart, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return doSave(updateSelective, total_filepart, idSaved, request, response);
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@Transactional
	public ResponseEntity<Map<String, Object>> doSave(Boolean updateSelective, Integer total_filepart, String idSaved, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RestJson j=new RestJson();
		EwTimeProject ewTimeProject = EntityUtil.createEntity(EwTimeProject.class,request);
		if (Objects.nonNull(updateSelective) && updateSelective) {
			if (Objects.nonNull(ewTimeProject)) {
				int rows=ewTimeProjectDao.updateSelective(ewTimeProject);
				if (rows!=1) {
					throw new IotequException(IotequThrowable.FAILURE,"rows=="+rows);
				}
			} else throw new IotequException(IotequThrowable.NULL_OBJECT,"ewTimeProject");
			return j.toResponse();
		}
		if (ewTimeProject==null) {
			j.setErrorCode(IotequThrowable.NULL_OBJECT,"ewTimeProject");
		}
		int rows=0;
		if (ewTimeProject0!=null && StringUtil.toJsonString(ewTimeProject0).equals(StringUtil.toJsonString(ewTimeProject)) && (total_filepart==null || total_filepart==0)) {
			j.setSuccess(true);
			j.setMessage("nochange");
			j.parameter("noChanged", true);
			return j.toResponse();
		}
		changeEmpty2Null(ewTimeProject);
		service.beforeSave(ewTimeProject,request);
		if (ewTimeProject.getId()!=null)
			ewTimeProject.setIcon(removeFilesExclusive("payTimeProject","icon",ewTimeProject.getId().toString(),ewTimeProject.getIcon(),true));
		else
			ewTimeProject.setIcon(null);
			if (Util.isEmpty(ewTimeProject.getId())) {
			rows=ewTimeProjectDao.insert(ewTimeProject);
			if (rows>0) {
				Util.writeLog(log,"ew_time_project insert",null,"Insert into <%s> %s=%s","ew_time_project","id",ewTimeProject.getId().toString());
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("idName", "id");
				map.put("recordId", ewTimeProject.getId());
				j.parameter(map);
			}
		} else {
			if (EntityUtil.entityEquals(ewTimeProject.getId(),idSaved))
				rows=ewTimeProjectDao.update(ewTimeProject);
			else {
				rows=ewTimeProjectDao.updateBy(ewTimeProject,Integer.valueOf(idSaved));
				if (rows>0) {
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("idName", "id");
					map.put("recordId", ewTimeProject.getId());
					j.parameter(map);
				}
			}
			if (rows>0) Util.writeLog(log,"ew_time_project update",null,"Update %s %s=%s","ew_time_project","id",ewTimeProject.getId().toString());
		}
		if (rows<=0) {
			throw new IotequException(IotequThrowable.FAILURE,"rows <= 0");
		} else {
			service.afterSave(ewTimeProject0,ewTimeProject,request,j);
		}
		if (total_filepart!=null && total_filepart>0) {
			@SuppressWarnings("serial")
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>(){{
				add(new HashMap<String,Object>() {{
				String fileName = uploadFile("payTimeProject","icon",ewTimeProject.getId().toString(),false,true,request);
				ewTimeProject.setIcon(fileName);
				put("field", "icon");
				put("value", fileName);
				log.debug("upload file to /payTimeProject/icon_{}",ewTimeProject.getId().toString());
				}});
			}};
			ewTimeProjectDao.update(ewTimeProject);
			j.parameter("itemSync",list);
		}
		return j.toResponse();
	}

	@RequestMapping(value = "/delete",method = {RequestMethod.DELETE})
	public ResponseEntity<Map<String, Object>> delete(String id,HttpServletRequest request, HttpServletResponse response) {
		try {
			return doDelete(id,request, response);
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@Transactional
	public ResponseEntity<Map<String, Object>> doDelete(String id,HttpServletRequest request, HttpServletResponse response) throws Exception {
		RestJson j=new RestJson();
		if (Util.isEmpty(id)) throw new IotequException(IotequThrowable.NULL_OBJECT,"id==null");
		service.beforeDelete(id,request);
		int rows = ewTimeProjectDao.delete(Integer.valueOf(id));
		if (rows>0) Util.writeLog(log,"ew_time_project delete",null,"Delete from %s id=%s","ew_time_project",id);
		if (rows<=0) {
			throw new IotequException(IotequThrowable.FAILURE,"rows <= 0");
		} else {
			service.afterDelete(id,request,j);
		}
		return j.toResponse();
	}
	@RequestMapping(value = "/batdel",method = {RequestMethod.DELETE})
	public ResponseEntity<Map<String, Object>> deleteBatch(String ids,HttpServletRequest request, HttpServletResponse response) {
		try {
			return doDeleteBatch(ids,request, response);
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@Transactional
	public ResponseEntity<Map<String, Object>> doDeleteBatch(String ids,HttpServletRequest request, HttpServletResponse response) throws Exception {
		RestJson j=new RestJson();
		if (Util.isEmpty(ids)) throw new IotequException(IotequThrowable.NULL_OBJECT,"ids==null");
		service.beforeDelete(ids,request);
		int rows = ewTimeProjectDao.deleteBatch(ids);
		if (rows>0) Util.writeLog(log,"ew_time_project batch delete",null,"Batch delete %s ids=%s","ew_time_project",ids);
		if (rows<=0) throw new IotequException(IotequThrowable.FAILURE,"rows<=0");
		service.afterDelete(ids,request,j);
		j.parameter("rows",rows);
		return j.toResponse();
	}
	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			if (!service.customExport(sort,order,search,request, response)) {
				String defaultOrder=request.getParameter("defaultOrder");
				final String orderString=getOrderString(EwTimeProject.class,sort,order,null,new String[]{""},defaultOrder);
				final String whereString=getListCondition(search,request);
				Util.writeLog(log,"export",null,"table data (where = %s)",whereString);
				setDictDataFromMap(request, getDictionary(request,false,null));
				IGetPagedData<EwTimeProject> mainPD = new IGetPagedData<EwTimeProject>() {
					@Override
					public PageInfo<EwTimeProject> getPage(int pageNum,int pageSize) {
						PageHelper.startPage(pageNum, pageSize);
						List<EwTimeProject> data = ewTimeProjectDao.listBy(whereString,orderString);
						try {
							service.beforeExport(data,request);
							PageInfo<EwTimeProject> pageInfo = new PageInfo<>(data);
							return pageInfo;
						} catch (Exception e) {
							log.debug(e.getMessage());
						}
						return null;
					}		
				};
				exportExcel(request, response, "ewTimeProject.xlsx", "ewTimeProject",mainPD) ;
			}
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		} finally {
			Util.setProgress(100);
		}
		return null;
	}
	@RequestMapping(value = "/import",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> importFile(MultipartFile file,HttpServletRequest request) {
		try {
			return doImportFile(file,request);
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@Transactional
	public ResponseEntity<Map<String, Object>> doImportFile(MultipartFile file,HttpServletRequest request) throws Exception{
		RestJson j=new RestJson();
		j.parameter(Util._condition_filter_org_,false);
		if (!service.customImport(file,request,j)) {
			if (Util.isEmpty(file)) throw new IotequException(IotequThrowable.NULL_OBJECT,"file");
			Util.writeLog(log,"import",null,"table = ew_time_project");
			IImportPagedData<EwTimeProject> mainPD=new IImportPagedData<EwTimeProject>(){
				@Override
				public String saveData(List<EwTimeProject> list) throws Exception {
					if (list==null || list.size()==0) return null;
					else {
						for (EwTimeProject ewTimeProject : list) {
							if (ewTimeProject.getBaseValue()==null) {
								ewTimeProject.setBaseValue(60);
							}
							if (ewTimeProject.getBasePrice()==null) {
								ewTimeProject.setBasePrice(10);
							}
						}
						for (EwTimeProject ewTimeProject : list) {
						   service.beforeDelete(ewTimeProject.getId().toString(),request);
						}
						service.beforeImport(list,request);
						if (list.size()==0) return null;
						ewTimeProjectDao.deleteList(list);
						ewTimeProjectDao.insertBatchWithId(list);
						service.afterImport(list,"id",request,j);
						if ("Oracle".equals(sqlSessionTemplate.getConfiguration().getDatabaseId()))
							SqlUtil.sqlExecute("CALL CALC_SEQUENCE('EW_TIME_PROJECT','ID')");
						return null;
					}
				}
			};
			String msg = importExcel(request,file, "ewTimeProject",EwTimeProject.class,null,mainPD);
			if (!Util.isEmpty(msg)) j.setMessage(msg);
		}
		return j.toResponse();
	}
	@Override
	public void start() {
		Object bean = Util.getBean("payTimeProjectService");
		if (Objects.nonNull(bean)) service = (IIotequService<EwTimeProject>) bean;
		else service = new BaseIotequService<EwTimeProject>();
else service=new IotequService<User>();    service.initial();
	}
	@Override
	public void stop() {
	}
	@Override
	public boolean isRunning() {
		return false;
	}
}