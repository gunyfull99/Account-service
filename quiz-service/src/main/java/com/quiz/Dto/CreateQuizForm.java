package com.quiz.Dto;

import com.quiz.entity.Quiz;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuizForm {
    List<Topic> topics;
    private QuizDto quiz;


}
