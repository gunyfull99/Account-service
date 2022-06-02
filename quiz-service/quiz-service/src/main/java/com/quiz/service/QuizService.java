package com.quiz.service;

import com.quiz.Dto.*;
import com.quiz.entity.Question;
import com.quiz.entity.QuestionChoice;
import com.quiz.entity.Quiz;
import com.quiz.entity.QuizQuestion;
import com.quiz.exception.ResourceBadRequestException;
import com.quiz.repository.CategoryRepository;
import com.quiz.repository.QuestionChoiceRepository;
import com.quiz.repository.QuizQuestionRepository;
import com.quiz.repository.QuizRepository;
import com.quiz.restTemplate.RestTemplateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static javax.persistence.FetchType.EAGER;

@Service
@RequiredArgsConstructor
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;
    @Autowired
    private QuesTionService quesTionService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    @Autowired
    RestTemplateService restTemplateService;

    private static final Logger logger = LoggerFactory.getLogger(QuesTionService.class);


    public Quiz save(Quiz entity) {
        return quizRepository.save(entity);
    }

    public Quiz createQuiz(Quiz quiz) throws ResourceBadRequestException {
        logger.info("receive info to create quiz");

        Quiz quiz1 = new Quiz();
        LocalDateTime date1 = quiz.getStartTime();
        LocalDateTime date2 = quiz.getExpiredTime();
        if (date1.isAfter(date2)) {
            throw new ResourceBadRequestException(new BaseResponse(80805, "StartTime must before ExpireTime"));
        } else {
            quiz1.setDescription(quiz.getDescription());
            quiz1.setCreateDate(quiz.getCreateDate());
            quiz1.setStartTime(quiz.getStartTime());
            quiz1.setExpiredTime(quiz.getExpiredTime());
            quiz1.setStatus(quiz.getStatus());
            quiz1.setUserId(quiz.getUserId());
            quiz1.setCreator(quiz.getCreator());
            return quizRepository.save(quiz1);
        }
    }

    public Quiz getDetailQuiz(Long id) {
        logger.info("receive info to create quiz");

        Quiz quiz = quizRepository.findById(id).get();
        quiz.setQuestions(null);
        return quiz;
    }


    public String addQuesToQuiz(CreateQuizForm form) {
        logger.info("receive info to add Question To Quiz");
        String cate="";
        for (int k = 0; k < form.getQuiz().getUserId().size(); k++) {

            Quiz quiz1 = new Quiz(form.getQuiz().getId(), form.getQuiz().getDescription(), form.getQuiz().getQuizTime(),
                    form.getQuiz().getUserId().get(k), LocalDateTime.now(),form.getQuiz().getStartTime(), form.getQuiz().getEndTime(),
                    form.getQuiz().getExpiredTime(), form.getQuiz().getStatus(), form.getQuiz().getNumberQuestions()
                    , form.getQuiz().getScore(),form.getQuiz().getCreator(),cate,form.getQuiz().getQuestions(),form.getQuiz().getUserStartQuiz()
            );
            Quiz quiz = createQuiz(quiz1);
            int numberQuestion = 0;
            int totalTime = 0;
            List<Question> q = new ArrayList<>();


            for (int i = 0; i < form.getTopics().size(); i++) {
                String getCateName=categoryRepository.getById(form.getTopics().get(i).getCate()).getName().toUpperCase();
                cate= i==0 ? getCateName : cate+","+getCateName;

                List<Question> hasTag1 = quesTionService.getAllQuestionByCate(form.getTopics().get(i).getCate());
                Collections.shuffle(hasTag1);
                numberQuestion += form.getTopics().get(i).getQuantity();
                if (form.getTopics().get(i).getQuantity() > hasTag1.size()) {
                    throw new ResourceBadRequestException(new BaseResponse(80808, "Not enough question for topic " + i));
                }
                for (int j = 0; j < form.getTopics().get(i).getQuantity(); j++) {

                    q.add(hasTag1.get(j));
                    totalTime += hasTag1.get(j).getQuestionTime();
                }
                if (form.getTopics().get(i).getText() != null) {
                    List<Question> listText = quesTionService.getAllQuestionText(form.getTopics().get(i).getCate());
                    Collections.shuffle(listText);
                    for (int e = 0; e < form.getTopics().get(i).getQuantityText(); e++) {
                        q.add(listText.get(e));
                    }
                }
            }
            quiz.setNumberQuestions(numberQuestion);
            quiz.setQuizTime(totalTime);
            quiz.setCate(cate);
            quizRepository.save(quiz);

            for (int i = 0; i < q.size(); i++) {
                QuizQuestion q1 = new QuizQuestion();
                q1.setQuestions_id(q.get(i).getId());
                q1.setQuiz_id(quiz.getId());
                q1.setUser_answer("not yet");
                quizQuestionRepository.save(q1);
            }
        }

        return "Create success";
    }

    public Quiz calculateScore(List<QuestDTO> questDTO) {
        logger.info("receive info to calculate Score");

        int score = 0;
        float percent = 0;
        String user_answer = "";
        List<QuizQuestion> questionIds = quizQuestionRepository.getListQuestionByQuizId(questDTO.get(0).getQuiz_id());
        for (int i = 0; i < questDTO.size(); i++) {
            if (questDTO.get(i).getQuestionType().getId() == 2) {
                int count = questionChoiceRepository.countCorrect(questDTO.get(i).getQuestions_id());
                int count1 = 0;
                for (int j = 0; j < questDTO.get(i).getQuestionChoiceDTOs().size(); j++) {
                    user_answer = user_answer + " ; " + questDTO.get(i).getQuestionChoiceDTOs().get(j).getId();

                    if (questionChoiceRepository.checkCorrectAnswer(questDTO.get(i).getQuestionChoiceDTOs().get(j).getId()) == true) {
                        count1 += 1;
                    }
                }
                String a = user_answer.replaceFirst(";", "").trim();

                if (count == count1) {
                    score += 1;
                    questionIds.get(i).setUser_answer(a);
                }
            } else if (questDTO.get(i).getQuestionType().getId() == 1) {
                questionIds.get(i).setUser_answer(questDTO.get(i).getQuestionChoiceDTOs().get(0).getId() + "");

                if (questionChoiceRepository.checkCorrectAnswer(questDTO.get(i).getQuestionChoiceDTOs().get(0).getId()) == true
                ) {
                    score += 1;
                }
            } else {
                questionIds.get(i).setUser_answer(questDTO.get(i).getQuestionChoiceDTOs().get(0).getText());
            }
            quizQuestionRepository.save(questionIds.get(i));
        }

        Quiz quiz = quizRepository.findById(questDTO.get(0).getQuiz_id()).get();
        float per = ((float) score / (float) quiz.getNumberQuestions()) * 100;
        percent = (float) (Math.round(per * 100.0) / 100.0);
        quiz.setScore((score + "/" + quiz.getNumberQuestions() + "  (" + percent + "%)"));
        quiz.setStatus("done");
        quiz.setEndTime(LocalDateTime.now());
        quizRepository.save(quiz);
        quiz.setQuestions(null);
        return quiz;
    }

    public List<Quiz> getListQuizByUserWhenDone(long id) {
        logger.info("receive info to get List Quiz By User When Done");

        List<Quiz> list = quizRepository.getQuizByUserWhenDone(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestions(null);
        }
        return list;
    }

    public List<Quiz> getAllQuizByUser(long id) {
        logger.info("receive info to get All Quiz By User");

        List<Quiz> list = quizRepository.getAllByUser(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestions(null);
        }
        return list;
    }

    public List<Quiz> getListQuizNotStart(long id) {

        logger.info("receive info to get List Quiz Not Start");

        List<Quiz> list = quizRepository.getQuizNotStart(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestions(null);
        }
        return list;
    }

