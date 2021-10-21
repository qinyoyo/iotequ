package top.iotequ.oauth2.security.oauth2;


import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class Oauth2CodeTestController {
    @RequestMapping(value="/res/code")
    String codeTest(HttpServletRequest request,String code,String state) throws Exception {
        if (code!=null) {
        	System.out.printf("code=%s, state=%s\n",code,state);
            String url = OAuth2Util.getTokenByCodeUrl("https://localhost", "pay",
                    "e10adc3949ba59abbe56e057f20f883e",
                    code, "https://localhost/res/code",state);
            System.out.println(url);
            return url;
        } else{
        	System.out.println("code=null");
            return null;
        }
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
		state = OAuth2Util.isEmpty(state)?"abcd":state;
		redirectUri = OAuth2Util.isEmpty(redirectUri)?"http://www.sevin.com.cn/res/code":redirectUri;
    	if (type.equals("client"))
    		url=OAuth2Util.getClientCredentialsTokenUrl(host,clientId ,secret,scope);
    	else if (type.equals("code")) {
			url = OAuth2Util.getAuthorizeCodeUrl(host, clientId, host + "/res/code", scope, state);
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
