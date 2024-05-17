package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.request.QuestionOptionRequest;
import ra.model.dto.request.QuestionRequest;
import ra.model.dto.response.QuestionResponse;
import ra.model.entity.Enums.EQuestionLevel;
import ra.model.entity.Question;
import ra.model.entity.Test;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Page<Question> getAll(Pageable pageable);

    Optional<Question> getQuestionById(Long questionId);

    Question save(Question question);

    Question save(QuestionRequest questionRequest);

    Question patchUpdateQuestion(Long questionId, QuestionRequest questionRequest);

    void questionDelete(Long questionId);

    Page<Question> findByQuestionContent(String questionContent, Pageable pageable);

    List<QuestionResponse> getAllByTest(Test test);

    List<Question> getAllQuestionByTest(Test test);

    Question saveQuestionAndOption(QuestionOptionRequest questionOptionRequest);

    List<QuestionResponse> getAllByCreatedDate(LocalDate date);

    List<QuestionResponse> getAllByQuestionLevel(EQuestionLevel questionLevel);

    //* Lay ds cau hoi random
    List<QuestionResponse> getAllByTestRandom(Test test);

    QuestionResponse entityAMap(Question question);

    Question entityAMap(QuestionRequest questionRequest);

}
