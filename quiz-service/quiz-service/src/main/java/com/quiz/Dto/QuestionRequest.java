package com.quiz.Dto;

import com.quiz.entity.Category;
import com.quiz.entity.QuestionChoice;
import com.quiz.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {

    private String content;

    private QuestionType questionType;

    private Category category;

    private int questionTime;

    private List<QuestionChoice> questionChoice = new ArrayList<>();
}
