package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.request.ListStudentChoice;
import ra.model.dto.request.StudentChoice;
import ra.model.entity.*;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EOptionStatus;
import ra.repository.ResultRepository;
import ra.security.UserDetail.UserLoggedIn;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ResultServiceImp implements ResultService {
    private final ResultRepository resultRepository;
    private final QuestionService questionService;
    private final TestService testService;
    private final UserLoggedIn userLoggedIn;
    private final UserService userService;

    @Override
    public Result checkAndResultTest(ListStudentChoice listStudentChoice, Long testId) throws CustomException {
        Optional<Test> testOptional = testService.getTestById ( testId );
        if (testOptional.isPresent ()) {
            List<Question> questions = questionService.getAllQuestionByTest ( testOptional.get () );
            double markOfQuestion = (double) 10 / (questions.size ());
            double myMark = 0;
            List<StudentChoice> studentChoices = listStudentChoice.getStudentChoices ();
            for (Question question : questions) {
                for (StudentChoice studentChoice : studentChoices) {
                    if (Objects.equals ( question.getId (), studentChoice.getIdQuestion () )) {
                        List<Long> listIdOptionCorrect = new ArrayList<> ();
                        for (Option option : question.getOptions ()) {
                            if (option.getIsCorrect () == EOptionStatus.CORRECT) {
                                listIdOptionCorrect.add ( option.getId () );
                            }
                        }
                        if (compareLists ( listIdOptionCorrect, studentChoice.getListIdOptionStudentChoice () )) {
                            myMark += markOfQuestion;
                        }
                        break;
                    }
                }
            }
            List<Result> results = findAllByUserAndTest ( userLoggedIn.getUserLoggedIn (), testOptional.get () );
            int count = results.size ();
            Result result = Result.builder ()
                    .user ( userLoggedIn.getUserLoggedIn () )
                    .test ( testOptional.get () )
                    .status ( EActiveStatus.ACTIVE )
                    .mark ( myMark )
                    .examTimes ( ++count )
                    .build ();
            return resultRepository.save ( result );
        }
        throw new CustomException ( "Test is not exist!" );
    }

    @Override
    public List<Result> getAllByStudent() {
        return resultRepository.getAllByUser ( userLoggedIn.getUserLoggedIn () );
    }

    @Override
    public List<Result> findAllByUserAndTest(User user, Test test) {
        return resultRepository.findAllByUserAndTest ( user, test );
    }

    @Override
    public Optional<Result> getById(Long id) {
        return resultRepository.findById ( id );
    }

    @Override
    public Result save(Result result) {
        return resultRepository.save ( result );
    }

    public boolean compareLists(List<Long> list1, List<Long> list2) {
        // Nếu độ dài của hai danh sách khác nhau, chúng không giống nhau
        if (list1.size () != list2.size ()) {
            return false;
        }
        // Sắp xếp hai danh sách
        Collections.sort ( list1 );
        Collections.sort ( list2 );
        // So sánh từng phần tử của hai danh sách
        for (int i = 0; i < list1.size (); i++) {
            if (!list1.get ( i ).equals ( list2.get ( i ) )) {
                return false; // Nếu phần tử tại vị trí i không giống nhau, trả về false
            }
        }
        // Nếu tất cả các phần tử giống nhau, trả về true
        return true;
    }


}
