package yuquiz.security.token.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yuquiz.common.utils.redis.RedisUtil;

import static yuquiz.common.utils.redis.RedisProperties.REFRESH_TOKEN_KEY_PREFIX;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisUtil redisUtil;

    @Value("${token.refresh.in-redis}")
    private long REDIS_REFRESH_EXPIRATION;

    /* redis에 저장 */
    public void saveRefreshToken(String username, String refreshToken) {

        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        redisUtil.set(key, refreshToken);
        redisUtil.expire(key, REDIS_REFRESH_EXPIRATION);
    }

    /* refreshToken으로 redis에서 불러오기 */
    public boolean existedRefreshToken(String username) {

        String key = REFRESH_TOKEN_KEY_PREFIX + username;

        return redisUtil.existed(key);
    }

    /* redis에서 삭제 */
    public void deleteRefreshToken(String username) {

        String key = REFRESH_TOKEN_KEY_PREFIX + username;
        redisUtil.del(key);
    }
}
