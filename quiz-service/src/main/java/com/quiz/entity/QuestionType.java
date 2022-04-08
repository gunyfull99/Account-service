package com.quiz.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questionType")
public class QuestionType {
    @Id
    @SequenceGenerator(name = "questionT_generator", sequenceName = "questionT_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "questionT_generator")
    private long id;
    private String name;
}
