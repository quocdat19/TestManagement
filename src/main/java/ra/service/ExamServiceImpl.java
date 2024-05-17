package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.request.ExamRequest;
import ra.model.dto.response.ExamResponse;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Exam;
import ra.model.entity.Subject;
import ra.repository.ExamRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final SubjectService subjectService;

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
    public Exam save(ExamRequest examRequest) {
        return examRepository.save ( entityAMap ( examRequest ) );
    }

    @Override
    public Exam patchUpdateExam(Long examId, ExamRequest examRequest) throws CustomException {
        Optional<Exam> updateExam = examRepository.findById ( examId );
        if (updateExam.isPresent ()) {
            Exam exam = updateExam.get ();
            if (examRequest.getExamName () != null) exam.setExamName ( examRequest.getExamName () );
            if (examRequest.getStatus () != null) {
                EActiveStatus activeStatus = switch (examRequest.getStatus ().toUpperCase ()) {
                    case "INACTIVE" -> EActiveStatus.INACTIVE;
                    case "ACTIVE" -> EActiveStatus.ACTIVE;
                    default -> null;
                };
                exam.setStatus ( activeStatus );
            }
            if (examRequest.getSubjectId () != null) {
                Optional<Subject> subject = subjectService.getSubjectById ( examRequest.getSubjectId () );
                if (subject.isEmpty ()) throw new CustomException ( "Subject is not exists." );
                exam.setSubject ( subject.get () );
            }
            return examRepository.save ( exam );
        }
        throw new CustomException ( "Exam is not exists to update." );
    }

    @Override
    public void examDelete(Long examId) {
        examRepository.deleteById ( examId );
    }

    @Override
    public Page<Exam> findByExamName(String examName, Pageable pageable) {
        return examRepository.findByExamName ( examName, pageable );
    }

    @Override
    public void softDeleteById(Long examId) throws CustomException {
        Optional<Exam> deleteExam = getExamById ( examId );
        if (deleteExam.isEmpty ())
            throw new CustomException ( "Exam is not exists to delete." );
        Exam exam = deleteExam.get ();
        exam.setStatus ( EActiveStatus.INACTIVE );
        examRepository.save ( exam );
    }

    @Override
    public Page<Exam> getAllBySubjectId(Long subjectId, Pageable pageable) {
        return examRepository.getAllBySubjectId ( subjectId, pageable );
    }

    @Override
    public Exam entityAMap(ExamRequest examRequest) {
        EActiveStatus activeStatus = switch (examRequest.getStatus ().toUpperCase ()) {
            case "INACTIVE" -> EActiveStatus.INACTIVE;
            case "ACTIVE" -> EActiveStatus.ACTIVE;
            default -> null;
        };
        return Exam.builder ()
                .examName ( examRequest.getExamName () )
                .status ( activeStatus )
                .subject ( subjectService.getSubjectById ( examRequest.getSubjectId () ).orElse ( null ) )
                .build ();
    }
}
