package com.quiz.repository;

import com.quiz.entity.Category;
import com.quiz.entity.GroupQuiz;
import com.quiz.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupQuizRepository extends JpaRepository<GroupQuiz,Long> {
    @Query(value = "select * from group_quiz where lower(cate) like %:cate% and (lower(description) like %:description% or creator  IN :creator) ", nativeQuery = true)
    Page<GroupQuiz> filter(@Param("cate") String cate,
                           @Param("description") String description,
                           @Param("creator") List<Long> creator,
                           Pageable pageable);
}
