package com.dbarch.main.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dbarch.main.repository.AuroraStudentRepository;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initializeDatabase(
            AuroraStudentRepository repository) {

        return args -> {
            repository.initialize();
            System.out.println("Aurora students table ready.");
        };
    }
}