package ra.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.request.ListStudentChoice;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Result;
import ra.service.ResultService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/student")
public class SResultController {
    private final ResultService resultService;

    @PostMapping("/doTest/{testId}")
    public ResponseEntity<?> doTest(@RequestBody @Valid ListStudentChoice listStudentChoice, @PathVariable String testId) throws CustomException {
        try {
            Long idTest = Long.parseLong(testId);
            Result result = resultService.checkAndResultTest(listStudentChoice, idTest);
            return new ResponseEntity<>(
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value(),
                            HttpStatus.OK.name(),
                            result
                    ), HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new CustomException("Incorrect id number format");
        }
    }

    @GetMapping("/result/checkHistory")
    public ResponseEntity<?> checkHistory() {
        List<Result> results = resultService.getAllByStudent();
        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        EHttpStatus.SUCCESS,
                        HttpStatus.OK.value(),
                        HttpStatus.OK.name(),
                        results
                ), HttpStatus.OK);
    }

}
