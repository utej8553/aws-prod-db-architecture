package com.dbarch.main.repository;

import com.dbarch.main.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public class StundentRepository{
    Optional<Student> findByRoll(String roll);
}