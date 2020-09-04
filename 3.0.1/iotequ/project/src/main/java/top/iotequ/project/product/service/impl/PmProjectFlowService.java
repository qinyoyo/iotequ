package top.iotequ.project.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.flow.impl.SysFlowProcessService;
import top.iotequ.framework.service.utils.DictionaryUtil;
import top.iotequ.framework.util.EntityUtil;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.StringUtil;
import top.iotequ.framework.util.Util;
import top.iotequ.project.ConstData;
import top.iotequ.project.product.dao.PmProjectDao;
import top.iotequ.project.product.pojo.PmProject;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 所有函数均需要根据自己需求重写
@Service
public class PmProjectFlowService extends SysFlowProcessService<PmProject> {
    public static final String DECISION = "decision";
    public static final String FINISHED = "finished";
    // 所有的流程操作定义
    public static final String  assess = "assess";
    public static final String  decision = "decision";
    public static final String  develop = "develop";
    public static final String  publish = "publish";
    public static final String  review = "review";
    public static final String  test = "test";
    public static final String[] allOperations={assess,decision,develop,publish,review,test};
    
    public static final String peopleSql = "select distinct sys_user.id as value,sys_user.real_name as text from sys_user right join pm_people on pm_people.user_id=sys_user.id right join pm_people_group on pm_people_group.id=pm_people.group_id"
            + " where pm_people_group.group_type = ? ";
    @Autowired
    PmProjectDao pmProjectDao;

    @Override
    public Class<PmProject> getEntityClass() {
        return PmProject.class;
    }

    @Override
    public String getBaseRoute() { return "/project/product/pmProject"; }

    @Override
    public String getModuleName() {
        return "project";
    }

    @Override
    public String getName() {
        return "立项";
    }

    @Override
    public String getItemName(PmProject pmProject) {
        return (pmProject == null ? null : pmProject.getName());
    }

    @Override
    public List<Map<String, Object>> getStates() {
        return DictionaryUtil.getDictList(ConstData.pm_project_states, ConstData.pm_project_state_names);
    }

    @Override
    public List<Map<String, Object>> getSelections(String operation) {
        if (Util.isEmpty(operation)) return null;
        else return new ArrayList<Map<String, Object>>() {{
            if (operation.equals(develop)) {
                add(new HashMap<String, Object>() {{
                    put("value", APPROVE);
                    put("text", "启动开发");
                }});
                add(new HashMap<String, Object>() {{
                    put("value", FINISHED);
                    put("text", "有可用版本");
                }});
            } else if (operation.equals(test)) {
                add(new HashMap<String, Object>() {{
                    put("value", APPROVE);
                    put("text", "提交测试");
                }});
            } else if (operation.equals(publish)) {
                add(new HashMap<String, Object>() {{
                    put("value", APPROVE);
                    put("text", "版本发布");
                }});
                add(new HashMap<String, Object>() {{
                    put("value", DENY);
                    put("text", "版本终止");
                }});
                add(new HashMap<String, Object>() {{
                    put("value", REVIEW);
                    put("text", "提交测试报告,继续测试");
                }});
            } else {
                add(new HashMap<String, Object>() {{
                    put("value", APPROVE);
                    put("text", "同意");
                }});
                add(new HashMap<String, Object>() {{
                    put("value", DENY);
                    put("text", "否决");
                }});
                add(new HashMap<String, Object>() {{
                    put("value", REVIEW);
                    put("text", "需要重审或完善项目信息");
                }});
                if (operation.equals(review) && ConstData.isUserInGroup(Util.getUser().getId(), "CTO")) {
                    add(new HashMap<String, Object>() {{
                        put("value", DECISION);
                        put("text", "需要上层决策");
                    }});
                }
            }
        }};
    }

    @Override
    public String getOperationName(String operation) {
        return "pmProject.title."+ operation;
    }

    @Override
    public String [] getAllOperations() {
        return allOperations;
    }

