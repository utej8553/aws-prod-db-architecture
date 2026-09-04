package com.dbarch.main.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.dbarch.main.model.Student;

@Repository
public class ElastiCacheRepository {
    private final RedisTemplate<String, String> redisTemplate;
    public ElastiCacheRepository(@Qualifier("elasticacheTemplate")RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public void save(Student student){
        String key = "student: " + student.getRoll();
        String value = student.getName() + "|" + student.getBranch();
        redisTemplate.opsForValue().set(key, value);
    }
    public Student findByRoll(String roll){
        String key = "student: " + roll;
        String value = redisTemplate.opsForValue().get(key);
        if(value == null){
            return null;
        }
        String[] parts = value.split("\\|", 2);
        return new Student(parts[0], roll, parts[1]);
    }
    public boolean hasItem(String roll) {
        String key = "student:" + roll;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    public void delete(String roll) {
        String key = "student:" + roll;
        redisTemplate.delete(key);
    }
}
