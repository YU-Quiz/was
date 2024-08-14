package yuquiz.domain.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth2.kakao")
@Getter @Setter
public class KakaoConfig {

    private String tokenUri;
    private String clientId;
    private String redirectUri;
    private String getUserInfoUri;
}
