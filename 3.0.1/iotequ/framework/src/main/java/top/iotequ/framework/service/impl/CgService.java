package top.iotequ.framework.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.pojo.CgEntity;
import top.iotequ.framework.service.ICgService;
import top.iotequ.framework.service.IDaoService;
import top.iotequ.framework.service.IGetPagedData;
import top.iotequ.framework.service.IImportPagedData;
import top.iotequ.framework.service.utils.*;
import top.iotequ.framework.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.*;

public abstract class CgService<T extends CgEntity> implements ICgService<T>, CommandLineRunner {
    /**************************  Licence 控制管理    **********************************************/
    private Integer verLicence=null;
    private Integer verTrialDays=null;
    Class<T> clazz = getEntityClass();
    private CgTableAnnotation annotation=null;
    protected IDaoService daoService = getDaoService();
    protected IFlowService flowService = getFlowService();

    /** Licence 处理 **************************************/

    @Override public Integer getLicence() { return verLicence;};
    @Override public Integer getTrialDays() { return verTrialDays; };

    @Override
    public int checkAvailable() throws IotequException {
        if (verTrialDays==null)  return Integer.MAX_VALUE;
        else if (verTrialDays<=0) throw new IotequException(IotequThrowable.VERSION_EXPIRED,null);
        else return verTrialDays;
    }
    @Override
    public int getLicenceLeft() throws IotequException {
        if (verLicence==null) return Integer.MAX_VALUE;
        else {
            int count = SqlUtil.checkTableLicenceLeft(annotation.module(), annotation.name() , verLicence);
            if (count <= 0) throw new IotequException(IotequThrowable.NO_ENOUGH_LICENCE,null);
            else return count;
        }
    }

