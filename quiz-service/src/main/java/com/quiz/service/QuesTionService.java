package com.quiz.service;

import com.quiz.Dto.*;
import com.quiz.entity.Question;
import com.quiz.entity.QuestionChoice;
import com.quiz.entity.QuestionType;
import com.quiz.entity.QuizQuestion;
import com.quiz.repository.CategoryRepository;
import com.quiz.repository.QuestionChoiceRepository;
import com.quiz.repository.QuestionRepository;
import com.quiz.repository.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class QuesTionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    QuizService quizService;

    @Autowired
    QuestionChoiceRepository questionChoiceRepository;
    @Autowired
    QuestionTypeRepository questionTypeRepository;

    private static final Logger logger = LoggerFactory.getLogger(QuesTionService.class);

    public List<Question> getAllQuestionByCate(long id) {
        logger.info("Receive id to get All Question By Cate");

        return questionRepository.getAllQuestionByCateToCreateQuiz(id);
    }

    public List<Question> getAllQuestionText(long id) {
        logger.info("Receive id to get All Question Text");

        return questionRepository.getAllQuestionText(id);
    }

    public List<Question> findAllById(long id) {
        logger.info("Receive id to find All question By Id");


        return questionRepository.findAllById(Collections.singleton(id));
    }

    public Question getQuestionById(long id) {
        logger.info("Receive id to get Question By Id");

        return questionRepository.getById(id);
    }

    public String blockQuestion(List<Long> listId) {
        logger.info("Receive id to block Question");

        for (int i = 0; i < listId.size(); i++) {
            questionRepository.blockQuestion(listId.get(i));
        }
        return "Block question success";
    }

    public String openQuestion(List<Long> listId) {
        logger.info("Receive id to open Question");

        for (int i = 0; i < listId.size(); i++) {
            questionRepository.openQuestion(listId.get(i));

        }
        return "Open question success";
    }

    public void createQuestion(QuestionRequest request) {
        logger.info("Receive info of question {} to create", request.getContent());


        if (questionRepository.findByContent(request.getContent()) != null) {
            logger.error("this question was crate before !!!");

            throw new RuntimeException("this question was crate before !!!");
        }
        ModelMapper mapper = new ModelMapper();
        Question questionEntity = mapper.map(request, Question.class);
        Question q1 = questionRepository.save(questionEntity);
        List<QuestionChoice> q = request.getQuestionChoice();
        for (int i = 0; i < q.size(); i++) {
            q.get(i).setQuestion(q1);
            questionChoiceRepository.save(q.get(i));
        }


    }

    public List<QuestionRequest> getAllQuestion() {
        logger.info("get all question");

        List<Question> question = questionRepository.getAllQuestion();
        if (question.isEmpty()) {
            logger.error("no question. please input new question !!!");
            throw new RuntimeException("no question. please input new question !!!");
        }
        List<QuestionRequest> questionRequests = new ArrayList<>();
        for (Question question1 : question) {
            QuestionRequest request = new QuestionRequest();
            request.setContent(question1.getContent());
            request.setQuestionType(question1.getQuestionType());
            request.setCategory(question1.getCategory());
            request.setQuestionChoice(question1.getQuestionChoice());
            request.setQuestionTime(question1.getQuestionTime());
            questionRequests.add(request);
        }
        return questionRequests;
    }

    public List<QuestionRequest> getAllQuestionBlock() {
        logger.info("get all question block");

        List<Question> question = questionRepository.getAllQuestionBlock();
        if (question.isEmpty()) {
            logger.error("no question block. please add new question block!!!");
            throw new RuntimeException("no question block. please add new question block!!!");
        }
        List<QuestionRequest> questionRequests = new ArrayList<>();
        for (Question question1 : question) {
            QuestionRequest request = new QuestionRequest();
            request.setContent(question1.getContent());
            request.setQuestionType(question1.getQuestionType());
            request.setCategory(question1.getCategory());
            request.setQuestionChoice(question1.getQuestionChoice());
            request.setQuestionTime(question1.getQuestionTime());
            questionRequests.add(request);
        }
        return questionRequests;
    }


    public void editQuestion(QuestionEditRequest request) {
        logger.info("Receive info of question {} to edit", request.getContent());

        Question questionEntity = questionRepository.getDetailQuestion(request.getId());
        if (questionEntity == null) {
            logger.error("this question not exist or wrong id!!!");
            throw new RuntimeException("this question not exist or wrong id!!!");
        }
        questionEntity.setContent(request.getContent());
        questionEntity.setQuestionType(request.getQuestionType());
        questionEntity.setCategory(request.getCategory());
        questionEntity.setQuestionChoice(request.getQuestionChoice());
        questionEntity.setQuestionTime(request.getQuestionTime());
        questionRepository.save(questionEntity);
    }

    public QuestionPaging getQuestionByCategory(QuestionPaging questionPaging) {
        logger.info("Receive info of question {} to edit", questionPaging.getCateId());
        Pageable pageable = PageRequest.of(questionPaging.getOffset() - 1, questionPaging.getPageSize());
        Page<Question> questionEntity = questionRepository.findAllByCategoryId(questionPaging.getCateId(), pageable);
        List<QuestDTO> questionRequests = new ArrayList<>();

        for (Question question : questionEntity.getContent()) {
            ModelMapper mapper = new ModelMapper();
            QuestDTO request = mapper.map(question, QuestDTO.class);
            List<QuestionChoiceDTO> questionChoiceDTOS = new ArrayList<>();
            for (QuestionChoice questionChoice : question.getQuestionChoice()) {
                ModelMapper mapper1 = new ModelMapper();
                QuestionChoiceDTO questionChoiceDTO = mapper1.map(questionChoice, QuestionChoiceDTO.class);
                questionChoiceDTOS.add(questionChoiceDTO);
            }
            request.setQuestions_id(question.getId());
            request.setQuestionChoiceDTOs(questionChoiceDTOS);
            request.setQuestionTime(question.getQuestionTime());
            questionRequests.add(request);
        }
        return new QuestionPaging((int) questionEntity.getTotalElements(), questionRequests);
    }

    public List<QuestDTO> getListQuestionByQuizId(long id) {
        logger.info("Receive id to get List Question By Quiz Id", id);

        List<QuizQuestion> list = quizService.getListQuestionByQuizId(id);
        if (list.isEmpty()) {
            logger.error("this list question is not exist !!!");
            throw new RuntimeException("this list question is not exist !!!");
        }
        List<Question> questionEntity = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Question q = questionRepository.getById(list.get(i).getQuestions_id());
            questionEntity.add(q);
        }
        List<QuestDTO> questionRequests = new ArrayList<>();

        for (Question question : questionEntity) {
            ModelMapper mapper = new ModelMapper();
            QuestDTO request = mapper.map(question, QuestDTO.class);
            request.setQuiz_id(list.get(0).getQuiz_id());
            request.setQuestions_id(question.getId());
            List<QuestionChoiceDTO> questionChoiceDTOS = new ArrayList<>();
            for (QuestionChoice questionChoice : question.getQuestionChoice()) {
                QuestionChoiceDTO questionChoiceDTO = new QuestionChoiceDTO();
                questionChoiceDTO.setId(questionChoice.getId());
                questionChoiceDTO.setName(questionChoice.getName());
                questionChoiceDTOS.add(questionChoiceDTO);
            }
            request.setQuestionChoiceDTOs(questionChoiceDTOS);
            request.setQuestionTime(question.getQuestionTime());
            questionRequests.add(request);
        }
        return questionRequests;
    }

    public void createQuestionType(QuestionTypeRequest type) {
        QuestionType questionType = new QuestionType();
        questionType.setName(type.getName());
        questionTypeRepository.save(questionType);
    }

    public List<QuestionType> getAllQuestionType() {
        List<QuestionType> question = questionTypeRepository.getAllQuestionType();
        return question;
    }

}
