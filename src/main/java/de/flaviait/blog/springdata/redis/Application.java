package de.flaviait.blog.springdata.redis;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableCaching
public class Application {

  @Bean
  public RedisTemplate<String, String> template(RedisConnectionFactory factory) {
    final StringRedisTemplate template = new StringRedisTemplate(factory);
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Book.class));

    return template;
  }

  @Bean
  public CacheManager cacheManager(RedisTemplate<?, ?> template) {
    final RedisCacheManager cacheManager = new RedisCacheManager(template);
    cacheManager.setTransactionAware(true);
    cacheManager.setDefaultExpiration(Duration.ofSeconds(5).getSeconds());
    return cacheManager;
  }

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }
}
