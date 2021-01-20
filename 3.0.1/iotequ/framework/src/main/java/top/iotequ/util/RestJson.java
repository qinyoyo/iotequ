package top.iotequ.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.AuthenticationException;
import top.iotequ.framework.exception.IotequAccessDeniedException;
import top.iotequ.framework.exception.IotequDatabaseException;
import top.iotequ.framework.exception.IotequException;
import top.iotequ.framework.exception.IotequThrowable;

import javax.servlet.http.HttpServletResponse;

public class RestJson extends HttpJson <RestJson> {
	private boolean downloadFileMode = false;

    @Override
    public RestJson setMessage(Exception e) {
		if (e==null) return this;
		if (e instanceof AuthenticationException) {
			String url= Util.getRequest()==null ? null : Util.getRequest().getServletPath() ;
			e= new IotequAccessDeniedException(IotequThrowable.ACCESS_DENIED,url);
		}
		if (Util.runInIdeMode || !(e instanceof IotequThrowable)
		   || (e instanceof IotequThrowable && ((IotequThrowable) e).getError().equals(IotequThrowable.EXCEPTION)))
			e.printStackTrace();
		String detail = StringUtil.printStackTrace(e);
		if (e instanceof IotequDatabaseException) {
			String sql = ((IotequDatabaseException)e).getSql();
			if (!Util.isEmpty(sql)) detail = sql + "\n" +detail;
		}
		attributes.put(K_DETAIL,detail);
		setSuccess(false);
		if (e instanceof IotequThrowable) {
			attributes.put(K_ERROR, ((IotequThrowable)e).getError());
			attributes.put(K_MESSAGE, ((IotequThrowable)e).getAddtionalMessage());
		} else {
			IotequException ne=IotequException.newInstance(e);
			attributes.put(K_ERROR, ne.getError());
			attributes.put(K_MESSAGE, ne.getAddtionalMessage());
		}
		return this;
	}

	public ResponseEntity<Map<String, Object>> toResponse(int status) {
		if (downloadFileMode) return null;
		HttpStatus httpStatus = HttpStatus.valueOf(status);
		return new ResponseEntity<>(attributes, httpStatus);
	}
	public ResponseEntity<Map<String, Object>> toResponse() {
		if (downloadFileMode) return null;
		return new ResponseEntity<>(attributes, HttpStatus.OK);
	}
	@Override
	public String toString(){
		Gson gson = Util.getGson();
		String s=gson.toJson(this.attributes);
		return s;
	}
	@Override
	public RestJson setError(String errorCode, Exception e) {
		super.setError(errorCode,e);
		if (Util.runInIdeMode) e.printStackTrace();
		return this;
	}
	public OutputStream getDownloadOutputStream(HttpServletResponse response, String mediaType, String fileName) {
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
		} catch (Exception e) {
			setMessage(e);
			return null;
		}
		response.setContentType(mediaType);
		downloadFileMode = true;
		try {
			return response.getOutputStream();
		} catch (IOException e) {
			downloadFileMode = false;
			setMessage(e);
			return null;
		}
	}
	public void sendTo(HttpServletResponse response) {
		response.setContentType("text/json; charset=UTF-8");
		try {
			String s=toString();
			response.getWriter().print(s);
			response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				response.getWriter().close();
			} catch (IOException e) {
			}
		}

	}
}
