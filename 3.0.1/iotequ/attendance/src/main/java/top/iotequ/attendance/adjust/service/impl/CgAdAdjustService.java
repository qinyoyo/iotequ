package top.iotequ.attendance.adjust.service.impl;
import top.iotequ.attendance.adjust.pojo.AdAdjust;
import top.iotequ.attendance.adjust.dao.AdAdjustDao;
import top.iotequ.framework.flow.*;
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
import top.iotequ.attendance.util.AdUtil;
import java.util.*;

/**************************************************
Create by iotequ codegenerator 3.0.0
Author : Qinyoyo
"**************************************************/
@ConditionalOnMissingClass({"top.iotequ.attendance.adjust.service.impl.AdAdjustService"})
@Service(value="adAdjustService")
public class CgAdAdjustService extends CgService<AdAdjust>  {
private static final Logger log = LoggerFactory.getLogger(CgAdAdjustService.class);
    private static final int maxNoteLength = 100;
    @Autowired IFlowService<AdAdjust> flowService;
    @Autowired
    private AdAdjustDao adAdjustDao;
    public static String [] dictAdjustTypeValue= {String.valueOf(AdUtil.ad_add_data),String.valueOf(AdUtil.ad_business_local),String.valueOf(AdUtil.ad_business_out),  String.valueOf(AdUtil.ad_leave_s),String.valueOf(AdUtil.ad_leave_b),String.valueOf(AdUtil.ad_leave_c),  String.valueOf(AdUtil.ad_leave_n),String.valueOf(AdUtil.ad_leave_t),String.valueOf(AdUtil.ad_overwork)};
    public static String [] dictAdjustTypeText= {"adAdjust.field.adjustType_0","adAdjust.field.adjustType_1","adAdjust.field.adjustType_2","adAdjust.field.adjustType_3","adAdjust.field.adjustType_4","adAdjust.field.adjustType_5","adAdjust.field.adjustType_6","adAdjust.field.adjustType_7","adAdjust.field.adjustType_8"};
    public static String [] dictStateValue= {String.valueOf(AdUtil.st_waiting),String.valueOf(AdUtil.st_doing),String.valueOf(AdUtil.st_passed),String.valueOf(AdUtil.st_refused)};
    public static String [] dictStateText= {"adAdjust.field.state_0","adAdjust.field.state_1","adAdjust.field.state_2","adAdjust.field.state_3"};
    @Override
    public Class<AdAdjust> getEntityClass() {
        return AdAdjust.class;
    }
    @Override
    public IDaoService<AdAdjust> getDaoService() {
        return adAdjustDao ;
    }
    @Override
    public IFlowService<AdAdjust> getFlowService() {
        return flowService;
    }
    @Override
    public Map<String,Object> getDictionary(AdAdjust obj, Boolean useTree, String dynaFields) {
        Map<String,Object> map=new HashMap<>();
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"orgCode")) {
            if (useTree) map.put("dictOrgCode", DictionaryUtil.getTreeViewData(obj,"sys_org","org_code","name","parent","org_code",null,null,null));
            else map.put("dictOrgCode", DictionaryUtil.getDictListFromDatabase(obj,"sys_org","org_code","name",null,false,null));
        }
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"adjustType")) map.put("dictAdjustType", DictionaryUtil.getDictList(dictAdjustTypeValue,dictAdjustTypeText));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"state")) map.put("dictState", DictionaryUtil.getDictList(dictStateValue,dictStateText));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowSelection")) map.put("dictFlowSelection",getFlowService()==null ? null: getFlowService().getSelections(Util.getRequest().getParameter(IFlowService.flowAction)));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowNextOperator")) map.put("dictFlowNextOperator",getFlowService()==null ? null: getFlowService().getDictionaryOfNextOperator(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection)));
        if (Objects.isNull(dynaFields) || StringUtil.containsItem(dynaFields,"flowCopyToList")) map.put("dictFlowCopyToList",getFlowService()==null ? null: getFlowService().getDictionaryOfCopyToList(obj,Util.getRequest().getParameter(IFlowService.flowAction),Util.getRequest().getParameter(IFlowService.flowSelection)));
        return map;
    }
    @Override
    public AdAdjust getDefaultObject(AdAdjust adAdjust) throws IotequException {
        checkAvailable();
        if (adAdjust==null)  adAdjust=new AdAdjust();
        else adAdjust.setId(null);
        flowService.checkPrivilege(null,"add",null);
        adAdjust.setHr(AdUtil.getHr(Util.getUser().getId()));
        adAdjust.setHrRealName((String)QueryUtil.getSqlField("select hr_real_name from sys_user where id=${hr}",adAdjust));
        adAdjust.setApprover(AdUtil.getManager(Util.getUser().getId()));
        adAdjust.setApproverName((String)QueryUtil.getSqlField("select approver_name from sys_user where id=${approver}",adAdjust));
        adAdjust.setApproveOrg(AdUtil.getManageOrg(Util.getUser().getId()));
        return adAdjust;
    }
    @Override
    public void changeEmpty2Null(AdAdjust adAdjust) {
        if (Objects.nonNull(adAdjust)) {
        }
    }
    @Override
    public void changeFileField( AdAdjust adAdjust) {
        if (adAdjust.getId()!=null)
            adAdjust.setAddFile(UploadDownUtil.removeFilesExclusive(getEntityClass(),"addFile",adAdjust.getId().toString(),adAdjust.getAddFile(),true));
        else
            adAdjust.setAddFile(null);
    }
}
