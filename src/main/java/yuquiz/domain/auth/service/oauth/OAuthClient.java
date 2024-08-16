package yuquiz.domain.auth.service.oauth;

import yuquiz.domain.auth.dto.OAuthCodeDto;
import yuquiz.domain.auth.dto.UserInfoDto;

public interface OAuthClient {

    String getAccessToken(OAuthCodeDto codeDto);

    UserInfoDto getUserInfo(String accessToken);

    boolean isExists(String email);
}
