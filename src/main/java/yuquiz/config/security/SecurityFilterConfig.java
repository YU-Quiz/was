package yuquiz.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import yuquiz.common.jwt.filter.JwtAuthenticationFilter;
import yuquiz.common.jwt.filter.JwtExceptionFilter;
import yuquiz.common.utils.jwt.JwtProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterConfig {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService, jwtProvider, objectMapper);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }
}
