package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevReaderGroup;
import top.iotequ.reader.dao.DevReaderGroupDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevReaderGroupService"})
@Service(value="devReaderGroupService")
public class CgDevReaderGroupService extends CgService<DevReaderGroup>  {
private static final Logger log = LoggerFactory.getLogger(CgDevReaderGroupService.class);
    @Autowired
    private DevReaderGroupDao devReaderGroupDao;
    @Override
    public Class<DevReaderGroup> getEntityClass() {
        return DevReaderGroup.class;
    }
    @Override
    public IDaoService<DevReaderGroup> getDaoService() {
        return devReaderGroupDao ;
    }
    @Override
    public IFlowService<DevReaderGroup> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevReaderGroup obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"parent")) {
            if (useTree) map.put("dictParent", DictionaryUtil.getTreeViewData(obj,"dev_reader_group","id","name","parent","id",null,null,null));
            else map.put("dictParent", DictionaryUtil.getDictListFromDatabase(obj,"dev_reader_group","id","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgAuth")) map.put("dictOrgAuth", DictionaryUtil.getDictListFromDatabase(obj,"select * from dev_auth_role","id","name",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"subOrgAuth")) map.put("dictSubOrgAuth", DictionaryUtil.getDictListFromDatabase(obj,"select * from dev_auth_role","id","name",null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"authGroupList")) map.put("dictAuthGroupList", DictionaryUtil.getDictListFromDatabase(obj,"dev_auth_group","id","name",null,false,null));
        return map;
    }
    @Override
    public DevReaderGroup getDefaultObject(DevReaderGroup devReaderGroup) throws IotequException {
        checkAvailable();
        if (devReaderGroup==null)  devReaderGroup=new DevReaderGroup();
        else devReaderGroup.setId(null);
        return devReaderGroup;
    }
    @Override
    public void changeEmpty2Null(DevReaderGroup devReaderGroup) {
        if (Objects.nonNull(devReaderGroup)) {
        }
    }
}
