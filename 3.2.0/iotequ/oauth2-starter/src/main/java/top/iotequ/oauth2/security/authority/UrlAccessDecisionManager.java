package top.iotequ.oauth2.security.authority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Service
public class UrlAccessDecisionManager implements AccessDecisionManager {
	private static final Logger log = LoggerFactory.getLogger(UrlAccessDecisionManager.class);
	public static final String FORBIDDEN_URL = "FORBIDDEN_URL";
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
	            throws AccessDeniedException, InsufficientAuthenticationException {
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		String url = request.getServletPath();
		log.debug("url={} , roles={}",url,configAttributes);
		if (url.toLowerCase().startsWith("/oauth/")) return;
		if (configAttributes!=null && configAttributes.size()>0) {
			for (ConfigAttribute role: configAttributes) {
				if (url.startsWith("/"+role.getAttribute()+"/")) return;
			}
		}
		throw new AccessDeniedException("ACCESS_DENIED<"+url+">");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return  clazz.equals(FilterInvocation.class);
	}
}
