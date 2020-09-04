package top.iotequ.svasclient;

import top.iotequ.framework.exception.IotequException;

public class SvasException extends IotequException {
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS  = 0;
	public static final int ERR_NO_MEMERY	=		1;
	public static final int ERR_TEMPLATE_FORMAT =	2;
	public static final int ERR_TEMPLATE_LENGTH	=	3;
	public static final int ERR_NOT_FOUND	=		4;
	public static final int ERR_TEMPLATE_EXISTS	=	5;
	public static final int ERR_MULTI_FOUND	=		6;
	public static final int ERR_TIMEOUT		=		7;
	public static final int ERR_MUTEX			=	8;
	public static final int ERR_PARAMETER		=	9;
	public static final int ERR_THREAD_START	 =	10 ;
	public static final int  ERR_NOT_CHANGED  =	11;
	public static final int  ERR_LOW_TEMPLATES  =		12;
	public static final int   ERR_NOT_ACTIVITED	  =  13;
	public static final int   ERR_LICENCE_OUT		=	14;
	public static final int   ERR_CONFIG_LOST		=	15;
	public static final int  ERR_NAME_MISMATCH      = 16 ;
	public static final int TRIAL_EXPIRED		=	99;	
	public static final int ERR_DB_NOT_CONNECTED =	101;
	public static final int ERR_NULL_IDNO		=	102;

	static String  getError(int errorno) {
		String err;
		switch (errorno) {
			case ERR_NO_MEMERY : err = "内存申请错误"; break;
			case ERR_TEMPLATE_FORMAT : err = "指静脉数据格式错误"; break;
			case ERR_TEMPLATE_LENGTH : err = "指静脉数据长度不正确"; break;
			case ERR_NOT_FOUND : err = "未找到匹配数据"; break;
			case ERR_TEMPLATE_EXISTS : err = "指静脉数据已经存在"; break;
			case ERR_MULTI_FOUND : err = "匹配到多个值"; break;
			case ERR_TIMEOUT : err = "请求超时"; break;
			case ERR_MUTEX : err = "数据锁失败"; break;
			case ERR_PARAMETER : err = "参数不全或不正确"; break;
			case ERR_THREAD_START: err = "线程启动失败"; break;
			case  ERR_NOT_CHANGED : err = "没有任何更改"; break;
			case  ERR_LOW_TEMPLATES: err = "模板之间不匹配"; break;
			case  ERR_NOT_ACTIVITED : err = "svas没有正确激活"; break;
			case  ERR_LICENCE_OUT: err = "Licence许可数量用完"; break;			
			case ERR_DB_NOT_CONNECTED : err = "数据库未连接"; break;
			case ERR_NULL_IDNO : err = "错误的空编号"; break;
			case ERR_CONFIG_LOST : err= "没有找到 svas.ini 或无权限" ;break;
			case TRIAL_EXPIRED : err= "软件已过试用期" ;break;
			case ERR_NAME_MISMATCH: err="证件名称不匹配"; break;
			default : if (errorno>=1000) err="Mysql数据操作错误"; else err="其他错误";
		};
		return String.format("Error %d : %s", errorno,err);
	}
	public SvasException(int errorno) {
		super("svas_error_"+errorno,getError(errorno));
	}
}
