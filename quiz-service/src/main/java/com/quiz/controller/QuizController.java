package com.quiz.controller;

import com.quiz.Dto.*;
import com.quiz.config.ResponseError;
import com.quiz.entity.*;
import com.quiz.exception.ResourceBadRequestException;
import com.quiz.exception.ResourceForbiddenRequestException;
import com.quiz.restTemplate.RestTemplateService;
import com.quiz.service.CategoryService;
import com.quiz.service.NomineeService;
import com.quiz.service.QuesTionService;
import com.quiz.service.QuizService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final long perQuiz = 1;
    private final long perQuestion = 2;

    @Autowired
    private QuizService quizService;

    @Autowired
    private RestTemplateService templateService;
    @Autowired
    private ResponseError r;
    @Autowired
    CategoryService categoryService;

    @Autowired
    QuesTionService quesTionService;

    @Autowired
    NomineeService nomineeService;

    //http://localhost:8080/quiz/createquestion
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/createquestion")
    public void createQuestion(@RequestBody QuestionRequest request) {
//        if(templateService.getCanCreate(perQuiz)==false){
//            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
//        }
        quesTionService.createQuestion(request);
    }


    //http://localhost:8080/quiz/editquestion
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PutMapping("/editquestion")
    public BaseResponse editQuestion(@RequestBody QuestionEditRequest request
                                     //     , @RequestHeader("Authorization") String token
    ) {
//
//        if (templateService.getCanUpdate(perQuiz, token) == false) {
//            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
//        }
        quesTionService.editQuestion(request);
        return new BaseResponse(200, "Update question thành công");
    }

    //http://localhost:8080/quiz/blockquestion
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PutMapping("/blockquestion")
    public BaseResponse deleteQuestion(@RequestBody List<Long> id) {
        return quesTionService.blockQuestion(id);
    }


    //http://localhost:8080/quiz/openquestion/
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PutMapping("/openquestion")
    public BaseResponse openQuestion(@RequestBody List<Long> id) {
        return quesTionService.openQuestion(id);
    }

    //http://localhost:8080/quiz/getquestionpaging
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/getquestionpaging")
    public QuestionPaging getQuestionByCategory(@RequestBody QuestionPaging questionPaging
                                                //, @RequestHeader("Authorization") String token
    ) {
        //  if (templateService.getCanRead(perQuestion, token) == false) {
        //   throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
        // }
        return quesTionService.getQuestionPaging(questionPaging);
    }

    //http://localhost:8080/quiz/getquestionbytype
//    @CrossOrigin(origins = "http://localhost:8080/quiz")
//    @PostMapping("/getquestionbytype")
//    public QuestionPaging getQuestionByType(@RequestBody QuestionPaging questionPaging
////            , @RequestHeader("Authorization") String token
//    ) {
////        if (templateService.getCanRead(perQuestion, token) == false) {
////            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
////        }
//        return quesTionService.getQuestionByQuestionType(questionPaging);
//    }

    //http://localhost:8080/quiz/getAllQuestion
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/getAllQuestion")
    public List<QuestionRequest> getAllQuestion(
            //  @RequestHeader("Authorization") String token
    ) {
//        if (templateService.getCanRead(perQuestion, token) == false) {
//            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
//        }
        return quesTionService.getAllQuestion();
    }

    //http://localhost:8080/quiz/getAllQuestionBlock
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/getAllQuestionBlock")
    public List<QuestionRequest> getAllQuestionBlock
    (
            // @RequestHeader("Authorization") String token
    ) {
//        if (templateService.getCanRead(perQuestion, token) == false) {
//            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
//        }
        return quesTionService.getAllQuestionBlock();
    }

    //http://localhost:8080/quiz/createCategory
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/createCategory")
    public void createCategory(@RequestBody CategoryRequest category) {
        categoryService.createCategory(category);
    }

    //http://localhost:8080/quiz/editCategory
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PutMapping("/editCategory")
    public void editCategory(@RequestBody CategoryEditRequest category) {
        categoryService.editCategory(category);
    }

    //    //http://localhost:8080/quiz/getAllCategory
//    @GetMapping("/getAllCategory")
//    public List<CategoryRequest> getAllCategory() {
//        return categoryService.getAllCategory();
//    }
    //http://localhost:8080/quiz/cate/list
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/cate/list")
    public List<Category> getAllCate() {
        return categoryService.getAllCate();
    }

    //http://localhost:8080/quiz/getAllQuestionType
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/getAllQuestionType")
    public List<QuestionType> getAllQuestionType(
//            @RequestHeader("Authorization") String token
    ) {
//        if (templateService.getCanRead(perQuestion, token) == false) {
//            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
//        }
        return quesTionService.getAllQuestionType();
    }

    //http://localhost:8080/quiz/createquesiontype
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/createquesiontype")
    public void createCategory(@RequestBody QuestionTypeRequest category) {
        quesTionService.createQuestionType(category);
    }

    //http://localhost:8080/quiz/createnominee
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/createnominee")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "create success", response = Nominee.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public void createNominee(@RequestBody NomineeRequest nomineeRequest) {
        nomineeService.createNominee(nomineeRequest);
    }

    //http://localhost:8080/quiz/editnominee
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PutMapping("/editnominee")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update success", response = Nominee.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public void editNominee(@RequestBody NomineeEditRequest nomineeRequest) {
        nomineeService.editNominee(nomineeRequest);
    }

    //http://localhost:8080/quiz/deletenominee
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PutMapping("/deletenominee/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "delete success", response = Nominee.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public void deleteNominee(@PathVariable("id") Long id) {
        nomineeService.deleteNominee(id);
    }

    //http://localhost:8080/quiz/nominee/list
