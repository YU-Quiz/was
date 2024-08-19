package yuquiz.domain.auth.service.oauth;

import yuquiz.domain.auth.dto.OAuthCodeDto;
import yuquiz.domain.auth.dto.UserInfoDto;
import yuquiz.domain.user.entity.User;

public interface OAuthClient {

    String getAccessToken(OAuthCodeDto codeDto);

    UserInfoDto getUserInfo(String accessToken);

    User getOAuthUser(String platformId);
}
