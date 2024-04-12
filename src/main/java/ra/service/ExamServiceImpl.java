package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.dto.request.ExamRequest;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Exam;
import ra.repository.ExamRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;

    @Override
    public Page<Exam> getAll(Pageable pageable) {
        return examRepository.findAll ( pageable );
    }

    @Override
    public Optional<Exam> getExamById(Long examId) {
        return examRepository.findById ( examId );
    }

    @Override
    public Exam save(Exam exam) {
        return examRepository.save ( exam );
    }

    @Override
    public void examDelete(Long examId) {
        examRepository.deleteById ( examId );
    }

    @Override
    public Page<Exam> findByExamName(String examName, Pageable pageable) {
        return examRepository.findByExamName ( examName, pageable );
    }
}
