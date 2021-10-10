package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevAuthGroup;
import top.iotequ.reader.dao.DevAuthGroupDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevAuthGroupService"})
@Service(value="devAuthGroupService")
public class CgDevAuthGroupService extends CgService<DevAuthGroup>  {
    private static final Logger log = LoggerFactory.getLogger(CgDevAuthGroupService.class);
    @Autowired
    private DevAuthGroupDao devAuthGroupDao;
    @Override
    public Class<DevAuthGroup> getEntityClass() {
        return DevAuthGroup.class;
    }
    @Override
    public IDaoService<DevAuthGroup> getDaoService() {
        return devAuthGroupDao ;
    }
    @Override
    public IFlowService<DevAuthGroup> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevAuthGroup obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"auth")) map.put("dictAuth", DictionaryUtil.getDictListFromDatabase(obj,"select * from dev_auth_role","id","name",null,false,null));
        return map;
    }
    @Override
    public DevAuthGroup getDefaultObject(DevAuthGroup devAuthGroup) throws IotequException {
        checkAvailable();
        if (devAuthGroup==null)  devAuthGroup=new DevAuthGroup();
        else devAuthGroup.setId(null);
        return devAuthGroup;
    }
    @Override
    public void changeEmpty2Null(DevAuthGroup devAuthGroup) {
        if (Objects.nonNull(devAuthGroup)) {
        }
    }
}
