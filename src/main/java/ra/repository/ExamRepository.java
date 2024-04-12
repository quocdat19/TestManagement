package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findByExamName(String examName, Pageable pageable);
}
