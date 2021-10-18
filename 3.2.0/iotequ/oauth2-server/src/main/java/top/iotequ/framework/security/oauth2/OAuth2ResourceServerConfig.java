package top.iotequ.framework.security.oauth2;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


//@Configuration
//@EnableResourceServer
public class OAuth2ResourceServerConfig implements ResourceServerConfigurer  {
	 @Override
	    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		 	resources
		 		.resourceId("resource_iotequ")
		 	;
	    }

	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	    	http
	            .authorizeRequests().anyRequest().authenticated()
	        ;
	    }

}
