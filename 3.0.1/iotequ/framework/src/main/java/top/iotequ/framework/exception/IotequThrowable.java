package top.iotequ.framework.exception;

import top.iotequ.util.Utils;

public interface IotequThrowable {
	public static final String VERSION_EXPIRED = "version_expired";
	public static final String VERSION_NOT_MATCHED = "version_not_matched";
	public static final String NO_ANSWER = "no_answer";
	public static final String INVALID_OPERATION = "invalid_operation";
	public static final String FAILURE = "failure";
	public static final String UNKNOWN = "unknown";
	public static final String NOT_FOUND = "not_found";

	public static final String NO_ENOUGH_LICENCE = "no_enough_licence";
	public static final String INVALID_VERIFICATION_CODE = "invalid_verification_code";
	public static final String USER_NOT_EXIST="user_not_exist";
	public static final String USER_EXPIRED="user_expired";
	public static final String USER_LOCKED="user_locked";
	public static final String USER_DISABLED="user_disabled";
	public static final String PASSWORD_MISS="password_missing";
	public static final String PASSWORD_EXPIRED="password_expired";
	public static final String LOGIN_REFUSED="login_refused";
	public static final String NOT_LOGIN="not_login";
	public static final String SESSION_TIMEOUT="session_timeout";

	public static final String ALI_SMS_ERROR="aliyun_sms_error";
	public static final String SMS_SERVICE_MISS="sms_service_missing";
	public static final String SMS_TOO_FREQUENTLY="sms_too_frequently";
	public static final String MOBILE_REGISTERED="mobile_registered";
	public static final String MOBILE_REFUSED="mobile_refused";

	public static final String WX_CLIENT_INFO_MISS="wechat_client_info_missing";
	public static final String WX_TOKEN_FAILURE="wechat_token_failure";
	public static final String WX_API_ERROR="wechat_api_error";
	
	public static final String IO_FORMATTER_ERROR="invalid_formatter";
	public static final String IO_OBJECT_NOT_EXIST="object_not_exist";
	public static final String IO_EXCEPTION="io_exception";
	
	public static final String CONFIG_ERROR="config_error";

	public static final String DB_ERROR="database_error";
	public static final String DB_ERROR_DUPLICATE_ENTRY="database_error_duplicate_entry";
	public static final String DB_ERROR_NULL="database_error_null";
	public static final String DB_ERROR_TOO_LONG="database_error_too_long";
	public static final String DB_ERROR_ACCESS="database_error_access";

	public static final String REFLECTION_ERROR="reflection_error";
	public static final String NULL_OBJECT="null_object";
	public static final String PARAMETER_ERROR="invalid_parameter";
	public static final String PARENT_CIRCULAR_REFERENCE="parent_circular_reference";

	public static final String ERROR_404 = "resource_not_found";
	public static final String EXCEPTION = "Internal Server Error";
	public static final String ERROR = "error"; //OAuth2Exception.ERROR;
	public static final String DESCRIPTION = "error_description"; //OAuth2Exception.DESCRIPTION;
	public static final String URI = "error_uri"; //OAuth2Exception.URI;
	public static final String INVALID_REQUEST = "invalid_request"; //OAuth2Exception.INVALID_REQUEST;
	public static final String INVALID_CLIENT = "invalid_client"; //OAuth2Exception.INVALID_CLIENT;
	public static final String INVALID_GRANT = "invalid_grant"; // OAuth2Exception.INVALID_GRANT;
	public static final String UNAUTHORIZED_CLIENT = "unauthorized_client"; // OAuth2Exception.UNAUTHORIZED_CLIENT;
	public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type"; // OAuth2Exception.UNSUPPORTED_GRANT_TYPE;
	public static final String INVALID_SCOPE = "invalid_scope"; // OAuth2Exception.INVALID_SCOPE;
	public static final String INSUFFICIENT_SCOPE = "insufficient_scope"; // OAuth2Exception.INSUFFICIENT_SCOPE;
	public static final String INVALID_TOKEN = "invalid_token"; // OAuth2Exception.INVALID_TOKEN;
	public static final String REDIRECT_URI_MISMATCH = "redirect_uri_mismatch"; // OAuth2Exception.REDIRECT_URI_MISMATCH;
	public static final String UNSUPPORTED_RESPONSE_TYPE =  "unsupported_response_type"; // OAuth2Exception.UNSUPPORTED_RESPONSE_TYPE;
	public static final String ACCESS_DENIED= "access_denied"; // OAuth2Exception.ACCESS_DENIED;
	public static final String NO_AUTHORITY= "no_authority";
	public static final String TOKEN_EXPIRED="token_expired";

	public static final String FLOW_ERROR_STATE="flow_error_state";
	public static final String FLOW_ERROR_OPRATION="flow_error_operation";

	static String mergeMessage(String code, String addtional) {
		return code + (Utils.isEmpty(addtional)?"":"("+addtional+")");
	}
	String getError();
	String getAddtionalMessage();
}
