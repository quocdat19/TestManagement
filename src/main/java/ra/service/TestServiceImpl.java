package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Test;
import ra.repository.TestRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;

    @Override
    public Page<Test> getAll(Pageable pageable) {
        return testRepository.findAll ( pageable );
    }

    @Override
    public Optional<Test> getTestById(Long testId) {
        return testRepository.findById ( testId );
    }

    @Override
    public Test save(Test test) {
        return testRepository.save ( test );
    }

    @Override
    public void testDelete(Long testId) {
        testRepository.deleteById ( testId );
    }
}
