package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Page<Subject> findBySubjectName(String subjectName, Pageable pageable);
}
