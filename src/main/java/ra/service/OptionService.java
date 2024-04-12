package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.dto.request.OptionRequest;
import ra.model.entity.Option;

import java.util.Optional;

public interface OptionService {
    Page<Option> getAll(Pageable pageable);

    Optional<Option> getOptionById(Long optionId);

    Option save(Option option);

    void optionDelete(Long optionId);
}
