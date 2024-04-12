package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.request.ExamRequest;
import ra.model.entity.Exam;

import java.util.Optional;

public interface ExamService {
    Page<Exam> getAll(Pageable pageable);

    Optional<Exam> getExamById(Long examId);

    Exam save(Exam exam);

    void examDelete(Long examId);

    Page<Exam> findByExamName(String examName, Pageable pageable);
}
