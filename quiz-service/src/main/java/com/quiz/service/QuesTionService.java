package com.quiz.service;

import com.quiz.Dto.*;
import com.quiz.entity.*;
import com.quiz.exception.ResourceBadRequestException;
import com.quiz.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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
    QuizRepository quizRepository;

    @Autowired
    QuizQuestionRepository quizQuestionRepository;

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
            request.setPublic(question1.isPublic());
            request.setCompany_id(question1.getCompany_id());
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
            request.setPublic(question1.isPublic());
            request.setCompany_id(question1.getCompany_id());
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
        questionEntity.setQuestionType(questionTypeRepository.getById(request.getQuestionTypeId()));
        questionEntity.setCategory(categoryRepository.getById(request.getCateId()));
        questionEntity.setQuestionChoice(request.getQuestionChoice());
        questionEntity.setQuestionTime(request.getQuestionTime());
        questionEntity.setPublic(request.isPublic());
        questionEntity.setCompany_id(request.getCompany_id());
        questionRepository.save(questionEntity);
    }

    public QuestionPaging getQuestionPaging(QuestionPaging questionPaging) {
        logger.info("Receive info of question {} to edit", questionPaging.getCateId());
        Pageable pageable = PageRequest.of(questionPaging.getPage() - 1, questionPaging.getLimit(), Sort.by("id").descending());
        Page<Question> questionEntity = null;
        String search = "";
        if (questionPaging.getSearch() == null) {
            search = "";
        } else {
            search = questionPaging.getSearch();
        }

        if (questionPaging.getTypeId() == 0 && questionPaging.getCateId() == 0) {
            questionEntity = questionRepository.findAllByContentContainingIgnoreCaseAndIsActive(search, true,pageable);
        } else if (questionPaging.getCateId() == 0) {
            questionEntity = questionRepository.findAllByQuestionTypeIdAndContentContainingIgnoreCaseAndIsActive(questionPaging.getTypeId(), search,true, pageable);
        } else if (questionPaging.getTypeId() == 0) {
            questionEntity = questionRepository.findAllByCategoryIdAndContentContainingIgnoreCaseAndIsActive(questionPaging.getCateId(),
                    search,true,
                    pageable);
        } else {
            questionEntity = questionRepository.findAllByQuestionTypeIdAndCategoryIdAndContentContainingIgnoreCaseAndIsActive(
                    questionPaging.getTypeId(), questionPaging.getCateId(), search,true, pageable
            );
        }
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
        return new QuestionPaging((int) questionEntity.getTotalElements(), questionRequests, questionPaging.getPage(), questionPaging.getLimit(), questionPaging.getCateId(), questionPaging.getSearch());
    }

    public QuestionPaging getQuestionByQuestionType(QuestionPaging questionPaging) {
        logger.info("Receive info of question {} to edit", questionPaging.getCateId());
        Pageable pageable = PageRequest.of(questionPaging.getPage() - 1, questionPaging.getLimit());
        Page<Question> questionEntity = null;
        if (questionPaging.getSearch() == null || questionPaging.getSearch().trim().equals("")) {
            questionEntity = questionRepository.findAllByQuestionTypeId(questionPaging.getTypeId(), pageable);
        } else {
            questionEntity = questionRepository.findAllByQuestionTypeIdAndContentContainingIgnoreCaseAndIsActive(questionPaging.getTypeId(),
                    questionPaging.getSearch(),true,
                    pageable);
        }
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
        return new QuestionPaging((int) questionEntity.getTotalElements(), questionRequests, questionPaging.getPage(), questionPaging.getLimit(), questionPaging.getSearch(), questionPaging.getTypeId());
    }

    public List<QuestDTO> getListQuestionByQuizId(long id,boolean isView) {
        logger.info("Receive id to get List Question By Quiz Id", id);
        boolean view = isView;

        List<QuizQuestion> list = quizService.getListQuestionByQuizId(id);
        if (list.isEmpty()) {
            logger.error("this list question is not exist !!!");
            throw new RuntimeException("this list question is not exist !!!");
        }
        List<Question> questionEntity = new ArrayList<>();
        Quiz quiz = quizRepository.findById(id).get();
        long timeStart = quiz.getUserStartQuiz();

        if (timeStart == 0 && !isView) {
            timeStart=System.currentTimeMillis();
            quiz.setUserStartQuiz(timeStart);
            quizService.save(quiz);
        }

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
            String userAnswer=quizQuestionRepository.getUserAnswer(id,question.getId());
            List<QuestionChoiceDTO> questionChoiceDTOS = new ArrayList<>();
            for (QuestionChoice questionChoice : question.getQuestionChoice()) {
                QuestionChoiceDTO questionChoiceDTO = new QuestionChoiceDTO();
                questionChoiceDTO.setId(questionChoice.getId());
                questionChoiceDTO.setName(questionChoice.getName());
                questionChoiceDTO.setUserAnswer(questionChoice.getUserAnswer());
                if(isView){
                    if(userAnswer.equals(questionChoice.getId()+"")){
                        questionChoiceDTO.setUserAnswer(userAnswer);
                    }
                    questionChoiceDTO.setTrue(questionChoice.isTrue());
                }
                questionChoiceDTOS.add(questionChoiceDTO);
            }
            request.setQuestionChoiceDTOs(questionChoiceDTOS);
            request.setQuestionTime(question.getQuestionTime());
            request.setUserStartQuiz(timeStart);
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

    public void createQuestion(QuestionRequest request) {
        logger.info("Receive info of question {} to create", request.getContent());
//        if (questionRepository.findByContent(request.getContent()) != null) {
//            logger.error("this question was crate before !!!");
//            throw new RuntimeException("this question was crate before !!!");
//        }
        ModelMapper mapper = new ModelMapper();
        Question questionEntity = mapper.map(request, Question.class);
        Question q1 = questionRepository.save(questionEntity);
        List<QuestionChoice> q = request.getQuestionChoice();
        for (int i = 0; i < q.size(); i++) {
            q.get(i).setQuestion(q1);
            questionChoiceRepository.save(q.get(i));
        }
    }

    public void updateListChoice(Question q1, List<QuestionChoice> q) {
        for (int i = 0; i < q.size(); i++) {
            q.get(i).setQuestion(q1);
            questionChoiceRepository.save(q.get(i));
        }
    }

    public BaseResponse excelImport(MultipartFile file) throws ResourceBadRequestException, IOException {
        String content = null;
        QuestionType questionType = null;
        Category category = null;
        int questionTime = 0;
        List<QuestionChoice> questionChoice = new ArrayList<>();
        long company_id = 0;
        boolean isPublic = true;
        long start = System.currentTimeMillis();

        Path tempDir = Files.createTempDirectory("");
        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);
        try {
            Workbook workbook = new XSSFWorkbook(tempFile);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                QuestionChoice qc = new QuestionChoice();
                boolean isBlank = true;
                String answer = "null1";
                boolean answerIsTrue = false;
                QuestionChoice qc1 = new QuestionChoice();
                int check = 0;
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    String cellValue = "null1";
                    long cellNumber=0;
                    boolean cellBool = false;
                    CellType type = nextCell.getCellType();
                    if (type == CellType.STRING) {
                        cellValue = nextCell.getStringCellValue();
                    } else if (type == CellType.NUMERIC) {
                        cellNumber = (long) nextCell.getNumericCellValue();
                    } else if(type == CellType.BOOLEAN) {
                        cellBool = nextCell.getBooleanCellValue();
                    }else if(type==CellType.BLANK){
                        cellValue = "null1";
                    }

                    if (columnIndex == 0 && cellNumber!=0
                    ) {
                        isBlank = false;
                        check = 1;
                    } else if (columnIndex == 3 && isBlank == true) {

                        if (cellValue.equals("null1") == false) {
                            answer = cellValue;
                        } else {
                            Question q = questionRepository.getLastQuestion();
                            updateListChoice(q, questionChoice);
                            questionChoice.clear();
                        }
                    } else if (columnIndex == 4 && isBlank == true) {
                        if (cellValue.equals("null1") == false) {
                            answerIsTrue = true;
                        }
                        if (answer.equals("null1") == false) {
                            qc.setName(answer);
                            qc.setTrue(answerIsTrue);
                            questionChoice.add(qc);
                        }
                    } else if (columnIndex == 4 && isBlank == false) {
                        boolean isTrue = true;
                        if (nextCell.getStringCellValue() == null || nextCell.getStringCellValue().trim().equals("")) {
                            isTrue = false;
                        }
                        qc1.setTrue(isTrue);
                    } else if (check == 1) {
                        switch (columnIndex) {
                            case 1:
                                String nameCate = nextCell.getStringCellValue();
                                category = categoryRepository.findByNameIgnoreCase(nameCate);
                                break;
                            case 2:
                                content = nextCell.getStringCellValue();
                                break;
                            case 3:
                                qc1.setName(nextCell.getStringCellValue());
                                break;
                            case 4:
                                boolean isTrue = true;
                                if (nextCell.getStringCellValue() == null || nextCell.getStringCellValue().trim().equals("")) {
                                    isTrue = false;
                                }
                                qc1.setTrue(isTrue);
                                break;
                            case 5:
                                questionTime = (int) nextCell.getNumericCellValue();
                                break;
                            case 6:
                                questionType = new QuestionType();
                                questionType.setId((long) nextCell.getNumericCellValue());
                                break;
                            case 7:
                                company_id = (long) nextCell.getNumericCellValue();
                                break;
                            case 8:
                                isPublic = (boolean) nextCell.getBooleanCellValue();
                                break;
                        }
                    }
                }
                if (isBlank == false) {
                    questionChoice.add(qc1);
                    QuestionRequest qr = new QuestionRequest(content, questionType, category, questionTime, questionChoice, company_id, isPublic);
                    createQuestion(qr);
                }
            }
            workbook.close();
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));
        } catch (Exception e) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Tạo câu hỏi thất bại"));
        }
        return new BaseResponse(200,"Tạo câu hỏi thành công");
    }
}
