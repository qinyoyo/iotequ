package top.iotequ.oauth2.security;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
	private static final String ERROR_CLASS="error_class";
	private static final String ERROR_DESCRIPTION="error_description";
	private static final String ERROR_CODE="error";
	private static final String MESSAGE="message";
	private static final String TITLE="title";
	private static final String SUCCESS="success";

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest,boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
		Throwable exception = getError(webRequest);
		if (exception!=null) 
			 errorAttributes.put(ERROR_CLASS, exception.getClass().getName());
		 if (errorAttributes!=null) {
			 int code=200;
			 errorAttributes.remove("trace");
			 Object o=errorAttributes.get("status");
			 if (o!=null) {
				 try { 
					code=Integer.parseInt(o.toString()) ; 
					if (code == 500) {
						errorAttributes.put(TITLE, "exception");
						if (exception instanceof InvalidClientException) {
							errorAttributes.put(ERROR_DESCRIPTION, "client不存在或鉴权失败");
							errorAttributes.put(ERROR_CODE, OAuth2Exception.INVALID_CLIENT);
						} else if (exception instanceof OAuth2Exception) {
							errorAttributes.put(ERROR_CODE, ((OAuth2Exception) exception).getOAuth2ErrorCode());
							if (errorAttributes.get(ERROR_DESCRIPTION)==null)
								errorAttributes.put(ERROR_DESCRIPTION, exception.getMessage());
						} else if (exception instanceof IllegalArgumentException) {
							String msg = exception.getMessage();
							Pattern p = Pattern.compile("^<(.*)>");
							Matcher m = p.matcher(msg);
							if (m.find()) {
								errorAttributes.put(ERROR_CODE, m.group(1));
								errorAttributes.put(ERROR_DESCRIPTION, msg.substring(m.end()));
							} else errorAttributes.put(ERROR_DESCRIPTION, msg);
						}
						else {
							if (errorAttributes.get(ERROR_CODE)==null)
								errorAttributes.put(ERROR_CODE, "ERROR");
							if (exception!=null && errorAttributes.get(ERROR_DESCRIPTION)==null) {

								errorAttributes.put(ERROR_DESCRIPTION, exception.getMessage());
							}
						}
					}
					else if (code == 404) {
						errorAttributes.put(MESSAGE, "404");
						errorAttributes.put(ERROR_CODE,"ERROR_404");
					}
					else if (code == 403) {
						errorAttributes.put(MESSAGE,  "403");
						errorAttributes.put(ERROR_CODE,OAuth2Exception.ACCESS_DENIED);
					}
				 }  catch(Exception e) {
						errorAttributes.put(ERROR_CODE,"ERROR");
				 };
			 }
			errorAttributes.put(SUCCESS, false);
		 }
		 return errorAttributes;
	 };
}
