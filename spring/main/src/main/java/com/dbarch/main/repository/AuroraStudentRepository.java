package com.dbarch.main.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dbarch.main.model.Student;

@Repository
public class AuroraStudentRepository {
    private final JdbcTemplate jdbcTemplate;
    public AuroraStudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void save(Student student){
        String sql = "INSERT INTO students (roll, name, branch) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, student.getRoll(), student.getName(), student.getBranch());
    }
    public Student findByRoll(String roll){
        String sql = "SELECT roll, name, branch FROM students WHERE roll = ?";
        return jdbcTemplate.query(sql, new Object[]{roll}, (rs, rowNum) -> {
            return new Student(rs.getString("name"), rs.getString("roll"), rs.getString("branch"));
        }).stream().findFirst().orElse(null);
    }
    public List<String> findAll(){
        String sql = "SELECT roll, name, branch FROM students";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new Student(rs.getString("name"), rs.getString("roll"), rs.getString("branch"));
        }).stream().map(Student::getRoll).toList();
    }
    public boolean hasItem(String roll) {
        return findByRoll(roll) != null;
    }
    public void delete(String roll) {
        String sql = "DELETE FROM students WHERE roll = ?";
        jdbcTemplate.update(sql, roll);
    }
}
