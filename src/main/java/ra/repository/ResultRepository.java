package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.model.entity.Result;
import ra.model.entity.Test;
import ra.model.entity.User;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> getAllByUser(User user);

    List<Result> findAllByUserAndTest(User user, Test test);
}
