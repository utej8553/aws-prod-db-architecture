package com.dbarch.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class MemoryDbConfig {

    @Value("${aws.memorydb.endpoint}")
    private String endpoint;

    @Value("${aws.memorydb.port}")
    private int port;

    @Value("${aws.memorydb.username}")
    private String username;

    @Value("${aws.memorydb.password}")
    private String password;

    @Bean
    public LettuceConnectionFactory memoryDbConnectionFactory() {
        RedisClusterConfiguration config =
                new RedisClusterConfiguration();

        config.clusterNode(endpoint, port);
        config.setUsername(username);
        config.setPassword(password);

        LettuceClientConfiguration clientConfig =
                LettuceClientConfiguration.builder()
                        .useSsl()
                        .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public RedisTemplate<String, String> memoryDbTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(memoryDbConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