//    @CrossOrigin(origins = "http://localhost:8080/quiz")
//    @GetMapping("/nominee/list")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Nominee.class),
//            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
//            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
//            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
//    public List<Nominee> getAllNominee() {
//
//        return nomineeService.getAll();
//    }

    // Create quiz
    // http://localhost:8080/quiz
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "create success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<BaseResponse> createQuiz(@Valid @RequestBody CreateQuizForm form
                                                   //   , @RequestHeader("Authorization") String token
    ) throws ResourceBadRequestException, ResourceForbiddenRequestException {
//        if (templateService.getCanCreate(perQuiz, token) == false) {
//            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
//        }
        return new ResponseEntity<BaseResponse>(quizService.addQuesToQuiz(form), HttpStatus.CREATED);
    }

    // get detail quiz
    // http://localhost:8080/quiz/1
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Quiz> getDetailQuiz(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getDetailQuiz(id));
    }

    // get list quiz by user when done
    // http://localhost:8080/quiz/listbyuser/2

    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/listbyuser/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Quiz>> getListQuizByUserWhenDone(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListQuizByUserWhenDone(id));
    }

    // get all quiz by user
    // http://localhost:8080/quiz/allbyuser
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/allbyuser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<QuizPaging> getAllQuizByUser(@RequestBody QuizPaging quizPaging) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getAllQuizByUser(quizPaging));
    }

    // get list quiz not start by user
    // http://localhost:8080/quiz/notstart/2
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/notstart/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Quiz>> getListQuizNotStartByUser(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListQuizNotStart(id));
    }

    // get list quiz
    // http://localhost:8080/quiz/list
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<QuizPaging> getListQuizPaging(@RequestBody QuizPaging quizPaging) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListQuizPaging(quizPaging));
    }

    // get list group quiz
    // http://localhost:8080/quiz/list
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/listgroup")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<GroupQuizPaging> getListGroupQuizPaging(@RequestBody GroupQuizPaging quizPaging) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListGroupQuizPaging(quizPaging));
    }

    // start quiz
    // http://localhost:8080/quiz/startquiz/1952
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/startquiz/{qizid}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<QuestDTO>> getListQuestionByQuizId(@PathVariable("qizid") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quesTionService.getListQuestionByQuizId(id, false));
    }

    // view quiz
    // http://localhost:8080/quiz/viewquiz/1952
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/viewquiz/{qizid}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<QuestDTO>> viewQuizHaveQuestion(@PathVariable("qizid") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quesTionService.getListQuestionByQuizId(id, true));
    }


    // get list choice by questionId
    // http://localhost:8080/quiz/listchoice/54
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/listchoice/{questionid}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<QuestionChoice>> getListChoiceByQuestionId(@PathVariable("questionid") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListChoiceByQuestionId(id));
    }

    // calculate quiz
    // http://localhost:8080/quiz/calculate
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/calculate")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "calculate success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public Quiz calculate(@RequestBody List<QuestDTO> questDTO) throws ResourceBadRequestException {
        return quizService.calculateScore(questDTO);
    }
    // http://localhost:8080/quiz/getname
//    @PostMapping("/getname")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
//            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
//            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
//            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
//    public List<Object> getName() throws ResourceBadRequestException {
//        return quizService.getName();
//    }

    // http://localhost:8080/quiz/getUserDidTheTest
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @GetMapping("/getUserDidTheTest")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public List<AccountDto> getUserDidTheTest() throws ResourceBadRequestException {
        return quizService.getUserDidTheTest();
    }

    //http://localhost:8080/quiz/importquestion
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @PostMapping("/importquestion")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "create success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<BaseResponse> importQuestion(@RequestParam MultipartFile file) throws IOException {
//        if(templateService.getCanCreate(perQuiz)==false){
//            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
//        }
        return ResponseEntity.ok().body(quesTionService.excelImport(file));
    }


    //http://localhost:8080/quiz/deletelistquiz
    @CrossOrigin(origins = "http://localhost:8080/quiz")
    @DeleteMapping("/deletelistquiz")
    public BaseResponse deleteListQuiz(@RequestBody List<Long> id) {
        return quesTionService.deleteListQuiz(id);
    }

}
