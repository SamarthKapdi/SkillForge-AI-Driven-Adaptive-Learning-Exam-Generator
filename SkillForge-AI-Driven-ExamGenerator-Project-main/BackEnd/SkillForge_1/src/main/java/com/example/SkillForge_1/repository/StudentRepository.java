package com.example.SkillForge_1.repository;

import com.example.SkillForge_1.model.Student;
import com.example.SkillForge_1.model.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByUser_Id(Long userId);
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByUser(UserAuthentication user);
}
