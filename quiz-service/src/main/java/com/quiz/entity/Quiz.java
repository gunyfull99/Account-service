package com.quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz")
public class Quiz  {

    @Id
    @SequenceGenerator(name = "quiz_generator", sequenceName = "quiz_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_generator")
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
    private long creator;
    private String cate;
    @ManyToMany(fetch = EAGER)
    private List<Question> questions = new ArrayList<>();
    private long userStartQuiz=0;

    @ManyToOne
    @JoinColumn(name = "group_quiz_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private GroupQuiz groupQuiz;

}
