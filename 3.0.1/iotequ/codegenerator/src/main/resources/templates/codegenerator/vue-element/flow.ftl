package ${basePackage}.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import top.iotequ.framework.flow.*;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.${table.entity?uncap_first}.FlowProcess;
import top.iotequ.framework.util.*;
import top.iotequ.framework.util.SqlUtil;
import ${basePackage}.${POJO}.${table.entity};

@Service
public class ${generatorName?cap_first}FlowService extends SysFlowProcessService<${table.entity}> {
	// 所有的流程操作定义
	<#assign allPath = '' />
	<#list flowList as flow>
	<#list flow.path?split(",") as path>
	public static final String  ${path} = "${path}";
	<#if allPath == ''>
	<#assign allPath = path />
	<#else>
	<#assign allPath = allPath + "," + path />
	</#if>
	</#list>
	</#list>
	public static final String[] allOperations={${allPath}};

	// 所有的状态定义
	public static final int stXxx = 1;
	public static final int stYyy = 2;
	public static final int [] allStateValues = {stXxx,stYyy};
	static final String [] allStateNames = {"${generatorName}.flow.stXxx","${generatorName}.flow.stYyy"};

    /**
     * 获得该操作对应的实体类
     *
     * @return 实体类
     */
	@Override
	public Class<${table.entity}> getEntityClass() {
		return ${table.entity}.class;
	}

	/**
	* 获得基本的route地址
	*
	* @return route地址
	*/
	@Override
	public String getBaseRoute() {
		return "/${moduleName}/<#if subModule??>${subModule}/</#if>${generatorName}";
	}

	/* 使用 sys_flow_process 记录流程处理数据，则不需要重载以下函数
	@Override
	public List<FlowRecord> getFlowProcess(String flowId) {
		return super.getFlowProcess(flowId);
	}
	@Override
	public void saveFlowProcess(Object flowProcess) throws IotequException {
		super.saveFlowProcess(flowProcess);
	}
	@Override
	public void deleteFlowProcess(String ids) throws IotequException {
		super.deleteFlowProcess(ids);
	}
	@Override
	public Object generateFlowProcess(HttpServletRequest request, ${table.entity} obj0, ${table.entity} obj1, String operation) throws IotequException {
		return super.generateFlowProcess(request, obj0, obj1, operation);
	}
	@Override
	public void sendMessage(HttpServletRequest request, ${table.entity} obj0, ${table.entity} obj1, String operation) throws IotequException {
		super.sendMessage(request, obj0, obj1, operation);
	}
	@Override
	public String flowRoute(String operation, String flowId) {
		return "/" + operation+"?openMode=edit&id="+flowId;
	}
	@Override
	public String recordViewRoute(String flowId) {
		return "/record?openMode=view&id="+flowId;
	}
	*/

    /**
     * 获得模块名称
     *
     * @return 模块名称
     */
	@Override
	public String getModuleName() {
		return "${moduleName}";
	}

	 /**
     * 获得流程名称
     *
     * @return 流程名称
     */
	@Override
	public String getName() {
		return "你的流程处理名称";
	}

    /**
     * 获得流程项目名称
     *
     * @param ${table.entity?uncap_first} 实体类对象,不能为空
     * @return 流程项目名称
     */
	@Override
	public String getItemName(${table.entity} ${table.entity?uncap_first}) {
		return "流程对象的名称";
	}

    /**
     * 获得状态列表
     *
     * @return 所有的状态有序序列表, map 键值 ： value ： Integer，状态值，text：String 状态名称
     */
	@Override
	public List<Map<String, Object>> getStates() {
		if (allStates.size()==0) allStates = InterfaceFlow.buildStates(allStateValues,allStateNames);
		return allStates;
		//return Util.getDataDict("xx_xxx");
	}

