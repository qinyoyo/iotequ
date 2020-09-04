package top.iotequ.ewallet.service.impl;
import top.iotequ.ewallet.pojo.EwUserCount;
import top.iotequ.ewallet.dao.EwUserCountDao;
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
@ConditionalOnMissingClass({"top.iotequ.ewallet.service.impl.EwUserCountService"})
@Service(value="ewUserCountService")
public class CgEwUserCountService extends CgService<EwUserCount>  {
private static final Logger log = LoggerFactory.getLogger(CgEwUserCountService.class);
    @Autowired
    private EwUserCountDao ewUserCountDao;
    @Override
    public Class<EwUserCount> getEntityClass() {
        return EwUserCount.class;
    }
    @Override
    public IDaoService<EwUserCount> getDaoService() {
        return ewUserCountDao ;
    }
    @Override
    public IFlowService<EwUserCount> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(EwUserCount obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public EwUserCount getDefaultObject(EwUserCount ewUserCount) throws IotequException {
        return ewUserCount;
    }
    @Override
    public void changeEmpty2Null(EwUserCount ewUserCount) {
        if (Objects.nonNull(ewUserCount)) {
        }
    }
    @Override
    public void changeNull2Default(EwUserCount ewUserCount) {
        if (ewUserCount.getId()==null) {
            ewUserCount.setId(0);
        }
        if (ewUserCount.getUserNo()==null) {
            ewUserCount.setUserNo("");
        }
        if (ewUserCount.getUserName()==null) {
            ewUserCount.setUserName("");
        }
        if (ewUserCount.getCountId()==null) {
            ewUserCount.setCountId(0);
        }
        if (ewUserCount.getProjectName()==null) {
            ewUserCount.setProjectName("");
        }
        if (ewUserCount.getAmountCount()==null) {
            ewUserCount.setAmountCount(0);
        }
        if (ewUserCount.getCheckCode()==null) {
            ewUserCount.setCheckCode("");
        }
        if (ewUserCount.getCostLimit()==null) {
            ewUserCount.setCostLimit(0);
        }
        if (ewUserCount.getDayLimit()==null) {
            ewUserCount.setDayLimit(0);
        }
    }
}
