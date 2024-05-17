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
import ra.model.entity.Test;
import ra.service.TestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/student/test")
public class STestController {
    private final TestService testService;
    @GetMapping("/exam/{examId}")
    public ResponseEntity<?> getAllTestByExamIdToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "testName", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order,
            @PathVariable String examId
    ) throws CustomException {
        try {
            Long idExam = Long.parseLong ( examId );
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Test> testResponses = testService.getAllByExamId ( idExam, pageable );
            if (testResponses.getContent ().isEmpty ()) throw new CustomException ( "Tests page is empty." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            testResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }
}