package com.capellax.grocery_app_backend.controller.auth;

import com.capellax.grocery_app_backend.dto.request.auth.*;
import com.capellax.grocery_app_backend.dto.response.auth.ForgotPasswordResponse;
import com.capellax.grocery_app_backend.dto.response.auth.LoginResponse;
import com.capellax.grocery_app_backend.dto.response.auth.RegisterResponse;
import com.capellax.grocery_app_backend.dto.response.auth.ResetPasswordResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomMailException;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-uri}/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints related to authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Register user & Send activation code via email to activate their account")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) throws CustomMailException {
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

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) throws CustomMailException {
        ApiResponse<ForgotPasswordResponse> response = authenticationService.forgotPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        ApiResponse<ResetPasswordResponse> response = authenticationService.resetPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
