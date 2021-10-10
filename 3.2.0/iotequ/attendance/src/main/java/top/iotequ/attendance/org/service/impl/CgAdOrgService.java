package top.iotequ.attendance.org.service.impl;
import top.iotequ.attendance.org.pojo.AdOrg;
import top.iotequ.attendance.org.dao.AdOrgDao;
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
import top.iotequ.attendance.util.AdUtil;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.attendance.org.service.impl.AdOrgService"})
@Service(value="adOrgService")
public class CgAdOrgService extends CgService<AdOrg>  {
    private static final Logger log = LoggerFactory.getLogger(CgAdOrgService.class);
    @Autowired
    private AdOrgDao adOrgDao;
    @Override
    public Class<AdOrg> getEntityClass() {
        return AdOrg.class;
    }
    @Override
    public IDaoService<AdOrg> getDaoService() {
        return adOrgDao ;
    }
    @Override
    public IFlowService<AdOrg> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdOrg obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"parent")) {
            if (useTree) map.put("dictParent", DictionaryUtil.getTreeViewData(obj,"sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictParent", DictionaryUtil.getDictListFromDatabase(obj,"sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"shiftId")) map.put("dictShiftId", DictionaryUtil.getDictListFromDatabase(obj,"ad_shift","id","name",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"managerOrgCode")) {
            if (useTree) map.put("dictManagerOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictManagerOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public AdOrg getDefaultObject(AdOrg adOrg) throws IotequException {
        return adOrg;
    }
    @Override
    public void changeEmpty2Null(AdOrg adOrg) {
        if (Objects.nonNull(adOrg)) {
        }
    }
}
