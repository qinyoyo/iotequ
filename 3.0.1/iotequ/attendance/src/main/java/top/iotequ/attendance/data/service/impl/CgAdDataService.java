package top.iotequ.attendance.data.service.impl;
import top.iotequ.attendance.data.pojo.AdData;
import top.iotequ.attendance.data.dao.AdDataDao;
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
import top.iotequ.attendance.util.AdUtil;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.attendance.data.service.impl.AdDataService"})
@Service(value="adDataService")
public class CgAdDataService extends CgService<AdData>  {
private static final Logger log = LoggerFactory.getLogger(CgAdDataService.class);
    @Autowired
    private AdDataDao adDataDao;
    public static String [] dictRecTypeValue= {String.valueOf(AdUtil.workOn),String.valueOf(AdUtil.workOff),String.valueOf(AdUtil.workUnknown)};
    public static String [] dictRecTypeText= {"adData.field.recType_0","adData.field.recType_1","adData.field.recType_2"};
    @Override
    public Class<AdData> getEntityClass() {
        return AdData.class;
    }
    @Override
    public IDaoService<AdData> getDaoService() {
        return adDataDao ;
    }
    @Override
    public IFlowService<AdData> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdData obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"recType")) map.put("dictRecType", DictionaryUtil.getDictList(dictRecTypeValue,dictRecTypeText));
        return map;
    }
    @Override
    public AdData getDefaultObject(AdData adData) throws IotequException {
        return adData;
    }
    @Override
    public void changeEmpty2Null(AdData adData) {
        if (Objects.nonNull(adData)) {
        }
    }
    @Override
    public void changeNull2Default(AdData adData) {
        if (adData.getId()==null) {
            adData.setId("");
        }
        if (adData.getEmployeeNo()==null) {
            adData.setEmployeeNo("");
        }
        if (adData.getRealName()==null) {
            adData.setRealName("");
        }
        if (adData.getRecType()==null) {
            adData.setRecType(3);
        }
        if (adData.getRecTime()==null) {
            adData.setRecTime(new Date());
        }
        if (adData.getIsUsed()==null) {
            adData.setIsUsed(Util.boolValue("0"));
        }
    }
}
