package yuquiz.common.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import yuquiz.common.exception.dto.ExceptionsRes;
import yuquiz.common.exception.exceptionCode.ExceptionCode;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.security.token.blacklist.BlackListTokenService;

import java.io.IOException;

import static yuquiz.common.utils.jwt.JwtProperties.ACCESS_HEADER_VALUE;
import static yuquiz.common.utils.jwt.JwtProperties.TOKEN_PREFIX;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final BlackListTokenService blackListTokenService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    private static final String SPECIAL_CHARACTERS_PATTERN = "[`':;|~!@#$%()^&*+=?/{}\\[\\]\\\"\\\\\"]+$";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenGetHeader = request.getHeader(ACCESS_HEADER_VALUE);

        /* 로그인 되어 있지 않은 사용자 */
        if(accessTokenGetHeader == null || !accessTokenGetHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(response, accessTokenGetHeader);

        if (accessToken == null)    // resolveAccessToken 메서드에 의해 accessToken에 문제가 있을 경우.
            return;

        Authentication auth = getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    /* jwt 확인 */
    private String resolveAccessToken(HttpServletResponse response, String accessTokenGetHeader) throws IOException {
        String accessToken = accessTokenGetHeader.substring(TOKEN_PREFIX.length()).trim();

        accessToken = accessToken.replaceAll(SPECIAL_CHARACTERS_PATTERN, "");

        if (jwtProvider.isExpired(accessToken)) {      // 만료되었는지
            handleExceptionToken(response, JwtExceptionCode.ACCESS_TOKEN_EXPIRED);
            return null;
        }

        if (blackListTokenService.existsBlackListCheck(accessToken)) {
            handleExceptionToken(response, JwtExceptionCode.BLACKLIST_ACCESS_TOKEN);
            return null;
        }
        return accessToken;
    }


    /* Authentication 가져오기 */
    private Authentication getAuthentication(String token) {

        String userId = String.valueOf(jwtProvider.getUserId(token));

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /* 예외 처리 */
    private void handleExceptionToken(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {

        ExceptionsRes exceptionsRes = ExceptionsRes.of(exceptionCode.getStatus(), exceptionCode.getMessage());
        String messageBody = objectMapper.writeValueAsString(exceptionsRes);

        log.error("Error occurred: {}", exceptionCode.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exceptionCode.getStatus());
        response.getWriter().write(messageBody);
    }
}