package com.quiz.repository;

import com.quiz.entity.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NomineeRepository extends JpaRepository<Nominee,Long> {
}
