package yuquiz.domain.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth2.naver")
@Getter
@Setter
public class NaverConfig {

    private String tokenUri;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String getUserInfoUri;
}
