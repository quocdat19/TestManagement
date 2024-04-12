package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Question;

public interface QuestionRepository extends JpaRepository<Question,Long> {
}