    /**
     * 获得某一操作的可选操作
     *
     * @param operation 操作代码，通常为表定义code
     * @return 所有的可选操作序列表, map 键值 ： value ： String，选择代码，text：String 选择名称
     */
	@Override
	public List<Map<String, Object>> getSelections(String operation) {
		return new ArrayList<Map<String, Object>>() {{
			add(new HashMap<String, Object>() {{
				put("value",APPROVE);	put("text","system.action.flowApprove");
			}});
			add(new HashMap<String, Object>() {{
				put("value",DENY);	put("text","system.action.flowDeny");
			}});
			add(new HashMap<String, Object>() {{
				put("value",REVIEW);	put("text","system.action.flowReview");
			}});
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
		return "${generatorName}.title."+ operation;
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
     * @param ${table.entity?uncap_first}      实体类对象,新建时为空
     * @param operation 不能为空，操作代码，add,update，以及流程代码，流程代码不能与系统操作代码相同
     * @param userId    用户，null表示当前用户
     * @throws IotequException 没有权限时抛出异常
     */
	@Override
	public void checkPrivilege(${table.entity} ${table.entity?uncap_first}, String operation, String userId) throws IotequException {
		if (userId==null) userId=Util.getUser().getId();
		if (${table.entity?uncap_first}==null)  { // 新建
		}
		else if (operation==null)  { // 新建参数判断		
		}
		else if ("update".equals(operation)) {  // 修改权限判断
			if (!EntityUtil.entityEquals(${table.entity?uncap_first}.getFlowState(),getStates().get(0).get("value")) || !userId.equals(${table.entity?uncap_first}.getFlowRegisterBy()))
			   throw new IotequException(IotequThrowable.NO_AUTHORITY,"update");
			else return;
		}		
		else {  
			int state=${table.entity?uncap_first}.getFlowState();
			switch (operation) {
				<#list flowList as flow>
				case "${flow.path}" : 
					if (state!=...) throw new Exception ("本状态不允许执行该操作");
					else {
					}
				</#list>
			}				
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
	public void checkDeletePrivilege(String ids, String userId) throws IotequException {
		if (Util.isEmpty(ids)) return;
		if (userId==null) userId=Util.getUser().getId();
		if (ids.split(",").length!=SqlUtil.sqlQueryInteger(false, "SELECT count(*) FROM ${table.name} where ${pk.name}=? and flow_note is null and flow_register_by=?", ids,userId))
			throw new IotequException(IotequThrowable.NO_AUTHORITY,null);
	}

	/**
     * 获得状态
     * @param pojo 流程对象
     * @return 状态
     */
	@Override
	public int getState(${table.entity} ${table.entity?uncap_first}) {
		return ${table.entity?uncap_first}.getFlowState();
	}

	 /**
     * 设置状态
     * @param pojo 对象
     * @param state 状态
     */
	@Override
	public void setState(${table.entity} ${table.entity?uncap_first}, int state) {
		${table.entity?uncap_first}.setFlowState(state);
	}

    /**
     * 获得流程发起人
     *
     * @param ${table.entity?uncap_first} 流程对象
     * @return 发起人id
     */
	@Override
	public String getRegisterBy(${table.entity} ${table.entity?uncap_first}) {
		return ${table.entity?uncap_first}.getFlowRegisterBy();
	}

    /**
     * 获得下一步处理人
     * @param ${table.entity?uncap_first} 流程对象
     * @return 下一步处理人id
     */
	@Override
	public String getNextOperator(${table.entity} ${table.entity?uncap_first}) {
		return ${table.entity?uncap_first}.getFlowNextOperator();
	}

    /**
     * 设置下一步处理人员
     * @param ${table.entity?uncap_first} 对象
     * @param nextOperator 下一步处理人id
     */
	@Override
	public void setNextOperator(${table.entity} ${table.entity?uncap_first}, String nextOperator) {
		${table.entity?uncap_first}.setFlowNextOperator(nextOperator);
	}

    /**
     * 获得下一步处理人员的字典
     * @param ${table.entity?uncap_first} 对象
     * @param operation 流程操作,null表示编辑状态
     * @param selection 当前选择,null表示编辑状态
     * @return 下一步处理人员的字典
     */
	@Override
	public List<Map<String, Object>> getDictionaryOfNextOperator(${table.entity} ${table.entity?uncap_first}, String operation, String selection) {
		return SqlUtil.sqlQuery(false,"select id as value,real_name as text from sys_user where id = ?", ...);
	}

	/**
     * 获得默认下一步处理人员
     * @param ${table.entity?uncap_first} 对象
     * @param operation 流程操作,null表示编辑状态
     * @param selection 当前选择,null表示编辑状态
     * @return 默认下一步处理人员id
     */
	@Override
	public String defaultNextOperator(${table.entity} ${table.entity?uncap_first}, String operation, String selection) {
		return Util.getUser().getId();
	}

    /**
     * 获得对象的抄送对象列表
     * @param ${table.entity?uncap_first} 流程对象
     * @return 抄送人员id列表
     */
	@Override
	public String getCopyToList(${table.entity} ${table.entity?uncap_first}) {
		return ${table.entity?uncap_first}.getFlowCopyToList();
	}

    /**
     * 设置抄送人员清单
     * @param ${table.entity?uncap_first} 对象
     * @param copyToList 抄送名单
     */
	@Override
	public void setCopyToList(${table.entity} ${table.entity?uncap_first}, String copyToList) {
		${table.entity?uncap_first}.setFlowCopyToList(copyToList);
	}

    /**
     * 获得抄送人员可以选择的字典
     * @param ${table.entity?uncap_first} 对象
     * @param operation 流程操作,null表示编辑状态
     * @param selection 当前选择,null表示编辑状态
     * @return 抄送人员字典
     */
	@Override
	public List<Map<String, Object>> getDictionaryOfCopyToList(${table.entity} ${table.entity?uncap_first}, String operation, String selection) {
		return SqlUtil.sqlQuery(false,"select id as value,real_name as text from sys_user where id = ?", ...);
	}

    /**
     * 获得抄送人员默认值
     * @param ${table.entity?uncap_first} 对象
     * @param operation 流程操作
     * @param selection 当前选择,null表示编辑状态
     * @return 默认抄送人员
     */
	@Override
	public String defaultCopyToList(${table.entity} ${table.entity?uncap_first}, String operation, String selection) {
		return Util.getUser().getId();
	}

	/**
     * 状态迁移函数。
     *
     * @param ${table.entity?uncap_first}      操作的对象，对象不同，可能选择不一样
     * @param operation 操作代码，通常为表定义code
     * @param selection 操作选择
     * @return 新的状态
     * @throws IotequException 无法迁移，返回异常
     */
	@Override
	public int stateTransfer(${table.entity} ${table.entity?uncap_first}, String operation, String selection) throws IotequException {
		int state0=${table.entity?uncap_first}.getFlowState();
		int state=state0;
		switch (state0) {
			case 1:
				switch (operation) {
					<#list flowList as flow>
					case "${flow.path}" :
						if (APPROVE.equals(selection)) {
							state=...;
						} else if (DENY.equals(selection)) {
							state=...;
						} else if (REVIEW.equals(selection)) {
							state=...;
						}
						break;
					</#list>
				}
				break;
			default:
		}
		${table.entity?uncap_first}.setFlowState(state);
		return state;
	}

    /**
     * 检查参数设置是否正确，该函数在状态迁移后执行
     *
     * @param ${table.entity?uncap_first} 实体类对象
     * @throws IotequException 存在参数错误抛出异常
     */
	@Override
	public void checkParameters(${table.entity} ${table.entity?uncap_first}) throws IotequException {
	}
}