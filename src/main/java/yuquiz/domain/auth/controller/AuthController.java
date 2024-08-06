package yuquiz.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yuquiz.common.api.SuccessRes;
import yuquiz.common.exception.CustomException;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.cookie.CookieUtil;
import yuquiz.domain.auth.dto.SignInReq;
import yuquiz.domain.auth.dto.TokenDto;
import yuquiz.domain.auth.service.AuthService;

import java.util.HashMap;
import java.util.Map;

import static yuquiz.common.utils.jwt.JwtProperties.REFRESH_COOKIE_VALUE;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    /* 일반 로그인 */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInReq signInReq) {

        TokenDto tokenDto = authService.signIn(signInReq);

        return createTokenRes(tokenDto);
    }

    /* AccessToken 재발급 */
    @GetMapping("/token-reissue")
    public ResponseEntity<?> reIssueToken(@CookieValue(name = REFRESH_COOKIE_VALUE, required = false) String refreshToken, HttpServletResponse response) {

        if (refreshToken == null) {     // 쿠키에 Refresh Token이 없다면
            throw new CustomException(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND);
        }

        TokenDto tokenDto = authService.reissueAccessToken(refreshToken);

        return createTokenRes(tokenDto);
    }

    /* 로그아웃 */
    @GetMapping("/sign-out")
    public ResponseEntity<?> signOut(@CookieValue(name = REFRESH_COOKIE_VALUE, required = false) String refreshToken,
                                     HttpServletResponse response) {

        if (refreshToken == null) {
            throw new CustomException(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND);
        }

        authService.signOut(refreshToken, response);

        return ResponseEntity.ok(SuccessRes.from("로그아웃 되었습니다."));
    }


    /* 토큰과 관련된 응답값 생성. */
    private ResponseEntity<?> createTokenRes(TokenDto tokenDto) {

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("accessToken", tokenDto.accessToken());

        return ResponseEntity.ok()
                .header("Set-Cookie",
                        cookieUtil.createCookie(REFRESH_COOKIE_VALUE, tokenDto.refreshToken()).toString())
                .body(responseData);
    }
}
