package ra.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.dto.request.SubjectRequest;
import ra.model.dto.response.ResponseAPI;
import ra.service.SubjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ASubjectController {
    private final SubjectService subjectService;

    @PostMapping("")
    ResponseEntity<?> createSubject(@RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới môn học thành công" ) );
    }
    @DeleteMapping("/delete/{subjectId}")
    ResponseEntity<?> deleteSubject(@PathVariable("subjectId") String deleteSubjectId) {
        Long subjectId = Long.parseLong ( deleteSubjectId );
        subjectService.subjectDelete ( subjectId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Đã xóa thành công" ) );
    }
}
