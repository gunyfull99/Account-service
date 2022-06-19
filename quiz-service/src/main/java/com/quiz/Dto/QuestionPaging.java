package com.quiz.Dto;

import com.quiz.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPaging {
    int total;
    List<QuestDTO> questions;
    int page;
    int limit;
    Long cateId;
    String search;
    Long typeId;

    public  QuestionPaging(int totalElements,List<QuestDTO> questions){
        this.total=totalElements;
        this.questions=questions;
    }

    public  QuestionPaging(int totalElements,List<QuestDTO> questions,int page,int limit,Long cateId,String search){
        this.total=totalElements;
        this.questions=questions;
        this.page=page;
        this.limit=limit;
        this.cateId=cateId;
        this.search=search;
    }

    public  QuestionPaging(int totalElements,List<QuestDTO> questions,int page,int limit,String search,Long typeId){
        this.total=totalElements;
        this.questions=questions;
        this.page=page;
        this.limit=limit;
        this.typeId=typeId;
        this.search=search;
    }

}
