package com.quiz.Dto;

import com.quiz.entity.GroupQuiz;
import com.quiz.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupQuizPaging {
    int total ;
    List<GroupQuiz> groupQuizList;
    int page ;
    int limit ;
    Long userId;
    Date createDate;
    Date startTime;
    Date expiredTime;
    String keywords;
    String cate;

    public GroupQuizPaging(int total,List<GroupQuiz> quizList,int page,int limit){
        this.total=total;
        this.groupQuizList=quizList;
        this.page=page;
        this.limit=limit;
    }
}
