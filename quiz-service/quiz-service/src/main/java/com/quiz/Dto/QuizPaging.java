package com.quiz.Dto;

import com.quiz.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizPaging {
    int total ;
    List<Quiz> quizList;
    int page ;
    int limit ;
    long userId;
    String status;

    public QuizPaging(int total,List<Quiz> quizList,int page,int limit){
        this.total=total;
        this.quizList=quizList;
        this.page=page;
        this.limit=limit;
    }
}
