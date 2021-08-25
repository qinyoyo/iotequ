package top.iotequ.checkIn.service.impl;
import top.iotequ.checkIn.pojo.CkRegister;
import top.iotequ.checkIn.dao.CkRegisterDao;
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
@ConditionalOnMissingClass({"top.iotequ.checkIn.service.impl.CkRegisterService"})
@Service(value="ckRegisterService")
public class CgCkRegisterService extends CgService<CkRegister>  {
    private static final Logger log = LoggerFactory.getLogger(CgCkRegisterService.class);
    @Autowired
    private CkRegisterDao ckRegisterDao;
    @Override
    public Class<CkRegister> getEntityClass() {
        return CkRegister.class;
    }
    @Override
    public IDaoService<CkRegister> getDaoService() {
        return ckRegisterDao ;
    }
    @Override
    public IFlowService<CkRegister> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CkRegister obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sex")) map.put("dictSex", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public CkRegister getDefaultObject(CkRegister ckRegister) throws IotequException {
        checkAvailable();
        if (ckRegister==null)  ckRegister=new CkRegister();
        else ckRegister.setId(null);
        return ckRegister;
    }
    @Override
    public void changeEmpty2Null(CkRegister ckRegister) {
        if (Objects.nonNull(ckRegister)) {
        }
    }
    @Override
    public void changeNull2Default(CkRegister ckRegister) {
        if (ckRegister.getId()==null) {
            ckRegister.setId("");
        }
        if (ckRegister.getUserNo()==null) {
            ckRegister.setUserNo("");
        }
        if (ckRegister.getName()==null) {
            ckRegister.setName("");
        }
        if (ckRegister.getSex()==null) {
            ckRegister.setSex("1");
        }
        if (ckRegister.getOrgCode()==null) {
            ckRegister.setOrgCode(0);
        }
        if (ckRegister.getOrgName()==null) {
            ckRegister.setOrgName("");
        }
        if (ckRegister.getInDate()==null) {
            ckRegister.setInDate(new Date());
        }
        if (ckRegister.getOnTime()==null) {
            ckRegister.setOnTime(new Date());
        }
    }
}
