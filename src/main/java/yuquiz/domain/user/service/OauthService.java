package yuquiz.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.domain.auth.dto.UserInfoDto;
import yuquiz.domain.user.entity.OAuth;
import yuquiz.domain.user.entity.OAuthPlatform;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.repository.OAuthRepository;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OAuthRepository oAuthRepository;

    /* OAuth 정보 저장 */
    @Transactional
    public void saveOAuthInfo(UserInfoDto userInfoDto, OAuthPlatform platform, User user) {

        oAuthRepository.save(
                OAuth.builder()
                        .email(userInfoDto.email())
                        .platform(platform)
                        .platformId(userInfoDto.id())
                        .user(user)
                        .build()
        );
    }
}
