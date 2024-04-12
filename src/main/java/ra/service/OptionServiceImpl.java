package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.dto.request.OptionRequest;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Option;
import ra.repository.OptionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;

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
    public void optionDelete(Long optionId) {
        optionRepository.deleteById ( optionId );
    }
}
