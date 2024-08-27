package yuquiz.security.token.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yuquiz.common.exception.CustomException;
import yuquiz.common.exception.exceptionCode.JwtExceptionCode;
import yuquiz.common.utils.jwt.JwtProvider;
import yuquiz.common.utils.redis.RedisUtil;

import java.util.Optional;

import static yuquiz.common.utils.redis.RedisProperties.REFRESH_TOKEN_KEY_PREFIX;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisUtil redisUtil;
    private final JwtProvider jwtProvider;

    @Value("${token.refresh.in-redis}")
    private long REDIS_REFRESH_EXPIRATION;

    /* redis에 저장 */
    public void saveRefreshToken(String username, String refreshToken) {

        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        redisUtil.set(key, refreshToken);
        redisUtil.expire(key, REDIS_REFRESH_EXPIRATION);
    }

    /* refreshToken으로 redis에서 불러오기 */
    public String findRefreshToken(String refreshToken) {

        String username = jwtProvider.getUsername(refreshToken);
        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        Optional<String> foundRefreshToken = Optional.ofNullable((String) redisUtil.get(key));

        if(foundRefreshToken.isEmpty())
            throw new CustomException(JwtExceptionCode.REFRESH_TOKEN_NOT_FOUND);

        return foundRefreshToken.get();
    }

    /* redis에서 삭제 */
    public void deleteRefreshToken(String refreshToken) {

        String username = jwtProvider.getUsername(refreshToken);
        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        redisUtil.del(key);
    }

    /* accessToken 재발급 */
    public String accessTokenReIssue(String refreshToken) {

        String role = jwtProvider.getRole(refreshToken);
        Long userId = jwtProvider.getUserId(refreshToken);
        String username = jwtProvider.getUsername(refreshToken);

        return jwtProvider.generateAccessToken(role, userId, username);      // 재발급
    }

    /* Refresh token rotation(RTR) 사용 */
    public String refreshTokenReIssue(String refreshToken) {

        this.deleteRefreshToken(refreshToken);

        String username = jwtProvider.getUsername(refreshToken);
        Long userId = jwtProvider.getUserId(refreshToken);
        String role = jwtProvider.getRole(refreshToken);

        String refreshTokenReIssue = jwtProvider.generateRefreshToken(role, userId, username);

        this.saveRefreshToken(username, refreshTokenReIssue);

        return refreshTokenReIssue;
    }

}
