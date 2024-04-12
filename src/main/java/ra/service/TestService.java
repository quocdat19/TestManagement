package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Test;

import java.util.Optional;

public interface TestService {
    Page<Test> getAll(Pageable pageable);

    Optional<Test> getTestById(Long testId);

    Test save(Test test);

    void testDelete(Long testId);
}
