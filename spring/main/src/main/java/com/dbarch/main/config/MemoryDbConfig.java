package com.dbarch.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class MemoryDbConfig {

    @Value("${aws.memorydb.endpoint}")
    private String endpoint;

    @Value("${aws.memorydb.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory memoryDbConnectionFactory() {

        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration(
                        endpoint,
                        port
                );

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, String> memoryDbTemplate(
            LettuceConnectionFactory memoryDbConnectionFactory) {

        RedisTemplate<String, String> template =
                new RedisTemplate<>();

        template.setConnectionFactory(
                memoryDbConnectionFactory
        );

        template.afterPropertiesSet();

        return template;
    }
}