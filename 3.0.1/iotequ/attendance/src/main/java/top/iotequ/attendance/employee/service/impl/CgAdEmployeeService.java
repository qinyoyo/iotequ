package top.iotequ.attendance.employee.service.impl;
import top.iotequ.attendance.employee.pojo.AdEmployee;
import top.iotequ.attendance.employee.dao.AdEmployeeDao;
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
@ConditionalOnMissingClass({"top.iotequ.attendance.employee.service.impl.AdEmployeeService"})
@Service(value="adEmployeeService")
public class CgAdEmployeeService extends CgService<AdEmployee>  {
private static final Logger log = LoggerFactory.getLogger(CgAdEmployeeService.class);
    @Autowired
    private AdEmployeeDao adEmployeeDao;
    @Override
    public Class<AdEmployee> getEntityClass() {
        return AdEmployee.class;
    }
    @Override
    public IDaoService<AdEmployee> getDaoService() {
        return adEmployeeDao ;
    }
    @Override
    public IFlowService<AdEmployee> getFlowService() {
        return null;
    }
    @Override
    public Map<String,Object> getDictionary(AdEmployee obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"sex")) map.put("dictSex", DictionaryUtil.getDictListFromDatabase(obj,"sys_sex",null,null,null,false,null));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"select org_code,name from sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"select org_code,name from sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"shiftId")) map.put("dictShiftId", DictionaryUtil.getDictListFromDatabase(obj,"ad_shift","id","name",null,false,null));
        return map;
    }
    @Override
    public AdEmployee getDefaultObject(AdEmployee adEmployee) throws IotequException {
        return adEmployee;
    }
    @Override
    public void changeEmpty2Null(AdEmployee adEmployee) {
        if (Objects.nonNull(adEmployee)) {
        }
    }
    @Override
    public void changeNull2Default(AdEmployee adEmployee) {
        if (adEmployee.getId()==null) {
            adEmployee.setId("");
        }
        if (adEmployee.getRealName()==null) {
            adEmployee.setRealName("");
        }
        if (adEmployee.getEmployeeNo()==null) {
            adEmployee.setEmployeeNo("");
        }
        if (adEmployee.getEmLevel()==null) {
            adEmployee.setEmLevel(1);
        }
        if (adEmployee.getIsAttendance()==null) {
            adEmployee.setIsAttendance(Util.boolValue("1"));
        }
        if (adEmployee.getOvertimeMinutes()==null) {
            adEmployee.setOvertimeMinutes(0);
        }
    }
}
