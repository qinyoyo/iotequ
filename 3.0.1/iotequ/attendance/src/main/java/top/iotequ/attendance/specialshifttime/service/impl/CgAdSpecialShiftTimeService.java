package top.iotequ.attendance.specialshifttime.service.impl;
import top.iotequ.attendance.specialshifttime.pojo.AdSpecialShiftTime;
import top.iotequ.attendance.specialshifttime.dao.AdSpecialShiftTimeDao;
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
@ConditionalOnMissingClass({"top.iotequ.attendance.specialshifttime.service.impl.AdSpecialShiftTimeService"})
@Service(value="adSpecialShiftTimeService")
public class CgAdSpecialShiftTimeService extends CgService<AdSpecialShiftTime>  {
private static final Logger log = LoggerFactory.getLogger(CgAdSpecialShiftTimeService.class);
    @Autowired
    private AdSpecialShiftTimeDao adSpecialShiftTimeDao;
    @Override
    public Class<AdSpecialShiftTime> getEntityClass() {
        return AdSpecialShiftTime.class;
    }
    @Override
    public IDaoService<AdSpecialShiftTime> getDaoService() {
        return adSpecialShiftTimeDao ;
    }
    @Override
    public IFlowService<AdSpecialShiftTime> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdSpecialShiftTime obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"name")) map.put("dictName", DictionaryUtil.getDictListFromDatabase(obj,"ad_shift_name",null,null,null,false,null));
        return map;
    }
    @Override
    public AdSpecialShiftTime getDefaultObject(AdSpecialShiftTime adSpecialShiftTime) throws IotequException {
        checkAvailable();
        if (adSpecialShiftTime==null)  adSpecialShiftTime=new AdSpecialShiftTime();
        else adSpecialShiftTime.setId(null);
        return adSpecialShiftTime;
    }
    @Override
    public void changeEmpty2Null(AdSpecialShiftTime adSpecialShiftTime) {
        if (Objects.nonNull(adSpecialShiftTime)) {
        }
    }
}
