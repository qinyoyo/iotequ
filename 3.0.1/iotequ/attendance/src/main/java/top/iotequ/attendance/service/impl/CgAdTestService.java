package top.iotequ.attendance.service.impl;
import top.iotequ.attendance.pojo.AdTest;
import top.iotequ.attendance.dao.AdTestDao;
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
@ConditionalOnMissingClass({"top.iotequ.attendance.service.impl.AdTestService"})
@Service(value="adTestService")
public class CgAdTestService extends CgService<AdTest>  {
private static final Logger log = LoggerFactory.getLogger(CgAdTestService.class);
    @Autowired
    private AdTestDao adTestDao;
    @Override
    public Class<AdTest> getEntityClass() {
        return AdTest.class;
    }
    @Override
    public IDaoService<AdTest> getDaoService() {
        return adTestDao ;
    }
    @Override
    public IFlowService<AdTest> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdTest obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public AdTest getDefaultObject(AdTest adTest) throws IotequException {
        checkAvailable();
        if (adTest==null)  adTest=new AdTest();
        else adTest.setId(null);
        return adTest;
    }
    @Override
    public void changeEmpty2Null(AdTest adTest) {
        if (Objects.nonNull(adTest)) {
        }
    }
}
