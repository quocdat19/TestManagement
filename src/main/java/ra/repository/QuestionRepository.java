package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.entity.Enums.EQuestionLevel;
import ra.model.entity.Question;
import ra.model.entity.Test;

import java.time.LocalDate;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> getAllByTest(Test test);
    List<Question> getAllByCreatedDate(LocalDate date);
    Page<Question> findAllByQuestionContentIsContainingIgnoreCase(String questionContent, Pageable pageable);
    List<Question> getAllByQuestionLevel(EQuestionLevel questionLevel);
    List<Question> getQuestionsByTestId(Long testId);
}
