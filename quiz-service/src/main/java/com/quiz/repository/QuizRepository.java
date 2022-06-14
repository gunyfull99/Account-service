package com.quiz.repository;

import com.quiz.entity.Question;
import com.quiz.entity.Quiz;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query(value = "select * from quiz where user_id = :id and status ='done' ", nativeQuery = true)
    List<Quiz> getQuizByUserWhenDone(@Param("id") long userId);

    @Query(value = "select * from quiz where user_id = :id   order by id DESC ", nativeQuery = true)
    Page<Quiz> getAllByUser(@Param("id") long userId,  Pageable pageable);

    @Query(value = "SELECT  id from quiz", nativeQuery = true)
    List<Integer> getAllId();
    @Query(value = "select DISTINCT user_id from quiz where status = 'done'", nativeQuery = true)
    List<Integer> getIdByStatus();
    @Query(value = "select * from quiz where user_id= :id and status = 'not_start' ", nativeQuery = true)
    List<Quiz> getQuizNotStart(@Param("id") long userId);

    @Modifying
    @Query(value = "update quiz set user_start_quiz= :time where id = :id", nativeQuery = true)
    void updateUserStartQuiz(@Param("id") Long id,@Param("time") Long time);

    @Query(value = "delete from quiz where id = :id", nativeQuery = true)
    void deleteQuiz(@Param("id") Long id);

    @Query(value = "select * from quiz where lower(status) like :status and lower(cate) like %:cate% and (lower(description) like %:description% or creator  IN :creator) and group_quiz_id=:gid ", nativeQuery = true)
    Page<Quiz> filterWhereNoUserId(@Param("status") String status,
                                    @Param("cate") String cate,
                                    @Param("description") String description,
                                   @Param("creator") List<String> creator,
                                   @Param("gid") Long groupId,
                                   Pageable pageable);



    Page<Quiz> findAllByUserId(long id, Pageable p);
    Page<Quiz> findAllByStatus(String status, Pageable p);
    Page<Quiz> findAllByUserIdAndStatus(long id,String status, Pageable p);


}
