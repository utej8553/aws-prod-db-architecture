package com.dbarch.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dbarch.main.model.Student;
import com.dbarch.main.service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping
    public ResponseEntity<String> postMethodName(@RequestBody Student student) {
        studentService.save(student);
        return ResponseEntity.ok("Student saved successfully");
    }
    @GetMapping("/{roll}")
    public ResponseEntity<Student> getMethodName(@PathVariable String roll) {
        Student student = studentService.findByRoll(roll);
        if(student == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }
    @GetMapping
    public ResponseEntity<List<Student>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }
    @DeleteMapping("/{roll}")
    public ResponseEntity<String> delete(@PathVariable String roll) {
        studentService.delete(roll);
        return ResponseEntity.ok("Student deleted successfully");
    }
}
