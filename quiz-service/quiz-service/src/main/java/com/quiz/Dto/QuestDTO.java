package com.quiz.Dto;

import com.quiz.entity.Category;
import com.quiz.entity.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestDTO {
    private  long quiz_id;
    private  long questions_id;
    private String content;

    private QuestionType questionType;

    private Category category;

    private int questionTime;

    private List<QuestionChoiceDTO> questionChoiceDTOs;
    private long company_id;
    private boolean isPublic;
    long userStartQuiz;
}
