package top.iotequ.oauth2.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
//@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return new AuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}