    @Override
    public void checkPrivilege(PmProject pmProject, String operation, String userId) throws IotequException {
        if (userId == null) userId = Util.getUser().getId();
        if (operation == null) throw new IotequException(IotequThrowable.NULL_OBJECT, "");
        if ("add".equals(operation)) {
            if (!SqlUtil.sqlExist(false, "select * from pm_people where user_id=?", userId))
                throw new IotequException(IotequThrowable.NO_AUTHORITY, "非项目分组成员不可以立项");
            return;
        } else if ("update".equals(operation)) {
            if ((Util.isEmpty(pmProject.getFlowNote()) || pmProject.getFlowState() == ConstData.st_start)
                    && Util.getUser().getId().equals(pmProject.getFlowRegisterBy()))
                return;
            else throw new IotequException(IotequThrowable.NO_AUTHORITY, "");
        } else {
            int type = pmProject.getType();
            int state = pmProject.getFlowState();
            String files0 = pmProjectDao.select(pmProject.getId()).getAddFile();
            String files = pmProject.getAddFile();
            switch (operation) {
                case assess:
                    if ((type == 1 || type == 2) && state == ConstData.st_start) {
                        if (!ConstData.isUserInGroup(userId, "CMO"))
                            throw new IotequException(IotequThrowable.NO_AUTHORITY, "只有市场管理人员才可以先审核市场立项项目");
                        else return;
                    } else throw new IotequException(IotequThrowable.PARAMETER_ERROR, "新建的市场立项项目才可以进行市场初审");
                case review:
                    if (state == ConstData.st_assessed || state == ConstData.st_reviewed || (type != 1 && type != 2 && state == ConstData.st_start)) {   // 允许二次评审
                        if (!ConstData.isUserInGroup(userId, "PM,CTO"))
                            throw new IotequException(IotequThrowable.NO_AUTHORITY, "只有项目管理人员和技术总监才可以审核非市场的新立项项目或需要技术审核项目");
                        else return;
                    } else throw new IotequException(IotequThrowable.PARAMETER_ERROR, "项目状态不正确，无法技术审核");
                case decision:
                    if (state == ConstData.st_waitdecision) {
                        if (!ConstData.isUserInGroup(userId, "CEO"))
                            throw new IotequException(IotequThrowable.NO_AUTHORITY, "只有项目决策人员才可以终审该项目");
                        else return;
                    } else throw new IotequException(IotequThrowable.PARAMETER_ERROR, "项目状态不正确，无法决策");
                case develop:
                    if (EntityUtil.entityEquals(files0, files))
                        new IotequException(IotequThrowable.PARAMETER_ERROR, "必须在附件中添加项目计划、设计文档等附件才可启动开发");
                    if (state == ConstData.st_initialed) {
                        if (!ConstData.isUserInGroup(userId, "PM"))
                            throw new IotequException(IotequThrowable.NO_AUTHORITY, "只有项目管理人员才可以启动项目开发");
                        else return;
                    } else throw new IotequException(IotequThrowable.PARAMETER_ERROR, "项目状态不正确，无法启动开发");
                case test:
                    if (EntityUtil.entityEquals(files0, files))
                        new IotequException(IotequThrowable.PARAMETER_ERROR, "必须在附件中添加自测报告才可以提交测试");
                    if (state == ConstData.st_develpoping) {
                        if (!ConstData.isUserInGroup(userId, "R&D"))
                            throw new IotequException(IotequThrowable.NO_AUTHORITY, "只有项目开发人员才可以提交测试");
                        else return;
                    } else throw new IotequException(IotequThrowable.PARAMETER_ERROR, "项目状态不正确，无法提交测试");
                case publish:
                    if (EntityUtil.entityEquals(files0, files))
                        new IotequException(IotequThrowable.PARAMETER_ERROR, "必须在附件中添加测试报告才可以发布");
                    if (state == ConstData.st_testing) {
                        if (!ConstData.isUserInGroup(userId, "VM"))
                            throw new IotequException(IotequThrowable.NO_AUTHORITY, "只有版本管理人员才可以发布版本");
                        else return;
                    } else throw new IotequException(IotequThrowable.PARAMETER_ERROR, "项目状态不正确，无法发布版本");
            }
            throw new IotequException(IotequThrowable.NO_AUTHORITY, " 未授权，原因未知");
        }
    }

