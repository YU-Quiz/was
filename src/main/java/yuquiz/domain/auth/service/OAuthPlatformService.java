package yuquiz.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.OauthCodeDto;
import yuquiz.domain.auth.dto.UserInfoDto;
import yuquiz.domain.auth.exception.AuthExceptionCode;
import yuquiz.domain.user.entity.OAuthPlatform;

@Service
@RequiredArgsConstructor
public class OAuthPlatformService {

    private final KakaoService kakaoService;

    public String getAccessToken(OauthCodeDto codeDto, OAuthPlatform platform) {
        return getPlatformService(platform).getAccessToken(codeDto);
    }

    public UserInfoDto getUserInfo(String accessToken, OAuthPlatform platform) {
        return getPlatformService(platform).getUserInfo(accessToken);
    }

    public boolean isExists(String email, OAuthPlatform platform) {
        return getPlatformService(platform).isExists(email);
    }

    private OAuthClient getPlatformService(OAuthPlatform platform) {
        return switch (platform) {
            case KAKAO -> kakaoService;
            default -> throw new CustomException(AuthExceptionCode.UNSUPPORTED_SOCIAL_LOGIN_ERROR);
        };
    }
}
