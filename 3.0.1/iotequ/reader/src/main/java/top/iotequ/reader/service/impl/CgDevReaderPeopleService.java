package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevReaderPeople;
import top.iotequ.reader.dao.DevReaderPeopleDao;
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
import top.iotequ.util.*;
import top.iotequ.util.StringUtil;

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevReaderPeopleService"})
@Service(value="devReaderPeopleService")
public class CgDevReaderPeopleService extends CgService<DevReaderPeople>  {
private static final Logger log = LoggerFactory.getLogger(CgDevReaderPeopleService.class);
    @Autowired
    private DevReaderPeopleDao devReaderPeopleDao;
    @Override
    public Class<DevReaderPeople> getEntityClass() {
        return DevReaderPeople.class;
    }
    @Override
    public IDaoService<DevReaderPeople> getDaoService() {
        return devReaderPeopleDao ;
    }
    @Override
    public IFlowService<DevReaderPeople> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevReaderPeople obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"idType")) map.put("dictIdType", DictionaryUtil.getDictListFromDatabase(obj,"sys_id_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"registerType")) map.put("dictRegisterType", DictionaryUtil.getDictListFromDatabase(obj,"dev_register_type",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sex")) map.put("dictSex", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        return map;
    }
    @Override
    public DevReaderPeople getDefaultObject(DevReaderPeople devReaderPeople) throws IotequException {
        checkAvailable();
        if (devReaderPeople==null)  devReaderPeople=new DevReaderPeople();
        else devReaderPeople.setId(null);
        return devReaderPeople;
    }
    @Override
    public void changeEmpty2Null(DevReaderPeople devReaderPeople) {
        if (Objects.nonNull(devReaderPeople)) {
        }
    }
    @Override
    public void changeNull2Default(DevReaderPeople devReaderPeople) {
        if (devReaderPeople.getType()==null) {
            devReaderPeople.setType(0);
        }
        if (devReaderPeople.getUserNo()==null) {
            devReaderPeople.setUserNo("");
        }
        if (devReaderPeople.getBirthDate()==null) {
            devReaderPeople.setBirthDate(new Date());
        }
        if (devReaderPeople.getId()==null) {
            devReaderPeople.setId(0);
        }
    }
    @Override
    public void changeFileField( DevReaderPeople devReaderPeople) {
        if (devReaderPeople.getId()!=null)
            devReaderPeople.setPhoto(UploadDownUtil.removeFilesExclusive(getEntityClass(),"photo",devReaderPeople.getId().toString(),devReaderPeople.getPhoto(),true));
        else
            devReaderPeople.setPhoto(null);
    }
}
