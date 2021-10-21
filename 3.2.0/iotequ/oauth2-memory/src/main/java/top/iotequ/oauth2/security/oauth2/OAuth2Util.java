package top.iotequ.oauth2.security.oauth2;

import org.springframework.lang.NonNull;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;


import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OAuth2Util {
	public static final String HTTP_HEADER_KEY = "Authorization";
	public static String  toString(Object o) {
		if (o==null) return null;
		else return o.toString();
	}
	public static String getHost(HttpServletRequest request) {
		StringBuffer surl = request.getRequestURL();
		String suri = request.getRequestURI();
		return surl.substring(0,surl.length()-suri.length()+1);
	}
	public static boolean isEmpty(Object o) {
		return (o==null || o.toString().isEmpty());
	}
	public static String encodePassword(String password) {
		if (password==null || password.isEmpty())
			return password;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		char[] charArray = password.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	public static String getPasswordTokenUrl(@NonNull String host,@NonNull String userName,@NonNull String password,
			@NonNull String clientId,@NonNull String clientSecret,@NonNull String scope) {
		if (!host.endsWith("/")) host=host+"/";
		return host+String.format("oauth/token?username=%s&password=%s&grant_type=password&client_id=%s&client_secret=%s&scope=%s", 
				userName,password,clientId,clientSecret,scope);
	}	
	
	public static String getImplicitTokenUrl(@NonNull String host,@NonNull String clientId,@NonNull String clientSecret,@NonNull String scope,@NonNull String redirectUrl) {
		if (!host.endsWith("/")) host=host+"/";
		return host+String.format("oauth/token?grant_type=implicit&client_id=%s&client_secret=%s&scope=%s&redirect_uri=%s", 
				clientId,clientSecret,scope,redirectUrl);
	}	
	
	public static String getClientCredentialsTokenUrl(@NonNull String host,@NonNull String clientId,@NonNull String clientSecret,@NonNull String scope) {
		if (!host.endsWith("/")) host=host+"/";
		return host+String.format("oauth/token?grant_type=client_credentials&client_id=%s&client_secret=%s&scope=%s", clientId,clientSecret,scope);
	}

	public static String getAuthorizeCodeUrl(@NonNull String host,@NonNull String clientId,@NonNull String redirectUrl,@NonNull String scope,String state) {
		if (!host.endsWith("/")) host=host+"/";
		return host+String.format("oauth/authorize?client_id=%s&response_type=code&scope=%s&redirect_uri=%s%s",
				clientId,scope,redirectUrl,(state==null?"":"&state="+state));
	}

	public static String getTokenByCodeUrl(@NonNull String host,@NonNull String clientId,@NonNull String clientSecret,@NonNull String code,@NonNull String redirectUrl, String state) {
		if (!host.endsWith("/")) host=host+"/";
		String url = host+String.format("oauth/token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s",
				code,clientId,clientSecret,redirectUrl);
		return isEmpty(state)?url:url+"&state="+state;
	}

	public static Collection<ConfigAttribute> getAttributes(HttpServletRequest request,
			TokenStore tokenStore) throws IllegalArgumentException {
		if (tokenStore==null) throw  new IllegalArgumentException("<"+ OAuth2Exception.ERROR +">");
		if (request!=null) {
			String tokenHeader=request.getHeader(HTTP_HEADER_KEY);
		    if (tokenHeader==null) tokenHeader=request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
			if (tokenHeader==null || tokenHeader.isEmpty())  return null;
			else {
				String[] tokenParams = tokenHeader.split(" ");
				String token = tokenParams[tokenParams.length-1];
				OAuth2AccessToken ac=tokenStore.readAccessToken(token);
				if (ac==null) throw new IllegalArgumentException("<"+ OAuth2Exception.INVALID_TOKEN +">");
				if (ac.isExpired()) throw new IllegalArgumentException("<token_expired>");
				OAuth2Authentication auth = tokenStore.readAuthentication(token);
				if (auth!=null) {
					Collection<ConfigAttribute> collection = new ArrayList<>();
					for ( GrantedAuthority a : auth.getAuthorities()) {
						String role = a.getAuthority().substring(5);
						collection.add(new ConfigAttribute() {
							@Override
							public String getAttribute() {
								return role;
							}
						});
					}
					return collection;
				} else {
					throw  new IllegalArgumentException("<"+ OAuth2Exception.INVALID_TOKEN +">");
				}
			}
		} else return null;
	}

	public static OAuth2AccessToken getOAuth2AccessToken(Map<String, String> result) {
		try {
			if (result!=null) return DefaultOAuth2AccessToken.valueOf(result);
			else return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static Map<String,Object> getTokenHeader(OAuth2AccessToken token) {
		if (token==null) return null;
		String tokenString = token.getValue();
		String tokenType=token.getTokenType();
		if (tokenType!=null && !tokenType.isEmpty()) tokenString=tokenType+" "+tokenString;
		Map<String,Object> map = new HashMap<>();
		map.put(HTTP_HEADER_KEY,tokenString);
		return map;
	}

}
