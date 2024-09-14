package yuquiz.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.req.OAuthCodeDto;
import yuquiz.domain.auth.dto.res.UserInfoDto;
import yuquiz.domain.auth.exception.AuthExceptionCode;
import yuquiz.domain.auth.service.oauth.KakaoService;
import yuquiz.domain.auth.service.oauth.NaverService;
import yuquiz.domain.auth.service.oauth.OAuthClient;
import yuquiz.domain.user.entity.OAuthPlatform;
import yuquiz.domain.user.entity.User;

@Service
@RequiredArgsConstructor
public class OAuthPlatformService {

    private final NaverService naverService;
    private final KakaoService kakaoService;

    public String getAccessToken(OAuthCodeDto codeDto, OAuthPlatform platform) {
        return getPlatformService(platform).getAccessToken(codeDto);
    }

    public UserInfoDto getUserInfo(String accessToken, OAuthPlatform platform) {
        return getPlatformService(platform).getUserInfo(accessToken);
    }

    public User readOAuthUser(String platformId, OAuthPlatform platform) {
        return getPlatformService(platform).getOAuthUser(platformId);
    }

    private OAuthClient getPlatformService(OAuthPlatform platform) {
        return switch (platform) {
            case NAVER -> naverService;
            case KAKAO -> kakaoService;
            default -> throw new CustomException(AuthExceptionCode.UNSUPPORTED_SOCIAL_LOGIN_ERROR);
        };
    }
}
