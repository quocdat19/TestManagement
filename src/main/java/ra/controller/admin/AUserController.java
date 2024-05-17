package ra.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.auth.RegisterRequest;
import ra.model.dto.request.UserRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.dto.response.UserResponse;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EActiveStatus;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.User;
import ra.security.UserDetail.UserLoggedIn;
import ra.service.UserService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/users")
public class AUserController {
    private final UserService userService;
    private final UserLoggedIn userLogin;

    @GetMapping
    public ResponseEntity<?> getAllUsersToPages(
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "username", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<UserResponse> userResponses = userService.getAllUserResponsesToList ( pageable );
            if (userResponses.getContent ().isEmpty ()) throw new CustomException ( "Users page is empty." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            userResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> handleRegister(@RequestBody @Valid RegisterRequest RegisterRequest) throws CustomException {
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.CREATED.value (),
                        HttpStatus.CREATED.name (),
                        userService.entityMap ( userService.handleRegister ( RegisterRequest ) )
                ), HttpStatus.CREATED );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId) throws CustomException {
        try {
            Long id = Long.parseLong ( userId );
            Optional<UserResponse> user = userService.getUserResponseById ( id );
            if (user.isEmpty ()) throw new CustomException ( "User is not exists." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            user.get ()
                    ), HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @PutMapping("/switchStatus/{userId}")
    public ResponseEntity<?> switchUserStatus(@PathVariable("userId") String userId) throws CustomException {
        try {
            Long id = Long.parseLong ( userId );
            Optional<User> updateUser = userService.getUserById ( id );
            if (updateUser.isPresent ()) {
                User user = updateUser.get ();
                if (user.getId ().equals ( userLogin.getUserLoggedIn ().getId () )) {
                    throw new CustomException ( "Cant switch this account status" );
                }
                user.setStatus ( user.getStatus () == EActiveStatus.ACTIVE ? EActiveStatus.INACTIVE : EActiveStatus.ACTIVE );
                User updatedUser = userService.save ( user );
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                userService.entityMap ( updatedUser )
                        ), HttpStatus.OK );
            }
            // ? Xử lý Exception cần tìm được user theo id trước khi khoá/mở khoá trong Controller.
            throw new CustomException ( "User is not exists." );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByFullNameOrUserName(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(defaultValue = "5", name = "limit") int limit,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "username", name = "sort") String sort,
            @RequestParam(defaultValue = "asc", name = "order") String order
    ) throws CustomException {
        try {
            Pageable pageable;
            if (order.equals ( "asc" )) pageable = PageRequest.of ( page, limit, Sort.by ( sort ).ascending () );
            else pageable = PageRequest.of ( page, limit, Sort.by ( sort ).descending () );
            Page<UserResponse> userResponses = userService.findByUsernameOrFullNameContainingIgnoreCase ( keyword, pageable );
            if (userResponses.getContent ().isEmpty ()) throw new CustomException ( "User page is empty." );
            return new ResponseEntity<> (
                    new ResponseWrapper<> (
                            EHttpStatus.SUCCESS,
                            HttpStatus.OK.value (),
                            HttpStatus.OK.name (),
                            userResponses.getContent ()
                    ), HttpStatus.OK );
        } catch (Exception exception) {
            throw new CustomException ( "An error occurred while processing the query!" );
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteAccount(@PathVariable("userId") String userId) throws CustomException {
        try {
            Long id = Long.parseLong ( userId );
            Optional<User> deleteUser = userService.getUserById ( id );
            if (deleteUser.isPresent ()) {
                if (deleteUser.get ().getId ().equals ( userLogin.getUserLoggedIn ().getId () )) {
                    throw new CustomException ( "Cant delete this account" );
                }
                userService.deleteById ( id );
                return new ResponseEntity<> (
                        new ResponseWrapper<> (
                                EHttpStatus.SUCCESS,
                                HttpStatus.OK.value (),
                                HttpStatus.OK.name (),
                                "Delete user successfully."
                        )
                        , HttpStatus.OK );
            }
            // ? Xử lý Exception cần tìm được user theo id trước khi khoá/mở khoá trong Controller.
            throw new CustomException ( "User is not exists." );
        } catch (NumberFormatException e) {
            throw new CustomException ( "Incorrect id number format" );
        }
    }
}
