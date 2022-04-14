package com.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questionChoice")
public class QuestionChoice {
    @Id
    @SequenceGenerator(name = "questionC_generator", sequenceName = "questionC_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionC_generator")
    private long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String name;

    private boolean isTrue=true;
}
