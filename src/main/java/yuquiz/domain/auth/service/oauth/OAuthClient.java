package yuquiz.domain.auth.service;

import yuquiz.domain.auth.dto.OauthCodeDto;
import yuquiz.domain.auth.dto.UserInfoDto;

public interface OAuthClient {

    String getAccessToken(OauthCodeDto codeDto);

    UserInfoDto getUserInfo(String accessToken);

    boolean isExists(String email);
}
