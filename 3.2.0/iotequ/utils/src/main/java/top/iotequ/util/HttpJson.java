package top.iotequ.util;

import java.util.HashMap;
import java.util.Map;
public class HttpJson <C extends HttpJson> {
	public static final String K_SUCCESS = "success";
	public static final String K_ERROR = "error";
	public static final String K_FAILURE = "failure";
	public static final String K_MESSAGE = "message";
	public static final String K_DETAIL = "detailMessage";
	public static final String K_DATA = "data";
	public static final String K_DICTIONARY = "dictionary";
	public static final String K_PARAMETER = "parameter";
	

	protected Map<String, Object> attributes;
	public HttpJson() {
		attributes=new HashMap<>();
		attributes.put(K_SUCCESS, true);
		attributes.put(K_ERROR, null);  // error-code
		attributes.put(K_MESSAGE, K_SUCCESS);
		attributes.put(K_DETAIL, null);
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public  C setAttributes(Map<String, Object> attributes) {
		this.attributes.putAll(attributes);
		return (C)this;
	}
	public Object get(String key) {
		return attributes.get(key);
	}
	public C put(String key, Object value) {
		this.attributes.put(key, value);
		return (C)this;
	}
	private void mapAttribute(String attributeKey,Map<String, Object> value) {
		this.attributes.put(attributeKey, value);
	}
	@SuppressWarnings({ "serial", "unchecked" })
	private void mapAttribute(String attributeKey,String key,Object value) {
		Object data=this.attributes.get(attributeKey);
		if (data==null && value!=null) {
			this.attributes.put(attributeKey, new HashMap<String,Object>(){{
				put(key,value);
			}});
		} else if (data!=null) {
			if (data instanceof Map) ((Map<String, Object>)data).put(key,value);
			else {
				try {
					ObjectUtil.setPrivateField(data,key,value);
				} catch (Exception e) {
					Map<String,Object> map = ObjectUtil.mapFromEntity(data);
					map.put(key,value);
					this.attributes.put(attributeKey,map);
				}
			}
		}
	}
	public C data(Object value) {
		this.attributes.put(K_DATA,value);
		return (C)this;
	}
	public C data(String field, Object value) {
		this.mapAttribute(K_DATA,field,value);
		return (C)this;
	}
	public C dictionary(Map<String, Object> value) {
		mapAttribute(K_DICTIONARY,value);
		return (C)this;
	}
	public C dictionary(String key, Object value) {
		mapAttribute(K_DICTIONARY,key,value);
		return (C)this;
	}
	public C parameter(Map<String, Object> value) {
		mapAttribute(K_PARAMETER,value);
		return (C)this;
	}
	public C parameter(String key, Object value) {
		mapAttribute(K_PARAMETER,key,value);
		return (C)this;
	}
	public String getMessage() {
		return StringUtil.toString(attributes.get(K_MESSAGE));
	}
	public C setMessage(String message) {
		attributes.put(K_MESSAGE,  message);
		return (C)this;
	}

	public boolean isSuccess() {
		Object o=attributes.get(K_SUCCESS);
		if (o!=null) return (Boolean)o;
		else return false;
	}

	public C setSuccess(boolean success) {
		attributes.put(K_SUCCESS, success);
		attributes.put(K_MESSAGE, (success ? K_SUCCESS : K_FAILURE));
		return (C)this;
	}
	public C setError(String errorCode, String message, String detailMessage) {
		attributes.put(K_SUCCESS, false);
		attributes.put(K_ERROR, errorCode);
		attributes.put(K_MESSAGE, message);
		attributes.put(K_DETAIL, detailMessage);
		return (C)this;
	}
	public C setErrorCode(String errorCode, String message) {
		return setError(errorCode,message,null);
	}
	public C setError(String errorCode, Exception e) {
		attributes.put(K_SUCCESS, false);
		attributes.put(K_ERROR, errorCode);
		attributes.put(K_MESSAGE,e.getMessage());
		attributes.put(K_DETAIL,StringUtil.printStackTrace(e));
		return (C)this;
	}
	public C setError(Exception e) {
		return setMessage(e);
	}
	public C setMessage(Exception e) {
		if (e==null) return (C)this;
		String detail = StringUtil.printStackTrace(e);
		attributes.put(K_DETAIL,detail);
		setSuccess(false);
		attributes.put(K_MESSAGE, e.getMessage());
		return (C)this;
	}
}
