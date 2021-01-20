package top.iotequ.project.version.service.impl;

import java.util.*;

import org.springframework.stereotype.Service;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;
import top.iotequ.framework.flow.impl.SysFlowProcessService;
import top.iotequ.framework.flow.IFlowService;
import top.iotequ.util.Util;
import top.iotequ.util.SqlUtil;
import top.iotequ.project.ConstData;
import top.iotequ.project.version.pojo.PmVersionApplication;
// 所有函数均需要根据自己需求重写
@Service
public class PmVersionApplicationFlowService extends SysFlowProcessService<PmVersionApplication> {
	public static final String  DECISION = "decision";
	
	public static final String  pm_va_assess = "pm_va_assess";
	public static final String  pm_va_decision = "pm_va_decision";
	public static final String  pm_va_publish = "pm_va_publish";
	public static final String  pm_va_review = "pm_va_review";
	public static final String[] allOperations={pm_va_assess,pm_va_decision,pm_va_publish,pm_va_review};
	static final int [] allStateValues = {ConstData.st_start,ConstData.st_assessed,ConstData.st_reviewed,ConstData.st_waitPublish,ConstData.st_published,ConstData.st_denied};
	static final String [] allStateNames = {"待审核","已评估","已评审","待发布","已提交","否决"};
	static List<Map<String, Object>> allStates = new ArrayList<Map<String, Object>>();

	public static final String peopleSql = "select distinct sys_user.id as value,sys_user.real_name as text from sys_user right join pm_people on pm_people.user_id=sys_user.id right join pm_people_group on pm_people_group.id=pm_people.group_id"
			+ " where pm_people_group.group_type = ? ";


	/**
	 * 获得该操作对应的实体类
	 *
	 * @return 实体类
	 */
	@Override
	public Class<PmVersionApplication> getEntityClass() {
		return PmVersionApplication.class;
	}

	/**
	 * 获得基本的route地址
	 *
	 * @return route地址
	 */
	@Override
	public String getBaseRoute() {
		return "/project/version/pmVersionApplication";
	}

	@Override
	public String getModuleName() {
		return "project";
	}
	@Override
	public String getName(){
		return "版本申请";
	}
	@Override
	public String getItemName(PmVersionApplication pmVersionApplication) {
		return (pmVersionApplication==null?null:pmVersionApplication.getCustomer());
	}
	@Override
	public List<Map<String, Object>> getStates() {
		if (allStates.size()==0) allStates = IFlowService.buildStates(allStateValues,allStateNames);
		return allStates;
	}


	@Override
	public List<Map<String, Object>> getSelections(String operation) {
		return new ArrayList<Map<String, Object>>() {{
			add(new HashMap<String, Object>() {{
				put("value",APPROVE);	put("text","同意");
			}});
			add(new HashMap<String, Object>() {{
				put("value",DENY);	put("text","否决");
			}});
			add(new HashMap<String, Object>() {{
				put("value",REVIEW);	put("text","重新修改");
			}});		
			if (pm_va_decision.equals(operation) && ConstData.isUserInGroup(null, "CTO")) {
				add(new HashMap<String, Object>() {{
					put("value",DECISION);	put("text","需要上级决策");
				}});						
			}
		}};
	}
	/**
	 * 获得指定操作的标题
	 *
	 * @param operation 操作代码，通常为表定义code
	 * @return 标题
	 */
	@Override
	public String getOperationName(String operation) {
		return "pmVersionApplication.title."+ operation;
	}

	/**
	 * 获得所有可以执行的操作
	 * @return 所有的操作
	 */
	@Override
	public String [] getAllOperations() {
		return allOperations;
	}

