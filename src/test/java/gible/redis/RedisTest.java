package gible.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.config.RedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataRedisTest
@TestPropertySource(locations ="classpath:application.yml")
class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @MockBean
    RedisConfig redisConfig;

    @Test
    public void testRedisConnection() {
        // RedisConnectionFactory가 제대로 주입되었는지 확인
        assertThat(redisConnectionFactory).isNotNull();

        // RedisTemplate을 사용하여 값을 저장하고 조회하여 Redis 연결 테스트
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("testKey", "testValue");
        String value = ops.get("testKey");
        assertThat(value).isEqualTo("testValue");
    }

}
