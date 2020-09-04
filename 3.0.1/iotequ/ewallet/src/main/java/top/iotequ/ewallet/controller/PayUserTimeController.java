package top.iotequ.ewallet.controller;
import top.iotequ.ewallet.pojo.EwUserTime;
import top.iotequ.ewallet.dao.EwUserTimeDao;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/ewallet/payUserTime")
public class PayUserTimeController extends BaseController<EwUserTime> implements SmartLifecycle {
	private static final Logger log = LoggerFactory.getLogger(PayUserTimeController.class);
	@Autowired
	private EwUserTimeDao ewUserTimeDao;
	IIotequService<EwUserTime> service = null;
	public Class getEntityClass() {
		return EwUserTime.class;
	}
	public Map<String,Object> getDictionary(HttpServletRequest request, Boolean useTree, String dynaFields) {
		Map<String,Object> map=new HashMap<>();
		return map;
	}
	private String getListCondition(String search,HttpServletRequest request) {
		String entities = request.getParameter("queryEntities");
		String whereString = getWhereString(EwUserTime.class,service.orgCodePrivilege(request,EwUserTime.class),
			Util.isEmpty(entities) ? null : search, Util.isEmpty(entities) ? null : entities.split(","), request);
		String filter=service.listFilter(request.getParameter("pathNameOfQuery"));
		if (!Util.isEmpty(filter)) {
			if (Util.isEmpty(whereString)) whereString=filter;
			else whereString="("+filter+") and (" +whereString+")";
		}
		return whereString;
	}
	public List<EwUserTime> getDataList(Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request) {
		List<EwUserTime> data=null;
		String defaultOrder=request.getParameter("defaultOrder");
		String orderString=getOrderString(EwUserTime.class,sort,order,null,new String[]{""},defaultOrder);
		String whereString=getListCondition(search,request);
		String sql=request.getParameter(Util._addtional_condition);
		if (sql==null || !sql.trim().toLowerCase().startsWith("select ")) {
			log.debug("Dao.listBy({},{})",whereString,orderString);
			startPageForList(limit,offset);
			data=ewUserTimeDao.listBy(whereString,orderString);
		} else {
			sql = SqlUtil.sqlAddCondition(sql, whereString);
			sql = SqlUtil.sqlAddOrderCondition(sql, orderString);
			log.debug("List by : {}",sql);
			try {
				startPageForList(limit,offset);
				data=SqlUtil.sqlQuery(EwUserTime.class,getFilterOrg(request),sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (data==null) return new ArrayList<EwUserTime>();
		return data;
	}
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean needLoadDictionary,String resortFirstField, Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		if (!Util.isEmpty(resortFirstField)) {
			try {
				service.dragSort(EwUserTime.class, resortFirstField, "id", getDataList(null, null, resortFirstField, "asc", null, request), request);
			} catch (Exception e) {
				return new RestJson().setMessage(e).toResponse();
			}
		}
		List<EwUserTime> data=getDataList(limit,offset,sort,order, search,request);
		service.beforeList(data,request);
		PageInfo<EwUserTime> pageInfo = new PageInfo<>(data);
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) {
			Map<String, Object> obj = getPagedTableData(pageInfo);
			RestJson j = new RestJson();
			j.put("data", obj);
			j.dictionary(getDictionary(request,true,null));
			return j.toResponse();
		} else return getTableDataEntity(pageInfo);
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, String requestDynaFields, String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(getDictionary(request,true, requestDynaFields)).toResponse();
		RestJson j = new RestJson();
		EwUserTime ewUserTime=null;
		if (Util.isEmpty(id)) {
			ewUserTime=EntityUtil.createEntity(EwUserTime.class,request);
			if (ewUserTime==null) ewUserTime=new EwUserTime();
			else {
				List<EwUserTime> list=ewUserTimeDao.list(ewUserTime);
				if (list!=null && list.size()>0)	ewUserTime=list.get(0);
			}
		} else {
			Integer iid=Integer.valueOf(id);
			ewUserTime=ewUserTimeDao.select(iid);
			if (ewUserTime==null)	ewUserTime=new EwUserTime();
		} 
		//在这里附加字段值
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(getDictionary(request,true,null));
		service.beforeUpdate(ewUserTime,request);
		j.data(ewUserTime);
		return j.toResponse();		
	}
	public void changeEmpty2Null(EwUserTime ewUserTime) {
		if (ewUserTime!=null) {
		}
	}
	@RequestMapping(value = "/save",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> save(Boolean updateSelective, @RequestBody EwUserTime ewUserTime, String idSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return doSave(updateSelective, ewUserTime, idSaved, request, response);
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@Transactional
	public ResponseEntity<Map<String, Object>> doSave(Boolean updateSelective, EwUserTime ewUserTime, String idSaved, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RestJson j=new RestJson();
		if (Objects.nonNull(updateSelective) && updateSelective) {
			if (Objects.nonNull(ewUserTime)) {
				int rows=ewUserTimeDao.updateSelective(ewUserTime);
				if (rows!=1) {
					throw new IotequException(IotequThrowable.FAILURE,"rows=="+rows);
				}
			} else throw new IotequException(IotequThrowable.NULL_OBJECT,"ewUserTime");
			return j.toResponse();
		}
		if (ewUserTime==null) {
			j.setErrorCode(IotequThrowable.NULL_OBJECT,"ewUserTime");
		}
		int rows=0;
		if (ewUserTime0!=null && StringUtil.toJsonString(ewUserTime0).equals(StringUtil.toJsonString(ewUserTime))) {
			j.setSuccess(true);
			j.setMessage("nochange");
			j.parameter("noChanged", true);
			return j.toResponse();
		}
		changeEmpty2Null(ewUserTime);
		service.beforeSave(ewUserTime,request);
			if (Util.isEmpty(ewUserTime.getId())) {
			rows=ewUserTimeDao.insert(ewUserTime);
			if (rows>0) {
				Util.writeLog(log,"ew_user_time insert",null,"Insert into <%s> %s=%s","ew_user_time","id",ewUserTime.getId().toString());
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("idName", "id");
				map.put("recordId", ewUserTime.getId());
				j.parameter(map);
			}
		} else {
			if (EntityUtil.entityEquals(ewUserTime.getId(),idSaved))
				rows=ewUserTimeDao.update(ewUserTime);
			else {
				rows=ewUserTimeDao.updateBy(ewUserTime,Integer.valueOf(idSaved));
				if (rows>0) {
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("idName", "id");
					map.put("recordId", ewUserTime.getId());
					j.parameter(map);
				}
			}
			if (rows>0) Util.writeLog(log,"ew_user_time update",null,"Update %s %s=%s","ew_user_time","id",ewUserTime.getId().toString());
		}
		if (rows<=0) {
			throw new IotequException(IotequThrowable.FAILURE,"rows <= 0");
		} else {
			service.afterSave(ewUserTime0,ewUserTime,request,j);
		}
		return j.toResponse();
	}

	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			if (!service.customExport(sort,order,search,request, response)) {
				String defaultOrder=request.getParameter("defaultOrder");
				final String orderString=getOrderString(EwUserTime.class,sort,order,null,new String[]{""},defaultOrder);
				final String whereString=getListCondition(search,request);
				Util.writeLog(log,"export",null,"table data (where = %s)",whereString);
				setDictDataFromMap(request, getDictionary(request,false,null));
				IGetPagedData<EwUserTime> mainPD = new IGetPagedData<EwUserTime>() {
					@Override
					public PageInfo<EwUserTime> getPage(int pageNum,int pageSize) {
						PageHelper.startPage(pageNum, pageSize);
						List<EwUserTime> data = ewUserTimeDao.listBy(whereString,orderString);
						try {
							service.beforeExport(data,request);
							PageInfo<EwUserTime> pageInfo = new PageInfo<>(data);
							return pageInfo;
						} catch (Exception e) {
							log.debug(e.getMessage());
						}
						return null;
					}		
				};
				exportExcel(request, response, "ewUserTime.xlsx", "ewUserTime",mainPD) ;
			}
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		} finally {
			Util.setProgress(100);
		}
		return null;
	}
	@Override
	public void start() {
		Object bean = Util.getBean("payUserTimeService");
		if (Objects.nonNull(bean)) service = (IIotequService<EwUserTime>) bean;
		else service = new BaseIotequService<EwUserTime>();
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