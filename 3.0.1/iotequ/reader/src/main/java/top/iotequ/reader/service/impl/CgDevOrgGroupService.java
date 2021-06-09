package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevOrgGroup;
import top.iotequ.reader.dao.DevOrgGroupDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevOrgGroupService"})
@Service(value="devOrgGroupService")
public class CgDevOrgGroupService extends CgService<DevOrgGroup>  {
    private static final Logger log = LoggerFactory.getLogger(CgDevOrgGroupService.class);
    @Autowired
    private DevOrgGroupDao devOrgGroupDao;
    @Override
    public Class<DevOrgGroup> getEntityClass() {
        return DevOrgGroup.class;
    }
    @Override
    public IDaoService<DevOrgGroup> getDaoService() {
        return devOrgGroupDao ;
    }
    @Override
    public IFlowService<DevOrgGroup> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevOrgGroup obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public DevOrgGroup getDefaultObject(DevOrgGroup devOrgGroup) throws IotequException {
        checkAvailable();
        if (devOrgGroup==null)  devOrgGroup=new DevOrgGroup();
        else devOrgGroup.setId(null);
        return devOrgGroup;
    }
    @Override
    public void changeEmpty2Null(DevOrgGroup devOrgGroup) {
        if (Objects.nonNull(devOrgGroup)) {
        }
    }
    @Override
    public void changeNull2Default(DevOrgGroup devOrgGroup) {
        if (devOrgGroup.getId()==null) {
            devOrgGroup.setId(0);
        }
        if (devOrgGroup.getGroupId()==null) {
            devOrgGroup.setGroupId(0);
        }
        if (devOrgGroup.getOrgId()==null) {
            devOrgGroup.setOrgId(0);
        }
        if (devOrgGroup.getOrgName()==null) {
            devOrgGroup.setOrgName("");
        }
        if (devOrgGroup.getIsIncludeSubOrg()==null) {
            devOrgGroup.setIsIncludeSubOrg(Util.boolValue("1"));
        }
    }
}
