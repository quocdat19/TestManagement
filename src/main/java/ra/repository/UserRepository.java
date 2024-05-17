package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> getUserByUsername(String username);

    @Query("SELECT u from User u join u.roles r where r.role <> 'ROLE_ADMIN' and (u.username LIKE CONCAT('%', :keyword, '%') OR u.fullName LIKE CONCAT('%', :keyword, '%'))")
    Page<User> findByUsernameOrFullNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.role <> 'ROLE_ADMIN'")
    Page<User> getAllUserExceptAdmin(Pageable pageable);
}
