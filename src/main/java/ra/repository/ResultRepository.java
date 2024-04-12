package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
}
