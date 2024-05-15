package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Exam;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findByExamName(String examName, Pageable pageable);
    Page<Exam> getAllByStatus(EActiveStatus status, Pageable pageable);
    Optional<Exam> findByIdAndStatus(Long examId, EActiveStatus status);
    @Query("SELECT e FROM Exam e WHERE e.subject.id=:subjectId")
    Page<Exam> getAllBySubjectId(Long subjectId, Pageable pageable);
}
