package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.entity.Subject;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Page<Subject> findBySubjectNameContainingIgnoreCase(String subjectName, Pageable pageable);

    @Query(value = "select u.subject_id from user_subject u where u.user_id = :userId", nativeQuery = true)
    List<Long> findByUserId(Long userId);
}
