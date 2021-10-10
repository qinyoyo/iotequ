package top.iotequ.project.people.service.impl;
import top.iotequ.project.people.pojo.PmPeople;
import top.iotequ.project.people.dao.PmPeopleDao;
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
@ConditionalOnMissingClass({"top.iotequ.project.people.service.impl.PmPeopleService"})
@Service(value="pmPeopleService")
public class CgPmPeopleService extends CgService<PmPeople>  {
    private static final Logger log = LoggerFactory.getLogger(CgPmPeopleService.class);
    @Autowired
    private PmPeopleDao pmPeopleDao;
    @Override
    public Class<PmPeople> getEntityClass() {
        return PmPeople.class;
    }
    @Override
    public IDaoService<PmPeople> getDaoService() {
        return pmPeopleDao ;
    }
    @Override
    public IFlowService<PmPeople> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(PmPeople obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sex")) map.put("dictSex", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public PmPeople getDefaultObject(PmPeople pmPeople) throws IotequException {
        checkAvailable();
        if (pmPeople==null)  pmPeople=new PmPeople();
        else pmPeople.setId(null);
        return pmPeople;
    }
    @Override
    public void changeEmpty2Null(PmPeople pmPeople) {
        if (Objects.nonNull(pmPeople)) {
        }
    }
}