    @Override
    public String getGeneratorName() {
        return annotation.generatorName();
    }
    @Override
    public void initial() {
        clazz = getEntityClass();
        annotation=clazz.getAnnotation(CgTableAnnotation.class);
        daoService = getDaoService();
        flowService = getFlowService();
        if (annotation.hasLicence()) {
            String scModule = annotation.module();
            Logger log = LoggerFactory.getLogger(this.getClass());
            Environment env = Util.getBean(Environment.class);
            String sc = null;
            try {
                sc = MachineInfo.getSetupCode();
            } catch (Exception e) {}
            String sn = (sc==null ? null : env.getProperty(sc+".sn_"+scModule));
            if (sn==null) sn =  env.getProperty("svas.sn_" + scModule);
            int li = 0;
            if (sn != null && !sn.isEmpty()) {
                li = Util.getLicence(sn, scModule);
            }
            if (li <= 0) {  // 未配置序列号
                if (annotation.trialLicence()>=0) verLicence = annotation.trialLicence();
                if (annotation.trialDays()>0) {
                    verTrialDays = annotation.trialDays();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            verTrialDays=0;
                        }
                    }, DateUtil.dateAdd(new Date(), verTrialDays, DateUtil.DAY));
                }
            } else {
                verLicence = li;
            }
            if (verTrialDays == null) {
                if (verLicence == null)
                    log.info(String.format("---------- %s free version", scModule));
                else
                    log.info(String.format("---------- %s version,licence = %d", scModule, verLicence));
            } else if (verTrialDays <= 0)
                log.info(String.format("---------- %s trial version expired", scModule));
            else {
                if (verLicence == null)
                    log.info(String.format("---------- %s unlimitted trial version : %d days left", scModule, verTrialDays));
                else
                    log.info(String.format("---------- %s trial version : %d days left , licence = %d", scModule, verTrialDays, verLicence));
            }
            try {
                getLicenceLeft();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    @Override
    public CgTableAnnotation getCgTableAnnotation() {
        return annotation;
    }

    @Override
    public T select(String id) {
        return ("String".equals(annotation.pkType()) ? (T) daoService.select(id) :(T) daoService.select(Integer.valueOf(id)));
    }

    @Override
    public String orgCodePrivilege(HttpServletRequest request) {
        return SqlUtil.getFilterOrg() ? QueryUtil.getFilterOrg(clazz) : null;
    }
    @Override
    public boolean dragSort(String sortField, List<T> list, HttpServletRequest request) throws IotequException {
        if (Util.isEmpty(list)) return false;
        try {
            String pkField = annotation.entityPk();
            int order0 = 10, orderStep = 10;
            String s = request.getParameter("minOrderNumber");
            if (!Util.isEmpty(s)) order0 = Integer.parseInt(s);
            s = request.getParameter("orderNumberStep");
            if (!Util.isEmpty(s)) orderStep = Integer.parseInt(s);
            String table = annotation.name();
            String pk = annotation.pk();
            String f = EntityUtil.getDBFieldNameFrom(clazz, sortField);
            String sql = "select " + pk + " from " + table + " order by " + f + " asc";
            int order = order0;
            for (Object obj : list) {
                SqlUtil.sqlExecute("update " + table + " set " + f + " = " + order + " where " + pk + " = ?", EntityUtil.getPrivateField(obj, pkField));
                order = order + orderStep;
            }
            return true;
        } catch (Exception e) {
            throw IotequException.newInstance(e);
        }
    }

    private String getListCondition(String search,HttpServletRequest request) {
        String entities = request.getParameter("queryEntities");
        String whereString = QueryUtil.getWhereString(clazz,orgCodePrivilege(request),
        Util.isEmpty(entities) ? null : search, Util.isEmpty(entities) ? null : entities.split(","), request);
        String filter=listFilter(request.getParameter("pathNameOfQuery"));
        if (!Util.isEmpty(filter)) {
            if (Util.isEmpty(whereString)) whereString=filter;
            else whereString="("+filter+") and (" +whereString+")";
        }
		if (annotation.hasLicence()) {
            filter = SqlUtil.licenceCondition(annotation.module(), annotation.name(), getLicence());
            if (!Util.isEmpty(filter)) {
                if (Util.isEmpty(whereString)) whereString = filter;
                else whereString = "(" + filter + ") and (" + whereString + ")";
            }
        }
        return whereString;
    }
    @Override
    public List<T> getDataList(Integer pageSize,Integer pageNumber,String sort,String order, String search, HttpServletRequest request) {
        List<T> data=null;
        String parentEntityField = annotation.parentEntityField();
        String defaultOrder=request.getParameter("defaultOrder");
        String groupByEntityFields=request.getParameter("groupByEntityFields");
        String orderString=QueryUtil.getOrderString(clazz,sort,order,
                Util.isEmpty(parentEntityField)?null:EntityUtil.getDBFieldNameFrom(clazz,parentEntityField),
                Util.isEmpty(groupByEntityFields)?null:groupByEntityFields.split(","), defaultOrder);
        String whereString=getListCondition(search,request);
        PageUtil.setStartPageNumber(pageSize,pageNumber);
        data=daoService.listBy(whereString,orderString);
        if (data==null) return new ArrayList<T>();
        return data;
    }

    public RestJson getListPageData(Boolean needLoadDictionary,String resortFirstField, Integer pageSize,Integer pageNumber,String sort,String order, String search,HttpServletRequest request) throws IotequException {
        checkAvailable();
        if (!Util.isEmpty(resortFirstField)) dragSort(resortFirstField, getDataList(null, null, resortFirstField, "asc", null, request), request);
        List<T> data=getDataList(pageSize, pageNumber, sort, order, search, request);
        RestJson j = new RestJson();
        beforeList(data,request);
	    if (Objects.nonNull(flowService) && !Util.isEmpty(data)) {
            String userId=Util.getUser().getId();
            for (T d:data) {
                EntityUtil.setPrivateField(d,"flowAvailableActions",IFlowService.getAllOperations(request,flowService, d, userId));
            }
        }
        PageInfo<T> pageInfo = new PageInfo<>(data);
        Map<String, Object> obj = PageUtil.getPagedTableData(pageInfo);
        j.put("data", obj);
        if (Objects.nonNull(needLoadDictionary) && needLoadDictionary) {
            try {
                j.dictionary(getDictionary(null,true,null));
            } catch (Exception e) {}
        }
        return j;
    }

    @Override
    public T getRecord(String id,HttpServletRequest request) throws Exception {
        checkAvailable();
        if (Util.isEmpty(id)) {
            T obj = EntityUtil.createEntity(clazz,request);
            if (Objects.isNull(obj)) obj=clazz.newInstance();
			else {
                List<T> list=daoService.list(obj);
                if (list!=null && list.size()>0) obj = list.get(0);
            }
            beforeUpdate(obj,request);
			return obj;
        } else {
            T obj = select(id);
            beforeUpdate(obj,request);
            return obj;
        }
    }

    @Override
    public void download(String field, String id, String fileName,HttpServletResponse response) throws IotequException {
		checkAvailable();
        UploadDownUtil.downloadFile(clazz,field, id, fileName, response);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public RestJson doSave(boolean isNew, String flowCode,Integer totalFilePart, T obj, String idSaved, HttpServletRequest request) throws Exception {
        if (obj==null) throw new IotequException(IotequThrowable.NULL_OBJECT,"obj");
        T objFromClient = EntityUtil.entityCopyFrom(getEntityClass(),obj);
        if (!Util.isEmpty(annotation.parentEntityField()) && !Util.isEmpty(annotation.entityPk())) {
            QueryUtil.checkParentValid(annotation.name(), EntityUtil.getDBFieldNameFrom(clazz,annotation.parentEntityField()), annotation.pk(),
                    EntityUtil.getPrivateField(obj,annotation.parentEntityField()), obj.getPkValue());
        }
        RestJson j=new RestJson();
        checkAvailable();
        getLicenceLeft();
        T obj0 = isNew || Util.isEmpty(idSaved)? null: select(idSaved);
        changeEmpty2Null(obj);
        int rows=0;
        if (!isNew && Util.isEmpty(flowCode) && obj0!=null && (totalFilePart==null || totalFilePart==0) && StringUtil.toJsonString(obj0).equals(StringUtil.toJsonString(obj)) ) {
            j.setSuccess(true);
            j.setMessage("nochange");
            j.parameter("noChanged", true);
            return j;
        }
        changeFileField(obj);

        if (isNew) {
            beforeSave(obj0,obj,false,request);
			setPrimaryKey(obj);
            if (Objects.nonNull(flowService)) flowService.checkPrivilege(obj,"add",null);
            if (Objects.nonNull(flowService)) {
                flowService.setNextOperator(obj,request.getParameter(flowService.flowNextOperator));
                flowService.setCopyToList(obj,request.getParameter(flowService.flowCopyToList));
            }
            rows=daoService.insert(obj);
            if (rows>0) {
                if (Objects.nonNull(flowService)) {
                    flowService.saveFlowProcess(flowService.generateFlowProcess(request, null, obj, null));
                    flowService.sendMessage(request, null, obj, "add");
                }
            }
        } else {
            beforeSave(obj0,obj,false,request);
            if (Objects.nonNull(flowService)) {
                flowService.checkPrivilege(obj, Util.isEmpty(flowCode, "update"), null);
                flowService.checkParameters(obj);
                if (!Util.isEmpty(flowCode)) flowService.stateTransfer(obj, flowCode, request.getParameter(IFlowService.flowSelection));
                flowService.setNextOperator(obj,request.getParameter(flowService.flowNextOperator));
                flowService.setCopyToList(obj,request.getParameter(flowService.flowCopyToList));
            }
            if (EntityUtil.entityEquals(obj.getPkValue(),idSaved))
                rows=daoService.update(obj);
			else {
                String type = annotation.pkType();
                if ("String".equals(type)) rows=daoService.updateBy(obj, idSaved );
                else rows = daoService.updateBy(obj,  Integer.valueOf(idSaved));
            }
			if (rows > 0) {
                if (Objects.nonNull(flowService)) {
                    flowService.saveFlowProcess(flowService.generateFlowProcess(request, obj0, obj, flowCode));
                    flowService.sendMessage(request, obj0, obj, Util.isEmpty(flowCode) ? "update" : flowCode);
                }
            }
        }
        if (rows<=0) throw new IotequException(IotequThrowable.FAILURE,"rows <= 0");
        if (totalFilePart!=null && totalFilePart>0) {  // must run after insert because fileuploading need pk value
            String fields = ",";
            for (Part p: request.getParts()) {
                String name = p.getName();
                if (!name.startsWith("filepart_")) continue;
                String field = p.getName().split("_")[1];
                if (fields.indexOf(","+field+",")<0) fields += (field+",");
            }
            final String [] fieldList=fields.split(",");
            Map<String,Object> newObj = new HashMap<>();
            for (String f : fieldList) {
                if (!Util.isEmpty(f)) {
                    String fileName = UploadDownUtil.uploadFile(clazz, f, obj.getPkValue().toString(), EntityUtil.getMultipleFrom(clazz, f), EntityUtil.getNullableFrom(clazz, f), request);
                    newObj.put(f, fileName);
                    EntityUtil.setPrivateField(obj, f, fileName);
                }
            }
            newObj.put(annotation.entityPk(),obj.getPkValue());
            daoService.updateSelective(newObj);
        }
        afterSave(obj0, obj, request, j);
        if (Objects.nonNull(flowService)) {
            try {
                String userId = Util.getUser().getId();
                EntityUtil.setPrivateField(obj, "flowAvailableActions", IFlowService.getAllOperations(request, flowService, obj, userId));
            } catch (Exception e) {
            }
        }
        j.parameter("record",EntityUtil.mapOfNonEqualField(obj,objFromClient));  // 通过 record 传递前后端变化字段
        return j;
    }
    @Transactional(rollbackFor=Exception.class)
    @Override
    public RestJson updateSelective(List<Map<String,Object>> list) throws IotequException {
        int rows = 0;
        RestJson j = new RestJson();
        List<Exception> exceptionList=new ArrayList<>();
        if (!Util.isEmpty(list)) {
            String [] ids = new String[list.size()];
            boolean isNew = false;
            for (int i=0;i<list.size();i++) {
                Map<String,Object> obj = list.get(i);
                Object newField = obj.get("thisRecordIsNewRecord");
                if (newField!=null) isNew = (Boolean)(newField);
                else isNew = Util.isEmpty(obj.get(this.getCgTableAnnotation().entityPk())) ;
                ids[i]=null;
                try {
                    if (isNew) {  // fastAdd
                        try {
                            for (String key:obj.keySet()) {
                                if ("[null]".equals(StringUtil.toString(obj.get(key))) && key.endsWith("_string_")) obj.put(key.substring(0,key.length()-8),null);
                            }
                            T entity = EntityUtil.entityFromMap(obj,this.getEntityClass());
                            doSave(true, null, 0, entity, null, Util.getRequest());
                            ids[i] = StringUtil.toString(entity.getPkValue());
                            rows++;
                        } catch (Exception e) {}
                    } else if (daoService.updateSelective(obj) == 1) {
                        ids[i] = StringUtil.toString(obj.get(this.getCgTableAnnotation().entityPk()));
                        rows++;
                    }
                } catch (Exception e) {
                    exceptionList.add(e);
                }
            }
            if (rows==0) {
                if (exceptionList.size()>0) j.setError(exceptionList.get(0));
                else j.setError(IotequThrowable.FAILURE,"rows=0",null);
            } else if (exceptionList.size()>0) {
                j.setMessage(exceptionList.get(0).getMessage());
            }
            j.put("savedRecordIds",ids);
        } else throw new IotequException(IotequThrowable.NULL_OBJECT,null);
        return j.parameter("rows",rows);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override public  RestJson doDelete(String id,List<Map<String,String>> sons, HttpServletRequest request) throws IotequException {
        RestJson j=new RestJson();
        checkAvailable();
        if (Util.isEmpty(id)) throw new IotequException(IotequThrowable.NULL_OBJECT,"id");
        if (Objects.nonNull(flowService)) flowService.checkDeletePrivilege(id,null);
        beforeDelete(id,request);
        T obj = Objects.nonNull(flowService) ? select(id) : null;
        String type=annotation.pkType();
        int rows = "String".equals(type) ? daoService.delete(id) : daoService.delete(Integer.valueOf(id));
        if (rows>0) {
            if (Objects.nonNull(flowService)) {
                flowService.deleteFlowProcess(id);
                flowService.sendMessage(request, null, obj, "delete");
            }
            UploadDownUtil.removeFilesWithId(clazz,id);
            afterDelete(id,request,j);
        }
        if (!Util.isEmpty(sons)) {
            for (Map<String,String> m : sons) {
                String table = m.get("table");
                String parent = m.get("parent");
                SqlUtil.sqlExecute(String.format("delete from %s where %s = ?",table,parent),id);
            }
        }
        return j;
    }
    @Transactional(rollbackFor=Exception.class)
    @Override public   RestJson doBatchDelete(String ids,List<Map<String,String>> sons,HttpServletRequest request) throws IotequException {
        checkAvailable();
        if (Util.isEmpty(ids)) throw new IotequException(IotequThrowable.NULL_OBJECT,"ids");
        RestJson j=new RestJson();
        if (Objects.nonNull(flowService)) flowService.checkDeletePrivilege(ids,null);
        beforeDelete(ids,request);
        int rows = daoService.deleteBatch(ids);
        if (rows>0) {
            if (Objects.nonNull(flowService)) flowService.deleteFlowProcess(ids);
            if (!Util.isEmpty(sons)) {
                for (Map<String,String> m : sons) {
                    String table = m.get("table");
                    String parent = m.get("parent");
                    SqlUtil.sqlExecute(String.format("delete from %s where %s = ?",table,parent),ids);
                }
            }
            UploadDownUtil.removeFilesWithId(clazz,ids);
            afterDelete(ids,request,j);
            j.parameter("rows",rows);
        }
        else throw new IotequException(IotequThrowable.FAILURE,"rows<=0");
        return j;
    }

    @Override public void export(SXSSFWorkbook workBook, int startProgress, int maxProgress, List<ICgService> sons, String fileName, String sheet, String sort, String order, String search, HttpServletRequest request, HttpServletResponse response) throws Exception{
        checkAvailable();
        if (!customExport(sort,order,search,request, response)) {
            String parentEntityField = annotation.parentEntityField();
            String defaultOrder=request.getParameter("defaultOrder");
            String groupByEntityFields=request.getParameter("groupByEntityFields");
            String orderString=QueryUtil.getOrderString(clazz,sort,order,
                    Util.isEmpty(parentEntityField)?null:EntityUtil.getDBFieldNameFrom(clazz,parentEntityField),
                    Util.isEmpty(groupByEntityFields)?null:groupByEntityFields.split(","), defaultOrder);
            String whereString=getListCondition(search,request);
            DictionaryUtil.setDictDataFromMap(request, getDictionary(null,false,null));
            IGetPagedData<T> mainPD = new IGetPagedData<T>() {
                @Override
                public PageInfo<T> getPage(int pageNum, int pageSize) {
                    PageHelper.startPage(pageNum, pageSize);
                    List<T> data = daoService.listBy(whereString,orderString);
                    try {
                        beforeExport(data,request);
                        PageInfo<T> pageInfo = new PageInfo<>(data);
                        return pageInfo;
                    } catch (Exception e) {
                    }
                    return null;
                }
            };
            if (Objects.nonNull(workBook)) PoiUtil.exportExcel(workBook,startProgress,maxProgress,request, response, fileName, sheet,mainPD);
            else if (Util.isEmpty(sons)) PoiUtil.exportExcel(request, response, fileName, sheet,mainPD);
            else {
                int step = 100/(sons.size()+1);
                int start = step;
                SXSSFWorkbook workBook1 = PoiUtil.exportExcel(null,0,step,request, response, fileName, sheet,mainPD);
                for (ICgService service : sons) {
                    //service.export(workBook1,start,start + step);
                }
                Util.setProgress(100);
                workBook1.close();
            }
        }
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public RestJson doImport(MultipartFile file,HttpServletRequest request) throws Exception {
        checkAvailable();
        RestJson j=new RestJson();
        j.parameter(Util.ORG_FILTER_CONDITION,false);
        if (!customImport(file,request,j)) {
            if (Util.isEmpty(file)) throw new IotequException(IotequThrowable.NULL_OBJECT,"file");
            IImportPagedData<T> mainPD=new IImportPagedData<T>(){
                @Override
                public String saveData(List<T> list,int startProgress,int endProgress) throws Exception {
                    Util.setProgress(startProgress);
                    try {
                        double step = ((double)(endProgress - startProgress)) / 7;
                        if (list==null || list.size()==0) return null;
                        else {
                            for (T obj : list) {
                                changeNull2Default(obj);
                                beforeDelete(obj.getPkValue().toString(), request);
                            }
                            Util.setProgress((int)(startProgress + step));
                            beforeImport(list, request);
                            Util.setProgress((int)(startProgress + 2*step));
                            if (list.size() == 0) return null;
                            daoService.deleteList(list);
                            Util.setProgress((int)(startProgress + 3*step));
                            daoService.insertBatchWithId(list);
                            Util.setProgress((int)(startProgress + 4*step));
                            for (T t : list) {
                                Object id = t.getPkValue();
                                if (id != null) afterDelete(id.toString(), request, j);
                                afterSave(null, t, request, j);
                            }
                            Util.setProgress((int)(startProgress + 5*step));
                            afterImport(list, annotation.entityPk(), request, j);
                            Util.setProgress((int)(startProgress + 6*step));
                            SqlSessionTemplate sqlSessionTemplate = Util.getBean(SqlSessionTemplate.class);
                            if ("1".equals(annotation.pkKeyType()) && "Oracle".equals(sqlSessionTemplate.getConfiguration().getDatabaseId())) {
                                String table = annotation.name().toUpperCase();
                                String pk = annotation.pk().toUpperCase();
                                SqlUtil.sqlExecute("CALL CALC_SEQUENCE('" + table + "','" + pk + "')");
                            }
                            return null;
                        }
                    } finally {
                        Util.setProgress(endProgress);
                    }
                }
            };
            // sons import

            String msg = PoiUtil.importExcel(request,file, StringUtil.firstLetterLower(clazz.getSimpleName()),clazz,null,mainPD);
            if (!Util.isEmpty(msg)) j.setMessage(msg);
        }
        return j;
    }
    @Override
    public RestJson sqlQuery(Map<String,Object> params) throws Exception {
        RestJson j=new RestJson();
        if (Util.isEmpty(params.get("fields"))) {
            j.setErrorCode(IotequThrowable.NULL_OBJECT,"fields");
        } else {
            try {
                String sql="SELECT "+params.get("fields").toString() + " FROM ${table.name}"
                        + (Util.isEmpty(params.get("join"))?"" : " "+params.get("join").toString())
                        + (Util.isEmpty(params.get("condition"))?"" : " "+params.get("condition").toString());
                List<Map<String, Object>> list=SqlUtil.sqlQuery(true,sql);
                j.put("data", list);
            } catch (Exception e) {
                j.setMessage(e);
            }
        }
        return j;
    }
    @Override
    public void run(String... args) throws Exception {
        initial();
    }
}
