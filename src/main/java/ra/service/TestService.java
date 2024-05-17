package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CustomException;
import ra.model.dto.request.TestRequest;
import ra.model.entity.Enums.ETestType;
import ra.model.entity.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TestService {
    Page<Test> getAllTestsToList(Pageable pageable);

    Optional<Test> getTestById(Long testId);

    Page<Test> findAllTestsByTestNameToList(String testName, Pageable pageable);

    Test save(Test test);

    Test save(TestRequest testRequest);

    void hardDeleteByTestId(Long testId) throws CustomException;
    void softDeleteByTestId(Long testId) throws CustomException;

    Test patchUpdateATest(Long testId, TestRequest testRequest) throws CustomException;

    //find by subjectId
    Page<Test> getAllByExamId(Long examId, Pageable pageable);

    //* find by TestType
    Page<Test> getAllByTestType(ETestType testType, Pageable pageable);

    Test entityAMap(TestRequest testRequest);
}
