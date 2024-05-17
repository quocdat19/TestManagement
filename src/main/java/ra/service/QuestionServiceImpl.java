package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.dto.request.OptionRequest;
import ra.model.dto.request.QuestionOptionRequest;
import ra.model.dto.request.QuestionRequest;
import ra.model.dto.response.QuestionResponse;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EQuestionLevel;
import ra.model.entity.Enums.EQuestionType;
import ra.model.entity.Option;
import ra.model.entity.Question;
import ra.model.entity.Test;
import ra.repository.QuestionRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TestService testService;
    private final OptionService optionService;

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
    public Question save(QuestionRequest questionRequest) {
        return questionRepository.save ( entityAMap ( questionRequest ) );
    }

    @Override
    public Question patchUpdateQuestion(Long questionId, QuestionRequest questionRequest) {
        Optional<Question> updateQuestion = questionRepository.findById ( questionId );
        if (updateQuestion.isPresent ()) {
            Question question = updateQuestion.get ();
            if (questionRequest.getQuestionContent () != null)
                question.setQuestionContent ( questionRequest.getQuestionContent () );
            if (questionRequest.getQuestionLevel () != null) {
                EQuestionLevel questionLevel = switch (questionRequest.getQuestionLevel ()) {
                    case "EASY" -> EQuestionLevel.EASY;
                    case "NORMAL" -> EQuestionLevel.NORMAL;
                    case "DIFFICULTY" -> EQuestionLevel.DIFFICULTY;
                    default -> null;
                };
                question.setQuestionLevel ( questionLevel );
            }
            if (questionRequest.getQuestionType () != null) {
                EQuestionType questionType = switch (questionRequest.getQuestionType ()) {
                    case "SINGLE" -> EQuestionType.SINGLE;
                    case "MULTIPLE" -> EQuestionType.MULTIPLE;
                    default -> null;
                };
                question.setQuestionType ( questionType );
            }
            if (questionRequest.getImage () != null)
                question.setImage ( questionRequest.getImage () );
            if (questionRequest.getTestId () != null)
                question.setTest ( testService.getTestById ( questionRequest.getTestId () ).get () );
            return questionRepository.save ( question );
        }
        return null;
    }

    @Override
    public void questionDelete(Long questionId) {
        questionRepository.deleteById ( questionId );
    }

    @Override
    public Page<Question> findByQuestionContent(String questionContent, Pageable pageable) {
        return questionRepository.findAllByQuestionContentIsContainingIgnoreCase ( questionContent, pageable );
    }

    @Override
    public List<QuestionResponse> getAllByTest(Test test) {
        List<Question> questions = questionRepository.getAllByTest ( test );
        return questions.stream ().map ( this::entityAMap ).toList ();
    }

    @Override
    public List<Question> getAllQuestionByTest(Test test) {
        return questionRepository.getAllByTest ( test );
    }

    @Override
    public Question saveQuestionAndOption(QuestionOptionRequest questionOptionRequest) {
        Question question = save ( questionOptionRequest.getQuestionRequest () );
        List<OptionRequest> optionRequests = questionOptionRequest.getOptionRequests ();
        for (OptionRequest optionRequest : optionRequests) {
            optionRequest.setQuestionId ( question.getId () );
            optionRequest.setStatus ( "ACTIVE" );
            optionService.save ( optionRequest );
        }
        List<Option> options = optionService.getAllByQuestion ( question );
        question.setOptions ( options );
        return question;
    }

    @Override
    public List<QuestionResponse> getAllByCreatedDate(LocalDate date) {
        List<Question> questions = questionRepository.getAllByCreatedDate ( date );
        return questions.stream ().map ( this::entityAMap ).toList ();
    }

    @Override
    public List<QuestionResponse> getAllByQuestionLevel(EQuestionLevel questionLevel) {
        List<Question> questions = questionRepository.getAllByQuestionLevel ( questionLevel );
        return questions.stream ().map ( this::entityAMap ).toList ();
    }

    @Override
    public List<QuestionResponse> getAllByTestRandom(Test test) {
        List<Question> questions = questionRepository.getAllByTest ( test );
        Collections.shuffle ( questions );
        return questions.stream ().map ( this::entityAMap ).toList ();
    }

    @Override
    public QuestionResponse entityAMap(Question question) {
        return QuestionResponse.builder ()
                .questionId ( question.getId () )
                .questionContent ( question.getQuestionContent () )
                .questionLevel ( question.getQuestionLevel () )
                .questionType ( question.getQuestionType () )
                .image ( question.getImage () )
                .status ( question.getStatus () )
                .createdDate ( question.getCreatedDate () )
                .testName ( question.getTest ().getTestName () )
                .options ( question.getOptions ().stream ().map ( optionService::entityAMap ).toList () )
                .build ();
    }

    @Override
    public Question entityAMap(QuestionRequest questionRequest) {
        EQuestionLevel questionLevel = null;
        if (questionRequest.getQuestionLevel () != null)
            questionLevel = switch (questionRequest.getQuestionLevel ()) {
                case "EASY" -> EQuestionLevel.EASY;
                case "NORMAL" -> EQuestionLevel.NORMAL;
                case "DIFFICULTY" -> EQuestionLevel.DIFFICULTY;
                default -> null;
            };
        EQuestionType questionType = switch (questionRequest.getQuestionType ()) {
            case "SINGLE" -> EQuestionType.SINGLE;
            case "MULTIPLE" -> EQuestionType.MULTIPLE;
            default -> null;
        };
        return Question.builder ()
                .questionContent ( questionRequest.getQuestionContent () )
                .questionLevel ( questionLevel )
                .questionType ( questionType )
                .image ( questionRequest.getImage () )
                .status ( EActiveStatus.ACTIVE )
                .test ( testService.getTestById ( questionRequest.getTestId () ).get () )
                .build ();
    }
}
