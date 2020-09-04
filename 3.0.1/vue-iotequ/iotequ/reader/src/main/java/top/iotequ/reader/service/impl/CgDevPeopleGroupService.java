package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevPeopleGroup;
import top.iotequ.reader.dao.DevPeopleGroupDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevPeopleGroupService"})
@Service(value="devPeopleGroupService")
public class CgDevPeopleGroupService extends CgService<DevPeopleGroup>  {
private static final Logger log = LoggerFactory.getLogger(CgDevPeopleGroupService.class);
    @Autowired
    private DevPeopleGroupDao devPeopleGroupDao;
    @Override
    public Class<DevPeopleGroup> getEntityClass() {
        return DevPeopleGroup.class;
    }
    @Override
    public IDaoService<DevPeopleGroup> getDaoService() {
        return devPeopleGroupDao ;
    }
    @Override
    public IFlowService<DevPeopleGroup> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevPeopleGroup obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public DevPeopleGroup getDefaultObject(DevPeopleGroup devPeopleGroup) throws IotequException {
        checkAvailable();
        if (devPeopleGroup==null)  devPeopleGroup=new DevPeopleGroup();
        else devPeopleGroup.setId(null);
        return devPeopleGroup;
    }
    @Override
    public void changeEmpty2Null(DevPeopleGroup devPeopleGroup) {
        if (Objects.nonNull(devPeopleGroup)) {
        }
    }
}
