package com.quiz.Dto;

import com.quiz.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestionForm {
    private long questions_id;
    private long quiz_id;
    private String user_answer;
    private Question question;
}
