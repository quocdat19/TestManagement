package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.entity.Enums.ETestType;
import ra.model.entity.Exam;
import ra.model.entity.Test;

import java.time.LocalDate;
import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {

    Page<Test> findByTestNameContainingIgnoreCase(String testName, Pageable pageable);

    //Find All Test By ExamId
    @Query("select t from Test t where t.exam.id=:examId")
    Page<Test> getAllByExamId(Long examId, Pageable pageable);

    Page<Test> getAllByTestType(ETestType testType, Pageable pageable);
    List<Test> findAllByExam(Exam exam);
}
