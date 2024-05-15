package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Page<Subject> findBySubjectNameContainingIgnoreCase(String subjectName, Pageable pageable);
}
