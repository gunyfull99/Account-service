package com.quiz.Dto;

import com.quiz.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPaging {
    int total;
    List<QuestDTO> questions;
    int page;
    int limit;
    long cateId;

    public  QuestionPaging(int totalElements,List<QuestDTO> questions){
        this.total=totalElements;
        this.questions=questions;
    }

}
