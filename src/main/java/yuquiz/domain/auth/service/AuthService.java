package yuquiz.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.OAuthCodeDto;
import yuquiz.domain.auth.dto.OAuthTokenDto;
import yuquiz.domain.auth.dto.SignInReq;
import yuquiz.domain.auth.dto.TokenDto;
import yuquiz.domain.auth.dto.UserInfoDto;
import yuquiz.domain.user.entity.OAuthPlatform;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthPlatformService oAuthPlatformService;
    private final JwtService jwtService;

    /* 일반 로그인 */
    @Transactional(readOnly = true)
    public TokenDto signIn(SignInReq signInReq) {

        User foundUser = userRepository.findByUsername(signInReq.username()).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD));

        if (!checkPassword(signInReq.password(), foundUser.getPassword())) {
            throw new CustomException(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD);
        }

        return jwtService.generateToken(foundUser);
    }

    /* 소셜 로그인 */
    @Transactional
    public OAuthTokenDto signInByOauth(OAuthCodeDto codeDto, OAuthPlatform platform) {

        String token = oAuthPlatformService.getAccessToken(codeDto, platform);
        UserInfoDto userInfoDto = oAuthPlatformService.getUserInfo(token, platform);

        User foundUser = oAuthPlatformService.readOAuthUser(userInfoDto.id(), platform);

        boolean isRegistered = foundUser.getEmail() != null;

        return OAuthTokenDto.of(isRegistered, jwtService.generateToken(foundUser));
    }

    /* 비밀번호 확인 */
    private boolean checkPassword(String actual, String expect) {

        return passwordEncoder.matches(actual, expect);
    }

    /* 로그아웃 */
    public void signOut(String accessTokenInHeader, String refreshToken, HttpServletResponse response) {

        String accessToken = accessTokenInHeader.substring(TOKEN_PREFIX.length()).trim();
        jwtService.deleteAndBlackListToken(accessToken, refreshToken, response);
    }
}
