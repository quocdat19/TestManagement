package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.dto.request.QuestionRequest;
import ra.model.entity.Question;
import ra.repository.QuestionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    @Override
    public Page<Question> getAll(Pageable pageable) {
        return questionRepository.findAll ( pageable );
    }

    @Override
    public Optional<Question> getQuestionById(Long questionId) {
        return questionRepository.findById ( questionId );
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save ( question );
    }

    @Override
    public void questionDelete(Long questionId) {
        questionRepository.deleteById ( questionId );
    }
}
