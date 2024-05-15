package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Option;
import ra.model.entity.Question;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option,Long> {
    List<Option> findAllByQuestion(Question question);

    void deleteByQuestion(Question question);
}
