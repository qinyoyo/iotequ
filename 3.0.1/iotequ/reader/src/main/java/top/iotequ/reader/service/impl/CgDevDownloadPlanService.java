package top.iotequ.reader.service.impl;
import top.iotequ.reader.pojo.DevDownloadPlan;
import top.iotequ.reader.dao.DevDownloadPlanDao;
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
@ConditionalOnMissingClass({"top.iotequ.reader.service.impl.DevDownloadPlanService"})
@Service(value="devDownloadPlanService")
public class CgDevDownloadPlanService extends CgService<DevDownloadPlan>  {
    private static final Logger log = LoggerFactory.getLogger(CgDevDownloadPlanService.class);
    @Autowired
    private DevDownloadPlanDao devDownloadPlanDao;
    @Override
    public Class<DevDownloadPlan> getEntityClass() {
        return DevDownloadPlan.class;
    }
    @Override
    public IDaoService<DevDownloadPlan> getDaoService() {
        return devDownloadPlanDao ;
    }
    @Override
    public IFlowService<DevDownloadPlan> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(DevDownloadPlan obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public DevDownloadPlan getDefaultObject(DevDownloadPlan devDownloadPlan) throws IotequException {
        checkAvailable();
        if (devDownloadPlan==null)  devDownloadPlan=new DevDownloadPlan();
        else devDownloadPlan.setId(null);
        return devDownloadPlan;
    }
    @Override
    public void changeEmpty2Null(DevDownloadPlan devDownloadPlan) {
        if (Objects.nonNull(devDownloadPlan)) {
        }
    }
    @Override
    public void changeNull2Default(DevDownloadPlan devDownloadPlan) {
        if (devDownloadPlan.getId()==null) {
            devDownloadPlan.setId(0);
        }
        if (devDownloadPlan.getUserNo()==null) {
            devDownloadPlan.setUserNo("");
        }
        if (devDownloadPlan.getReaderNo()==null) {
            devDownloadPlan.setReaderNo("");
        }
        if (devDownloadPlan.getType()==null) {
            devDownloadPlan.setType(0);
        }
        if (devDownloadPlan.getDownloadNum()==null) {
            devDownloadPlan.setDownloadNum(3);
        }
        if (devDownloadPlan.getTime()==null) {
            devDownloadPlan.setTime(new Date());
        }
    }
}