	/**
	 * 判断用户是否可以执行该操作
	 *
	 * @param pmVersionApplication      实体类对象,新建时为空
	 * @param operation 不能为空，操作代码，add,update，以及流程代码，流程代码不能与系统操作代码相同
	 * @param userId    用户，null表示当前用户
	 * @throws IotequException 没有权限时抛出异常
	 */
	@Override
	public void checkPrivilege(PmVersionApplication pmVersionApplication,String operation,String userId) throws IotequException {
		if (userId==null) userId= Util.getUser().getId();
		if ("add".equals(operation))  { // 新建参数判断
		}
		else if ("update".equals(operation)) {  // 修改权限判断
			if   (( Util.isEmpty(pmVersionApplication.getFlowNote()) || pmVersionApplication.getFlowState() == ConstData.st_start)
					&& Util.getUser().getId().equals(pmVersionApplication.getFlowRegisterBy()) )
				return;
			else throw new IotequException(IotequThrowable.NO_AUTHORITY,"");
		}
		else {  		
			int state=pmVersionApplication.getFlowState();
			switch (operation) {
				case pm_va_assess : 
					if (state!=ConstData.st_start) throw new IotequException(IotequThrowable.NO_AUTHORITY,"本状态不允许执行该操作");
					else if (!ConstData.isUserInGroup(userId,"CMO"))
						throw new IotequException(IotequThrowable.NO_AUTHORITY,"需要市场总监审核");
					else return;
				case pm_va_review : 
					if (state!=ConstData.st_assessed) throw new IotequException(IotequThrowable.NO_AUTHORITY,"本状态不允许执行该操作");
					else if (!ConstData.isUserInGroup(userId,"PM"))
						throw new IotequException(IotequThrowable.NO_AUTHORITY,"需要项目管理人员评审");
					else return;
				case pm_va_decision: 
					if (state!=ConstData.st_reviewed) throw new IotequException(IotequThrowable.NO_AUTHORITY,"本状态不允许执行该操作");
					else if (!ConstData.isUserInGroup(userId,"CTO,CEO"))
						throw new IotequException(IotequThrowable.NO_AUTHORITY,"需要技术总监或CEO决策");
					else return;
				case pm_va_publish : 
					if (state!=ConstData.st_waitPublish) throw new IotequException(IotequThrowable.NO_AUTHORITY,"本状态不允许执行该操作");
					else if (!ConstData.isUserInGroup(userId,"VM"))
						throw new IotequException(IotequThrowable.NO_AUTHORITY,"需要版本管理人员确定版本");
					else return;					
			}					
			throw new IotequException(IotequThrowable.NO_AUTHORITY,operation+ " 未授权，原因未知");
		}
	}

	/**
	 * 检查删除记录权限
	 *
	 * @param ids    以逗号分隔的id值
	 * @param userId null 表示当前用户
	 * @throws IotequException 没有权限时抛出异常
	 */
	@Override
	public void checkDeletePrivilege(String ids,String userId) throws IotequException {
		if (Util.isEmpty(ids)) return;
		if (userId==null) userId= Util.getUser().getId();
		if (ids.split(",").length!=SqlUtil.sqlQueryInteger(false, "SELECT count(*) FROM pm_version_application where id=? and flow_note is null and flow_register_by=?", ids,userId)) 
			throw new IotequException(IotequThrowable.NO_AUTHORITY,"");
	}

	@Override
	public int getState(PmVersionApplication pojo) {
		return pojo.getFlowState();
	}

	@Override
	public void setState(PmVersionApplication pojo, int state) {
		pojo.setFlowState(state);
	}

	/**
	 * 获得流程发起人
	 *
	 * @param pmVersionApplication 流程对象
	 * @return 发起人id
	 */
	@Override
	public String getRegisterBy(PmVersionApplication pmVersionApplication) {
		return pmVersionApplication.getFlowRegisterBy();
	}

	/**
	 * 获得下一步处理人
	 * @param pmVersionApplication 流程对象
	 * @return 下一步处理人id
	 */
	@Override
	public String getNextOperator(PmVersionApplication pmVersionApplication) {
		return pmVersionApplication.getFlowNextOperator();
	}

	/**
	 * 设置下一步处理人员
	 * @param pmVersionApplication 对象
	 * @param nextOperator 下一步处理人id
	 */
	@Override
	public void setNextOperator(PmVersionApplication pmVersionApplication, String nextOperator) {
		pmVersionApplication.setFlowNextOperator(nextOperator);
	}

	/**
	 * 获得下一步处理人员的字典
	 * @param pmVersionApplication 对象
	 * @param operation 流程操作,null表示编辑状态
	 * @param selection 当前选择,null表示编辑状态
	 * @return 下一步处理人员的字典
	 */
	@Override
	public List<Map<String, Object>> getDictionaryOfNextOperator(PmVersionApplication pmVersionApplication, String operation, String selection) {
		try {
			int state0=pmVersionApplication.getFlowState();
			int state=(Util.isEmpty(selection) ? state0 : (stateTransfer(pmVersionApplication, operation, selection)));
			if (REVIEW.equals(selection) && state==ConstData.st_start)  // 返回本人修改
				return SqlUtil.sqlQuery(false,"select id as value,real_name as text from sys_user where id = ?",pmVersionApplication.getFlowRegisterBy());
			String groups="";
			switch (state) {
				case ConstData.st_start:
					groups="CMO";
					break;
				case ConstData.st_assessed:
					groups="PM";
					break;
				case ConstData.st_reviewed:
					if (state0==ConstData.st_reviewed)	groups="CEO";
					else groups="CTO";
					break;
				case ConstData.st_waitPublish:
					groups="VM";
					break;
				default:
			}
			if (groups.isEmpty()) return null;
			else return SqlUtil.sqlQuery(false,peopleSql,  groups);
		} catch (IotequException e) {
			return null;
		}
	}

