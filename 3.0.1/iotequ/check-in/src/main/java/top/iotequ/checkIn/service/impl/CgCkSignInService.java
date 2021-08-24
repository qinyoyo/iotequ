package top.iotequ.checkIn.service.impl;
import top.iotequ.checkIn.pojo.CkSignIn;
import top.iotequ.checkIn.dao.CkSignInDao;
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
@ConditionalOnMissingClass({"top.iotequ.checkIn.service.impl.CkSignInService"})
@Service(value="ckSignInService")
public class CgCkSignInService extends CgService<CkSignIn>  {
    private static final Logger log = LoggerFactory.getLogger(CgCkSignInService.class);
    @Autowired
    private CkSignInDao ckSignInDao;
    @Override
    public Class<CkSignIn> getEntityClass() {
        return CkSignIn.class;
    }
    @Override
    public IDaoService<CkSignIn> getDaoService() {
        return ckSignInDao ;
    }
    @Override
    public IFlowService<CkSignIn> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(CkSignIn obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sex")) map.put("dictSex", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        return map;
    }
    @Override
    public CkSignIn getDefaultObject(CkSignIn ckSignIn) throws IotequException {
        return ckSignIn;
    }
    @Override
    public void changeEmpty2Null(CkSignIn ckSignIn) {
        if (Objects.nonNull(ckSignIn)) {
        }
    }
    @Override
    public void changeNull2Default(CkSignIn ckSignIn) {
        if (ckSignIn.getId()==null) {
            ckSignIn.setId("");
        }
        if (ckSignIn.getUserNo()==null) {
            ckSignIn.setUserNo("");
        }
        if (ckSignIn.getRealName()==null) {
            ckSignIn.setRealName("");
        }
        if (ckSignIn.getOrgCode()==null) {
            ckSignIn.setOrgCode(0);
        }
        if (ckSignIn.getRecSource()==null) {
            ckSignIn.setRecSource("");
        }
        if (ckSignIn.getRecSourceType()==null) {
            ckSignIn.setRecSourceType("U53");
        }
        if (ckSignIn.getEventType()==null) {
            ckSignIn.setEventType(1);
        }
        if (ckSignIn.getRecTime()==null) {
            ckSignIn.setRecTime(new Date());
        }
    }
}
