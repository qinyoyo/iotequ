package top.iotequ.oauth2.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import top.iotequ.oauth2.security.authentication.CustomAuthenticationProvider;
import top.iotequ.oauth2.security.authority.CustomFilterInvocationSecurityMetadataSource;
import top.iotequ.oauth2.security.authority.UrlAccessDecisionManager;
import top.iotequ.oauth2.security.oauth2.OAuth2Util;
import top.iotequ.oauth2.security.service.SecurityService;


@Configuration
@ComponentScan(basePackages = {"top.iotequ"})
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    TokenStore tokenStore;
    public static final String loginPage = "/login";
    public static final String resourcePage = "/res";
    public static final String commonPage = "/common";
    public static final String errorPage = "/error";
    public static final String loginProcessingUrl = "/login/login";
    // public static final String loginErrorUrl = "/login/error";
    // 默认值 public static final String logoutUrl = "/logout";
    public static final String[] whiteList = new String[]
            {
                "/index.html", "/favicon.ico", "/dashboard", "/static/**",   // allow vue inside
                "/","/m/**",
                errorPage + "/**",
                resourcePage + "/**",
                commonPage + "/**"
            };
    public static final String[] loginList = new String[]{"/oauth/**", loginPage + "/**", "/log*", "/"}; // 登录需要的白名单，不能放到whiteList里，由于绕过了security，认证登录等无法实现

    private static final Logger log = LoggerFactory.getLogger(SpringSecurityConfig.class);

    @Autowired
    private SecurityService userDetailsService;

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    public SpringSecurityConfig() {
        super();
    }

    private static boolean matchWhiteList(String value, String[] wl) {
        for (String s : wl) {
            if (s.endsWith("**")) {
                s = s.replace("**", "");
                if (value.startsWith(s)) {
                    return true;
                }
            } else if (s.endsWith("*")) {
                s = s.replace("*", "");
                if (value.startsWith(s)) {
                    String s1 = value.substring(s.length());
                    if (s1 == null || s1.length() == 0 || s1.indexOf("/") < 0) {
                        return true;
                    }
                }
            } else if (s.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean matchWhiteList(String value) {
        if (matchWhiteList(value, whiteList)) {
            return true;
        } else {
            return matchWhiteList(value, loginList);
        }
    }

    PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return OAuth2Util.encodePassword((String) rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(OAuth2Util.encodePassword((String) rawPassword));
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(whiteList);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authProvider())
                .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers(loginList).permitAll().and()
                .authorizeRequests().antMatchers("/oauth/**").permitAll().anyRequest().authenticated().and()
                .authorizeRequests()
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                            fsi.setSecurityMetadataSource(new CustomFilterInvocationSecurityMetadataSource().tokenStore(tokenStore));
                            fsi.setAccessDecisionManager(new UrlAccessDecisionManager());
                            return fsi;
                        }
                    })
                    .accessDecisionManager(new UrlAccessDecisionManager())
        ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}