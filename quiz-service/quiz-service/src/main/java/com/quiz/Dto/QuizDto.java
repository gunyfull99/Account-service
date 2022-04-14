package com.quiz.Dto;

import com.quiz.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {

    private long id;
    private String description;
    private int quizTime;
    private List<Long> userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime expiredTime;
    private String status;
    private int numberQuestions;
    private String score;

    private List<Question> questions ;
}