//    public QuizPaging getListQuizPaging(QuizPaging quizPaging) {
//
//        logger.info("receive info to get List Quiz");
//        Pageable pageable = PageRequest.of(quizPaging.getPage() - 1, quizPaging.getLimit());
//        Page<Quiz> list = null;
//        if(quizPaging.getUserId()==0 && quizPaging.getStatus()==null){
//            list=quizRepository.findAll(pageable);
//        }else if(quizPaging.getUserId()==0 && quizPaging.getStatus()!=null){
//            list=quizRepository.findAllByStatus(quizPaging.getStatus(),pageable);
//        }else if(quizPaging.getUserId()!=0 && quizPaging.getStatus()==null){
//            list=quizRepository.findAllByUserId(quizPaging.getUserId(),pageable);
//        }else{
//            list=quizRepository.findAllByUserIdAndStatus(quizPaging.getUserId(), quizPaging.getStatus(),pageable);
//        }
//
//        for (int i = 0; i < list.getContent().size(); i++) {
//            list.getContent().get(i).setQuestions(null);
//        }
//        QuizPaging qp=new QuizPaging((int) list.getTotalElements(),list.getContent(),quizPaging.getPage(),quizPaging.getLimit());
//        return qp;
//    }


    public QuizPaging getListQuizPaging(QuizPaging quizPaging) {

        logger.info("receive info to get List Quiz");
        Pageable pageable = PageRequest.of(quizPaging.getPage() - 1, quizPaging.getLimit());
        Page<Quiz> list = null;
        List<Long>listUserId=restTemplateService.getListUserId(quizPaging.getKeywords()==null||quizPaging.getKeywords().equals("")? " " : quizPaging.getKeywords());
        list=quizRepository.filterWhereNoUserId(quizPaging.getStatus()==null || quizPaging.getStatus().trim().equals("") ? "%%" : quizPaging.getStatus(),
                    quizPaging.getCate(),
                    quizPaging.getKeywords(),
                    listUserId,
                    pageable);
        for (int i = 0; i < list.getContent().size(); i++) {
            list.getContent().get(i).setQuestions(null);
        }
        QuizPaging qp=new QuizPaging((int) list.getTotalElements(),list.getContent(),quizPaging.getPage(),quizPaging.getLimit());
        return qp;
    }

    public List<QuizQuestion> getListQuestionByQuizId(long quizId) {
        logger.info("receive info to get List Question By QuizId");

        return quizQuestionRepository.getListQuestionByQuizId(quizId);
    }

    public List<QuestionChoice> getListChoiceByQuestionId(long id) {
        logger.info("receive info to get List Choice By QuestionId");

        List<QuestionChoice> list = questionChoiceRepository.getListChoiceByQuesId(id);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setQuestion(null);
        }
        return list;
    }

    public List<AccountDto> getUserDidTheTest() {
        logger.info("receive info to get User Did The Test");

        List<Integer> userId = quizRepository.getIdByStatus();
        List<AccountDto> user = new ArrayList<>();
        for (Integer integer : userId) {
            AccountDto o = restTemplateService.getName(integer);
            user.add(o);
        }
        return user;
    }
//    public List<Object> getName() {
//        List<Integer> accountId = quizRepository.getAllId();
//        List<Object> list = new ArrayList<>();
//        for (Integer integer : accountId) {
//            Object o = restTemplateService.getName(integer);
//            list.add(o);
//        }
//        return list;
//    }
}
