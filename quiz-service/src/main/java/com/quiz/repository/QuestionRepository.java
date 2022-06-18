package com.quiz.repository;

import com.quiz.entity.GroupQuiz;
import com.quiz.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    @Query(value = "select * from questions where cate_id = :id and type_id != 3 and is_active=true", nativeQuery = true)
    List<Question> getAllQuestionByCateToCreateQuiz(@Param("id") long id );

    Page<Question> findAllByCategoryId (long id, Pageable p);
    Page<Question> findAllByQuestionTypeId (long id, Pageable p);
    Page<Question> findAllByCategoryIdAndContentContainingIgnoreCaseAndIsActiveOrQuestionChoiceNameContainingIgnoreCase(long id,String content,boolean isActive,String content1, Pageable p);
    Page<Question> findAllByContentContainingIgnoreCaseAndIsActiveOrQuestionChoiceNameContainingIgnoreCase(String content,boolean isActive,String content1, Pageable p);
    Page<Question> findAllByQuestionTypeIdAndContentContainingIgnoreCaseAndIsActiveOrQuestionChoiceNameContainingIgnoreCase (long id,String content,boolean isActive,String content1, Pageable p);
    Page<Question> findAllByQuestionTypeIdAndCategoryIdAndContentContainingIgnoreCaseAndIsActiveOrQuestionChoiceNameContainingIgnoreCase (long idT,long idC,String content,boolean isActive,String content1, Pageable p);

//    SELECT q.id,q.content FROM  questions as q  join question_choice as qc
//    ON q.id=qc.question_id group by q.id

    @Query("select gq from GroupQuiz gq where (:cate is null OR LOWER(gq.cate) LIKE %:cate%)" +
            " AND ((:description is null OR LOWER(gq.description) LIKE %:description%) " +
            "OR ((:creators) is null OR (gq.creator IN :creators))) " +
            "AND (cast(:createDate as date) is null OR gq.createDate = :createDate) " +
            "AND (cast(:startTime as date) is null OR cast(gq.startTime as date) = :startTime)" +
            "AND (cast(:expiredTime as date) is null OR cast(gq.expiredTime as date) = :expiredTime)")
    Page<Question> filter(@Param("cate") String cate,
                           @Param("description") String description,
                           @Param("creators") List<String> creators,
                           @Param("createDate") Date createDate,
                           @Param("startTime") Date  startTime,
                           @Param("expiredTime") Date  expiredTime,
                           Pageable pageable);

    @Query(value = "select * from questions where cate_id = :id and type_id = 3 and is_active=true", nativeQuery = true)
    List<Question> getAllQuestionText(@Param("id") long id );

    @Query(value = "select * from questions where is_active=true", nativeQuery = true)
    List<Question> getAllQuestion();

    @Query(value = "select * from questions where id = :id", nativeQuery = true)
    Question getDetailQuestion(@Param("id") long id);

    @Query(value = "select * from questions order by id DESC limit 1", nativeQuery = true)
    Question getLastQuestion();

    @Query(value = "select * from questions where is_active=false", nativeQuery = true)
    List<Question> getAllQuestionBlock();

    @Modifying
    @Query(value = "update questions set is_active = false where id =:ques", nativeQuery = true)
    void blockQuestion(@Param("ques") Long ques);

    @Modifying
    @Query(value = "update questions set is_active = true where id =:ques", nativeQuery = true)
    void openQuestion(@Param("ques") Long ques);

    Question findByContent(String content);
    List<Question> findByCategory_Name(String name);
}
