package com.quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @SequenceGenerator(name = "question_generator", sequenceName = "question_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_generator")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private Category category;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionChoice> questionChoice=new ArrayList<>();
    private  int questionTime;
    private boolean isActive =true;
    private long company_id;
    private boolean isPublic;


}
