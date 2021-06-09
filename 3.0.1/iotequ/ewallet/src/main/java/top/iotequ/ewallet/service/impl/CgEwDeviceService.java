package top.iotequ.ewallet.service.impl;
import top.iotequ.ewallet.pojo.EwDevice;
import top.iotequ.ewallet.dao.EwDeviceDao;
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
@ConditionalOnMissingClass({"top.iotequ.ewallet.service.impl.EwDeviceService"})
@Service(value="ewDeviceService")
public class CgEwDeviceService extends CgService<EwDevice>  {
    private static final Logger log = LoggerFactory.getLogger(CgEwDeviceService.class);
    @Autowired
    private EwDeviceDao ewDeviceDao;
    @Override
    public Class<EwDevice> getEntityClass() {
        return EwDevice.class;
    }
    @Override
    public IDaoService<EwDevice> getDaoService() {
        return ewDeviceDao ;
    }
    @Override
    public IFlowService<EwDevice> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(EwDevice obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public EwDevice getDefaultObject(EwDevice ewDevice) throws IotequException {
        checkAvailable();
        if (ewDevice==null)  ewDevice=new EwDevice();
        else ewDevice.setId(null);
        return ewDevice;
    }
    @Override
    public void changeEmpty2Null(EwDevice ewDevice) {
        if (Objects.nonNull(ewDevice)) {
        }
    }
}
