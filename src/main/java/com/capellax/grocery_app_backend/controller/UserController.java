package com.capellax.grocery_app_backend.controller;

import com.capellax.grocery_app_backend.dto.request.user.UpdateUserProfileRequest;
import com.capellax.grocery_app_backend.dto.response.user.GetUserProfileResponse;
import com.capellax.grocery_app_backend.dto.response.user.UpdateUserProfileResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<GetUserProfileResponse>> getUserProfile() {
        ApiResponse<GetUserProfileResponse> response = userService.getUserProfile();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UpdateUserProfileResponse>> updateUserProfile(
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        ApiResponse<UpdateUserProfileResponse> response = userService.updateUserProfile(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
