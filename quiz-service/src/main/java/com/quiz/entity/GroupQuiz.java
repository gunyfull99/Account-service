package com.quiz.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_quiz")
public class GroupQuiz {
    @Id
    @SequenceGenerator(name = "GroupQuiz_generator", sequenceName = "GroupQuiz_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupQuiz_generator")
    private long id;

    private String description;
    private LocalDateTime createDate;
    private LocalDateTime startTime;
    private LocalDateTime expiredTime;
    private long creator;
    private String cate;

    @OneToMany(mappedBy = "groupQuiz", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Quiz> quiz;
}
