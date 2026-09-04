package com.dbarch.main.repository;

import com.dbarch.main.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuroraStudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuroraStudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Student student) {
        String sql = """
                INSERT INTO students (roll, name, branch)
                VALUES (?, ?, ?)
                ON CONFLICT (roll)
                DO UPDATE SET
                    name = EXCLUDED.name,
                    branch = EXCLUDED.branch
                """;

        jdbcTemplate.update(
                sql,
                student.getRoll(),
                student.getName(),
                student.getBranch()
        );
    }

    public Student findByRoll(String roll) {
        String sql = "SELECT roll, name, branch FROM students WHERE roll = ?";

        return jdbcTemplate.query(
                sql,
                rs -> {
                    if (rs.next()) {
                        return new Student(
                                rs.getString("name"),
                                rs.getString("roll"),
                                rs.getString("branch")
                        );
                    }
                    return null;
                },
                roll
        );
    }

    public void delete(String roll) {
        String sql = "DELETE FROM students WHERE roll = ?";
        jdbcTemplate.update(sql, roll);
    }
}
