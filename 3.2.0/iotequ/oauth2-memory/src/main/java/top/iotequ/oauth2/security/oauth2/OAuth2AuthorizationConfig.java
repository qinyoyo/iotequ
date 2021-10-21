package top.iotequ.oauth2.security.oauth2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import top.iotequ.oauth2.security.service.SecurityService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Order(6)   // // 重要，与 oauth2 config
@Configuration
@EnableAuthorizationServer()
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    SecurityService userDetailsService;

    @Autowired
    Environment env;
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

   @Override public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        Integer total = env.getProperty("oauth2.totalClients",Integer.class);
        if (total==null || total==0) return;
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        for (int i=0;i<total;i++) {
            String keyPre = "oauth2.client"+i+".";
            String id = env.getProperty(keyPre+"clientId");
            String[] types = env.getProperty(keyPre+"grantTypes").split(",");
            Integer validate = env.getProperty(keyPre+"tokenValiditySeconds",Integer.class);
            Integer refresh = env.getProperty(keyPre+"refreshTokenValiditySeconds",Integer.class);
            String[] authorities = env.getProperty(keyPre+"authorities").split(",");
            for (int s=0;s<authorities.length;s++) authorities[s] = "ROLE_"+authorities[s];
            String[] scopes = env.getProperty(keyPre+"scopes").split(",");
            String secret = env.getProperty(keyPre+"secret");
            String redirectUris = env.getProperty(keyPre+"redirectUris");

            builder.withClient(id)
                    .authorizedGrantTypes(types)
                    .accessTokenValiditySeconds(validate==null?86400:validate)
                    .refreshTokenValiditySeconds(refresh==null?600:refresh)
                    .authorities(authorities)
                    .scopes(scopes)
                    .secret(secret)  // 这里不能加密
                    .autoApprove(true)
                    .redirectUris(redirectUris)
                    ;
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .allowFormAuthenticationForClients()
                .passwordEncoder(new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence rawPassword) {
                        return OAuth2Util.encodePassword(String.valueOf(rawPassword));
                    }

                    @Override
                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
                        return encode(encodedPassword).equals(String.valueOf(rawPassword));
                    }
                })
        ;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .authenticationManager(authenticationManager)
            .tokenStore(tokenStore())
            .userDetailsService(userDetailsService)
            .reuseRefreshTokens(false);
    }


}
