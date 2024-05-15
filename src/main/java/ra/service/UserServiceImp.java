package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.ChangeInformation;
import ra.model.dto.ChangePassword;
import ra.model.dto.InformationAccount;
import ra.model.dto.auth.JwtResponse;
import ra.model.dto.auth.LoginRequest;
import ra.model.dto.auth.RegisterRequest;
import ra.model.dto.request.UserRequest;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.ERoles;
import ra.model.entity.Role;
import ra.model.entity.User;
import ra.repository.UserRepository;
import ra.security.Jwt.JwtProvider;
import ra.security.UserDetail.UserPrincipal;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public Page<User> getAllToList(Pageable pageable) {
        return userRepository.getAllUserExceptAdmin ( pageable );
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById ( userId );
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByUsername ( username );
    }

    @Override
    public User createUser(UserRequest userRequest) {
        User user = new User ();
        user.setUsername ( userRequest.getUsername () );
        user.setPassword ( userRequest.getPassword () );
        user.setEmail ( userRequest.getEmail () );
        user.setFullName ( userRequest.getFullName () );
        user.setPhone ( userRequest.getPhone () );
        return userRepository.save ( user );
    }

    @Override
    public JwtResponse handleLogin(LoginRequest loginRequest) throws CustomException {
        Authentication authentication;
        try {
            authentication = authenticationProvider.authenticate (
                    new UsernamePasswordAuthenticationToken ( loginRequest.getUsername (), loginRequest.getPassword () )
            );
        } catch (AuthenticationException e) {
            throw new CustomException ( "Username or password is wrong." );
        }
        SecurityContextHolder.getContext ().setAuthentication ( authentication );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal ();
        if (!(userPrincipal.getUser ().getStatus () == EActiveStatus.ACTIVE))
            throw new CustomException ( "This account is inactive." );
        return JwtResponse.builder ()
                .accessToken ( jwtProvider.generateToken ( userPrincipal ) )
                .fullName ( userPrincipal.getUser ().getFullName () )
                .username ( userPrincipal.getUsername () )
                .status ( userPrincipal.getUser ().getStatus () == EActiveStatus.ACTIVE )
                .roles ( userPrincipal.getAuthorities ().stream ().map ( GrantedAuthority::getAuthority ).collect ( Collectors.toSet () ) )
                .build ();
    }

    @Override
    public User updateAcc(ChangeInformation changeInformation, Long userId) throws CustomException {
        Optional<User> userOldOptional = getUserById ( userId );
        User userOld = userOldOptional.get ();
        Set<Role> roles = userOld.getRoles ();
        User user = User.builder ()
                .fullName ( changeInformation.getFullName () )
                .password ( userOld.getPassword () )
                .email ( changeInformation.getEmail () )
                .avatar ( changeInformation.getAvatar () )
                .phone ( changeInformation.getPhone () )
                .status ( EActiveStatus.ACTIVE )
                .roles ( roles )
                .build ();
        user.setId ( userOld.getId () );
        user.setUsername ( userOld.getUsername () );
        user.setCreateBy ( userOld.getCreateBy () );
        user.setCreatedDate ( userOld.getCreatedDate () );
        return userRepository.save ( user );
    }

    @Override
    public User updatePassword(ChangePassword changePassword, Long userId) throws CustomException {
        Optional<User> userOptional = getUserById ( userId );
        User user = userOptional.get ();
        if (!passwordEncoder.matches ( changePassword.getOldPassword (), user.getPassword () )) {
            throw new CustomException ( "Old password not true!" );
        } else if (changePassword.getOldPassword ().equals ( changePassword.getNewPassword () )) {
            throw new CustomException ( "New password like old password!" );
        } else if (!changePassword.getNewPassword ().equals ( changePassword.getConfirmPassword () )) {
            throw new CustomException ( "Confirm password not like new password" );
        }
        user.setPassword ( passwordEncoder.encode ( changePassword.getConfirmPassword () ) );
        return userRepository.save ( user );
    }

    @Override
    public User handleRegister(RegisterRequest registerRequest) throws CustomException {
        if (userRepository.existsByUsername ( registerRequest.getUsername () ))
            throw new CustomException ( "Username is exists" );
        Set<Role> userRoles = new HashSet<> ();
        userRoles.add ( roleService.findByRoleName ( ERoles.ROLE_USER ) );
        User users = User.builder ()
                .fullName ( registerRequest.getFullName () )
                .username ( registerRequest.getUsername () )
                .password ( passwordEncoder.encode ( registerRequest.getPassword () ) )
                .email ( registerRequest.getEmail () )
                .avatar ( registerRequest.getAvatar () )
                .phone ( registerRequest.getPhone () )
                .status ( EActiveStatus.INACTIVE )
                .roles ( userRoles )
                .build ();
        return userRepository.save ( users );
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById ( userId );
    }

    @Override
    public User save(User users) {
        return userRepository.save ( users );
    }

    @Override
    public Set<Role> getAllRolesByUser(User user) {
        return user.getRoles ();
    }

    @Override
    public Page<User> findByUsernameOrFullNameContainingIgnoreCase(String keyword, Pageable pageable) {
        return userRepository.findByUsernameOrFullNameContainingIgnoreCase ( keyword, pageable );
    }

    @Override
    public InformationAccount entityMap(User user) {
        return InformationAccount.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .build();
    }
}
