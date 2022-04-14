package com.quiz.repository;

import com.quiz.entity.Question;
import com.quiz.entity.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {
    @Query(value = "select * from question_type", nativeQuery = true)
    List<QuestionType> getAllQuestionType();
}
