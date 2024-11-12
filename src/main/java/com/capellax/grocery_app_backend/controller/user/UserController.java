package com.capellax.grocery_app_backend.controller.user;

import com.capellax.grocery_app_backend.dto.request.user.UpdateUserProfileRequest;
import com.capellax.grocery_app_backend.dto.response.user.GetUserProfileResponse;
import com.capellax.grocery_app_backend.dto.response.user.UpdateUserProfileResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.security.user.UserDetailsImpl;
import com.capellax.grocery_app_backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("${api.base-uri}/users")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<GetUserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<GetUserProfileResponse> response = userService.getUserProfile(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UpdateUserProfileResponse>> updateUserProfile(
            @Valid @RequestBody UpdateUserProfileRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<UpdateUserProfileResponse> response = userService.updateUserProfile(username, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
