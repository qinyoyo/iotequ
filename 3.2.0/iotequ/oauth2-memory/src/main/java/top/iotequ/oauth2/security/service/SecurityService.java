package top.iotequ.oauth2.security.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.*;

@Service("userDetailsService")
public class SecurityService implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String userName) { // 重写loadUserByUsername 方法获得 userdetails 类型用户
		return new UserDetails() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}

			@Override
			public String getPassword() {
				return "123456";
			}

			@Override
			public String getUsername() {
				return userName;
			}

			@Override
			public boolean isAccountNonExpired() {
				return true;
			}

			@Override
			public boolean isAccountNonLocked() {
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}

			@Override
			public boolean isEnabled() {
				return true;
			}
		};
	}

}