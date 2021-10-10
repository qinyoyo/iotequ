package top.iotequ.project.product.service.impl;
import top.iotequ.project.product.pojo.PmProject;
import top.iotequ.project.product.dao.PmProjectDao;
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
@ConditionalOnMissingClass({"top.iotequ.project.product.service.impl.PmProjectService"})
@Service(value="pmProjectService")
public class CgPmProjectService extends CgService<PmProject>  {
    private static final Logger log = LoggerFactory.getLogger(CgPmProjectService.class);
    private static final int maxNoteLength = 1000;
    @Autowired IFlowService<PmProject> flowService;
    @Autowired
    private PmProjectDao pmProjectDao;
    @Override
    public Class<PmProject> getEntityClass() {
        return PmProject.class;
    }
    @Override
    public IDaoService<PmProject> getDaoService() {
        return pmProjectDao ;
    }
    @Override
    public IFlowService<PmProject> getFlowService() {
        return flowService;
    }
    @Override
    public Map<String,Object> getDictionary(PmProject obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowState")) map.put("dictFlowState",flowService.getStates());
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"type")) map.put("dictType", DictionaryUtil.getDictListFromDatabase(obj,"pm_product_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowNextOperator")) map.put("dictFlowNextOperator",getFlowService()==null ? null: getFlowService().getDictionaryOfNextOperator(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection)));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowCopyToList")) map.put("dictFlowCopyToList",getFlowService()==null ? null: getFlowService().getDictionaryOfCopyToList(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection)));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowSelection")) map.put("dictFlowSelection",getFlowService()==null ? null: getFlowService().getSelections(Util.getRequest().getParameter(IFlowService.flowAction)));
        return map;
    }
    @Override
    public PmProject getDefaultObject(PmProject pmProject) throws IotequException {
        checkAvailable();
        if (pmProject==null)  pmProject=new PmProject();
        else pmProject.setId(null);
        flowService.checkPrivilege(null,"add",null);
        pmProject.setFlowRegisterTime(new Date());
        pmProject.setFlowRegisterBy(Util.getUser().getId());
        pmProject.setRegisterByName((String)QueryUtil.getSqlField("select register_by_name from sys_user where id=${flowRegisterBy}",pmProject));
        return pmProject;
    }
    @Override
    public void changeEmpty2Null(PmProject pmProject) {
        if (Objects.nonNull(pmProject)) {
            if ("".equals(pmProject.getCode())) pmProject.setCode(null);
        }
    }
    @Override
    public void changeNull2Default(PmProject pmProject) {
        if (pmProject.getId()==null) {
            pmProject.setId("");
        }
        if (pmProject.getFlowState()==null) {
            pmProject.setFlowState(1);
        }
        if (pmProject.getFlowRegisterTime()==null) {
            pmProject.setFlowRegisterTime(new Date());
        }
        if (pmProject.getFlowRegisterBy()==null) {
            pmProject.setFlowRegisterBy(Util.getUser().getId());
        }
        if (pmProject.getRegisterByName()==null) {
            pmProject.setRegisterByName("sql:select register_by_name from sys_user where id=${flowRegisterBy}");
        }
        if (pmProject.getName()==null) {
            pmProject.setName("");
        }
        if (pmProject.getType()==null) {
            pmProject.setType(1);
        }
    }
    @Override
    public void changeFileField( PmProject pmProject) {
        if (pmProject.getId()!=null)
            pmProject.setAddFile(UploadDownUtil.removeFilesExclusive(getEntityClass(),"addFile",pmProject.getId().toString(),pmProject.getAddFile(),true));
        else
            pmProject.setAddFile(null);
    }
}
