package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.dto.request.OptionRequest;
import ra.model.dto.response.OptionResponse;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EOptionStatus;
import ra.model.entity.Option;
import ra.model.entity.Question;
import ra.repository.OptionRepository;
import ra.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;

    @Override
    public Page<Option> getAll(Pageable pageable) {
        return optionRepository.findAll ( pageable );
    }

    @Override
    public Optional<Option> getOptionById(Long optionId) {
        return optionRepository.findById ( optionId );
    }

    @Override
    public Option save(Option option) {
        return optionRepository.save ( option );
    }

    @Override
    public Option save(OptionRequest optionRequest) {
        return optionRepository.save ( entityAMap ( (optionRequest) ) );
    }

    @Override
    public Option patchUpdateOption(Long optionId, OptionRequest optionRequest) {
        Optional<Option> updateOption = optionRepository.findById ( optionId );
        if (updateOption.isPresent ()) {
            Option option = updateOption.get ();
            if (optionRequest.getOptionContent () != null)
                option.setOptionContent ( optionRequest.getOptionContent () );
            if (optionRequest.getQuestionId () != null) {
                Question question = questionRepository.findById ( optionRequest.getQuestionId () ).orElse ( null );
                option.setQuestion ( question );
            }
            if (optionRequest.getIsCorrect () != null) {
                EOptionStatus isCorrect = switch (optionRequest.getIsCorrect ().toUpperCase ()) {
                    case "INCORRECT" -> EOptionStatus.INCORRECT;
                    case "CORRECT" -> EOptionStatus.CORRECT;
                    default -> null;
                };
                option.setIsCorrect ( isCorrect );
            }
            if (optionRequest.getStatus () != null) {
                EActiveStatus status = switch (optionRequest.getStatus ().toUpperCase ()) {
                    case "INACTIVE" -> EActiveStatus.INACTIVE;
                    case "ACTIVE" -> EActiveStatus.ACTIVE;
                    default -> null;
                };
                option.setStatus ( status );
            }
            return optionRepository.save ( option );
        }
        return null;
    }

    @Override
    public void optionDelete(Long optionId) {
        optionRepository.deleteById ( optionId );
    }

    @Override
    public List<Option> getAllByQuestion(Question question) {
        return optionRepository.findAllByQuestion ( question );
    }

    @Override
    public void deleteByQuestion(Question question) {
        optionRepository.deleteByQuestion ( question );
    }

    @Override
    public Option entityAMap(OptionRequest optionRequest) {
        EOptionStatus isCorrect = switch (optionRequest.getIsCorrect ().toUpperCase ()) {
            case "INCORRECT" -> EOptionStatus.INCORRECT;
            case "CORRECT" -> EOptionStatus.CORRECT;
            default -> null;
        };
        EActiveStatus status = switch (optionRequest.getStatus ().toUpperCase ()) {
            case "INACTIVE" -> EActiveStatus.INACTIVE;
            case "ACTIVE" -> EActiveStatus.ACTIVE;
            default -> null;
        };
        return Option.builder ()
                .optionContent ( optionRequest.getOptionContent () )
                .question ( questionRepository.findById ( optionRequest.getQuestionId () ).orElse ( null ) )
                .isCorrect ( isCorrect )
                .status ( status )
                .build ();
    }

    @Override
    public OptionResponse entityAMap(Option option) {
        return OptionResponse.builder()
                .optionId(option.getId())
                .optionContent(option.getOptionContent())
                .isCorrect(option.getIsCorrect())
                .status(option.getStatus())
                .build();
    }
}
