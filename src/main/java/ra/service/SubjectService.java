package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CustomException;
import ra.model.dto.request.SubjectRequest;
import ra.model.entity.Subject;

import java.util.Optional;

public interface SubjectService {
    Page<Subject> getAllSubject(Pageable pageable);

    Optional<Subject> getSubjectById(Long subjectId);

    Subject save(Subject subject);

    Subject save(SubjectRequest subjectRequest);

    Subject entityAMap(SubjectRequest subjectRequest);

    Subject patchUpdate(Long subjectId, SubjectRequest subjectRequest);

    void subjectDelete(Long subjectId) throws CustomException;

    void softDeleteById(Long subjectId) throws CustomException;

    Page<Subject> findBySubjectNameContainingIgnoreCase(String subjectName, Pageable pageable);
}
