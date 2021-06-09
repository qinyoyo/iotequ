package top.iotequ.attendance.specialshift.service.impl;
import top.iotequ.attendance.specialshift.pojo.AdSpecialShift;
import top.iotequ.attendance.specialshift.dao.AdSpecialShiftDao;
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
import top.iotequ.attendance.util.AdUtil;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.attendance.specialshift.service.impl.AdSpecialShiftService"})
@Service(value="adSpecialShiftService")
public class CgAdSpecialShiftService extends CgService<AdSpecialShift>  {
    private static final Logger log = LoggerFactory.getLogger(CgAdSpecialShiftService.class);
    @Autowired
    private AdSpecialShiftDao adSpecialShiftDao;
    public static String [] dictShiftModeValue= {String.valueOf(AdUtil.ss_free),String.valueOf(AdUtil.ss_adjust_shift),String.valueOf(AdUtil.ss_overwork)};
    public static String [] dictShiftModeText= {"adSpecialShift.field.shiftMode_0","adSpecialShift.field.shiftMode_1","adSpecialShift.field.shiftMode_2"};
    public static String [] dictModePropertyValue= {String.valueOf(AdUtil.md_attendance),String.valueOf(AdUtil.md_sign_in)};
    public static String [] dictModePropertyText= {"adSpecialShift.field.modeProperty_0","adSpecialShift.field.modeProperty_1"};
    @Override
    public Class<AdSpecialShift> getEntityClass() {
        return AdSpecialShift.class;
    }
    @Override
    public IDaoService<AdSpecialShift> getDaoService() {
        return adSpecialShiftDao ;
    }
    @Override
    public IFlowService<AdSpecialShift> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdSpecialShift obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"shiftMode")) map.put("dictShiftMode", DictionaryUtil.getDictList(dictShiftModeValue,dictShiftModeText));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"modeProperty")) map.put("dictModeProperty", DictionaryUtil.getDictList(dictModePropertyValue,dictModePropertyText));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCodes")) {
            if (useTree) map.put("dictOrgCodes", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent",null,null,null,null));
            else map.put("dictOrgCodes", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sexProperty")) map.put("dictSexProperty", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        return map;
    }
    @Override
    public AdSpecialShift getDefaultObject(AdSpecialShift adSpecialShift) throws IotequException {
        checkAvailable();
        if (adSpecialShift==null)  adSpecialShift=new AdSpecialShift();
        else adSpecialShift.setId(null);
        return adSpecialShift;
    }
    @Override
    public void changeEmpty2Null(AdSpecialShift adSpecialShift) {
        if (Objects.nonNull(adSpecialShift)) {
        }
    }
}
