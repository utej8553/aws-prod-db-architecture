package com.dbarch.main.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.dbarch.main.model.Student;

@Repository
public class MemoryDbRepository {

    private final RedisTemplate<String, String> redis;

    public MemoryDbRepository(
            RedisTemplate<String, String> memoryDbTemplate) {

        this.redis = memoryDbTemplate;
    }

    public void save(Student student) {

        String value =
                student.getName() + "|" +
                        student.getBranch();

        redis.opsForValue().set(
                "student:" + student.getRoll(),
                value
        );
    }

    public String get(String roll) {

        return redis.opsForValue()
                .get("student:" + roll);
    }

    public void delete(String roll) {

        redis.delete("student:" + roll);
    }
}