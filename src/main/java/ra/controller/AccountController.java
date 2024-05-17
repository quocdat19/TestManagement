package ra.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.ChangeInformation;
import ra.model.dto.ChangePassword;
import ra.model.dto.InformationAccount;
import ra.model.dto.wrapper.ResponseWrapper;
import ra.model.entity.Enums.EHttpStatus;
import ra.model.entity.User;
import ra.security.UserDetail.UserLoggedIn;
import ra.service.UserService;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
    private final UserLoggedIn userLogin;

    @GetMapping("/informationAccount")
    public ResponseEntity<?> informationAccount() {
        User user = userLogin.getUserLoggedIn ();
        InformationAccount informationPersonal = userService.entityMap ( user );
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.OK.value (),
                        HttpStatus.OK.name (),
                        informationPersonal
                ), HttpStatus.OK );
    }

    @PutMapping("/updateAccount")
    public ResponseEntity<?> updateAccount(@RequestBody @Valid ChangeInformation changeInformation) throws CustomException {
        User user = userService.updateAcc ( changeInformation, userLogin.getUserLoggedIn ().getId () );
        InformationAccount informationPersonal = userService.entityMap ( user );
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.OK.value (),
                        HttpStatus.OK.name (),
                        informationPersonal
                ), HttpStatus.OK );
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid ChangePassword changePassword) throws CustomException {
        User user = userService.updatePassword ( changePassword, userLogin.getUserLoggedIn ().getId () );
        userService.entityMap ( user );
        return new ResponseEntity<> (
                new ResponseWrapper<> (
                        EHttpStatus.SUCCESS,
                        HttpStatus.OK.value (),
                        HttpStatus.OK.name (),
                        "Change success"
                ), HttpStatus.OK );
    }
}
