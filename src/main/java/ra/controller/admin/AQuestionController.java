package ra.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.dto.request.QuestionRequest;
import ra.model.dto.response.ResponseAPI;
import ra.service.QuestionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AQuestionController {
    private final QuestionService questionService;

    @PostMapping("")
    ResponseEntity<?> createQuestion(@RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới thành công" ) );
    }

    @DeleteMapping("/delete/{questionId}")
    ResponseEntity<?> deleteQuestion(@PathVariable("questionId") String deleteQuestionId) {
        Long questionId = Long.parseLong ( deleteQuestionId );
        questionService.questionDelete ( questionId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Đã xóa thành công" ) );
    }
}
