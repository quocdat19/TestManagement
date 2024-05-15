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
import ra.model.dto.request.SubjectRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Subject;
import ra.service.SubjectService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/subjects")
public class ASubjectController {
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getAllSubjectToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "subjectName", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Subject> subjectResponses = subjectService.getAllSubject ( pageable );
            if (subjectResponses.getContent ().isEmpty ()) throw new CustomException ( "Classes page is empty." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            subjectResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getSubjectById(@PathVariable("subjectId") String subjectId) throws CustomException {
        try {
            Long id = Long.parseLong ( subjectId );
            Optional<Subject> subject = subjectService.getSubjectById ( id );
            if (subject.isEmpty ())
                throw new CustomException ( "Subject is not exists." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            subject.get ()
                    ), HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @PostMapping("")
    ResponseEntity<?> createSubject(@RequestBody SubjectRequest subjectRequest) {
        Subject subject = subjectService.save ( subjectRequest );
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới môn học thành công" ) );
    }

    @PatchMapping("/{subjectId}")
    public ResponseEntity<?> pathUpdateSubject(
            @PathVariable("subjectId") String updateSubjectId,
            @RequestBody @Valid SubjectRequest subjectRequest
    ) throws CustomException {
        try {
            Long id = Long.parseLong ( updateSubjectId );
            Subject subject = subjectService.patchUpdate ( id, subjectRequest );
            return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Sửa môn học thành công" ) );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @DeleteMapping("/delete/{subjectId}")
    ResponseEntity<?> deleteSubject(@PathVariable("subjectId") String deleteSubjectId) {
        Long subjectId = Long.parseLong ( deleteSubjectId );
        subjectService.subjectDelete ( subjectId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Đã xóa thành công" ) );
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAllSubjectToPages(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "subjectName", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<Subject> subjectResponses = subjectService.findBySubjectNameContainingIgnoreCase ( keyword, pageable );
            if (subjectResponses.getContent ().isEmpty ()) throw new CustomException ( "Classes page is empty." );
            return new ResponseEntity<> ( new ResponseWrapper<> ( EHttpStatus.SUCCESS, HttpStatus.OK.value (), HttpStatus.OK.name (), subjectResponses.getContent () ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }
}