    @Override
    public void checkDeletePrivilege(String ids, String userId) throws IotequException {
        if (Util.isEmpty(ids)) return;
        if (userId == null) userId = Util.getUser().getId();
        if (ids.split(",").length != SqlUtil.sqlQueryInteger(false, "SELECT count(*) FROM pm_project where id=? and flow_note is null and flow_register_by=?", ids, userId))
            throw new IotequException(IotequThrowable.NO_AUTHORITY, "只有本人在初始状态下才可以删除项目");
        if (SqlUtil.sqlExist(false, "select * from pm_version_application where project = ?", ids))
            throw new IotequException(IotequThrowable.FAILURE, "项目存在关联版本申请记录，不能删除");
    }

    @Override
    public int getState(PmProject pojo) {
        return pojo.getFlowState();
    }

    @Override
    public void setState(PmProject pojo, int state) {
        pojo.setFlowState(state);
    }


    @Override
    public String getRegisterBy(PmProject pojo) {
        return pojo.getFlowRegisterBy();
    }

    @Override
    public String getNextOperator(PmProject pojo) {
        return pojo.getFlowNextOperator();
    }

    @Override
    public void setNextOperator(PmProject pojo, String nextOperator) {
        pojo.setFlowNextOperator(nextOperator);
    }

    @Override
    public List<Map<String, Object>> getDictionaryOfNextOperator(PmProject pmProject, String operation, String selection) {
        try {
            int type = pmProject.getType();
            int state = (Util.isEmpty(selection) ? (type == 6 ? ConstData.st_published : ConstData.st_start) : (stateTransfer(pmProject, operation, selection)));
            if (REVIEW.equals(selection) && state == ConstData.st_start)  // 返回本人修改
                return SqlUtil.sqlQuery(false,"select * from sys_user where id=?", pmProject.getFlowRegisterBy());
            String groups = "";
            switch (state) {
                case ConstData.st_start:
                    if (type == 1 || type == 2) groups = "CMO";
                    else groups = "PM";
                    break;
                case ConstData.st_assessed:
                case ConstData.st_initialed:
                    groups = "PM";
                    break;
                case ConstData.st_reviewed:
                    groups = "CTO";
                    break;
                case ConstData.st_waitdecision:
                    groups = "CEO";
                    break;
                case ConstData.st_develpoping:
                    groups = "R&D";
                    break;
                case ConstData.st_testing:
                    groups = "VM";
                    break;
                default:
            }
            if (groups.isEmpty()) return null;
            else return SqlUtil.sqlQuery(false,peopleSql, groups);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String defaultNextOperator(PmProject pojo, String operation, String selection) {
        return null;
    }

    @Override
    public String getCopyToList(PmProject pojo) {
        return pojo.getFlowCopyToList();
    }

    @Override
    public void setCopyToList(PmProject pojo, String copyToList) {
        pojo.setFlowCopyToList(copyToList);
    }

    @Override
    public List<Map<String, Object>> getDictionaryOfCopyToList(PmProject pojo, String operation, String selection) {
        try {
            return SqlUtil.sqlQuery(false,peopleSql,  "CMO,PM,CTO,CEO");
        } catch (IotequException e) {
            return null;
        }
    }

    @Override
    public String defaultCopyToList(PmProject pojo, String operation, String selection) {
        return null;
    }

    @Override
    public int stateTransfer(PmProject pmProject, String operation, String selection) throws IotequException {
        int state0 = pmProject.getFlowState();
        int type = pmProject.getType();
        int state = state0;
        switch (state0) {
            case ConstData.st_start:  //
                switch (operation) {
                    case assess:
                        if (type != 1 && type != 2) throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                        if (APPROVE.equals(selection))
                            state = ConstData.st_assessed;
                        else if (DENY.equals(selection))
                            state = ConstData.st_denied;
                        else if (REVIEW.equals(selection))
                            state = state0;
                        break;
                    case review:
                        if (type == 1 || type == 2) throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                        if (APPROVE.equals(selection))
                            state = ConstData.st_reviewed;
                        else if (DENY.equals(selection))
                            state = ConstData.st_denied;
                        else if (REVIEW.equals(selection))
                            state = state0;
                        else if (DECISION.equals(selection))
                            state = ConstData.st_waitdecision;
                        break;
                    default:
                        throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                }
                break;
            case ConstData.st_assessed:
                switch (operation) {
                    case review:
                        if (APPROVE.equals(selection))
                            state = ConstData.st_reviewed;
                        else if (DENY.equals(selection))
                            state = ConstData.st_denied;
                        else if (REVIEW.equals(selection))
                            state = ConstData.st_start;
                        break;
                    default:
                        throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                }
                break;
            case ConstData.st_reviewed:
                switch (operation) {
                    case review:
                        if (APPROVE.equals(selection))
                            state = ConstData.st_initialed;
                        else if (DENY.equals(selection))
                            state = ConstData.st_denied;
                        else if (REVIEW.equals(selection))
                            state = ConstData.st_start;
                        else if (DECISION.equals(selection))
                            state = ConstData.st_waitdecision;
                        break;
                    default:
                        throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                }
                break;
            case ConstData.st_waitdecision:
                switch (operation) {
                    case decision:
                        if (APPROVE.equals(selection))
                            state = ConstData.st_initialed;
                        else if (DENY.equals(selection))
                            state = ConstData.st_denied;
                        else if (REVIEW.equals(selection))
                            state = ConstData.st_assessed;
                        break;
                    default:
                        throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                }
                break;
            case ConstData.st_initialed:
                switch (operation) {
                    case develop:
                        if (APPROVE.equals(selection))
                            state = ConstData.st_develpoping;
                        else if (FINISHED.equals(selection))
                            state = ConstData.st_published;
                        break;
                    default:
                        throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                }
                break;
            case ConstData.st_develpoping:
                switch (operation) {
                    case test:
                        if (APPROVE.equals(selection))
                            state = ConstData.st_testing;
                        break;
                    default:
                        throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                }
                break;
            case ConstData.st_testing:
                switch (operation) {
                    case publish:
                        if (APPROVE.equals(selection))
                            state = ConstData.st_published;
                        else if (DENY.equals(selection))
                            state = ConstData.st_discard;
                        else if (REVIEW.equals(selection))
                            state = ConstData.st_develpoping;
                        break;
                    default:
                        throw new IotequException(IotequThrowable.NO_AUTHORITY, "不允许的操作");
                }
                break;
            default:
        }
        pmProject.setFlowState(state);
        return state;
    }

    @Override
    public void checkParameters(PmProject pmProject) throws IotequException {
        int type = pmProject.getType();
        int state = pmProject.getFlowState();
        String group = "";
        String userId = pmProject.getFlowRegisterBy();
        if (type == 1 || type == 2) group = "MARKET,CMO"; // 市场项目必须由市场人员建立
        else if (type == 3 || type == 5) group = "PM,CTO";
        else if (type == 4) group = "PM,CTO,CEO";
        else if (type == 6) group = "VM";
        if (!ConstData.isUserInGroup(userId, group))
            throw new IotequException(IotequThrowable.NO_AUTHORITY, "立项项目类别与立项人分组权限不符");
        if (type == 1 || type == 2) {
            if (Util.isEmpty(pmProject.getCustomer()))
                throw new IotequException(IotequThrowable.PARAMETER_ERROR, "必须输入完整的客户信息");
            if (Util.isEmpty(pmProject.getMarketSize()))
                throw new IotequException(IotequThrowable.PARAMETER_ERROR, "必须输入市场规模");
        } else if (type == 6) {
            if (Util.isEmpty(pmProject.getCode()))
                throw new IotequException(IotequThrowable.PARAMETER_ERROR, "必须输入完整的内部代码");
        } else {
            if (Util.isEmpty(pmProject.getHumanCost()))
                throw new IotequException(IotequThrowable.PARAMETER_ERROR, "必须输入人力成本估算");
            if (Util.isEmpty(pmProject.getMaterialCost()))
                throw new IotequException(IotequThrowable.PARAMETER_ERROR, "必须输入物料成本估算");
        }
        if (Util.isEmpty(pmProject.getFlowNextOperator())) {
            if (state != ConstData.st_denied && state != ConstData.st_published)
                throw new IotequException(IotequThrowable.PARAMETER_ERROR, "必须指定下一步处理人");
        }
    }

}
