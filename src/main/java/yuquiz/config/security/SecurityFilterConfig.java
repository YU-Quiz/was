package yuquiz.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import yuquiz.common.jwt.filter.JwtAuthenticationFilter;
import yuquiz.common.jwt.filter.JwtExceptionFilter;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.security.token.blacklist.BlackListTokenService;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterConfig {

    private final UserDetailsService userDetailsService;
    private final BlackListTokenService blackListTokenService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService, blackListTokenService, jwtProvider, objectMapper);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }
}
