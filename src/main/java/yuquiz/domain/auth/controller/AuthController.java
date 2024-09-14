package yuquiz.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuquiz.common.api.SuccessRes;
import yuquiz.common.exception.CustomException;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.domain.auth.api.AuthApi;
import yuquiz.domain.auth.dto.req.FindUsernameReq;
import yuquiz.domain.auth.dto.req.OAuthCodeDto;
import yuquiz.domain.auth.dto.req.OAuthSignUpReq;
import yuquiz.domain.auth.dto.res.OAuthTokenDto;
import yuquiz.domain.auth.dto.req.PasswordResetReq;
import yuquiz.domain.auth.dto.req.SignInReq;
import yuquiz.domain.auth.dto.req.SignUpReq;
import yuquiz.domain.auth.dto.res.TokenDto;
import yuquiz.domain.auth.dto.req.UserVerifyReq;
import yuquiz.domain.auth.service.AccountService;
import yuquiz.domain.auth.service.AuthService;
import yuquiz.domain.auth.service.JwtService;
import yuquiz.domain.user.entity.OAuthPlatform;

import java.util.HashMap;
import java.util.Map;

import static yuquiz.common.utils.jwt.JwtProperties.ACCESS_HEADER_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final CookieUtil cookieUtil;
    private final JwtService jwtService;
    private final AccountService accountService;

    /* 회원가입 */
    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpReq signUpReq) {

        TokenDto tokenDto = authService.signUp(signUpReq);

        return createTokenRes(tokenDto);
    }

    /* OAuth 회원가입 */
    @Override
    @PostMapping("/sign-up/oauth")
    public ResponseEntity<?> oauthSignUp(@Valid @RequestBody OAuthSignUpReq oAuthSignUpReq) {

        TokenDto tokenDto = authService.oAuthSignUp(oAuthSignUpReq);

        return createTokenRes(tokenDto);
    }

    /* 일반 로그인 */
    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInReq signInReq) {

        TokenDto tokenDto = authService.signIn(signInReq);

        return createTokenRes(tokenDto);
    }

    /* AccessToken 재발급 */
    @Override
    @GetMapping("/token-reissue")
    public ResponseEntity<?> reIssueToken(@CookieValue(name = REFRESH_COOKIE_VALUE, required = false) String refreshToken) {

        if (refreshToken == null) {     // 쿠키에 Refresh Token이 없다면
            throw new CustomException(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND);
        }

        TokenDto tokenDto = jwtService.reissueAccessToken(refreshToken);

        return createTokenRes(tokenDto);
    }

    /* Oauth 로그인 (kakao) */
    @Override
    @PostMapping("/sign-in/kakao")
    public ResponseEntity<?> kakaoSignIn(@RequestBody OAuthCodeDto oauthCodeDto) {

        OAuthTokenDto oAuthTokenDto = authService.signInByOauth(oauthCodeDto, OAuthPlatform.KAKAO);

        return createTokenRes(oAuthTokenDto);
    }

    /* Oauth 로그인 (naver) */
    @Override
    @PostMapping("/sign-in/naver")
    public ResponseEntity<?> naverSignIn(@RequestBody OAuthCodeDto oauthCodeDto) {

        OAuthTokenDto oAuthTokenDto = authService.signInByOauth(oauthCodeDto, OAuthPlatform.NAVER);

        return createTokenRes(oAuthTokenDto);
    }

    /* 로그아웃 */
    @Override
    @GetMapping("/sign-out")
    public ResponseEntity<?> signOut(@RequestHeader(value = ACCESS_HEADER_VALUE, required = false) String accessToken,
                                     @CookieValue(name = REFRESH_COOKIE_VALUE, required = false) String refreshToken,
                                     HttpServletResponse response) {

        /*  https 설정하기 전까지 임시 제거 (쿠키 설정 때문)
        if (accessToken == null || refreshToken == null) {
            throw new CustomException(JwtExceptionCode.TOKEN_NOT_FOUND);
        }*/

        authService.signOut(accessToken, refreshToken, response);

        return ResponseEntity.ok(SuccessRes.from("로그아웃 되었습니다."));
    }

    /* 아이디 찾기 */
    @Override
    @PostMapping("/find-username")
    public ResponseEntity<?> findUsername(@Valid @RequestBody FindUsernameReq findUsernameReq) {

        return ResponseEntity.ok(SuccessRes.from(accountService.findUsernameByEmail(findUsernameReq.email())));
    }

    /* 비밀번호 재설정 확인 */
    @Override
    @PostMapping("/reset-password/verify-user")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody UserVerifyReq userVerifyReq) {

        accountService.validateUserForPasswordReset(userVerifyReq);
        return ResponseEntity.ok(SuccessRes.from("메일 전송 완료."));
    }

    /* 비밀번호 재설정 */
    @Override
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetReq passwordResetReq) {

        accountService.resetPassword(passwordResetReq);
        return ResponseEntity.ok(SuccessRes.from("비밀번호 재설정 성공."));
    }

    /* 토큰과 관련된 응답값 생성. - TokenDto (일반 로그인 및 토큰 재발급) */
    private ResponseEntity<?> createTokenRes(TokenDto tokenDto) {

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", tokenDto.accessToken());

        return ResponseEntity.ok()
                .header("Set-Cookie",
                        cookieUtil.createCookie(REFRESH_COOKIE_VALUE, tokenDto.refreshToken()).toString())
                .body(responseData);
    }

    /* 토큰과 관련된 응답값 생성 - OAuthTokenDto (소셜 로그인) */
    private ResponseEntity<?> createTokenRes(OAuthTokenDto oAuthTokenDto) {

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", oAuthTokenDto.tokenDto().accessToken());
        responseData.put("isRegistered", oAuthTokenDto.isRegistered());

        return ResponseEntity.ok()
                .header("Set-Cookie",
                        cookieUtil.createCookie(REFRESH_COOKIE_VALUE, oAuthTokenDto.tokenDto().refreshToken()).toString())
                .body(responseData);
    }
}
