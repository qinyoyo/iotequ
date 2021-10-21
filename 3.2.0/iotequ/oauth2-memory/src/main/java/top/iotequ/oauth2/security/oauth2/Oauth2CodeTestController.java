package top.iotequ.oauth2.security.oauth2;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Oauth2CodeTestController {
    @RequestMapping(value="/res/code")
    Map<String,Object> codeTest(HttpServletRequest request,String code,String state) {
		Map<String,Object> map=new HashMap<>();
		map.put("code",code);
		map.put("state",state);
		return map;
    }

    @RequestMapping(value="/res/oauth2/url")
    String testClient(HttpServletRequest request,String type,String secret, String clientId, String scope, String state, String redirectUri) throws Exception {
    	if (type==null) type="";
    	type=type.toLowerCase().trim();
		String host=OAuth2Util.getHost(request);
    	String url="/res/oauth2/url?type=[client|code|password|implicit]<&secret=123456><&clientId=svas><&scope=api><&redirectUri=http://www.baidu.com>";
		clientId = OAuth2Util.isEmpty(clientId)?"svas":clientId;
		String pass = OAuth2Util.isEmpty(secret)?"123456":secret;
		secret = OAuth2Util.isEmpty(secret)?"e10adc3949ba59abbe56e057f20f883e":OAuth2Util.encodePassword(secret);
		scope = OAuth2Util.isEmpty(scope)?"api":scope;
		state = OAuth2Util.isEmpty(state)?"req":state;
		redirectUri = OAuth2Util.isEmpty(redirectUri)?"http://www.baidu.com":redirectUri;
    	if (type.equals("client"))
    		url=OAuth2Util.getClientCredentialsTokenUrl(host,clientId ,secret,scope);
    	else if (type.equals("code")) {
			url = OAuth2Util.getAuthorizeCodeUrl(host, clientId, redirectUri, scope, state);
			String url2 = OAuth2Util.getTokenByCodeUrl(host,clientId,secret,"RECEIVED_CODE",redirectUri,state);
			url="{" +"\"code_url\":"+"\""+url+"\"" + ",\"token_url\":"+"\""+url2+"\"" +
					"}";
		}
    	else if (type.equals("password"))
    		url=OAuth2Util.getPasswordTokenUrl(host,"user",pass,clientId, secret, scope);
    	else if (type.equals("implicit"))
    		url=OAuth2Util.getImplicitTokenUrl(host,clientId, secret, scope,redirectUri);
    	return url;
    }
}
