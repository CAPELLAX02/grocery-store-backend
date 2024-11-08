package com.capellax.grocery_app_backend.controller;

import com.capellax.grocery_app_backend.dto.request.*;
import com.capellax.grocery_app_backend.dto.response.ForgotPasswordResponse;
import com.capellax.grocery_app_backend.dto.response.LoginResponse;
import com.capellax.grocery_app_backend.dto.response.RegisterResponse;
import com.capellax.grocery_app_backend.dto.response.ResetPasswordResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) {
        ApiResponse<RegisterResponse> response = authenticationService.registerUser(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/activate")
    public ResponseEntity<ApiResponse<String>> activateUser(
            @Valid @RequestBody ActivateAccountRequest request
    ) {
        ApiResponse<String> response = authenticationService.activateUser(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(
            @Valid @RequestBody LoginRequest request
    ) {
        ApiResponse<LoginResponse> response = authenticationService.loginUser(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        ApiResponse<ForgotPasswordResponse> response = authenticationService.forgotPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        ApiResponse<ResetPasswordResponse> response = authenticationService.resetPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
