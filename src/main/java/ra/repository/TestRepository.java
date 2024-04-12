package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}
