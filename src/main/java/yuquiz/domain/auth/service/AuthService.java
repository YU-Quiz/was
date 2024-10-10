package yuquiz.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yuquiz.common.exception.CustomException;
import yuquiz.domain.auth.dto.req.OAuthCodeDto;
import yuquiz.domain.auth.dto.req.OAuthSignUpReq;
import yuquiz.domain.auth.dto.res.OAuthTokenDto;
import yuquiz.domain.auth.dto.req.SignInReq;
import yuquiz.domain.auth.dto.req.SignUpReq;
import yuquiz.domain.auth.dto.res.TokenDto;
import yuquiz.domain.auth.dto.res.UserInfoDto;
import yuquiz.domain.auth.helper.JwtHelper;
import yuquiz.domain.user.entity.OAuthPlatform;
import yuquiz.domain.user.entity.User;
import yuquiz.domain.user.exception.UserExceptionCode;
import yuquiz.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuthPlatformService oAuthPlatformService;
    private final JwtHelper jwtHelper;

    /* 회원 생성 */
    @Transactional
    public TokenDto signUp(SignUpReq signUpReq) {

        String encodePassword = passwordEncoder.encode(signUpReq.password());
        User createdUser = userRepository.save(signUpReq.toEntity(encodePassword));

        return jwtHelper.createToken(createdUser);
    }

    /* 일반 로그인 */
    @Transactional(readOnly = true)
    public TokenDto signIn(SignInReq signInReq) {

        User foundUser = userRepository.findByUsername(signInReq.username()).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD));

        if (!checkPassword(signInReq.password(), foundUser.getPassword())) {
            throw new CustomException(UserExceptionCode.INVALID_USERNAME_AND_PASSWORD);
        }

        if (foundUser.getUnlockedAt() != null) {
            lockedCheck(foundUser.getUnlockedAt());
        }

        return jwtHelper.createToken(foundUser);
    }

    /* OAuth 최초 가입 시, 정보 추가 */
    @Transactional
    public void oAuthSignUp(OAuthSignUpReq oAuthSignUpReq, Long userId) {

        User foundUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(UserExceptionCode.INVALID_USERID));

        foundUser.updateOAuthInfo(oAuthSignUpReq);
    }

    /* 소셜 로그인 */
    @Transactional
    public OAuthTokenDto signInByOauth(OAuthCodeDto codeDto, OAuthPlatform platform) {

        String token = oAuthPlatformService.getAccessToken(codeDto, platform);
        UserInfoDto userInfoDto = oAuthPlatformService.getUserInfo(token, platform);

        User foundUser = oAuthPlatformService.readOAuthUser(userInfoDto.id(), platform);

        if (foundUser.getUnlockedAt() != null) {
            lockedCheck(foundUser.getUnlockedAt());
        }

        boolean isRegistered = foundUser.getEmail() != null;

        return OAuthTokenDto.of(isRegistered, jwtHelper.createToken(foundUser));
    }

    /* access 토큰 재발급 */
    public TokenDto reissueAccessToken(String refreshToken) {

        return jwtHelper.reissueToken(refreshToken);
    }

    /* 잠김 확인 */
    private void lockedCheck(LocalDateTime unlockedAt) {

        if (unlockedAt.isAfter(LocalDateTime.now())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");
            String formattedUnlockedAt = unlockedAt.format(formatter);
            String additionalMessage = "잠금 해제 시간: " + formattedUnlockedAt;
            throw new CustomException(UserExceptionCode.USER_LOCKED, additionalMessage);
        }
    }

    /* 비밀번호 확인 */
    private boolean checkPassword(String actual, String expect) {

        return passwordEncoder.matches(actual, expect);
    }

    /* 로그아웃 */
    public void signOut(String accessTokenInHeader, String refreshToken, HttpServletResponse response) {

        String accessToken = accessTokenInHeader.substring(TOKEN_PREFIX.length()).trim();
        jwtHelper.removeToken(accessToken, refreshToken, response);
    }
}
