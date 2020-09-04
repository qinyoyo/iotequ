package top.iotequ.ewallet.controller;
import top.iotequ.ewallet.pojo.EwBill;
import top.iotequ.ewallet.dao.EwBillDao;
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
@RequestMapping("/ewallet/payBill")
public class PayBillController extends BaseController<EwBill> implements SmartLifecycle {
	private static final Logger log = LoggerFactory.getLogger(PayBillController.class);
	@Autowired
	private EwBillDao ewBillDao;
	IIotequService<EwBill> service = null;
	public Class getEntityClass() {
		return EwBill.class;
	}
	public Map<String,Object> getDictionary(HttpServletRequest request, Boolean useTree, String dynaFields) {
		Map<String,Object> map=new HashMap<>();
		String [] dictCostTypeValue= {"1","2","3"};
		//钱包,计次,计时
		String [] dictCostTypeText= {"payBill.field.costType_0","payBill.field.costType_1","payBill.field.costType_2"};
		if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"costType")) map.put("dictCostType", getDictList(dictCostTypeValue,dictCostTypeText));
		return map;
	}
	private String getListCondition(String search,HttpServletRequest request) {
		String entities = request.getParameter("queryEntities");
		String whereString = getWhereString(EwBill.class,service.orgCodePrivilege(request,EwBill.class),
			Util.isEmpty(entities) ? null : search, Util.isEmpty(entities) ? null : entities.split(","), request);
		String filter=service.listFilter(request.getParameter("pathNameOfQuery"));
		if (!Util.isEmpty(filter)) {
			if (Util.isEmpty(whereString)) whereString=filter;
			else whereString="("+filter+") and (" +whereString+")";
		}
		return whereString;
	}
	public List<EwBill> getDataList(Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request) {
		List<EwBill> data=null;
		String defaultOrder=request.getParameter("defaultOrder");
		String orderString=getOrderString(EwBill.class,sort,order,null,new String[]{""},defaultOrder);
		String whereString=getListCondition(search,request);
		String sql=request.getParameter(Util._addtional_condition);
		if (sql==null || !sql.trim().toLowerCase().startsWith("select ")) {
			log.debug("Dao.listBy({},{})",whereString,orderString);
			startPageForList(limit,offset);
			data=ewBillDao.listBy(whereString,orderString);
		} else {
			sql = SqlUtil.sqlAddCondition(sql, whereString);
			sql = SqlUtil.sqlAddOrderCondition(sql, orderString);
			log.debug("List by : {}",sql);
			try {
				startPageForList(limit,offset);
				data=SqlUtil.sqlQuery(EwBill.class,getFilterOrg(request),sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (data==null) return new ArrayList<EwBill>();
		return data;
	}
	@RequestMapping(value = "/list",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> listData(Boolean needLoadDictionary,String resortFirstField, Integer limit,Integer offset,String sort,String order, String search,HttpServletRequest request, HttpServletResponse response) {
		if (!Util.isEmpty(resortFirstField)) {
			try {
				service.dragSort(EwBill.class, resortFirstField, "no", getDataList(null, null, resortFirstField, "asc", null, request), request);
			} catch (Exception e) {
				return new RestJson().setMessage(e).toResponse();
			}
		}
		List<EwBill> data=getDataList(limit,offset,sort,order, search,request);
		service.beforeList(data,request);
		PageInfo<EwBill> pageInfo = new PageInfo<>(data);
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
		EwBill ewBill=null;
		if (Util.isEmpty(id)) {
			ewBill=EntityUtil.createEntity(EwBill.class,request);
			if (ewBill==null) ewBill=new EwBill();
			else {
				List<EwBill> list=ewBillDao.list(ewBill);
				if (list!=null && list.size()>0)	ewBill=list.get(0);
			}
		} else {
			ewBill=ewBillDao.select(id);
			if (ewBill==null)	ewBill=new EwBill();
		} 
		//在这里附加字段值
		if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) j.dictionary(getDictionary(request,true,null));
		service.beforeUpdate(ewBill,request);
		j.data(ewBill);
		return j.toResponse();		
	}

	@RequestMapping(value = "/export",method = {RequestMethod.GET})
	public ResponseEntity<Map<String, Object>> export(String sort,String order, String search,HttpServletRequest request, HttpServletResponse response){
		try {
			if (!service.customExport(sort,order,search,request, response)) {
				String defaultOrder=request.getParameter("defaultOrder");
				final String orderString=getOrderString(EwBill.class,sort,order,null,new String[]{""},defaultOrder);
				final String whereString=getListCondition(search,request);
				Util.writeLog(log,"export",null,"table data (where = %s)",whereString);
				setDictDataFromMap(request, getDictionary(request,false,null));
				IGetPagedData<EwBill> mainPD = new IGetPagedData<EwBill>() {
					@Override
					public PageInfo<EwBill> getPage(int pageNum,int pageSize) {
						PageHelper.startPage(pageNum, pageSize);
						List<EwBill> data = ewBillDao.listBy(whereString,orderString);
						try {
							service.beforeExport(data,request);
							PageInfo<EwBill> pageInfo = new PageInfo<>(data);
							return pageInfo;
						} catch (Exception e) {
							log.debug(e.getMessage());
						}
						return null;
					}		
				};
				exportExcel(request, response, "ewBill.xlsx", "ewBill",mainPD) ;
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
		Object bean = Util.getBean("payBillService");
		if (Objects.nonNull(bean)) service = (IIotequService<EwBill>) bean;
		else service = new BaseIotequService<EwBill>();
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