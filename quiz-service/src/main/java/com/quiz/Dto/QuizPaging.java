package com.quiz.Dto;

import com.quiz.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizPaging {
    int total ;
    List<QuizPagingDto> quizList;
    int page ;
    int limit ;
    long userId;
    String  status;
    Date createDate;
    String keywords;
    String cate;
    long groupQuiz;

    public QuizPaging(int total,List<QuizPagingDto> quizList,int page,int limit){
        this.total=total;
        this.quizList=quizList;
        this.page=page;
        this.limit=limit;
    }

}
