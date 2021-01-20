package top.iotequ.attendance.approvelist.service.impl;
import top.iotequ.attendance.approvelist.pojo.AdApproveList;
import top.iotequ.attendance.approvelist.dao.AdApproveListDao;
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
@ConditionalOnMissingClass({"top.iotequ.attendance.approvelist.service.impl.AdApproveListService"})
@Service(value="adApproveListService")
public class CgAdApproveListService extends CgService<AdApproveList>  {
private static final Logger log = LoggerFactory.getLogger(CgAdApproveListService.class);
    @Autowired
    private AdApproveListDao adApproveListDao;
    public static String [] dictState1Value= {String.valueOf(AdUtil.st_waiting),String.valueOf(AdUtil.st_doing),String.valueOf(AdUtil.st_passed),String.valueOf(AdUtil.st_refused)};
    public static String [] dictState1Text= {"adApproveList.field.state1_0","adApproveList.field.state1_1","adApproveList.field.state1_2","adApproveList.field.state1_3"};
    public static String [] dictState0Value= {String.valueOf(AdUtil.st_waiting),String.valueOf(AdUtil.st_doing),String.valueOf(AdUtil.st_passed),String.valueOf(AdUtil.st_refused)};
    public static String [] dictState0Text= {"adApproveList.field.state0_0","adApproveList.field.state0_1","adApproveList.field.state0_2","adApproveList.field.state0_3"};
    @Override
    public Class<AdApproveList> getEntityClass() {
        return AdApproveList.class;
    }
    @Override
    public IDaoService<AdApproveList> getDaoService() {
        return adApproveListDao ;
    }
    @Override
    public IFlowService<AdApproveList> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdApproveList obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"state1")) map.put("dictState1", DictionaryUtil.getDictList(dictState1Value,dictState1Text));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"state0")) map.put("dictState0", DictionaryUtil.getDictList(dictState0Value,dictState0Text));
        return map;
    }
    @Override
    public AdApproveList getDefaultObject(AdApproveList adApproveList) throws IotequException {
        return adApproveList;
    }
    @Override
    public void changeEmpty2Null(AdApproveList adApproveList) {
        if (Objects.nonNull(adApproveList)) {
        }
    }
}
