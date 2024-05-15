package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.request.OptionRequest;
import ra.model.dto.response.OptionResponse;
import ra.model.entity.Option;
import ra.model.entity.Question;

import java.util.List;
import java.util.Optional;

public interface OptionService {
    Page<Option> getAll(Pageable pageable);

    Optional<Option> getOptionById(Long optionId);

    Option save(Option option);

    Option save(OptionRequest optionRequest);

    Option patchUpdateOption(Long optionId, OptionRequest optionRequest);

    void optionDelete(Long optionId);

    List<Option> getAllByQuestion(Question question);

    void deleteByQuestion(Question question);

    Option entityAMap(OptionRequest optionRequest);

    OptionResponse entityAMap(Option option);
}
