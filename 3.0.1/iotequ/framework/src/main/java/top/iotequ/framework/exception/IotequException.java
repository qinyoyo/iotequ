package top.iotequ.framework.exception;

import org.springframework.dao.DataAccessException;
import top.iotequ.util.CommonUtil;
import top.iotequ.util.SqlUtil;

public class IotequException extends Exception implements IotequThrowable {
	private static final long serialVersionUID = 1L;
	private String error;
	private String addtionalMessage;
	@Override
	public String getError() {
		return error;
	}
	@Override
	public String getAddtionalMessage() { return addtionalMessage; }

	public IotequException(String error, String msg) {
		super(IotequThrowable.mergeMessage(error,msg));
		this.error=error;
		this.addtionalMessage=msg;
	}
	public static IotequException newInstance(Exception e) {
		if (e instanceof IotequException) return (IotequException)e;
		else {
			if (e instanceof DataAccessException) return SqlUtil.changeException((DataAccessException)e);
			String msg=null;
			String clazz=e.getClass().getSimpleName();
			if (e.getCause()!=null ) msg=e.getCause().getMessage();
			if (CommonUtil.isEmpty(msg)) msg=e.getLocalizedMessage();
			return new IotequException("exception."+clazz,msg);
		}
	}

	@Override
	public String getMessage() {
		return IotequThrowable.mergeMessage(error,addtionalMessage);
	}

	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}
}
