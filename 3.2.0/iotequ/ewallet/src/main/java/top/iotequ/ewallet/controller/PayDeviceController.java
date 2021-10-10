package top.iotequ.ewallet.controller;
import top.iotequ.ewallet.pojo.EwDevice;
import top.iotequ.ewallet.dao.EwDeviceDao;
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
import top.iotequ.framework.service.BaseIotequService;
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
@RequestMapping("/ewallet/payDevice")
public class PayDeviceController extends BaseController<EwDevice> implements SmartLifecycle {
	private static final Logger log = LoggerFactory.getLogger(PayDeviceController.class);
	@Autowired
	private EwDeviceDao ewDeviceDao;
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	IIotequService<EwDevice> service = null;
	public Class getEntityClass() {
		return EwDevice.class;
	}
	public Map<String,Object> getDictionary(HttpServletRequest request, Boolean useTree, String dynaFields) {
		Map<String,Object> map=new HashMap<>();
		String [] dictPrivilegeListValue= {"1","2","3"};
		//金额消费,计次消费,计时消费
		String [] dictPrivilegeListText= {"payDevice.field.privilegeList_0","payDevice.field.privilegeList_1","payDevice.field.privilegeList_2"};
		if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"privilegeList")) map.put("dictPrivilegeList", getDictList(dictPrivilegeListValue,dictPrivilegeListText));
		return map;
	}
	private String getListCondition(String search,HttpServletRequest request) {
		String entities = request.getParameter("queryEntities");
		String whereString = getWhereString(EwDevice.class,service.orgCodePrivilege(request,EwDevice.class),
			Util.isEmpty(entities) ? null : search, Util.isEmpty(entities) ? null : entities.split(","), request);
		String filter=service.listFilter(request.getParameter("pathNameOfQuery"));
		if (!Util.isEmpty(filter)) {
			if (Util.isEmpty(whereString)) whereString=filter;
			else whereString="("+filter+") and (" +whereString+")";
		}
		return whereString;
	}
	public List<EwDevice> getDataList(Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request) {
		List<EwDevice> data=null;
		String defaultOrder=request.getParameter("defaultOrder");
		String orderString=getOrderString(EwDevice.class,sort,order,null,new String[]{""},defaultOrder);
		String whereString=getListCondition(search,request);
		String sql=request.getParameter(Util._addtional_condition);
		if (sql==null || !sql.trim().toLowerCase().startsWith("select ")) {
			log.debug("Dao.listBy({},{})",whereString,orderString);
			startPageForList(limit,offset);
			data=ewDeviceDao.listBy(whereString,orderString);
		} else {
			sql = SqlUtil.sqlAddCondition(sql, whereString);
			sql = SqlUtil.sqlAddOrderCondition(sql, orderString);
			log.debug("List by : {}",sql);
			try {
				startPageForList(limit,offset);
				data=SqlUtil.sqlQuery(EwDevice.class,getFilterOrg(request),sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (data==null) return new ArrayList<EwDevice>();
		return data;
	}
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean needLoadDictionary,String resortFirstField, Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		if (!Util.isEmpty(resortFirstField)) {
			try {
				service.dragSort(EwDevice.class, resortFirstField, "id", getDataList(null, null, resortFirstField, "asc", null, request), request);
			} catch (Exception e) {
				return new RestJson().setMessage(e).toResponse();
			}
		}
		List<EwDevice> data=getDataList(limit,offset,sort,order, search,request);
		service.beforeList(data,request);
		PageInfo<EwDevice> pageInfo = new PageInfo<>(data);
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) {
			Map<String, Object> obj = getPagedTableData(pageInfo);
			RestJson j = new RestJson();
			j.put("data", obj);
			j.dictionary(getDictionary(request,true,null));
			return j.toResponse();
		} else return getTableDataEntity(pageInfo);
	}
	@RequestMapping(value = "/default",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, EwDevice ewDevice,HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(getDictionary(request,true, requestDynaFields)).toResponse();
		if (ewDevice==null)  ewDevice=new EwDevice();
		else ewDevice.setId(null);  // 禁止绕开update修改数据
		//在这里设置服务器端的默认值
		service.customAdd(ewDevice, request);
		return new RestJson()
			.data(ewDevice)
			.dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? getDictionary(request,true,null) : null)
			.toResponse();
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, String requestDynaFields, String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(getDictionary(request,true, requestDynaFields)).toResponse();
		RestJson j = new RestJson();
		EwDevice ewDevice=null;
		if (Util.isEmpty(id)) {
			ewDevice=EntityUtil.createEntity(EwDevice.class,request);
			if (ewDevice==null) ewDevice=new EwDevice();
			else {
				List<EwDevice> list=ewDeviceDao.list(ewDevice);
				if (list!=null && list.size()>0)	ewDevice=list.get(0);
			}
		} else {
			Integer iid=Integer.valueOf(id);
			ewDevice=ewDeviceDao.select(iid);
			if (ewDevice==null)	ewDevice=new EwDevice();
		} 
		//在这里附加字段值
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(getDictionary(request,true,null));
		service.beforeUpdate(ewDevice,request);
		j.data(ewDevice);
		return j.toResponse();		
	}
	public void changeEmpty2Null(EwDevice ewDevice) {
		if (ewDevice!=null) {
		}
	}
	@RequestMapping(value = "/save",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> save(Boolean updateSelective, @RequestBody EwDevice ewDevice, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return doSave(updateSelective, ewDevice, idSaved, request, response);
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@Transactional
	public ResponseEntity<Map<String, Object>> doSave(Boolean updateSelective, EwDevice ewDevice, String idSaved, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RestJson j=new RestJson();
		if (Objects.nonNull(updateSelective) && updateSelective) {
			if (Objects.nonNull(ewDevice)) {
				int rows=ewDeviceDao.updateSelective(ewDevice);
				if (rows!=1) {
					throw new IotequException(IotequThrowable.FAILURE,"rows=="+rows);
				}
			} else throw new IotequException(IotequThrowable.NULL_OBJECT,"ewDevice");
			return j.toResponse();
		}
		if (ewDevice==null) {
			j.setErrorCode(IotequThrowable.NULL_OBJECT,"ewDevice");
		}
		int rows=0;
		if (ewDevice0!=null && StringUtil.toJsonString(ewDevice0).equals(StringUtil.toJsonString(ewDevice))) {
			j.setSuccess(true);
			j.setMessage("nochange");
			j.parameter("noChanged", true);
			return j.toResponse();
		}
		changeEmpty2Null(ewDevice);
		service.beforeSave(ewDevice,request);
			if (Util.isEmpty(ewDevice.getId())) {
			rows=ewDeviceDao.insert(ewDevice);
			if (rows>0) {
				Util.writeLog(log,"ew_device insert",null,"Insert into <%s> %s=%s","ew_device","id",ewDevice.getId().toString());
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("idName", "id");
				map.put("recordId", ewDevice.getId());
				j.parameter(map);
			}
		} else {
			if (EntityUtil.entityEquals(ewDevice.getId(),idSaved))
				rows=ewDeviceDao.update(ewDevice);
			else {
				rows=ewDeviceDao.updateBy(ewDevice,Integer.valueOf(idSaved));
				if (rows>0) {
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("idName", "id");
					map.put("recordId", ewDevice.getId());
					j.parameter(map);
				}
			}
			if (rows>0) Util.writeLog(log,"ew_device update",null,"Update %s %s=%s","ew_device","id",ewDevice.getId().toString());
		}
		if (rows<=0) {
			throw new IotequException(IotequThrowable.FAILURE,"rows <= 0");
		} else {
			service.afterSave(ewDevice0,ewDevice,request,j);
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
		int rows = ewDeviceDao.delete(Integer.valueOf(id));
		if (rows>0) Util.writeLog(log,"ew_device delete",null,"Delete from %s id=%s","ew_device",id);
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
		if (Util.isEmpty(ids)) throw new IotequException(IotequThrowable.NULL_OBJECT,"ids");
		service.beforeDelete(ids,request);
		int rows = ewDeviceDao.deleteBatch(ids);
		if (rows>0) Util.writeLog(log,"ew_device batch delete",null,"Batch delete %s ids=%s","ew_device",ids);
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
				final String orderString=getOrderString(EwDevice.class,sort,order,null,new String[]{""},defaultOrder);
				final String whereString=getListCondition(search,request);
				Util.writeLog(log,"export",null,"table data (where = %s)",whereString);
				setDictDataFromMap(request, getDictionary(request,false,null));
				IGetPagedData<EwDevice> mainPD = new IGetPagedData<EwDevice>() {
					@Override
					public PageInfo<EwDevice> getPage(int pageNum,int pageSize) {
						PageHelper.startPage(pageNum, pageSize);
						List<EwDevice> data = ewDeviceDao.listBy(whereString,orderString);
						try {
							service.beforeExport(data,request);
							PageInfo<EwDevice> pageInfo = new PageInfo<>(data);
							return pageInfo;
						} catch (Exception e) {
							log.debug(e.getMessage());
						}
						return null;
					}		
				};
				exportExcel(request, response, "ewDevice.xlsx", "ewDevice",mainPD) ;
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
			Util.writeLog(log,"import",null,"table = ew_device");
			IImportPagedData<EwDevice> mainPD=new IImportPagedData<EwDevice>(){
				@Override
				public String saveData(List<EwDevice> list) throws Exception {
					if (list==null || list.size()==0) return null;
					else {
						for (EwDevice ewDevice : list) {
						   service.beforeDelete(ewDevice.getId().toString(),request);
						}
						service.beforeImport(list,request);
						if (list.size()==0) return null;
						ewDeviceDao.deleteList(list);
						ewDeviceDao.insertBatchWithId(list);
						service.afterImport(list,"id",request,j);
						if ("Oracle".equals(sqlSessionTemplate.getConfiguration().getDatabaseId()))
							SqlUtil.sqlExecute("CALL CALC_SEQUENCE('EW_DEVICE','ID')");
						return null;
					}
				}
			};
			String msg = importExcel(request,file, "ewDevice",EwDevice.class,null,mainPD);
			if (!Util.isEmpty(msg)) j.setMessage(msg);
		}
		return j.toResponse();
	}
	@Override
	public void start() {
		Object bean = Util.getBean("payDeviceService");
		if (Objects.nonNull(bean)) service = (IIotequService<EwDevice>) bean;
		else service = new BaseIotequService<EwDevice>();
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