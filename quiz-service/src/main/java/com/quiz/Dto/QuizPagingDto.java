package com.quiz.Dto;

import com.quiz.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizPagingDto {
    private long id;

    private String description;
    private int quizTime;
    private long userId;
    private LocalDateTime createDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime expiredTime;
    private String status;
    private int numberQuestions;
    private String score;
    private String creator;
    private String cate;
    private List<Question> questions = new ArrayList<>();
    private long userStartQuiz=0;
}
