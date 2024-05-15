package ra.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.exception.CustomException;
import ra.model.dto.auth.LoginRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> handleLogin(@RequestBody @Valid LoginRequest loginRequest) throws CustomException {
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.OK.value (),
                        HttpStatus.OK.name (),
                        userService.handleLogin ( loginRequest )
                ), HttpStatus.OK );
    }
}