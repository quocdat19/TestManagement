package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
