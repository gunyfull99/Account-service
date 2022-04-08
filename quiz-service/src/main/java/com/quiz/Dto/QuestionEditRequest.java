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
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEditRequest {
    private Long id;

    private String content;

    private QuestionType questionType;

    private Category category;

    private List<QuestionChoice> questionChoice = new ArrayList<>();

    private int questionTime;
}
