package top.iotequ.attendance.adjust.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.attendance.adjust.dao.AdAdjustDao;
import top.iotequ.attendance.adjust.pojo.AdAdjust;
import top.iotequ.attendance.approvelist.dao.AdApproveListDao;
import top.iotequ.attendance.approvelist.pojo.AdApproveList;
import top.iotequ.attendance.org.pojo.AdOrg;
import top.iotequ.attendance.util.AdUtil;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.flow.impl.SysFlowProcessService;
import top.iotequ.framework.flow.FlowRecord;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.framework.security.service.SecurityService;
import top.iotequ.framework.util.EntityUtil;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.Util;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

// 所有函数均需要根据自己需求重写
@Service
public class AdAdjustFlowService extends SysFlowProcessService<AdAdjust> {
    @Autowired
    AdApproveListDao adApproveListDao;
    @Autowired AdAdjustDao adAdjustDao;
    public static final String approve = "approve";
    public static final String[] allOperations = {approve};

    static final int[] allStateValues = {AdUtil.st_waiting, AdUtil.st_doing, AdUtil.st_passed, AdUtil.st_refused};
    static final String[] allStateNames = {"待审核", "审核中", "批准", "否决"};
    static List<Map<String, Object>> allStates = new ArrayList<Map<String, Object>>();



    @Override
    public String getModuleName() {
        return "attendance";
    }

    @Override
    public String getName() {
        return "人事调整";
    }

    @Override
    public String getItemName(AdAdjust adAdjust) {
        int typ = adAdjust.getAdjustType();
        switch (typ) {
            case AdUtil.ad_add_data : return "补登数据";
            case AdUtil.ad_business_local : return "市内出差";
            case AdUtil.ad_business_out  : return "外地出差";
            case AdUtil.ad_leave_s  : return "事假";
            case AdUtil.ad_leave_b : return "病假";
            case AdUtil.ad_leave_c : return "产假";
            case AdUtil.ad_leave_n : return "年假";
            case AdUtil.ad_leave_t : return "调休";
            case AdUtil.ad_overwork : return "安排的加班";
            case AdUtil.ad_free_overwork  : return "自由加班";
            default: return "";
        }
    }

    @Override
    public String getBaseRoute() { return "/attendance/adjust/adAdjust/"; }

    @Override
    public List<Map<String, Object>> getStates() {
        if (allStates.size() == 0) allStates = IFlowService.buildStates(allStateValues, allStateNames);
        return allStates;
    }

