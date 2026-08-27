package com.dbarch.main.controller;

import com.dbarch.main.model.Student;
import com.dbarch.main.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{roll}")
    public ResponseEntity<Student> getStudent(@PathVariable String roll) {
        return ResponseEntity.ok(studentService.getStudentByRoll(roll));
    }

    @PutMapping("/{roll}")
    public ResponseEntity<Student> updateStudent(@PathVariable String roll, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(roll, student));
    }

    @DeleteMapping("/{roll}")
    public ResponseEntity<String> deleteStudent(@PathVariable String roll) {
        studentService.deleteStudent(roll);
        return ResponseEntity.ok("Student deleted successfully");
    }
}