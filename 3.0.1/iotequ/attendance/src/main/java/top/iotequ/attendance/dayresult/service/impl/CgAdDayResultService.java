package top.iotequ.attendance.dayresult.service.impl;
import top.iotequ.attendance.dayresult.pojo.AdDayResult;
import top.iotequ.attendance.dayresult.dao.AdDayResultDao;
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
@ConditionalOnMissingClass({"top.iotequ.attendance.dayresult.service.impl.AdDayResultService"})
@Service(value="adDayResultService")
public class CgAdDayResultService extends CgService<AdDayResult>  {
private static final Logger log = LoggerFactory.getLogger(CgAdDayResultService.class);
    @Autowired
    private AdDayResultDao adDayResultDao;
    @Override
    public Class<AdDayResult> getEntityClass() {
        return AdDayResult.class;
    }
    @Override
    public IDaoService<AdDayResult> getDaoService() {
        return adDayResultDao ;
    }
    @Override
    public IFlowService<AdDayResult> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdDayResult obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"SELECT org_code, name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"SELECT org_code, name from sys_org","org_code","name",null,false,null));
        }
        return map;
    }
    @Override
    public AdDayResult getDefaultObject(AdDayResult adDayResult) throws IotequException {
        return adDayResult;
    }
    @Override
    public void changeEmpty2Null(AdDayResult adDayResult) {
        if (Objects.nonNull(adDayResult)) {
        }
    }
    @Override
    public void changeNull2Default(AdDayResult adDayResult) {
        if (adDayResult.getId()==null) {
            adDayResult.setId(0);
        }
        if (adDayResult.getOrgCode()==null) {
            adDayResult.setOrgCode(0);
        }
        if (adDayResult.getOrgName()==null) {
            adDayResult.setOrgName("");
        }
        if (adDayResult.getEmployee()==null) {
            adDayResult.setEmployee("");
        }
        if (adDayResult.getEmployeeNo()==null) {
            adDayResult.setEmployeeNo("");
        }
        if (adDayResult.getIsAttendance()==null) {
            adDayResult.setIsAttendance(false);
        }
        if (adDayResult.getRealName()==null) {
            adDayResult.setRealName("");
        }
        if (adDayResult.getAdDate()==null) {
            adDayResult.setAdDate(new Date());
        }
        if (adDayResult.getShiftName()==null) {
            adDayResult.setShiftName("");
        }
        if (adDayResult.getState()==null) {
            adDayResult.setState(0);
        }
        if (adDayResult.getStateName()==null) {
            adDayResult.setStateName("");
        }
        if (adDayResult.getTimes()==null) {
            adDayResult.setTimes(1);
        }
        if (adDayResult.getMinutes()==null) {
            adDayResult.setMinutes(0);
        }
        if (adDayResult.getWorkMinutes()==null) {
            adDayResult.setWorkMinutes(0);
        }
        if (adDayResult.getShiftId()==null) {
            adDayResult.setShiftId(0);
        }
    }
}
