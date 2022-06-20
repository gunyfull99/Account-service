package com.quiz.service.repository;

import com.quiz.entity.Category;
import com.quiz.entity.GroupQuiz;
import com.quiz.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface GroupQuizRepository extends JpaRepository<GroupQuiz,Long> {

    @Query("select gq from GroupQuiz gq where (:cate is null OR LOWER(gq.cate) LIKE %:cate%)" +
            " AND ((:description is null OR LOWER(gq.description) LIKE %:description%) " +
            "OR ((:creators) is null OR (gq.creator IN :creators))) " +
            "AND (cast(:createDate as date) is null OR gq.createDate = :createDate) " +
            "AND (cast(:startTime as date) is null OR cast(gq.startTime as date) = :startTime)" +
            "AND (cast(:expiredTime as date) is null OR cast(gq.expiredTime as date) = :expiredTime)")
    Page<GroupQuiz> filter(@Param("cate") String cate,
                           @Param("description") String description,
                           @Param("creators") List<String> creators,
                           @Param("createDate") Date createDate,
                           @Param("startTime") Date  startTime,
                           @Param("expiredTime") Date  expiredTime,
                           Pageable pageable);
    @Modifying
    @Query(value = "delete from group_quiz where id = :id", nativeQuery = true)
    void deleteGroupQuiz(@Param("id") Long id);


}
