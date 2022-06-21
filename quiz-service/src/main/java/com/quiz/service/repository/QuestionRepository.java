package com.quiz.service.repository;

import com.quiz.entity.GroupQuiz;
import com.quiz.entity.Question;
import com.quiz.entity.QuestionChoice;
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
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select * from questions where cate_id = :id and type_id != 3 and is_active=true", nativeQuery = true)
    List<Question> getAllQuestionByCateToCreateQuiz(@Param("id") long id);


//    SELECT q.id,q.content FROM  questions as q  join question_choice as qc
//    ON q.id=qc.question_id group by q.id

    @Query("SELECT distinct(q) FROM  Question q  " +
            " left join QuestionChoice qc on q.id = qc.question.id  " +
            "WHERE  " +
            "(:search is null OR LOWER(q.content) LIKE %:search%  "
            + " OR LOWER(qc.name) LIKE %:search%"
            + ")"
            + "AND (:cateId is null OR (q.category.id) = :cateId) "
            + "AND ((q.isActive) = true)  "
            + "AND (:typeId is null OR (q.questionType.id) = :typeId)"
    )
    Page<Question> filter(@Param("cateId") Long cateId,
                          @Param("search") String search,
                          @Param("typeId") Long typeId,
                          Pageable pageable);

    @Query(value = "select * from questions where cate_id = :id and type_id = 3 and is_active=true", nativeQuery = true)
    List<Question> getAllQuestionText(@Param("id") long id);

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
