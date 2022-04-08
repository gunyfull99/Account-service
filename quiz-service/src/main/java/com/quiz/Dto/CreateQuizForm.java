package com.quiz.Dto;

import com.quiz.entity.Question;
import com.quiz.entity.Quiz;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CreateQuizForm {
    private long hasTag1;
    private long hasTag2;
    private long hasTag3;
    private int quantity1;
    private int quantity2;
    private int quantity3;
    private Quiz quiz;

}
