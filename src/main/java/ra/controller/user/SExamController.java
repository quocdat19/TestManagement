//package ra.controller.user;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ra.model.dto.wrapper.ResponseWrapper;
//import ra.model.entity.Enums.EHttpStatus;
//import ra.model.entity.Exam;
//import ra.service.ExamService;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/v1/student/exam")
//public class SExamController {
//    private final ExamService examService;
//
//    @GetMapping("/allExam")
//    public ResponseEntity<?> allSubject(){
//        List<Exam> exams = examService.getAllExamBySubjectOfStudent();
//        return new ResponseEntity<>(
//                new ResponseWrapper<> (
//                        EHttpStatus.SUCCESS,
//                        HttpStatus.OK.value(),
//                        HttpStatus.OK.name(),
//                        exams
//                ), HttpStatus.OK);
//    }
//}
