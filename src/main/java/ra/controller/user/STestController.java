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

    @GetMapping()
    public ResponseEntity<?> allSubject() {
        List<Test> tests = testService.getAllTestByExamOfStudent ();
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.OK.value (),
                        HttpStatus.OK.name (),
                        tests
                ), HttpStatus.OK );
    }
}