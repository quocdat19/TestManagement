package ra.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.dto.request.TestRequest;
import ra.model.dto.response.ResponseAPI;
import ra.service.TestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ATestController {
    private final TestService testService;

    @PostMapping("")
    ResponseEntity<?> createTest(@RequestBody TestRequest testRequest) {
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới thành công" ) );
    }

    @DeleteMapping("/delete/{testId}")
    ResponseEntity<?> deleteTest(@PathVariable("testId") String deleteTestId) {
        Long testId = Long.parseLong ( deleteTestId );
        testService.testDelete ( testId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Đã xóa thành công" ) );
    }
}
