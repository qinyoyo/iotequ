package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevAuthConfig;
import top.iotequ.reader.dao.DevAuthConfigDao;
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
import top.iotequ.util.*;
import top.iotequ.util.StringUtil;
import top.iotequ.util.Util;

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevAuthConfigService"})
@Service(value="devAuthConfigService")
public class CgDevAuthConfigService extends CgService<DevAuthConfig>  {
private static final Logger log = LoggerFactory.getLogger(CgDevAuthConfigService.class);
    @Autowired
    private DevAuthConfigDao devAuthConfigDao;
    @Override
    public Class<DevAuthConfig> getEntityClass() {
        return DevAuthConfig.class;
    }
    @Override
    public IDaoService<DevAuthConfig> getDaoService() {
        return devAuthConfigDao ;
    }
    @Override
    public IFlowService<DevAuthConfig> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevAuthConfig obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"auth")) map.put("dictAuth", DictionaryUtil.getDictListFromDatabase(obj,"dev_permit_mode",null,null,null,false,null));
        return map;
    }
    @Override
    public DevAuthConfig getDefaultObject(DevAuthConfig devAuthConfig) throws IotequException {
        checkAvailable();
        if (devAuthConfig==null)  devAuthConfig=new DevAuthConfig();
        else devAuthConfig.setId(null);
        return devAuthConfig;
    }
    @Override
    public void changeEmpty2Null(DevAuthConfig devAuthConfig) {
        if (Objects.nonNull(devAuthConfig)) {
        }
    }
    @Override
    public void changeNull2Default(DevAuthConfig devAuthConfig) {
        if (devAuthConfig.getId()==null) {
            devAuthConfig.setId(0);
        }
        if (devAuthConfig.getRoleId()==null) {
            devAuthConfig.setRoleId(0);
        }
        if (devAuthConfig.getOnlyWorkDay()==null) {
            devAuthConfig.setOnlyWorkDay(Util.boolValue("0"));
        }
        if (devAuthConfig.getAuth()==null) {
            devAuthConfig.setAuth(4);
        }
    }
}
