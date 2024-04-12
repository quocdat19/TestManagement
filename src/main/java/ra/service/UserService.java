package ra.service;

import ra.model.dto.request.UserRequest;
import ra.model.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long userId);
    User createUser(UserRequest userRequest);

}
