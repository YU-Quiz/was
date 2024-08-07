package yuquiz.common.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import yuquiz.common.exception.exceptionCode.ExceptionCode;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {              // 토큰 유효성 검증 실패 시 처리
            handleExceptionToken(response, JwtExceptionCode.INVALID_ACCESS_TOKEN);
        }
    }

    /* 예외 처리 */
    private void handleExceptionToken(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {

        String messageBody = objectMapper.writeValueAsString(exceptionCode);

        log.error("Error occurred: {}", exceptionCode.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(exceptionCode.getStatus());
        response.getWriter().write(messageBody);
    }
}