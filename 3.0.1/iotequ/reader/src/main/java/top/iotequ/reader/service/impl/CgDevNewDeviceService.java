package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevNewDevice;
import top.iotequ.reader.dao.DevNewDeviceDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevNewDeviceService"})
@Service(value="devNewDeviceService")
public class CgDevNewDeviceService extends CgService<DevNewDevice>  {
    private static final Logger log = LoggerFactory.getLogger(CgDevNewDeviceService.class);
    @Autowired
    private DevNewDeviceDao devNewDeviceDao;
    @Override
    public Class<DevNewDevice> getEntityClass() {
        return DevNewDevice.class;
    }
    @Override
    public IDaoService<DevNewDevice> getDaoService() {
        return devNewDeviceDao ;
    }
    @Override
    public IFlowService<DevNewDevice> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevNewDevice obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public DevNewDevice getDefaultObject(DevNewDevice devNewDevice) throws IotequException {
        return devNewDevice;
    }
    @Override
    public void changeEmpty2Null(DevNewDevice devNewDevice) {
        if (Objects.nonNull(devNewDevice)) {
        }
    }
}
