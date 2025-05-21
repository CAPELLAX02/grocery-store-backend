package com.capellax.grocery_app_backend.controller.furkan;

import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/furkidolki/premium-endpointler")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        ApiResponse<List<User>> res = userService.getAllUsers();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/delete-all-users")
    public ResponseEntity<ApiResponse<String>> deleteAllUsers() {
        ApiResponse<String> res = userService.deleteAllUsers();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUserById(
            @PathVariable String userId
    ) {
        ApiResponse<String> res = userService.deleteUserById(userId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
