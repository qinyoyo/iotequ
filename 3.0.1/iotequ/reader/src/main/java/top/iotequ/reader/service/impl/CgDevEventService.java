package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevEvent;
import top.iotequ.reader.dao.DevEventDao;
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

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevEventService"})
@Service(value="devEventService")
public class CgDevEventService extends CgService<DevEvent>  {
private static final Logger log = LoggerFactory.getLogger(CgDevEventService.class);
    @Autowired
    private DevEventDao devEventDao;
    @Override
    public Class<DevEvent> getEntityClass() {
        return DevEvent.class;
    }
    @Override
    public IDaoService<DevEvent> getDaoService() {
        return devEventDao ;
    }
    @Override
    public IFlowService<DevEvent> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevEvent obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"status")) map.put("dictStatus", DictionaryUtil.getDictListFromDatabase(obj,"dev_access_state",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"auditorOrg")) {
            if (useTree) map.put("dictAuditorOrg", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictAuditorOrg", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public DevEvent getDefaultObject(DevEvent devEvent) throws IotequException {
        return devEvent;
    }
    @Override
    public void changeEmpty2Null(DevEvent devEvent) {
        if (Objects.nonNull(devEvent)) {
        }
    }
    @Override
    public void changeNull2Default(DevEvent devEvent) {
        if (devEvent.getId()==null) {
            devEvent.setId("");
        }
        if (devEvent.getDevType()==null) {
            devEvent.setDevType("D10");
        }
        if (devEvent.getDevNo()==null) {
            devEvent.setDevNo("");
        }
        if (devEvent.getTime()==null) {
            devEvent.setTime(new Date());
        }
    }
}
