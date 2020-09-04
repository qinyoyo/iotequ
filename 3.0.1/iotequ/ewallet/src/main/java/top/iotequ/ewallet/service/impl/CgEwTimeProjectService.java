package top.iotequ.ewallet.service.impl;
import top.iotequ.ewallet.pojo.EwTimeProject;
import top.iotequ.ewallet.dao.EwTimeProjectDao;
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
import top.iotequ.framework.util.*;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.ewallet.service.impl.EwTimeProjectService"})
@Service(value="ewTimeProjectService")
public class CgEwTimeProjectService extends CgService<EwTimeProject>  {
private static final Logger log = LoggerFactory.getLogger(CgEwTimeProjectService.class);
    @Autowired
    private EwTimeProjectDao ewTimeProjectDao;
    @Override
    public Class<EwTimeProject> getEntityClass() {
        return EwTimeProject.class;
    }
    @Override
    public IDaoService<EwTimeProject> getDaoService() {
        return ewTimeProjectDao ;
    }
    @Override
    public IFlowService<EwTimeProject> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(EwTimeProject obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public EwTimeProject getDefaultObject(EwTimeProject ewTimeProject) throws IotequException {
        checkAvailable();
        if (ewTimeProject==null)  ewTimeProject=new EwTimeProject();
        else ewTimeProject.setId(null);
        return ewTimeProject;
    }
    @Override
    public void changeEmpty2Null(EwTimeProject ewTimeProject) {
        if (Objects.nonNull(ewTimeProject)) {
        }
    }
    @Override
    public void changeNull2Default(EwTimeProject ewTimeProject) {
        if (ewTimeProject.getId()==null) {
            ewTimeProject.setId(0);
        }
        if (ewTimeProject.getName()==null) {
            ewTimeProject.setName("");
        }
        if (ewTimeProject.getBaseValue()==null) {
            ewTimeProject.setBaseValue(60);
        }
        if (ewTimeProject.getBasePrice()==null) {
            ewTimeProject.setBasePrice(10);
        }
        if (ewTimeProject.getBonusPoint()==null) {
        }
    }
    @Override
    public void changeFileField( EwTimeProject ewTimeProject) {
        if (ewTimeProject.getId()!=null)
            ewTimeProject.setIcon(UploadDownUtil.removeFilesExclusive(getEntityClass(),"icon",ewTimeProject.getId().toString(),ewTimeProject.getIcon(),true));
        else
            ewTimeProject.setIcon(null);
    }
}
