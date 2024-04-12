package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Option;

public interface OptionRepository extends JpaRepository<Option,Long> {
}
