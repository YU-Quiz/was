package yuquiz.domain.auth.service.oauth;

import yuquiz.domain.auth.dto.req.OAuthCodeDto;
import yuquiz.domain.auth.dto.res.UserInfoDto;
import yuquiz.domain.user.entity.User;

public interface OAuthClient {

    String getAccessToken(OAuthCodeDto codeDto);

    UserInfoDto getUserInfo(String accessToken);

    User getOAuthUser(String platformId);
}
