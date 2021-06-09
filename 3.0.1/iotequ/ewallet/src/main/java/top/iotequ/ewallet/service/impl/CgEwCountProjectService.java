package top.iotequ.ewallet.service.impl;
import top.iotequ.ewallet.pojo.EwCountProject;
import top.iotequ.ewallet.dao.EwCountProjectDao;
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
@ConditionalOnMissingClass({"top.iotequ.ewallet.service.impl.EwCountProjectService"})
@Service(value="ewCountProjectService")
public class CgEwCountProjectService extends CgService<EwCountProject>  {
    private static final Logger log = LoggerFactory.getLogger(CgEwCountProjectService.class);
    @Autowired
    private EwCountProjectDao ewCountProjectDao;
    @Override
    public Class<EwCountProject> getEntityClass() {
        return EwCountProject.class;
    }
    @Override
    public IDaoService<EwCountProject> getDaoService() {
        return ewCountProjectDao ;
    }
    @Override
    public IFlowService<EwCountProject> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(EwCountProject obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public EwCountProject getDefaultObject(EwCountProject ewCountProject) throws IotequException {
        checkAvailable();
        if (ewCountProject==null)  ewCountProject=new EwCountProject();
        else ewCountProject.setId(null);
        return ewCountProject;
    }
    @Override
    public void changeEmpty2Null(EwCountProject ewCountProject) {
        if (Objects.nonNull(ewCountProject)) {
        }
    }
    @Override
    public void changeNull2Default(EwCountProject ewCountProject) {
        if (ewCountProject.getId()==null) {
            ewCountProject.setId(0);
        }
        if (ewCountProject.getName()==null) {
            ewCountProject.setName("");
        }
        if (ewCountProject.getBasePrice()==null) {
            ewCountProject.setBasePrice(10);
        }
        if (ewCountProject.getBaseValue()==null) {
            ewCountProject.setBaseValue(1);
        }
        if (ewCountProject.getBonusPoint()==null) {
        }
    }
    @Override
    public void changeFileField( EwCountProject ewCountProject) {
        if (ewCountProject.getId()!=null)
            ewCountProject.setIcon(UploadDownUtil.removeFilesExclusive(getEntityClass(),"icon",ewCountProject.getId().toString(),ewCountProject.getIcon(),true));
        else
            ewCountProject.setIcon(null);
    }
}
