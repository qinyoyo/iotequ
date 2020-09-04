package top.iotequ.ewallet.controller;
import top.iotequ.ewallet.pojo.EwUser;
import top.iotequ.ewallet.dao.EwUserDao;
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
@RequestMapping("/ewallet/payUser")
public class PayUserController extends BaseController<EwUser> implements SmartLifecycle {
	private static final Logger log = LoggerFactory.getLogger(PayUserController.class);
	@Autowired
	private EwUserDao ewUserDao;
	IIotequService<EwUser> service = null;
	public Class getEntityClass() {
		return EwUser.class;
	}
	public Map<String,Object> getDictionary(HttpServletRequest request, Boolean useTree, String dynaFields) {
		Map<String,Object> map=new HashMap<>();
		if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"gender")) map.put("dictGender", getDictListFromDatabase(request,"sys_sex",null,null,null,false,null));
		if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"idType")) map.put("dictIdType", getDictListFromDatabase(request,"sys_id_type",null,null,null,false,null));
		return map;
	}
	private String getListCondition(String search,HttpServletRequest request) {
		String entities = request.getParameter("queryEntities");
		String whereString = getWhereString(EwUser.class,service.orgCodePrivilege(request,EwUser.class),
			Util.isEmpty(entities) ? null : search, Util.isEmpty(entities) ? null : entities.split(","), request);
		String filter=service.listFilter(request.getParameter("pathNameOfQuery"));
		if (!Util.isEmpty(filter)) {
			if (Util.isEmpty(whereString)) whereString=filter;
			else whereString="("+filter+") and (" +whereString+")";
		}
		return whereString;
	}
	public List<EwUser> getDataList(Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request) {
		List<EwUser> data=null;
		String defaultOrder=request.getParameter("defaultOrder");
		String orderString=getOrderString(EwUser.class,sort,order,null,new String[]{""},defaultOrder);
		String whereString=getListCondition(search,request);
		String sql=request.getParameter(Util._addtional_condition);
		if (sql==null || !sql.trim().toLowerCase().startsWith("select ")) {
			log.debug("Dao.listBy({},{})",whereString,orderString);
			startPageForList(limit,offset);
			data=ewUserDao.listBy(whereString,orderString);
		} else {
			sql = SqlUtil.sqlAddCondition(sql, whereString);
			sql = SqlUtil.sqlAddOrderCondition(sql, orderString);
			log.debug("List by : {}",sql);
			try {
				startPageForList(limit,offset);
				data=SqlUtil.sqlQuery(EwUser.class,getFilterOrg(request),sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (data==null) return new ArrayList<EwUser>();
		return data;
	}
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean needLoadDictionary,String resortFirstField, Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		if (!Util.isEmpty(resortFirstField)) {
			try {
				service.dragSort(EwUser.class, resortFirstField, "userNo", getDataList(null, null, resortFirstField, "asc", null, request), request);
			} catch (Exception e) {
				return new RestJson().setMessage(e).toResponse();
			}
		}
		List<EwUser> data=getDataList(limit,offset,sort,order, search,request);
		service.beforeList(data,request);
		PageInfo<EwUser> pageInfo = new PageInfo<>(data);
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) {
			Map<String, Object> obj = getPagedTableData(pageInfo);
			RestJson j = new RestJson();
			j.put("data", obj);
			j.dictionary(getDictionary(request,true,null));
			return j.toResponse();
		} else return getTableDataEntity(pageInfo);
	}
	@RequestMapping(value = "/default",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> defaultValue(Boolean needLoadDictionary, String requestDynaFields, EwUser ewUser,HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(getDictionary(request,true, requestDynaFields)).toResponse();
		if (ewUser==null)  ewUser=new EwUser();
		else ewUser.setUserNo(null);  // 禁止绕开update修改数据
		//在这里设置服务器端的默认值
		service.customAdd(ewUser, request);
		return new RestJson()
			.data(ewUser)
			.dictionary(Objects.nonNull(needLoadDictionary) && needLoadDictionary ? getDictionary(request,true,null) : null)
			.toResponse();
	}
	@RequestMapping(value = "/record",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> record(Boolean needLoadDictionary, String requestDynaFields, String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!Util.isEmpty(requestDynaFields)) return new RestJson().dictionary(getDictionary(request,true, requestDynaFields)).toResponse();
		RestJson j = new RestJson();
		EwUser ewUser=null;
		if (Util.isEmpty(id)) {
			ewUser=EntityUtil.createEntity(EwUser.class,request);
			if (ewUser==null) ewUser=new EwUser();
			else {
				List<EwUser> list=ewUserDao.list(ewUser);
				if (list!=null && list.size()>0)	ewUser=list.get(0);
			}
		} else {
			ewUser=ewUserDao.select(id);
			if (ewUser==null)	ewUser=new EwUser();
		} 
		//在这里附加字段值
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(getDictionary(request,true,null));
		service.beforeUpdate(ewUser,request);
		j.data(ewUser);
		return j.toResponse();		
	}
	public void changeEmpty2Null(EwUser ewUser) {
		if (ewUser!=null) {
			if ("".equals(ewUser.getMobilePhone())) ewUser.setMobilePhone(null);
			if ("".equals(ewUser.getEmail())) ewUser.setEmail(null);
			if ("".equals(ewUser.getWechatOpenid())) ewUser.setWechatOpenid(null);
		}
	}
	@RequestMapping(value = "/save",method = {RequestMethod.PUT,RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> save(Boolean updateSelective, @RequestBody EwUser ewUser, String userNoSaved, HttpServletRequest request, HttpServletResponse response) {
		try {
			return doSave(updateSelective, ewUser, userNoSaved, request, response);
		} catch (Exception e) {
			return new RestJson().setMessage(e).toResponse();
		}
	}
	@Transactional
	public ResponseEntity<Map<String, Object>> doSave(Boolean updateSelective, EwUser ewUser, String userNoSaved, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RestJson j=new RestJson();
		if (Objects.nonNull(updateSelective) && updateSelective) {
			if (Objects.nonNull(ewUser)) {
				int rows=ewUserDao.updateSelective(ewUser);
				if (rows!=1) {
					throw new IotequException(IotequThrowable.FAILURE,"rows=="+rows);
				}
			} else throw new IotequException(IotequThrowable.NULL_OBJECT,"ewUser");
			return j.toResponse();
		}
		if (ewUser==null) {
			j.setErrorCode(IotequThrowable.NULL_OBJECT,"ewUser");
		}
		if (Util.isEmpty(ewUser.getMobilePhone())) ewUser.setMobilePhone(null);
		if (Util.isEmpty(ewUser.getEmail())) ewUser.setEmail(null);
		if (Util.isEmpty(ewUser.getWechatOpenid())) ewUser.setWechatOpenid(null);
		int rows=0;
		if (ewUser0!=null && StringUtil.toJsonString(ewUser0).equals(StringUtil.toJsonString(ewUser))) {
			j.setSuccess(true);
			j.setMessage("nochange");
			j.parameter("noChanged", true);
			return j.toResponse();
		}
		changeEmpty2Null(ewUser);
		service.beforeSave(ewUser,request);
			if (Util.isEmpty(userNoSaved)) {
			if (Util.isEmpty(ewUser.getUserNo())) service.setPrimaryKey(ewUser);
			rows=ewUserDao.insert(ewUser);
			if (rows>0) {
				Util.writeLog(log,"ew_user insert",null,"Insert into <%s> %s=%s","ew_user","userNo",ewUser.getUserNo().toString());
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("idName", "userNo");
				map.put("recordId", ewUser.getUserNo());
				j.parameter(map);
			}
		} else {
			if (EntityUtil.entityEquals(ewUser.getUserNo(),userNoSaved))
				rows=ewUserDao.update(ewUser);
			else {
				rows=ewUserDao.updateBy(ewUser,userNoSaved);
				if (rows>0) {
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("idName", "userNo");
					map.put("recordId", ewUser.getUserNo());
					j.parameter(map);
				}
			}
			if (rows>0) Util.writeLog(log,"ew_user update",null,"Update %s %s=%s","ew_user","userNo",ewUser.getUserNo().toString());
		}
		if (rows<=0) {
			throw new IotequException(IotequThrowable.FAILURE,"rows <= 0");
		} else {
			service.afterSave(ewUser0,ewUser,request,j);
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
		int rows = ewUserDao.delete(id);
		if (rows>0) Util.writeLog(log,"ew_user delete",null,"Delete from %s id=%s","ew_user",id);
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
		int rows = ewUserDao.deleteBatch(ids);
		if (rows>0) Util.writeLog(log,"ew_user batch delete",null,"Batch delete %s ids=%s","ew_user",ids);
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
				final String orderString=getOrderString(EwUser.class,sort,order,null,new String[]{""},defaultOrder);
				final String whereString=getListCondition(search,request);
				Util.writeLog(log,"export",null,"table data (where = %s)",whereString);
				setDictDataFromMap(request, getDictionary(request,false,null));
				IGetPagedData<EwUser> mainPD = new IGetPagedData<EwUser>() {
					@Override
					public PageInfo<EwUser> getPage(int pageNum,int pageSize) {
						PageHelper.startPage(pageNum, pageSize);
						List<EwUser> data = ewUserDao.listBy(whereString,orderString);
						try {
							service.beforeExport(data,request);
							PageInfo<EwUser> pageInfo = new PageInfo<>(data);
							return pageInfo;
						} catch (Exception e) {
							log.debug(e.getMessage());
						}
						return null;
					}		
				};
				exportExcel(request, response, "ewUser.xlsx", "ewUser",mainPD) ;
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
		Object bean = Util.getBean("payUserService");
		if (Objects.nonNull(bean)) service = (IIotequService<EwUser>) bean;
		else service = new BaseIotequService<EwUser>();
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