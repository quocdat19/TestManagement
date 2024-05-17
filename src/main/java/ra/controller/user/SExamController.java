package ra.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Exam;
import ra.service.ExamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/student/exam")
public class SExamController {
    private final ExamService examService;

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
