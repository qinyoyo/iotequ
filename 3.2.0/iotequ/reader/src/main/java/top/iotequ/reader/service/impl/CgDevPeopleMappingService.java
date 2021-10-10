package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevPeopleMapping;
import top.iotequ.reader.dao.DevPeopleMappingDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevPeopleMappingService"})
@Service(value="devPeopleMappingService")
public class CgDevPeopleMappingService extends CgService<DevPeopleMapping>  {
    private static final Logger log = LoggerFactory.getLogger(CgDevPeopleMappingService.class);
    @Autowired
    private DevPeopleMappingDao devPeopleMappingDao;
    @Override
    public Class<DevPeopleMapping> getEntityClass() {
        return DevPeopleMapping.class;
    }
    @Override
    public IDaoService<DevPeopleMapping> getDaoService() {
        return devPeopleMappingDao ;
    }
    @Override
    public IFlowService<DevPeopleMapping> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevPeopleMapping obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public DevPeopleMapping getDefaultObject(DevPeopleMapping devPeopleMapping) throws IotequException {
        checkAvailable();
        if (devPeopleMapping==null)  devPeopleMapping=new DevPeopleMapping();
        else devPeopleMapping.setId(null);
        return devPeopleMapping;
    }
    @Override
    public void changeEmpty2Null(DevPeopleMapping devPeopleMapping) {
        if (Objects.nonNull(devPeopleMapping)) {
        }
    }
}
