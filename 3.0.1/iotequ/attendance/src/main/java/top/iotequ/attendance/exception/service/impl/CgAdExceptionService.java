package top.iotequ.attendance.exception.service.impl;
import top.iotequ.attendance.exception.pojo.AdException;
import top.iotequ.attendance.exception.dao.AdExceptionDao;
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
@ConditionalOnMissingClass({"top.iotequ.attendance.exception.service.impl.AdExceptionService"})
@Service(value="adExceptionService")
public class CgAdExceptionService extends CgService<AdException>  {
private static final Logger log = LoggerFactory.getLogger(CgAdExceptionService.class);
    @Autowired
    private AdExceptionDao adExceptionDao;
    @Override
    public Class<AdException> getEntityClass() {
        return AdException.class;
    }
    @Override
    public IDaoService<AdException> getDaoService() {
        return adExceptionDao ;
    }
    @Override
    public IFlowService<AdException> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdException obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"weekDay")) map.put("dictWeekDay", DictionaryUtil.getDictListFromDatabase(obj,"sys_week",null,null,null,false,null));
        return map;
    }
    @Override
    public AdException getDefaultObject(AdException adException) throws IotequException {
        checkAvailable();
        if (adException==null)  adException=new AdException();
        else adException.setId(null);
        return adException;
    }
    @Override
    public void changeEmpty2Null(AdException adException) {
        if (Objects.nonNull(adException)) {
        }
    }
}
