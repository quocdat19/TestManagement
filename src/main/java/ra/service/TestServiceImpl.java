package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.request.TestRequest;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.ETestType;
import ra.model.entity.Test;
import ra.repository.TestRepository;
import ra.security.UserDetail.UserLoggedIn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final UserLoggedIn userLoggedIn;
    private final ExamService examService;

    @Override
    public Page<Test> getAllTestsToList(Pageable pageable) {
        return testRepository.findAll ( pageable );
    }

    @Override
    public Optional<Test> getTestById(Long testId) {
        return testRepository.findById ( testId );
    }

    @Override
    public Test save(Test test) {
        test.setCreateBy ( userLoggedIn.getUserLoggedIn ().getUsername () );
        return testRepository.save ( test );
    }

    @Override
    public Test patchUpdateATest(Long testId, TestRequest testRequest) throws CustomException {
        Optional<Test> updateTest = getTestById ( testId );
        if (updateTest.isPresent ()) {
            Test test = updateTest.get ();
            if (testRequest.getTestName () != null)
                test.setTestName ( testRequest.getTestName () );
            if (testRequest.getTestTime () != null)
                test.setTestTime ( testRequest.getTestTime () );
            ETestType testType = null;
            if (testRequest.getTestType () != null) {
                testType = switch (testRequest.getTestType ().toUpperCase ()) {
                    case "EASY" -> ETestType.EASY;
                    case "NORMAL" -> ETestType.NORMAL;
                    case "DIFFICULTY" -> ETestType.DIFFICULTY;
                    default -> null;
                };
                test.setTestType ( testType );
            }
            EActiveStatus status = null;
            if (testRequest.getStatus () != null) {
                status = switch (testRequest.getStatus ().toUpperCase ()) {
                    case "ACTIVE" -> EActiveStatus.ACTIVE;
                    case "INACTIVE" -> EActiveStatus.INACTIVE;
                    default -> null;
                };
                test.setStatus ( status );
            }
            return testRepository.save ( test );
        }
        throw new CustomException ( "Test is not exists." );
    }

    @Override
    public void hardDeleteByTestId(Long testId) throws CustomException {
        if (!testRepository.existsById ( testId )) throw new CustomException ( "Test is not exists to delete." );
        testRepository.deleteById ( testId );
    }

    @Override
    public Page<Test> findAllTestsByTestNameToList(String testName, Pageable pageable) {
        return testRepository.findByTestNameContainingIgnoreCase ( testName, pageable );
    }

    //find by examId
    @Override
    public Page<Test> getAllByExamId(Long examId, Pageable pageable) {
        return testRepository.getAllByExamId ( examId, pageable );
    }

    @Override
    public Page<Test> getAllByTestType(ETestType testType, Pageable pageable) {
        return testRepository.getAllByTestType ( testType, pageable );
    }

    @Override
    public Page<Test> getAllByCreatedDate(LocalDate createdDate, Pageable pageable) {
        return testRepository.getAllByCreatedDate ( createdDate, pageable );
    }

    @Override
    public Page<Test> getAllFromDateToDate(LocalDate dateStart, LocalDate dateEnd, Pageable pageable) {
        return testRepository.getAllFromDateToDate ( dateStart, dateEnd, pageable );
    }
}
