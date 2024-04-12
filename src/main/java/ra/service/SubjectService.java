package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.request.SubjectRequest;
import ra.model.entity.Subject;

import java.util.Optional;

public interface SubjectService {
    Page<Subject> getAllSubject(Pageable pageable);

    Optional<Subject> getSubjectById(Long subjectId);

    Subject save(Subject subject);

    void subjectDelete(Long subjectId);

    Page<Subject> findBySubjectName(String subjectName, Pageable pageable);
}
