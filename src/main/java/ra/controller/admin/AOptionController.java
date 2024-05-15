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
import ra.model.dto.request.OptionRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.Option;
import ra.model.entity.Question;
import ra.service.OptionService;
import ra.service.QuestionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/options")
public class AOptionController {
    private final OptionService optionService;
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getAllQuestionsToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "contentOptions", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        Pageable pageable;
        if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
        else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
        try {
            Page<Option> options = optionService.getAll ( pageable );
            if (!options.isEmpty ()) {
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                options.getContent ()
                        ), HttpStatus.OK );
            }
            throw new CustomException ( "Options page is empty." );
        } catch (IllegalArgumentException e) {
            throw new CustomException ( "Options page is out of range." );
        }
    }

    // * Get option by id.
    @GetMapping("/{optionId}")
    public ResponseEntity<?> getOptionById(@PathVariable("optionId") Long optionId) throws CustomException {
        Optional<Option> option = optionService.getOptionById ( optionId );
        if (option.isPresent ())
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            option.get ()
                    ), HttpStatus.OK );
        throw new CustomException ( "Option is not exists." );
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> getAllOptionsByQuestion(@PathVariable("questionId") Long questionId) throws CustomException {
        Optional<Question> question = questionService.getQuestionById ( questionId );
        if (question.isPresent ()) {
            List<Option> options = optionService.getAllByQuestion ( question.get () );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            options
                    ), HttpStatus.OK );
        }
        throw new CustomException ( "Question is not exists." );
    }

    @PostMapping("")
    ResponseEntity<?> createOption(@RequestBody OptionRequest optionRequest) {
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Thêm mới thành công" ) );
    }

    @PatchMapping("/{optionId}")
    public ResponseEntity<?> patchUpdateOption(
            @PathVariable("optionId") Long optionId,
            @RequestBody @Valid OptionRequest optionRequest) {
        Option updatedOption = optionService.patchUpdateOption ( optionId, optionRequest );
        return ResponseEntity.status ( 201 ).body ( new ResponseAPI ( true, "Sửa thành công" ) );
    }

    @DeleteMapping("/delete/{optionId}")
    ResponseEntity<?> deleteOption(@PathVariable("optionId") Long optionId) throws CustomException {
        Optional<Option> deletedOption = optionService.getOptionById ( optionId );
        if (deletedOption.isPresent ()) {
            optionService.optionDelete ( optionId );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            "Delete option successfully."
                    ), HttpStatus.OK );
        }
        throw new CustomException ( "Option is not exists." );
    }
}
