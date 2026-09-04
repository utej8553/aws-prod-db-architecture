package com.dbarch.main.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dbarch.main.model.Student;
import com.dbarch.main.repository.*;

@Service
public class StudentService {
    private final ElastiCacheRepository elastiCacheRepository;
    private final MemoryDbRepository memoryDbRepository;
    private final DynamoStudentRepository dynamoStudentRepository;
    private final AuroraStudentRepository auroraStudentRepository;
    
    public StudentService(ElastiCacheRepository elastiCacheRepository, MemoryDbRepository memoryDbRepository, DynamoStudentRepository dynamoStudentRepository, AuroraStudentRepository auroraStudentRepository) {
        this.elastiCacheRepository = elastiCacheRepository;
        this.memoryDbRepository = memoryDbRepository;
        this.dynamoStudentRepository = dynamoStudentRepository;
        this.auroraStudentRepository = auroraStudentRepository;
    }
    public void save(Student student){
        elastiCacheRepository.save(student);
        memoryDbRepository.save(student);
        dynamoStudentRepository.save(student);
        auroraStudentRepository.save(student);
    }
    public Student findByRoll(String roll){
        Student student = elastiCacheRepository.findByRoll(roll);
        if(student != null){
            return student;
        }
        student = memoryDbRepository.findByRoll(roll);
        if(student != null){
            elastiCacheRepository.save(student);
            return student;
        }
        student = dynamoStudentRepository.findByRoll(roll);
        if(student != null){
            elastiCacheRepository.save(student);
            memoryDbRepository.save(student);
            return student;
        }
        student = auroraStudentRepository.findByRoll(roll);
        if(student != null){
            elastiCacheRepository.save(student);
            memoryDbRepository.save(student);
            dynamoStudentRepository.save(student);
            return student;
        }
        return null;
    }
    public List<Student> findAll(){
        return dynamoStudentRepository.findAll();
    }
    public void delete(String roll){
        elastiCacheRepository.delete(roll);
        memoryDbRepository.delete(roll);
        dynamoStudentRepository.delete(roll);
        auroraStudentRepository.delete(roll);
    }
}
