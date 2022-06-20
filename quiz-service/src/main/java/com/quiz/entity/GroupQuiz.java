package com.quiz.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group_quiz")
public class GroupQuiz  {
    @Id
    @SequenceGenerator(name = "GroupQuiz_generator", sequenceName = "GroupQuiz_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupQuiz_generator")
    private Long id;

    private String description;
    private Date createDate;
    private Date startTime;
    private Date expiredTime;
    private String creator;
    private String cate;

    @OneToMany(mappedBy = "groupQuiz")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Quiz> quiz;
}
