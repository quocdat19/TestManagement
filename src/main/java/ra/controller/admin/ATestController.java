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
import ra.model.dto.request.TestRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Enums.ETestType;
import ra.model.entity.Test;
import ra.service.TestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/tests")
public class ATestController {
    private final TestService testService;

    @GetMapping
    public ResponseEntity<?> getAllATestsToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "testName", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "sortBy") String sortBy) throws CustomException {
        try {
            Pageable pageable;
            if (sortBy.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Test> testResponses = testService.getAllTestsToList ( pageable );
            if (testResponses.getContent ().isEmpty ()) throw new CustomException ( "Tests page is empty." );
            return new ResponseEntity<> ( new ResponseWrapper<> ( EHttpStatus.SUCCESS, HttpStatus.OK.value (), HttpStatus.OK.name (), testResponses.getContent () ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    @PostMapping("")
    ResponseEntity<?> createTest(@RequestBody TestRequest testRequest) {
        Test test = testService.save ( testRequest );
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Create test successfully" ) );
    }

    @PatchMapping("/{testId}")
    public ResponseEntity<?> patchUpdateTest(
            @PathVariable("testId") String testId,
            @RequestBody @Valid TestRequest testRequest) throws CustomException {
        try {
            Long id = Long.parseLong ( testId );
            Test testUpdate = testService.patchUpdateATest ( id, testRequest );
            return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Update Test Successfully" ) );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @DeleteMapping("/delete/{testId}")
    ResponseEntity<?> deleteTest(@PathVariable("testId") String deleteTestId) throws CustomException {
        Long testId = Long.parseLong ( deleteTestId );
        testService.hardDeleteByTestId ( testId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Delete successfully" ) );
    }

    @DeleteMapping("/{testId}")
    public ResponseEntity<?> softDeleteTestById(@PathVariable("testId") String testId) throws CustomException {
        try {
            Long id = Long.parseLong ( testId );
            testService.softDeleteByTestId ( id );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            "Delete test successfully."
                    ), HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> findByTestName(@RequestParam(name = "testName") String keyword,
                                            @RequestParam(defaultValue = "5", name = "limit") int limit,
                                            @RequestParam(defaultValue = "0", name = "page") int page,
                                            @RequestParam(defaultValue = "testName", name = "sort") String sort,
                                            @RequestParam(defaultValue = "asc", name = "sortBy") String sortBy) throws CustomException {
        try {
            Pageable pageable;
            if (sortBy.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Test> testResponses = testService.findAllTestsByTestNameToList ( keyword, pageable );
            if (testResponses.getContent ().isEmpty ()) throw new CustomException ( "Tests page is empty." );
            return new ResponseEntity<> ( new ResponseWrapper<> ( EHttpStatus.SUCCESS, HttpStatus.OK.value (), HttpStatus.OK.name (), testResponses.getContent () ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

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

    @GetMapping("/typeTest")
    public ResponseEntity<?> getAllTestByTypeTest(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "testName", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order,
            @RequestParam(defaultValue = "QUIZTEST", name = "typeTest") String typeTest
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Test> testResponses = null;
            if (typeTest.equalsIgnoreCase ( String.valueOf ( ETestType.EASY ) )) {
                testResponses = testService.getAllByTestType ( ETestType.EASY, pageable );
            } else if (typeTest.equalsIgnoreCase ( String.valueOf ( ETestType.NORMAL ) )) {
                testResponses = testService.getAllByTestType ( ETestType.NORMAL, pageable );
            } else if (typeTest.equalsIgnoreCase ( String.valueOf ( ETestType.DIFFICULTY ) )) {
                testResponses = testService.getAllByTestType ( ETestType.DIFFICULTY, pageable );
            } else {
                testResponses = Page.empty ();
            }
            if (testResponses.getContent ().isEmpty ()) throw new CustomException ( "Tests page is empty." );

            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            testResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

}
