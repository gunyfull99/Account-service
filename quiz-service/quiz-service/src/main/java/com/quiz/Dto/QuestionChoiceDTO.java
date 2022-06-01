package com.quiz.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionChoiceDTO {
    private long id;
    private String name;
    private boolean isTrue;
    private String text;
    private String userAnswer;
}
