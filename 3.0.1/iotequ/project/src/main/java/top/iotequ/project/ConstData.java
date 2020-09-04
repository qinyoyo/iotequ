package top.iotequ.project;

import top.iotequ.framework.bean.SpringContext;
import top.iotequ.framework.util.SqlUtil;
import top.iotequ.framework.util.Util;

public class ConstData {
	public static final int                    st_start = 1;
	public static final int                    st_assessed = 2;
	public static final int                    st_reviewed= 3;
	
	public static final int                    st_waitdecision = 4;
	public static final int         			st_waitPublish = 4;
	
	public static final int                    st_initialed = 5;
	public static final int                    st_develpoping = 6;
	public static final int                    st_testing = 7;
	public static final int                    st_published = 8;
	public static final int                    st_denied = 20;
	public static final int                    st_discard = 21;
	public static final String 				pm_project_state = "pm_project_state";
	public static final Integer []		 	pm_project_states = {st_start,st_assessed,st_reviewed,st_waitdecision,st_initialed,st_develpoping,st_testing,st_published,st_denied,st_discard};
	public static final String []	 		    pm_project_state_names = {"待审核","已评估","已评审","待决策","立项完成","研发中","测试中","已发布","否决","废弃"};
	
	public static final String 				pm_group_type = "pm_group_type";
	public static final String []		 	    pm_group_type_values = {"MARKET","CMO",       "R&D",        "PM",        "CTO",      "CEO"  ,"VM" };
	public static final String []	 		    pm_group_type_names = {"市场",       "市场总监",  "研发团队",   "项目主管","技术总监","决策","版本管理"   };
	
	
	public static final Integer [] 			pm_product_type_values = {1,2,3,4,5,6};
	public static final String []	 		    pm_product_type_names = {"新市场项目","客户维护项目","在研项目","预研项目","版本演进","已发布版本"};	

	public static final Integer [] 			pm_va_type_values = {1,2,3,4,5};
	public static final String []	 		    pm_va_type_names = {"合同项目","试验项目","工程演示","合作研发","其他"};	
	
	public static void initialDataDict() {
		Util.setDataDict(pm_group_type,pm_group_type_values,pm_group_type_names);
		Util.setDataDict("pm_product_type",pm_product_type_values,pm_product_type_names);
		Util.setDataDict("pm_va_type",pm_va_type_values,pm_va_type_names);
		SpringContext.getDictData();
	}
	public static boolean isUserInGroup(String userId,String group) {
		if (userId==null) userId=Util.getUser().getId();
		return SqlUtil.sqlExist(false, "select * from pm_people,pm_people_group where pm_people.group_id=pm_people_group.id and pm_people.user_id=? and pm_people_group.group_type = ?", userId,group);
	}
}
