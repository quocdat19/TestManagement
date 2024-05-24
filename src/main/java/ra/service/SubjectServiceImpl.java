package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.base.AuditableEntity;
import ra.model.dto.request.SubjectRequest;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Subject;
import ra.repository.SubjectRepository;
import ra.security.UserDetail.UserLoggedIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserLoggedIn userLoggedIn;

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
    public Subject save(SubjectRequest subjectRequest) {
        return subjectRepository.save ( entityAMap ( subjectRequest ) );
    }

    @Override
    public Subject entityAMap(SubjectRequest subjectRequest) {
        return Subject.builder ()
                .subjectName ( subjectRequest.getSubjectName () )
                .status ( EActiveStatus.valueOf ( subjectRequest.getStatus () ) )
                .build ();
    }

    @Override
    public Subject patchUpdate(Long subjectId, SubjectRequest subjectRequest) {
        Optional<Subject> updateSubject = getSubjectById ( subjectId );
        if (updateSubject.isPresent ()) {
            Subject subject = updateSubject.get ();
            AuditableEntity auditableEntity = updateSubject.get ();
            if (auditableEntity.getCreatedDate () != null)
                auditableEntity.setCreatedDate ( auditableEntity.getCreatedDate () );
            if (subjectRequest.getSubjectName () != null)
                subject.setSubjectName ( subjectRequest.getSubjectName () );
            if (subjectRequest.getStatus () != null)
                subject.setStatus ( EActiveStatus.valueOf ( subjectRequest.getStatus () ) );
            return save ( subject );
        }
        return null;
    }

    @Override
    public void subjectDelete(Long subjectId) throws CustomException {
        if (!subjectRepository.existsById ( subjectId ))
            throw new CustomException ( "Subject is not exists to delete." );
        subjectRepository.deleteById ( subjectId );
    }

    @Override
    public void softDeleteById(Long subjectId) throws CustomException {
        // ? Exception cần tìm thấy thì mới có thể xoá mềm.
        Optional<Subject> deleteSubject = getSubjectById ( subjectId );
        if (deleteSubject.isEmpty ())
            throw new CustomException ( "Subject is not exists to delete." );
        Subject subject = deleteSubject.get ();
        subject.setStatus ( EActiveStatus.INACTIVE );
        subjectRepository.save ( subject );
    }

    @Override
    public Page<Subject> findBySubjectNameContainingIgnoreCase(String subjectName, Pageable pageable) {
        return subjectRepository.findBySubjectNameContainingIgnoreCase ( subjectName, pageable );
    }
    @Override
    public List<Subject> getAllSubjectByUserId() {
        List<Subject> subjects = new ArrayList<> ();
        List<Long> subjectIds =  subjectRepository.findByUserId ( userLoggedIn.getUserLoggedIn().getId () );
        subjectIds.forEach ( id -> {
            subjects.add (  getSubjectById ( id ).orElse ( null ));
        } );
        return subjects;
    }
}
