package top.iotequ.oauth2.security.authority;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import top.iotequ.oauth2.security.oauth2.OAuth2Util;

import java.util.Collection;

public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private TokenStore tokenStore=null;
	public CustomFilterInvocationSecurityMetadataSource tokenStore(TokenStore tokenStore) {
		this.tokenStore=tokenStore;
		return this;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		@SuppressWarnings("unused")
		FilterInvocation fi = (FilterInvocation) object;
		Collection<ConfigAttribute> collection = OAuth2Util.getAttributes(fi.getHttpRequest(),tokenStore);
		if (collection==null || collection.isEmpty()) throw new IllegalArgumentException("NO_AUTHORITY");
		return collection;
	}


	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}