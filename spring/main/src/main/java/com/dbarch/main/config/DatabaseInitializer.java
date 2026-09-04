package com.dbarch.main.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {
    private final JdbcTemplate jdbcTemplate;
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @PostConstruct
    public void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS students (roll VARCHAR(50) PRIMARY KEY, name VARCHAR(100), branch VARCHAR(100))";
        jdbcTemplate.execute(sql);
    }
}