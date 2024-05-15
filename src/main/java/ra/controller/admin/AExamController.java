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
import ra.model.dto.request.ExamRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Exam;
import ra.service.ExamService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/exams")
public class AExamController {
    private final ExamService examService;
    @GetMapping
    public ResponseEntity<?> getAllExamsToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "examName", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Exam> examResponses = examService.getAll ( pageable );
            if (examResponses.getContent ().isEmpty ()) throw new CustomException ( "exams page is empty." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            examResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    @PostMapping("")
    ResponseEntity<?> createExam(@RequestBody ExamRequest examRequest) {
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới bài thi thành công" ) );
    }

    @PatchMapping("/{examId}")
    public ResponseEntity<?> patchUpdateExam(
            @PathVariable("examId") String examId,
            @RequestBody @Valid ExamRequest examRequest) throws CustomException {
        try {
            Long id = Long.parseLong ( examId );
            Exam exam = examService.patchUpdateExam ( id, examRequest );
            return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Sửa bài thi thành công" ) );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @DeleteMapping("/delete/{examId}")
    ResponseEntity<?> deleteExam(@PathVariable("examId") String deleteExamId) {
        Long examId = Long.parseLong ( deleteExamId );
        examService.examDelete ( examId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Đã xóa thành công" ) );
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllByExamName(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "id", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Exam> examResponses = examService.findByExamName ( keyword, pageable );
            if (examResponses.getContent ().isEmpty ()) throw new CustomException ( "exams page is empty." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            examResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    // * Get Exam by id.
    @GetMapping("/{examId}")
    public ResponseEntity<?> getExamById(@PathVariable("examId") String examId) throws CustomException {
        try {
            Long id = Long.parseLong ( examId );
            Optional<Exam> exam = examService.getExamById ( id );
            if (exam.isPresent ())
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                exam.get ()
                        ), HttpStatus.OK );
            throw new CustomException ( "Exam is not exists." );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<?> getAllExamBySubjectIdToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "id", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order,
            @PathVariable String subjectId
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Long idSubject = Long.parseLong ( subjectId );
            Page<Exam> examResponses = examService.getAllBySubjectId ( idSubject, pageable );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            examResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }
}
