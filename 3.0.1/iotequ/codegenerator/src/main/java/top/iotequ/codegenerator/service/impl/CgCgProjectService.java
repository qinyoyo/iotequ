package top.iotequ.codegenerator.service.impl;
import top.iotequ.codegenerator.pojo.CgProject;
import top.iotequ.codegenerator.dao.CgProjectDao;
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
@ConditionalOnMissingClass({"top.iotequ.codegenerator.service.impl.CgProjectService"})
@Service(value="cgProjectService")
public class CgCgProjectService extends CgService<CgProject>  {
private static final Logger log = LoggerFactory.getLogger(CgCgProjectService.class);
    @Autowired
    private CgProjectDao cgProjectDao;
    @Override
    public Class<CgProject> getEntityClass() {
        return CgProject.class;
    }
    @Override
    public IDaoService<CgProject> getDaoService() {
        return cgProjectDao ;
    }
    @Override
    public IFlowService<CgProject> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CgProject obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"modules")) map.put("dictModules", DictionaryUtil.getDictListFromDatabase(obj,"cg_modules",null,null,null,false,null));
        return map;
    }
    @Override
    public CgProject getDefaultObject(CgProject cgProject) throws IotequException {
        checkAvailable();
        if (cgProject==null)  cgProject=new CgProject();
        else cgProject.setId(null);
        return cgProject;
    }
    @Override
    public void changeEmpty2Null(CgProject cgProject) {
        if (Objects.nonNull(cgProject)) {
        }
    }
    @Override
    public void changeNull2Default(CgProject cgProject) {
        if (cgProject.getId()==null) {
            cgProject.setId("");
        }
        if (cgProject.getCode()==null) {
            cgProject.setCode("");
        }
        if (cgProject.getCreator()==null) {
            cgProject.setCreator("");
        }
        if (cgProject.getGroupId()==null) {
            cgProject.setGroupId("top.iotequ");
        }
        if (cgProject.getName()==null) {
            cgProject.setName("");
        }
        if (cgProject.getVersion()==null) {
            cgProject.setVersion("3.0.1-SNAPSHOT");
        }
        if (cgProject.getSpringModule()==null) {
            cgProject.setSpringModule(Util.boolValue("1"));
        }
        if (cgProject.getMavenModule()==null) {
            cgProject.setMavenModule(Util.boolValue("1"));
        }
        if (cgProject.getTest()==null) {
            cgProject.setTest(Util.boolValue("0"));
        }
    }
}
