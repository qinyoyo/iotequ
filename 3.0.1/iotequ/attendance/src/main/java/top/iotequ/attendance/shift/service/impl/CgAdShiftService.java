package top.iotequ.attendance.shift.service.impl;
import top.iotequ.attendance.shift.pojo.AdShift;
import top.iotequ.attendance.shift.dao.AdShiftDao;
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
import top.iotequ.attendance.util.AdUtil;
import top.iotequ.util.StringUtil;

import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.attendance.shift.service.impl.AdShiftService"})
@Service(value="adShiftService")
public class CgAdShiftService extends CgService<AdShift>  {
private static final Logger log = LoggerFactory.getLogger(CgAdShiftService.class);
    @Autowired
    private AdShiftDao adShiftDao;
    public static String [] dictAdModeValue= {String.valueOf(AdUtil.md_attendance),String.valueOf(AdUtil.md_sign_in)};
    public static String [] dictAdModeText= {"adShift.field.adMode_0","adShift.field.adMode_1"};
    @Override
    public Class<AdShift> getEntityClass() {
        return AdShift.class;
    }
    @Override
    public IDaoService<AdShift> getDaoService() {
        return adShiftDao ;
    }
    @Override
    public IFlowService<AdShift> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdShift obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"adMode")) map.put("dictAdMode", DictionaryUtil.getDictList(dictAdModeValue,dictAdModeText));
        return map;
    }
    @Override
    public AdShift getDefaultObject(AdShift adShift) throws IotequException {
        checkAvailable();
        if (adShift==null)  adShift=new AdShift();
        else adShift.setId(null);
        return adShift;
    }
    @Override
    public void changeEmpty2Null(AdShift adShift) {
        if (Objects.nonNull(adShift)) {
        }
    }
    @Override
    public void changeNull2Default(AdShift adShift) {
        if (adShift.getId()==null) {
            adShift.setId(0);
        }
        if (adShift.getName()==null) {
            adShift.setName("");
        }
        if (adShift.getAdMode()==null) {
            adShift.setAdMode(1);
        }
        if (adShift.getStartDate()==null) {
            adShift.setStartDate(new Date());
        }
        if (adShift.getEndDate()==null) {
            adShift.setEndDate(new Date());
        }
        if (adShift.getMinutesPerDay()==null) {
            adShift.setMinutesPerDay(480);
        }
    }
}