	/**
	 * 获得默认下一步处理人员
	 * @param pmVersionApplication 对象
	 * @param operation 流程操作,null表示编辑状态
	 * @param selection 当前选择,null表示编辑状态
	 * @return 默认下一步处理人员id
	 */
	@Override
	public String defaultNextOperator(PmVersionApplication pmVersionApplication, String operation, String selection) {
		return null;
	}

	/**
	 * 获得对象的抄送对象列表
	 * @param pmVersionApplication 流程对象
	 * @return 抄送人员id列表
	 */
	@Override
	public String getCopyToList(PmVersionApplication pmVersionApplication) {
		return pmVersionApplication.getFlowCopyToList();
	}

	/**
	 * 设置抄送人员清单
	 * @param pmVersionApplication 对象
	 * @param copyToList 抄送名单
	 */
	@Override
	public void setCopyToList(PmVersionApplication pmVersionApplication, String copyToList) {
		pmVersionApplication.setFlowCopyToList(copyToList);
	}

	/**
	 * 获得抄送人员可以选择的字典
	 * @param pmVersionApplication 对象
	 * @param operation 流程操作,null表示编辑状态
	 * @param selection 当前选择,null表示编辑状态
	 * @return 抄送人员字典
	 */
	@Override
	public List<Map<String, Object>> getDictionaryOfCopyToList(PmVersionApplication pmVersionApplication, String operation, String selection) {
		try {
			return SqlUtil.sqlQuery(false,peopleSql,  "CMO,PM,CTO,CEO");
		} catch (IotequException e) {
			return null;
		}
	}

	/**
	 * 获得抄送人员默认值
	 * @param pmVersionApplication 对象
	 * @param operation 流程操作
	 * @param selection 当前选择,null表示编辑状态
	 * @return 默认抄送人员
	 */
	@Override
	public String defaultCopyToList(PmVersionApplication pmVersionApplication, String operation, String selection) {
		return null;
	}

	@Override
	public int stateTransfer(PmVersionApplication pmVersionApplication, String operation, String selection) throws IotequException {
		int state0=pmVersionApplication.getFlowState();
		int state=state0;
		switch (state0) {
			case ConstData.st_start:
				switch (operation) {
					case pm_va_assess : 
						if (APPROVE.equals(selection)) {
							state=ConstData.st_assessed;					
						} else if (DENY.equals(selection)) {	
							state=ConstData.st_denied;				
						} else if (REVIEW.equals(selection)) {			
						}
						break;									
				}
				break;
			case ConstData.st_assessed:
				switch (operation) {
					case pm_va_review : 
						if (APPROVE.equals(selection)) {
							state=ConstData.st_reviewed;					
						} else if (DENY.equals(selection)) {	
							state=ConstData.st_denied;				
						} else if (REVIEW.equals(selection)) {	
							state=ConstData.st_start;	
						}
						break;										
				}
				break;				
			case ConstData.st_reviewed:
				switch (operation) {
					case pm_va_decision : 
						if (APPROVE.equals(selection)) {
							state=ConstData.st_waitPublish;					
						} else if (DENY.equals(selection)) {	
							state=ConstData.st_denied;				
						} else if (REVIEW.equals(selection)) {	
							state=ConstData.st_start;	
						}else if (DECISION.equals(selection)) {	
							state=ConstData.st_reviewed;	
						}
						break;										
				}
				break;	
			case ConstData.st_waitPublish:
				switch (operation) {
					case pm_va_publish : 
						if (APPROVE.equals(selection)) {
							state=ConstData.st_published;					
						} else if (DENY.equals(selection)) {	
							state=ConstData.st_denied;				
						} else if (REVIEW.equals(selection)) {	
							state=ConstData.st_start;	
						}
						break;										
				}
			default:					
		}
		pmVersionApplication.setFlowState(state);
		return state;
	}
	@Override
	public void checkParameters(PmVersionApplication pmVersionApplication) throws IotequException {
		if (pmVersionApplication!=null) {
			if (Util.isEmpty(pmVersionApplication.getCustomer()))  throw new IotequException(IotequThrowable.PARAMETER_ERROR,"必须输入完整的客户信息");
			if (Util.isEmpty(pmVersionApplication.getLicence()))  throw new IotequException(IotequThrowable.PARAMETER_ERROR,"必须输入完整的授权信息");
			int type=pmVersionApplication.getApplicationType();
			int state=pmVersionApplication.getFlowState();
			if (type==1) {				
				if (Util.isEmpty(pmVersionApplication.getContractNo()))  throw new IotequException(IotequThrowable.PARAMETER_ERROR,"必须输入合同号");
			} 	
			if (state!=ConstData.st_denied &&  state!=ConstData.st_published && Util.isEmpty(pmVersionApplication.getFlowNextOperator())) {
				throw new IotequException(IotequThrowable.PARAMETER_ERROR,"必须指定下一步操作人");
			}
		}
	}
}
