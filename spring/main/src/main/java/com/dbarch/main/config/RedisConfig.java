package com.dbarch.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${aws.elasticache.endpoint}")
    private String endpoint;

    @Value("${aws.elasticache.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory elasticacheConnectionFactory() {

        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration(
                        endpoint,
                        port
                );

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, String> elasticacheTemplate(
            LettuceConnectionFactory elasticacheConnectionFactory) {

        RedisTemplate<String, String> template =
                new RedisTemplate<>();

        template.setConnectionFactory(
                elasticacheConnectionFactory
        );

        template.afterPropertiesSet();

        return template;
    }
}