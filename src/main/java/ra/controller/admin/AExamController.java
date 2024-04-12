package ra.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.dto.request.ExamRequest;
import ra.model.dto.request.SubjectRequest;
import ra.model.dto.response.ResponseAPI;
import ra.service.ExamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AExamController {
    private final ExamService examService;

    @PostMapping("")
    ResponseEntity<?> createExam(@RequestBody ExamRequest examRequest) {
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới bài thi thành công" ) );
    }

    @DeleteMapping("/delete/{examId}")
    ResponseEntity<?> deleteExam(@PathVariable("examId") String deleteExamId) {
        Long examId = Long.parseLong ( deleteExamId );
        examService.examDelete ( examId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Đã xóa thành công" ) );
    }
}
