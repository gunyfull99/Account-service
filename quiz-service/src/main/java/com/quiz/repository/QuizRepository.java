package com.quiz.repository;

import com.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query(value = "select * from quiz where user_id = :id and status ='done' ", nativeQuery = true)
    List<Quiz> getQuizByUserWhenDone(@Param("id") long userId);

    @Query(value = "select * from quiz where user_id = :id  ", nativeQuery = true)
    List<Quiz> getAllByUser(@Param("id") long userId);

    @Query(value = "SELECT  id from quiz", nativeQuery = true)
    List<Integer> getAllId();
    @Query(value = "select DISTINCT user_id from quiz where status = 'done'", nativeQuery = true)
    List<Integer> getIdByStatus();
    @Query(value = "select * from quiz where user_id= :id and status = 'not start' ", nativeQuery = true)
    List<Quiz> getQuizNotStart(@Param("id") long userId);
}
