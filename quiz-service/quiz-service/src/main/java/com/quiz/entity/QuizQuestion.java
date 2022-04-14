package com.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_questions")
public class QuizQuestion {

    @Id
    @SequenceGenerator(name = "questionz_generator", sequenceName = "questionz_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionz_generator")
    private long id;

    private long questions_id;
    private long quiz_id;
    private String user_answer;
}

