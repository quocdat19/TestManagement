package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.dto.request.SubjectRequest;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Subject;
import ra.repository.SubjectRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public Page<Subject> getAllSubject(Pageable pageable) {
        return subjectRepository.findAll ( pageable );
    }

    @Override
    public Optional<Subject> getSubjectById(Long subjectId) {
        return subjectRepository.findById ( subjectId );
    }

    @Override
    public Subject save(Subject subject) {
        return subjectRepository.save ( subject );
    }

    @Override
    public void subjectDelete(Long subjectId) {
        subjectRepository.deleteById ( subjectId );
    }

    @Override
    public Page<Subject> findBySubjectName(String subjectName, Pageable pageable) {
        return subjectRepository.findBySubjectName ( subjectName, pageable );
    }
}
