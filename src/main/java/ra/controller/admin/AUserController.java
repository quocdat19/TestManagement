package ra.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ra.model.dto.request.UserRequest;
import ra.model.dto.response.ResponseAPI;
import ra.model.entity.User;
import ra.service.UserService;

@RestController

public class AUserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    ResponseEntity<?> createUser(@RequestBody UserRequest request){

        return ResponseEntity.status(201).body(new ResponseAPI(true, "Tạo tài khoản thành công"));
    }
}
