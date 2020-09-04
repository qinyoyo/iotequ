package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevPeople;
import top.iotequ.reader.dao.DevPeopleDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevPeopleService"})
@Service(value="devPeopleService")
public class CgDevPeopleService extends CgService<DevPeople>  {
private static final Logger log = LoggerFactory.getLogger(CgDevPeopleService.class);
    @Autowired
    private DevPeopleDao devPeopleDao;
    @Override
    public Class<DevPeople> getEntityClass() {
        return DevPeople.class;
    }
    @Override
    public IDaoService<DevPeople> getDaoService() {
        return devPeopleDao ;
    }
    @Override
    public IFlowService<DevPeople> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevPeople obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sex")) map.put("dictSex", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"idType")) map.put("dictIdType", DictionaryUtil.getDictListFromDatabase(obj,"sys_id_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"registerType")) map.put("dictRegisterType", DictionaryUtil.getDictListFromDatabase(obj,"dev_register_type",null,null,null,false,null));
        return map;
    }
    @Override
    public DevPeople getDefaultObject(DevPeople devPeople) throws IotequException {
        checkAvailable();
        if (devPeople==null)  devPeople=new DevPeople();
        else devPeople.setUserNo(null);
        return devPeople;
    }
    @Override
    public void changeEmpty2Null(DevPeople devPeople) {
        if (Objects.nonNull(devPeople)) {
            if ("".equals(devPeople.getCardNo())) devPeople.setCardNo(null);
            if ("".equals(devPeople.getMobilePhone())) devPeople.setMobilePhone(null);
            if ("".equals(devPeople.getEmail())) devPeople.setEmail(null);
        }
    }
    @Override
    public void changeNull2Default(DevPeople devPeople) {
        if (devPeople.getUserNo()==null) {
            devPeople.setUserNo("");
        }
        if (devPeople.getRealName()==null) {
            devPeople.setRealName("");
        }
        if (devPeople.getOrgCode()==null) {
            devPeople.setOrgCode(0);
        }
        if (devPeople.getIdType()==null) {
            devPeople.setIdType(1);
        }
        if (devPeople.getIdNumber()==null) {
            devPeople.setIdNumber("");
        }
        if (devPeople.getUserType()==null) {
            devPeople.setUserType(2);
        }
        if (devPeople.getRegisterType()==null) {
            devPeople.setRegisterType(1);
        }
    }
    @Override
    public void changeFileField( DevPeople devPeople) {
        if (devPeople.getUserNo()!=null)
            devPeople.setPhoto(UploadDownUtil.removeFilesExclusive(getEntityClass(),"photo",devPeople.getUserNo().toString(),devPeople.getPhoto(),true));
        else
            devPeople.setPhoto(null);
    }
}
