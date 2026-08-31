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

    public void initialize() {

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS students (
                roll VARCHAR(50) PRIMARY KEY,
                name VARCHAR(100),
                branch VARCHAR(100)
            )
        """);
    }

    public void save(Student student) {

        jdbcTemplate.update("""
            INSERT INTO students (roll, name, branch)
            VALUES (?, ?, ?)
            ON CONFLICT (roll)
            DO UPDATE SET
                name = EXCLUDED.name,
                branch = EXCLUDED.branch
        """,
                student.getRoll(),
                student.getName(),
                student.getBranch());
    }

    public Student findByRoll(String roll) {

        List<Student> students = jdbcTemplate.query(
                "SELECT roll, name, branch FROM students WHERE roll = ?",
                (rs, rowNum) ->
                        new Student(
                                rs.getString("roll"),
                                rs.getString("name"),
                                rs.getString("branch")
                        ),
                roll
        );

        return students.isEmpty() ? null : students.get(0);
    }

    public List<Student> findAll() {

        return jdbcTemplate.query(
                "SELECT roll, name, branch FROM students",
                (rs, rowNum) ->
                        new Student(
                                rs.getString("roll"),
                                rs.getString("name"),
                                rs.getString("branch")
                        )
        );
    }

    public void delete(String roll) {

        jdbcTemplate.update(
                "DELETE FROM students WHERE roll = ?",
                roll
        );
    }
}