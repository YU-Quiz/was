package yuquiz.common.utils.redis;

public interface RedisProperties {

    String REFRESH_TOKEN_KEY_PREFIX = "refreshToken::";
    String BLACKLIST_KEY_PREFIX = "blackList::";
    String CODE_KEY_PREFIX = "code::";
    long CODE_EXPIRATION_TIME = 3*60;
}
