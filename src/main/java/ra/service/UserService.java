package ra.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CustomException;
import ra.model.dto.ChangeInformation;
import ra.model.dto.ChangePassword;
import ra.model.dto.InformationAccount;
import ra.model.dto.auth.JwtResponse;
import ra.model.dto.auth.LoginRequest;
import ra.model.dto.auth.RegisterRequest;
import ra.model.dto.request.UserRequest;
import ra.model.entity.Role;
import ra.model.entity.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> getUserById(Long userId);

    Optional<User> getUserByUsername(String username);

    User createUser(UserRequest userRequest);

    JwtResponse handleLogin(LoginRequest loginRequest) throws CustomException;

    User updateAcc(ChangeInformation changeInformation, Long userId) throws CustomException;

    User updatePassword(ChangePassword newPassword, Long userId) throws CustomException;

    User handleRegister(RegisterRequest registerRequest) throws CustomException;

    Page<User> getAllToList(Pageable pageable);

    void deleteById(Long userId);

    User save(User users);

    Set<Role> getAllRolesByUser(User user);

    Page<User> findByUsernameOrFullNameContainingIgnoreCase(String keyword, Pageable pageable);

    InformationAccount entityMap(User user);
}
