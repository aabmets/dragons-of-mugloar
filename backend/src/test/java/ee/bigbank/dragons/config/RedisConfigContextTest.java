package ee.bigbank.dragons.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class RedisConfigContextTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withBean(RedisConnectionFactory.class, () -> Mockito.mock(RedisConnectionFactory.class))
                    .withUserConfiguration(RedisConfig.class);

    @Test
    void redisTemplateBeanIsCreatedAndHasConnectionFactory() {
        contextRunner.run(ctx -> {
            assertThat(ctx).hasSingleBean(RedisTemplate.class);

            @SuppressWarnings("unchecked")
            RedisTemplate<String, String> template = ctx.getBean(RedisTemplate.class);

            assertThat(template.getConnectionFactory()).isNotNull();
        });
    }
}
