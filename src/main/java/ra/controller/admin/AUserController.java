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
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.User;
import ra.service.UserService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/users")
public class AUserController {
    @Autowired
    private UserService userService;

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
            Page<User> userResponses = userService.getAllToList ( pageable );
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
        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        EHttpStatus.SUCCESS,
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.name(),
                        userService.entityMap(userService.handleRegister(RegisterRequest))
                ), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId) throws CustomException {
        try {
            Long id = Long.parseLong ( userId );
            Optional<User> user = userService.getUserById ( id );
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
            Page<User> userResponses = userService.findByUsernameOrFullNameContainingIgnoreCase ( keyword, pageable );
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
}
