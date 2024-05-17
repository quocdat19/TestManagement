package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CustomException;
import ra.model.dto.request.ExamRequest;
import ra.model.dto.response.ExamResponse;
import ra.model.entity.Exam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExamService {
    Page<Exam> getAll(Pageable pageable);

    Optional<Exam> getExamById(Long examId);

    Exam save(Exam exam);

    Exam save(ExamRequest examRequest);

    Exam patchUpdateExam(Long examId, ExamRequest examRequest) throws CustomException;

    void examDelete(Long examId);

    Page<Exam> findByExamName(String examName, Pageable pageable);

    void softDeleteById(Long examId) throws CustomException;

    Page<Exam> getAllBySubjectId(Long subjectId, Pageable pageable);
    Exam entityAMap(ExamRequest examRequest);
}
