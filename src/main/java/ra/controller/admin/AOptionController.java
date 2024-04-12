package ra.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.model.dto.request.OptionRequest;
import ra.model.dto.response.ResponseAPI;
import ra.service.OptionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AOptionController {
    private final OptionService optionService;
    @PostMapping("")
    ResponseEntity<?> createOption(@RequestBody OptionRequest optionRequest) {
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới thành công" ) );
    }

    @DeleteMapping("/delete/{optionId}")
    ResponseEntity<?> deleteOption(@PathVariable("optionId") String deleteOptionId) {
        Long optionId = Long.parseLong ( deleteOptionId );
        optionService.optionDelete ( optionId );
        return ResponseEntity.status ( 200 ).body ( new ResponseAPI ( true, "Đã xóa thành công" ) );
    }
}
