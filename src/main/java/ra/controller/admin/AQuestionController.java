package ra.controller.admin;

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
import ra.model.dto.request.QuestionRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Question;
import ra.service.QuestionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/questions")
public class AQuestionController {
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getAllQuestionsToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "questionContent", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        // ! Cần thêm Exception nếu như có 1 trường enum bằng null.
        Pageable pageable;
        if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
        else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
        try {
            Page<Question> question = questionService.getAll ( pageable );
            if (!question.isEmpty ()) {
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                question.getContent ()
                        ), HttpStatus.OK );
            }
            throw new CustomException ( "Questions page is empty." );
        } catch (IllegalArgumentException e) {
            throw new CustomException ( "Questions page is out of range." );
        }
    }

    // * Get question by id.
    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable("questionId") Long questionId) throws CustomException {
        Optional<Question> question = questionService.getQuestionById ( questionId );
        if (question.isEmpty ())
            throw new CustomException ( "Class is not exists." );
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.OK.value (),
                        HttpStatus.OK.name (),
                        question.get ()
                ), HttpStatus.OK );
    }

    @PostMapping("")
    ResponseEntity<?> createQuestion(@RequestBody QuestionRequest questionRequest) {
        Question question = questionService.save(questionRequest);
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Create question successfully" ) );
    }

    @PatchMapping("/{questionId}")
    public ResponseEntity<?> patchUpdateQuestion(
            @PathVariable("questionId") Long questionId,
            @RequestBody @Valid QuestionRequest questionRequest) {
        Question question = questionService.patchUpdateQuestion ( questionId, questionRequest );
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Update question successfully" ) );
    }

    @DeleteMapping("/delete/{questionId}")
    ResponseEntity<?> deleteQuestion(@PathVariable("questionId") String deleteQuestionId) {
        Long questionId = Long.parseLong ( deleteQuestionId );
        questionService.questionDelete ( questionId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "delete successfully" ) );
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "contentQuestion", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        Pageable pageable;
        if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
        else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
        try {
            Page<Question> question = questionService.findByQuestionContent ( keyword, pageable );
            if (!question.isEmpty ()) {
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                question.getContent ()
                        ), HttpStatus.OK );
            }
            throw new CustomException ( "Questions page is empty." );
        } catch (IllegalArgumentException e) {
            throw new CustomException ( "Questions page is out of range." );
        }
    }
}
