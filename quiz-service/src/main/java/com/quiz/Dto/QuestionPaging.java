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
    int totalElements;
    List<QuestDTO> questions;
    int offset;
    int pageSize;
    long cateId;

    public  QuestionPaging(int totalElements,List<QuestDTO> questions){
        this.totalElements=totalElements;
        this.questions=questions;
    }

}
