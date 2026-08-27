package com.dbarch.main.service;

import com.dbarch.main.model.Student;
import com.dbarch.main.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public class StudentService{
    private final StudentRepository studentRepository;
    public StundentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentByRoll(String roll) {
        return studentRepository.findByRoll(roll).orElseThrow(() -> new RuntimeException("Student not found: " + roll));
    }

    public Student updateStudent(String roll, Student updatedStudent) {
        Student existingStudent = studentRepository.findByRoll(roll).orElseThrow(() -> new RuntimeException("Student not found: " + roll));
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setBranch(updatedStudent.getBranch());
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(String roll) {
        Student student = studentRepository.findByRoll(roll).orElseThrow(() -> new RuntimeException("Student not found: " + roll));
        studentRepository.delete(student);
    }
}