package top.iotequ.framework.security.oauth2;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.GsonBuilder;
import org.springframework.lang.NonNull;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class OAuth2Util {
	public static final String HTTP_HEADER_KEY = "Authorization";

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

	public static String getTokenByCodeUrl(@NonNull String host,@NonNull String clientId,@NonNull String clientSecret,@NonNull String code,@NonNull String redirectUrl) {
		if (!host.endsWith("/")) host=host+"/";
		return host+String.format("oauth/token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s",
				code,clientId,clientSecret,redirectUrl);
	}

	public static Collection<ConfigAttribute> getAttributes(HttpServletRequest request,
			TokenStore tokenStore) throws IllegalArgumentException {
		if (tokenStore==null) throw  new IllegalArgumentException("Token store为空，请检查oauth2配置");
		if (request!=null) {
			String tokenHeader=request.getHeader(HTTP_HEADER_KEY);
		    if (tokenHeader==null) tokenHeader=request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
			if (tokenHeader==null || tokenHeader.isEmpty())  return null;
			else {
				String[] tokenParams = tokenHeader.split(" ");
				String token = tokenParams[tokenParams.length-1];
				OAuth2AccessToken ac=tokenStore.readAccessToken(token);
				if (ac==null) throw new IllegalArgumentException("INVALID_TOKEN");
				if (ac.isExpired()) throw new IllegalArgumentException("TOKEN_EXPIRED");
				OAuth2Authentication auth = tokenStore.readAuthentication(token);
				if (auth!=null) {
					Collection<ConfigAttribute> collection = new ArrayList<>();
					collection.add(new ConfigAttribute() {
						@Override
						public String getAttribute() {
							return "svas";
						}
					});
					return collection;
				} else {
					throw  new IllegalArgumentException("INVALID_TOKEN");
				}
			}
		} else return null;
	}

	public static OAuth2AccessToken getOAuth2AccessToken(String json) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> result = new GsonBuilder()
					.setLenient()
					.setDateFormat("yyyy-MM-dd HH:mm:ss")
					.create()
					.fromJson(json, Map.class);
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
