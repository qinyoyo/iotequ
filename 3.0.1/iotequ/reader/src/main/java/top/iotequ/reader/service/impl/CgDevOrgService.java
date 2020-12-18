package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevOrg;
import top.iotequ.reader.dao.DevOrgDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevOrgService"})
@Service(value="devOrgService")
public class CgDevOrgService extends CgService<DevOrg>  {
private static final Logger log = LoggerFactory.getLogger(CgDevOrgService.class);
    @Autowired
    private DevOrgDao devOrgDao;
    @Override
    public Class<DevOrg> getEntityClass() {
        return DevOrg.class;
    }
    @Override
    public IDaoService<DevOrg> getDaoService() {
        return devOrgDao ;
    }
    @Override
    public IFlowService<DevOrg> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevOrg obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public DevOrg getDefaultObject(DevOrg devOrg) throws IotequException {
        return devOrg;
    }
    @Override
    public void changeEmpty2Null(DevOrg devOrg) {
        if (Objects.nonNull(devOrg)) {
        }
    }
}
