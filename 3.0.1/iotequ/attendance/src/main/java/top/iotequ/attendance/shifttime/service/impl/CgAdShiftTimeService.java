package top.iotequ.attendance.shifttime.service.impl;
import top.iotequ.attendance.shifttime.pojo.AdShiftTime;
import top.iotequ.attendance.shifttime.dao.AdShiftTimeDao;
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
import top.iotequ.util.*;
import top.iotequ.util.StringUtil;

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.attendance.shifttime.service.impl.AdShiftTimeService"})
@Service(value="adShiftTimeService")
public class CgAdShiftTimeService extends CgService<AdShiftTime>  {
private static final Logger log = LoggerFactory.getLogger(CgAdShiftTimeService.class);
    @Autowired
    private AdShiftTimeDao adShiftTimeDao;
    @Override
    public Class<AdShiftTime> getEntityClass() {
        return AdShiftTime.class;
    }
    @Override
    public IDaoService<AdShiftTime> getDaoService() {
        return adShiftTimeDao ;
    }
    @Override
    public IFlowService<AdShiftTime> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdShiftTime obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"name")) map.put("dictName", DictionaryUtil.getDictListFromDatabase(obj,"ad_shift_name",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"weekDays")) map.put("dictWeekDays", DictionaryUtil.getDictListFromDatabase(obj,"sys_week",null,null,null,false,null));
        return map;
    }
    @Override
    public AdShiftTime getDefaultObject(AdShiftTime adShiftTime) throws IotequException {
        checkAvailable();
        if (adShiftTime==null)  adShiftTime=new AdShiftTime();
        else adShiftTime.setId(null);
        return adShiftTime;
    }
    @Override
    public void changeEmpty2Null(AdShiftTime adShiftTime) {
        if (Objects.nonNull(adShiftTime)) {
        }
    }
    @Override
    public void changeNull2Default(AdShiftTime adShiftTime) {
        if (adShiftTime.getId()==null) {
            adShiftTime.setId(0);
        }
        if (adShiftTime.getShiftId()==null) {
            adShiftTime.setShiftId(0);
        }
        if (adShiftTime.getName()==null) {
            adShiftTime.setName("");
        }
        if (adShiftTime.getWeekDays()==null) {
            adShiftTime.setWeekDays("1,2,3,4,5");
        }
        if (adShiftTime.getStartWorkTime()==null) {
            adShiftTime.setStartWorkTime(new Date());
        }
        if (adShiftTime.getEndWorkTime()==null) {
            adShiftTime.setEndWorkTime(new Date());
        }
    }
}
