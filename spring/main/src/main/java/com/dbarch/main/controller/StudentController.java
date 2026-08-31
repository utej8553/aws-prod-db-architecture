package com.dbarch.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dbarch.main.model.Student;
import com.dbarch.main.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> create(
            @RequestBody Student student) {

        service.save(student);

        return ResponseEntity.ok(
                "Student saved successfully"
        );
    }

    @GetMapping("/{roll}")
    public ResponseEntity<Student> get(
            @PathVariable String roll) {

        Student student = service.findByRoll(roll);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {

        return ResponseEntity.ok(
                service.findAll()
        );
    }

    @DeleteMapping("/{roll}")
    public ResponseEntity<String> delete(
            @PathVariable String roll) {

        service.delete(roll);

        return ResponseEntity.ok(
                "Student deleted successfully"
        );
    }
}