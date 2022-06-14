package com.quiz.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_quiz")
public class GroupQuiz implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "GroupQuiz_generator", sequenceName = "GroupQuiz_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupQuiz_generator")
    private Long id;

    private String description;
    private LocalDateTime createDate;
    private LocalDateTime startTime;
    private LocalDateTime expiredTime;
    private String creator;
    private String cate;

    @OneToMany(mappedBy = "groupQuiz", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Quiz> quiz;
}
