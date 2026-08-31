package com.dbarch.main.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dbarch.main.model.Student;
import com.dbarch.main.repository.*;

@Service
public class StudentService {

    private final DynamoStudentRepository dynamo;
    private final AuroraStudentRepository aurora;
    private final ElastiCacheRepository cache;
    private final MemoryDbRepository memoryDb;

    public StudentService(
            DynamoStudentRepository dynamo,
            AuroraStudentRepository aurora,
            ElastiCacheRepository cache,
            MemoryDbRepository memoryDb) {

        this.dynamo = dynamo;
        this.aurora = aurora;
        this.cache = cache;
        this.memoryDb = memoryDb;
    }

    public void save(Student student) {

        // Primary databases
        dynamo.save(student);
        aurora.save(student);

        // Caches
        cache.save(student);
        memoryDb.save(student);
    }

    public Student findByRoll(String roll) {

        // DynamoDB first
        Student student = dynamo.findByRoll(roll);

        if (student != null) {
            return student;
        }

        // Aurora fallback
        student = aurora.findByRoll(roll);

        if (student != null) {
            cache.save(student);
            memoryDb.save(student);
        }

        return student;
    }

    public List<Student> findAll() {

        return dynamo.findAll();
    }

    public void delete(String roll) {

        dynamo.delete(roll);
        aurora.delete(roll);

        cache.delete(roll);
        memoryDb.delete(roll);
    }
}