package com.quiz.Dto;

import com.quiz.entity.GroupQuiz;
import com.quiz.entity.Question;
import com.quiz.entity.Quiz;
import com.quiz.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto implements BaseDto {

    private long id;
    private String description;
    private int quizTime;
    private List<Long> userId;
    private Date startTime;
    private Date endTime;
    private Date expiredTime;
    private String status;
    private int numberQuestions;
    private String score;
    private String creator;
    private String[] cate;
    private List<Question> questions ;
    private long userStartQuiz;
    private GroupQuiz groupQuiz;

    @Override
    public void convertDateToString(Quiz quiz) {
      //  this.startTime = DateTimeUtil.convert(quiz.getStartTime(), DateTimeUtil.DATETIME_FORMAT_1);
    }

}
