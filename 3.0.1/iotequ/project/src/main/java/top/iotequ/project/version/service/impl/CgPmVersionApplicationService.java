package top.iotequ.project.version.service.impl;
import top.iotequ.project.version.pojo.PmVersionApplication;
import top.iotequ.project.version.dao.PmVersionApplicationDao;
import top.iotequ.framework.flow.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.service.impl.CgService;
import top.iotequ.framework.service.IDaoService;
import org.springframework.stereotype.Service;
import top.iotequ.framework.service.utils.DictionaryUtil;
import top.iotequ.framework.service.utils.UploadDownUtil;
import top.iotequ.framework.service.utils.QueryUtil;
import top.iotequ.util.*;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.project.version.service.impl.PmVersionApplicationService"})
@Service(value="pmVersionApplicationService")
public class CgPmVersionApplicationService extends CgService<PmVersionApplication>  {
    private static final Logger log = LoggerFactory.getLogger(CgPmVersionApplicationService.class);
    private static final int maxNoteLength = 1000;
    @Autowired IFlowService<PmVersionApplication> flowService;
    @Autowired
    private PmVersionApplicationDao pmVersionApplicationDao;
    @Override
    public Class<PmVersionApplication> getEntityClass() {
        return PmVersionApplication.class;
    }
    @Override
    public IDaoService<PmVersionApplication> getDaoService() {
        return pmVersionApplicationDao ;
    }
    @Override
    public IFlowService<PmVersionApplication> getFlowService() {
        return flowService;
    }
    @Override
    public Map<String,Object> getDictionary(PmVersionApplication obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowState")) map.put("dictFlowState",flowService.getStates());
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"project")) map.put("dictProject", DictionaryUtil.getDictListFromDatabase(obj,"SELECT * FROM pm_project where flow_state=8","id","name",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"applicationType")) map.put("dictApplicationType", DictionaryUtil.getDictListFromDatabase(obj,"pm_va_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowNextOperator")) map.put("dictFlowNextOperator",getFlowService()==null ? null: getFlowService().getDictionaryOfNextOperator(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection)));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowCopyToList")) map.put("dictFlowCopyToList",getFlowService()==null ? null: getFlowService().getDictionaryOfCopyToList(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection)));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowSelection")) map.put("dictFlowSelection",getFlowService()==null ? null: getFlowService().getSelections(Util.getRequest().getParameter(IFlowService.flowAction)));
        return map;
    }
    @Override
    public PmVersionApplication getDefaultObject(PmVersionApplication pmVersionApplication) throws IotequException {
        checkAvailable();
        if (pmVersionApplication==null)  pmVersionApplication=new PmVersionApplication();
        else pmVersionApplication.setId(null);
        flowService.checkPrivilege(null,"add",null);
        pmVersionApplication.setFlowRegisterTime(new Date());
        pmVersionApplication.setFlowRegisterBy(Util.getUser().getId());
        pmVersionApplication.setRegisterByName((String)QueryUtil.getSqlField("select register_by_name from sys_user where id=${flowRegisterBy}",pmVersionApplication));
        return pmVersionApplication;
    }
    @Override
    public void changeEmpty2Null(PmVersionApplication pmVersionApplication) {
        if (Objects.nonNull(pmVersionApplication)) {
        }
    }
    @Override
    public void changeNull2Default(PmVersionApplication pmVersionApplication) {
        if (pmVersionApplication.getId()==null) {
            pmVersionApplication.setId("");
        }
        if (pmVersionApplication.getFlowState()==null) {
            pmVersionApplication.setFlowState(1);
        }
        if (pmVersionApplication.getFlowRegisterTime()==null) {
            pmVersionApplication.setFlowRegisterTime(new Date());
        }
        if (pmVersionApplication.getFlowRegisterBy()==null) {
            pmVersionApplication.setFlowRegisterBy(Util.getUser().getId());
        }
        if (pmVersionApplication.getRegisterByName()==null) {
            pmVersionApplication.setRegisterByName("sql:select register_by_name from sys_user where id=${flowRegisterBy}");
        }
        if (pmVersionApplication.getProject()==null) {
            pmVersionApplication.setProject("");
        }
        if (pmVersionApplication.getApplicationType()==null) {
            pmVersionApplication.setApplicationType(0);
        }
        if (pmVersionApplication.getCustomer()==null) {
            pmVersionApplication.setCustomer("");
        }
        if (pmVersionApplication.getLicence()==null) {
            pmVersionApplication.setLicence("");
        }
        if (pmVersionApplication.getDescription()==null) {
            pmVersionApplication.setDescription("");
        }
    }
    @Override
    public void changeFileField( PmVersionApplication pmVersionApplication) {
        if (pmVersionApplication.getId()!=null)
            pmVersionApplication.setAddFile(UploadDownUtil.removeFilesExclusive(getEntityClass(),"addFile",pmVersionApplication.getId().toString(),pmVersionApplication.getAddFile(),true));
        else
            pmVersionApplication.setAddFile(null);
    }
}
