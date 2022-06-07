package com.quiz.repository;

import com.quiz.entity.QuestionChoice;
import com.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice,Long> {

    @Query(value = "select * from question_choice where question_id = :id  ", nativeQuery = true)
    List<QuestionChoice> getListChoiceByQuesId(@Param("id") long quesId);

    @Query(value = "select is_true from question_choice where id = :id ", nativeQuery = true)
    boolean checkCorrectAnswer(@Param("id") long id);

    @Query(value = "    SELECT count (*) FROM question_choice where is_true=true and question_id = :id ", nativeQuery = true)
    int countCorrect(@Param("id") long quesId);
}
