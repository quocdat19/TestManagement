package ra.controller.admin;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.request.OptionRequest;
import ra.model.dto.request.QuestionOptionRequest;
import ra.model.dto.response.QuestionResponse;
import ra.model.dto.time.DateSearchCreatedDate;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Enums.EQuestionLevel;
import ra.model.entity.Option;
import ra.model.entity.Question;
import ra.model.entity.Test;
import ra.service.CommonService;
import ra.service.OptionService;
import ra.service.QuestionService;
import ra.service.TestService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/question")
public class AQuestionOptionController {
    private final QuestionService questionService;
    private final CommonService commonService;
    private final TestService testService;
    private final OptionService optionService;

    // * lay danh sach question va option theo test
    @GetMapping("/test/{testId}")
    public ResponseEntity<?> getAllQuestionAndOptionByTest(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "questionContent", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order,
            @PathVariable String testId) throws CustomException {
        Pageable pageable;
        if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
        else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
        try {
            Long idTest = Long.parseLong ( testId );
            Optional<Test> test = testService.getTestById ( idTest );
            if (test.isEmpty ()) {
                throw new CustomException ( "Test is not exists." );
            }
            List<QuestionResponse> questionResponses = questionService.getAllByTest ( test.get () );
            Page<?> questions = commonService.convertListToPages ( pageable, questionResponses );
            if (!questions.isEmpty ()) {
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                questions.getContent ()
                        ), HttpStatus.OK );
            }
            throw new CustomException ( "Questions page is empty." );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        } catch (IllegalArgumentException e) {
            throw new CustomException ( "Questions page is out of range." );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    //* Hien thi danh sach question va option theo ngay thang tao
    @PostMapping("/createdDateExam")
    public ResponseEntity<?> getAllQuestionAndOptionByCreateDate(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "questionContent", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order,
            @RequestBody @Valid DateSearchCreatedDate dateSearchCreatedDate) throws CustomException {
        Pageable pageable;
        if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
        else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
        try {
            LocalDate date = LocalDate.parse ( dateSearchCreatedDate.getCreateDate () );
            List<QuestionResponse> questionResponses = questionService.getAllByCreatedDate ( date );
            Page<?> questions = commonService.convertListToPages ( pageable, questionResponses );
            if (!questions.isEmpty ()) {
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                questions.getContent ()
                        ), HttpStatus.OK );
            }
            throw new CustomException ( "Questions page is empty." );
        } catch (IllegalArgumentException e) {
            throw new CustomException ( "Questions page is out of range." );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    //* Hien thi danh sach question va option theo level question
    @PostMapping("/levelQuestion")
    public ResponseEntity<?> getAllQuestionAndOptionByLevelQuestion(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "questionContent", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order,
            @RequestParam(defaultValue = "EASY", name = "levelQuestion") String levelQuestion) throws CustomException {
        Pageable pageable;
        if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
        else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
        try {
            List<QuestionResponse> questionResponses = null;
            if (levelQuestion.equalsIgnoreCase ( String.valueOf ( EQuestionLevel.EASY ) )) {
                questionResponses = questionService.getAllByQuestionLevel ( EQuestionLevel.EASY );
            } else if (levelQuestion.equalsIgnoreCase ( String.valueOf ( EQuestionLevel.NORMAL ) )) {
                questionResponses = questionService.getAllByQuestionLevel ( EQuestionLevel.NORMAL );
            } else if (levelQuestion.equalsIgnoreCase ( String.valueOf ( EQuestionLevel.DIFFICULTY ) )) {
                questionResponses = questionService.getAllByQuestionLevel ( EQuestionLevel.DIFFICULTY );
            }
            Page<?> questions = commonService.convertListToPages ( pageable, questionResponses );
            if (!questions.isEmpty ()) {
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                questions.getContent ()
                        ), HttpStatus.OK );
            }
            throw new CustomException ( "Questions page is empty." );
        } catch (IllegalArgumentException e) {
            throw new CustomException ( "Questions page is out of range." );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    // * Get question by id.
    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable("questionId") String questionId) throws CustomException {
        try {
            Long idQuestion = Long.parseLong ( questionId );
            Optional<Question> question = questionService.getQuestionById ( idQuestion );
            if (question.isEmpty ())
                throw new CustomException ( "Question is not exists." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            question.get ()
                    ), HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    //* Them question va cac option
    @PostMapping("/addQuestion")
    public ResponseEntity<?> addQuestionAndOption(
            @RequestBody @Valid QuestionOptionRequest questionOptionRequest) throws CustomException {
        Question question = questionService.saveQuestionAndOption ( questionOptionRequest );
        QuestionResponse questionResponse = questionService.entityAMap ( question );
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.CREATED.value (),
                        HttpStatus.CREATED.name (),
                        questionResponse
                ), HttpStatus.CREATED );
    }

    //* Update question and Option
    @Transactional
    @PostMapping("/{questionId}")
    public ResponseEntity<?> patchUpdateQuestionAndOption(
            @PathVariable("questionId") String questionId,
            @RequestBody @Valid QuestionOptionRequest questionOptionRequest) throws CustomException {
        try {
            Long idQuestion = Long.parseLong ( questionId );
            Question question = questionService.patchUpdateQuestion ( idQuestion, questionOptionRequest.getQuestionRequest () );
            optionService.deleteByQuestion ( question );
            List<OptionRequest> aOptionRequests = questionOptionRequest.getOptionRequests ();
            for (OptionRequest aOptionRequest : aOptionRequests) {
                aOptionRequest.setQuestionId ( question.getId () );
                aOptionRequest.setStatus ( "ACTIVE" );
                optionService.save ( aOptionRequest );
            }
            List<Option> options = optionService.getAllByQuestion ( question );
            question.setOptions ( options );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            questionService.entityAMap ( question )
                    ), HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    //* delete question and option
    @Transactional
    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestionAndOption(
            @PathVariable("questionId") String questionId
    ) throws CustomException {
        try {
            Long idQuestion = Long.parseLong ( questionId );
            Optional<Question> question = questionService.getQuestionById ( idQuestion );
            if (question.isPresent ()) {
                optionService.deleteByQuestion ( question.get () );
                questionService.questionDelete ( idQuestion );
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                "Delete Success"
                        ), HttpStatus.OK );
            }
            throw new CustomException ( "Questions is empty." );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }
}
