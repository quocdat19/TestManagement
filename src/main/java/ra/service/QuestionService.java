package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.request.QuestionRequest;
import ra.model.entity.Question;


import java.util.Optional;

public interface QuestionService {
    Page<Question> getAll(Pageable pageable);

    Optional<Question> getQuestionById(Long questionId);

    Question save(Question question);

    void questionDelete(Long questionId);
}
