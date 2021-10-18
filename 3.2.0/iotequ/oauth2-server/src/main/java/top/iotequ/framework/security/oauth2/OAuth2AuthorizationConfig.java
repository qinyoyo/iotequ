package top.iotequ.framework.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    Environment env;
    @Bean
    public TokenStore iotequTokenStore() {
        return new InMemoryTokenStore();
    }


    public static String encodePassword(String password) {
        if (password==null||password.isEmpty())
            return "";
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new ClientDetailsService() {
            @Override
            public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                return new ClientDetails() {
                    @Override
                    public String getClientId() {
                        return clientId;
                    }

                    @Override
                    public Set<String> getResourceIds() {
                        return null;
                    }

                    @Override
                    public boolean isSecretRequired() {
                        return false;
                    }

                    @Override
                    public String getClientSecret() {
                        return null;
                    }

                    @Override
                    public boolean isScoped() {
                        return true;
                    }

                    @Override
                    public Set<String> getScope() {
                        return new HashSet<String>(){{ add("api");}};
                    }

                    @Override
                    public Set<String> getAuthorizedGrantTypes() {
                        return new HashSet<String>(){{ add("client_credentials");}};
                    }

                    @Override
                    public Set<String> getRegisteredRedirectUri() {
                        return null;
                    }

                    @Override
                    public Collection<GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public Integer getAccessTokenValiditySeconds() {
                        return 86400;
                    }

                    @Override
                    public Integer getRefreshTokenValiditySeconds() {
                        return 600;
                    }

                    @Override
                    public boolean isAutoApprove(String scope) {
                        return true;
                    }

                    @Override
                    public Map<String, Object> getAdditionalInformation() {
                        return null;
                    }
                };
            }
        });
    }
}
