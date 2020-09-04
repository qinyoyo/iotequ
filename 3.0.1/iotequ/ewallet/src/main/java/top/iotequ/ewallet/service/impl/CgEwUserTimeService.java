package top.iotequ.ewallet.service.impl;
import top.iotequ.ewallet.pojo.EwUserTime;
import top.iotequ.ewallet.dao.EwUserTimeDao;
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
@ConditionalOnMissingClass({"top.iotequ.ewallet.service.impl.EwUserTimeService"})
@Service(value="ewUserTimeService")
public class CgEwUserTimeService extends CgService<EwUserTime>  {
private static final Logger log = LoggerFactory.getLogger(CgEwUserTimeService.class);
    @Autowired
    private EwUserTimeDao ewUserTimeDao;
    @Override
    public Class<EwUserTime> getEntityClass() {
        return EwUserTime.class;
    }
    @Override
    public IDaoService<EwUserTime> getDaoService() {
        return ewUserTimeDao ;
    }
    @Override
    public IFlowService<EwUserTime> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(EwUserTime obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        return map;
    }
    @Override
    public EwUserTime getDefaultObject(EwUserTime ewUserTime) throws IotequException {
        return ewUserTime;
    }
    @Override
    public void changeEmpty2Null(EwUserTime ewUserTime) {
        if (Objects.nonNull(ewUserTime)) {
        }
    }
    @Override
    public void changeNull2Default(EwUserTime ewUserTime) {
        if (ewUserTime.getId()==null) {
            ewUserTime.setId(0);
        }
        if (ewUserTime.getUserNo()==null) {
            ewUserTime.setUserNo("");
        }
        if (ewUserTime.getUserName()==null) {
            ewUserTime.setUserName("");
        }
        if (ewUserTime.getTimeId()==null) {
            ewUserTime.setTimeId(0);
        }
        if (ewUserTime.getProjectName()==null) {
            ewUserTime.setProjectName("");
        }
        if (ewUserTime.getAmountTime()==null) {
            ewUserTime.setAmountTime(0);
        }
        if (ewUserTime.getCheckCode()==null) {
            ewUserTime.setCheckCode("");
        }
        if (ewUserTime.getCostLimit()==null) {
            ewUserTime.setCostLimit(0);
        }
        if (ewUserTime.getDayLimit()==null) {
            ewUserTime.setDayLimit(0);
        }
    }
}
