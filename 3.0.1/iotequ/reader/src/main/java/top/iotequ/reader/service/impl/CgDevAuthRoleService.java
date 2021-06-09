package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevAuthRole;
import top.iotequ.reader.dao.DevAuthRoleDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevAuthRoleService"})
@Service(value="devAuthRoleService")
public class CgDevAuthRoleService extends CgService<DevAuthRole>  {
    private static final Logger log = LoggerFactory.getLogger(CgDevAuthRoleService.class);
    @Autowired
    private DevAuthRoleDao devAuthRoleDao;
    @Override
    public Class<DevAuthRole> getEntityClass() {
        return DevAuthRole.class;
    }
    @Override
    public IDaoService<DevAuthRole> getDaoService() {
        return devAuthRoleDao ;
    }
    @Override
    public IFlowService<DevAuthRole> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevAuthRole obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public DevAuthRole getDefaultObject(DevAuthRole devAuthRole) throws IotequException {
        checkAvailable();
        if (devAuthRole==null)  devAuthRole=new DevAuthRole();
        else devAuthRole.setId(null);
        return devAuthRole;
    }
    @Override
    public void changeEmpty2Null(DevAuthRole devAuthRole) {
        if (Objects.nonNull(devAuthRole)) {
        }
    }
}
