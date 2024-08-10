package yuquiz.security.token.blacklist;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yuquiz.common.utils.redis.RedisUtil;

import static yuquiz.common.utils.redis.RedisProperties.BLACKLIST_KEY_PREFIX;

@Service
@RequiredArgsConstructor
public class BlackListTokenService {

    private final RedisUtil redisUtil;

    @Value("${token.blacklist.in-redis}")
    private long BLACKLIST_EXPIRATION_TIME;

    /* redis에 저장 */
    public void saveBlackList(String accessToken) {

        String key = BLACKLIST_KEY_PREFIX + accessToken;
        redisUtil.set(key, accessToken);
        redisUtil.expire(key, BLACKLIST_EXPIRATION_TIME);
    }

    /* 블랙리스트 확인. */
    public boolean existsBlackListCheck(String accessToken) {
        return redisUtil.existed(BLACKLIST_KEY_PREFIX + accessToken);
    }

}
