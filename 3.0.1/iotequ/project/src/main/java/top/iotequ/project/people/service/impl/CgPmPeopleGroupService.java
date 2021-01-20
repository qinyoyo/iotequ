package top.iotequ.project.people.service.impl;
import top.iotequ.project.people.pojo.PmPeopleGroup;
import top.iotequ.project.people.dao.PmPeopleGroupDao;
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
@ConditionalOnMissingClass({"top.iotequ.project.people.service.impl.PmPeopleGroupService"})
@Service(value="pmPeopleGroupService")
public class CgPmPeopleGroupService extends CgService<PmPeopleGroup>  {
private static final Logger log = LoggerFactory.getLogger(CgPmPeopleGroupService.class);
    @Autowired
    private PmPeopleGroupDao pmPeopleGroupDao;
    @Override
    public Class<PmPeopleGroup> getEntityClass() {
        return PmPeopleGroup.class;
    }
    @Override
    public IDaoService<PmPeopleGroup> getDaoService() {
        return pmPeopleGroupDao ;
    }
    @Override
    public IFlowService<PmPeopleGroup> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(PmPeopleGroup obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"parent")) {
            if (useTree) map.put("dictParent", DictionaryUtil.getTreeViewData(obj,"pm_people_group","id","name","parent","id",null,null,null));
            else map.put("dictParent", DictionaryUtil.getDictListFromDatabase(obj,"pm_people_group","id","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"groupType")) map.put("dictGroupType", DictionaryUtil.getDictListFromDatabase(obj,"pm_group_type",null,null,null,false,null));
        return map;
    }
    @Override
    public PmPeopleGroup getDefaultObject(PmPeopleGroup pmPeopleGroup) throws IotequException {
        checkAvailable();
        if (pmPeopleGroup==null)  pmPeopleGroup=new PmPeopleGroup();
        else pmPeopleGroup.setId(null);
        return pmPeopleGroup;
    }
    @Override
    public void changeEmpty2Null(PmPeopleGroup pmPeopleGroup) {
        if (Objects.nonNull(pmPeopleGroup)) {
        }
    }
    @Override
    public void changeNull2Default(PmPeopleGroup pmPeopleGroup) {
        if (pmPeopleGroup.getId()==null) {
            pmPeopleGroup.setId(0);
        }
        if (pmPeopleGroup.getName()==null) {
            pmPeopleGroup.setName("");
        }
        if (pmPeopleGroup.getGroupType()==null) {
            pmPeopleGroup.setGroupType("");
        }
        if (pmPeopleGroup.getEnabled()==null) {
            pmPeopleGroup.setEnabled(Util.boolValue("1"));
        }
    }
}
