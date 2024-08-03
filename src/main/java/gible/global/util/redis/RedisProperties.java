package gible.global.util.redis;

public interface RedisProperties {

    String REFRESH_TOKEN_KEY_PREFIX = "refreshToken::";
    String POST_KEY_PREFIX = "post::";
    long NEWEST_EXPIRATION = 21600;
}