    @SuppressWarnings("serial")
    @Override
    public List<Map<String, Object>> getSelections(String operation) {
        return new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {{
                put("value", APPROVE);
                put("text", "system.action.flowApprove");
            }});
            add(new HashMap<String, Object>() {{
                put("value", DENY);
                put("text", "system.action.flowDeny");
            }});
        }};
    }

    @Override
    public String getOperationName(String operation) {
        return "审批";
    }

    @Override
    public void checkPrivilege(AdAdjust adAdjust, String operation, String userId) throws IotequException {
        if (userId == null) userId = Util.getUser().getId();
        if (adAdjust == null || "add".equals(operation)) {
            if( !SecurityService.hasGrantedAttribute("/attendance/adjust/adAdjust/default",null)
                 || !SecurityService.hasGrantedAttribute("/attendance/adjust/adAdjust/save",null))
                throw new IotequException(IotequThrowable.NO_AUTHORITY, operation);
        } else if ("update".equals(operation)) {  // 修改权限判断
            if (!EntityUtil.entityEquals(adAdjust.getState(), getStates().get(0).get("value")) || !Util.getUser().getId().equals(adAdjust.getEmployee()))
                throw new IotequException(IotequThrowable.NO_AUTHORITY, "update");
            else if( !SecurityService.hasGrantedAttribute("/attendance/adjust/adAdjust/record",null)
                    || !SecurityService.hasGrantedAttribute("/attendance/adjust/adAdjust/save",null))
                throw new IotequException(IotequThrowable.NO_AUTHORITY, "update");
            else return;
        } else if (approve.equals(operation)) {
            int state=adAdjust.getState();
            if( !SecurityService.hasGrantedAttribute("/attendance/adjust/adAdjust/f_"+operation,null))
                throw new IotequException(IotequThrowable.NO_AUTHORITY, operation);
            else if (state != AdUtil.st_waiting && state != AdUtil.st_doing)
                throw new IotequException(IotequThrowable.FLOW_ERROR_STATE, String.valueOf(state));
            else if (!userId.equals(adAdjust.getApprover()))
                throw new IotequException(IotequThrowable.NO_AUTHORITY, userId);
        } else throw new IotequException(IotequThrowable.FLOW_ERROR_OPRATION, operation);
    }

    @Override
    public void checkDeletePrivilege(String ids, String userId) throws IotequException {
        if (Util.isEmpty(ids)) return;
        if (userId == null) userId = Util.getUser().getId();
        if (ids.split(",").length != SqlUtil.sqlQueryInteger(false, "SELECT count(*) FROM ad_adjust where id=? and state=0 and employee=?", ids, userId))
            throw new IotequException(IotequThrowable.NO_AUTHORITY, null);
    }

    @Override
    public int getState(AdAdjust pojo) {
        return pojo.getState();
    }

    @Override
    public void setState(AdAdjust pojo, int state) {
        pojo.setState(state);
    }

    @Override
    public String [] getAllOperations() {
        return allOperations;
    }

    @Override
    public int stateTransfer(AdAdjust adAdjust, String operation, String selection) throws IotequException {
        int state0 = adAdjust.getState();
        int typ = adAdjust.getAdjustType();
        int state = state0;
        switch (state0) {
            case AdUtil.st_waiting:
            case AdUtil.st_doing:
                switch (operation) {
                    case "approve":
                        Date dt0=adAdjust.getStartTime(),dt1=adAdjust.getEndTime();
                        if (APPROVE.equals(selection)) {
                            if (typ == AdUtil.ad_add_data) state = AdUtil.st_passed;
                            else {
                                int minitues = (int)((dt1.getTime() - dt0.getTime())/60000);
                                AdOrg org = AdUtil.findOrg(adAdjust.getApproveOrg());
                                if (Objects.isNull(org) || Objects.isNull(org.getManageLimit()) || minitues <= org.getManageLimit()) {
                                    state = AdUtil.st_passed;
                                    adAdjust.setApproveOrg(null);
                                    adAdjust.setApprover(null);
                                } else {
                                    state = AdUtil.st_doing;
                                    adAdjust.setApproveOrg(AdUtil.getManageOrg(adAdjust.getApprover()));
                                    adAdjust.setApprover(AdUtil.getManager(adAdjust.getApprover()));
                                }
                            }
                        } else if (DENY.equals(selection)) {
                            state = AdUtil.st_refused;
                        }
                        break;
                    default: throw new IotequException(IotequThrowable.FLOW_ERROR_OPRATION,operation) ;
                }
                break;
            case AdUtil.st_passed:
            case AdUtil.st_refused:
                throw new IotequException(IotequThrowable.FLOW_ERROR_OPRATION,operation) ;
            default: throw new IotequException(IotequThrowable.FLOW_ERROR_STATE,String.valueOf(state0));
        }
        adAdjust.setState(state);
        return state;
    }

    @Override
    public void checkParameters(AdAdjust adAdjust) throws IotequException {
        //if (Util.isEmpty(adAdjust.getDescription())) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"description");
        Date dt0=adAdjust.getStartTime(),dt1=adAdjust.getEndTime();
        int typ = adAdjust.getAdjustType();
        if (typ == AdUtil.ad_add_data && dt0==null && dt1==null)  throw new IotequException(IotequThrowable.PARAMETER_ERROR,"time");
        else if (dt0==null && dt1==null) throw new IotequException(IotequThrowable.PARAMETER_ERROR,"2 time");
    }

    @Override
    public String getCopyToList(AdAdjust adAdjust) {
        return adAdjust.getHr();
    }
    @Override
    public void setCopyToList(AdAdjust adAdjust, String copyToList) {
    }

    //抄送列表，本部门以及上级部门的manager和hr的字典
    private List<Map<String,Object>> getAllCopyToListOf(String id) {
        List<String> ids=new ArrayList<>();
        Integer orgCode=null;
        if (Util.isEmpty(id)) orgCode=Util.getUser().getOrgCode();
        else try{ orgCode = SqlUtil.sqlQueryInteger(false,"select org_code as org from sys_user where id=?",id);
        } catch (Exception e) {	}
        while (orgCode!=null && orgCode!=0) {
            String sql="SELECT manager,parent,hr  FROM  ad_org  join sys_org on ad_org.org_code=sys_org.org_code where ad_org.org_code=?";
            List<Map<String, Object>> list=null;
            try {
                list = SqlUtil.sqlQuery(false,sql, orgCode);
            } catch (Exception e) {	}
            if (list!=null && list.size()>0) {
                Object o=list.get(0).get("manager");
                if (o!=null) ids.add(o.toString());
                o=list.get(0).get("hr");
                if (o!=null) ids.add(o.toString());
                o=list.get(0).get("parent");
                orgCode = o==null ? null : (Integer)o;
            } else break;
        }
        try {
            return SqlUtil.sqlQuery(false,"select id as value,real_name as text from sys_user where id = ?",ids);
        } catch (IotequException e) {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getDictionaryOfCopyToList(AdAdjust pojo, String operation, String selection) {
        return getAllCopyToListOf(pojo==null || operation == null ? null : pojo.getEmployee());
        // 所有的抄送列表都一致，为发起人的部门以及上级部门的人事和行政主管
    }

    @Override
    public String defaultCopyToList(AdAdjust pojo, String operation,String selection) {
        //默认抄送人员为发起人的部门人事
        return AdUtil.getHr( pojo==null || operation==null ? Util.getUser().getId() : pojo.getEmployee());
    }

    @Override
    public String getNextOperator(AdAdjust adAdjust) {
        return adAdjust.getApprover();
    }
    @Override
    public void setNextOperator(AdAdjust adAdjust, String nextOperator) {
        adAdjust.setApprover(nextOperator);
    }

    @Override
    public List<Map<String, Object>> getDictionaryOfNextOperator(AdAdjust pojo, String operation, String selection) {
        String def = defaultNextOperator(pojo,operation,selection); // 字典与默认值相同，不允许选择
        try {
            return SqlUtil.sqlQuery(false,"select id as value, real_name as text from sys_user where id=?",def);
        } catch (IotequException e) {
            return null;
        }
    }

    @Override
    public String defaultNextOperator(AdAdjust adAdjust, String operation, String selection) {
        if (operation==null) {
            if (adAdjust != null && !Util.isEmpty(getNextOperator(adAdjust))) return getNextOperator(adAdjust);
            else return AdUtil.getManager(Util.getUser().getId());
        }
        else if (approve.equals(operation)) {
            if (APPROVE.equals(selection)) {
                Date dt0=adAdjust.getStartTime(),dt1=adAdjust.getEndTime();
                if (AdUtil.ad_add_data == adAdjust.getAdjustType()) return null;
                else {
                    int minitues = (int) ((dt1.getTime() - dt0.getTime()) / 60000);
                    AdOrg org = AdUtil.findOrg(adAdjust.getApproveOrg());
                    if (Objects.isNull(org) || Objects.isNull(org.getManageLimit()) || minitues <= org.getManageLimit()) {
                        return null;
                    } else {
                       return AdUtil.getManager(adAdjust.getApprover());
                    }
                }
            }
            else return null;
        }
        return null;
    }

    @Override
    public Class<AdAdjust> getEntityClass() {
        return AdAdjust.class;
    }

    @Override
    public List<FlowRecord> getFlowProcess(String adjustId) {
        AdAdjust adAdjust = adAdjustDao.select(adjustId);
        List<AdApproveList> list = adApproveListDao.listBy("adjust_id='"+adjustId+"'","approve_time desc");
        List<FlowRecord> ret = new ArrayList<>();
        if (!Util.isEmpty(list)) {
            for (AdApproveList fp : list) {
                FlowRecord f = new FlowRecord();
                f.setNextOperator(null);
                f.setNote(fp.getApproveNote());
                if (fp.getState0()==null)
                    f.setOperation("system.action.new "+Util.getTextFromDict(adAdjust.getAdjustType(),CgAdAdjustService.dictAdjustTypeValue,CgAdAdjustService.dictAdjustTypeText));
                else if (fp.getApprover().equals(adAdjust.getEmployee()))
                    f.setOperation("system.action.edit "+Util.getTextFromDict(adAdjust.getAdjustType(),CgAdAdjustService.dictAdjustTypeValue,CgAdAdjustService.dictAdjustTypeText));
                else  f.setOperation("adAdjust.title.approve");
                f.setOperator(fp.getState0()==null ? adAdjust.getRealName() : fp.getRealName());
                f.setSelection(fp.getState0()==null || fp.getApprover().equals(adAdjust.getEmployee()) ? null : fp.getState1() == AdUtil.st_refused ? "system.action.flowDeny" : "system.action.flowApprove");
                f.setState(Util.getTextFromDict(getStates(),fp.getState1()));
                f.setTime(fp.getApproveTime());
                switch (fp.getState1()) {
                    case AdUtil.st_waiting :
                        f.setIcon("fa fa-spinner");
                        f.setType("info");
                        break;
                    case AdUtil.st_doing:
                        f.setIcon("fa fa-refresh");
                        f.setType("primary");
                        break;
                    case AdUtil.st_passed:
                        f.setIcon("fa fa-check-square-o");
                        f.setType("success");
                        break;
                    case AdUtil.st_refused:
                        f.setIcon("fa fa-times-circle");
                        f.setType("danger");
                        break;
                }
                ret.add(f);
            }
        }
        return ret;
    }

    @Override
    public String getRegisterBy(AdAdjust pojo) {
        return pojo.getEmployee();
    }

    @Override
    public void saveFlowProcess(Object obj) throws IotequException{
        if (Objects.nonNull(obj) && obj instanceof AdApproveList) {
            AdApproveList fp=(AdApproveList) obj;
            fp.setId(null);
            adApproveListDao.insert(fp);
        }
    }
    @Override
    public void  deleteFlowProcess(String ids) throws IotequException {
        SqlUtil.sqlExecute("delete FROM ad_approve_list where adjust_id=?",ids);
    }
    @Override
    public Object generateFlowProcess(HttpServletRequest request, AdAdjust obj0, AdAdjust obj1, String operation) throws IotequException {
        if (request == null) throw new IotequException(IotequThrowable.NULL_OBJECT, "flow object");
        String id =obj1.getId();
        if (Util.isEmpty(id)) throw new IotequException(IotequThrowable.NULL_OBJECT, "id");
        AdApproveList fp = new AdApproveList();
        fp.setAdjustId(id);
        fp.setApproveTime(new Date());
        fp.setApproveNote(obj0==null || Util.isEmpty(operation) ? obj1.getDescription() : request.getParameter(flowNote));
        fp.setApprover(Util.getUser().getId());
        fp.setRealName(Util.getUser().getRealName());
        fp.setState0(obj0==null ? null:obj0.getState());
        fp.setState1(obj1.getState());
        fp.setId(null);
        return fp;
    }

}
