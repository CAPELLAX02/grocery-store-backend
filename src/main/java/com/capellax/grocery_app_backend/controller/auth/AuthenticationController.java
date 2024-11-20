package com.capellax.grocery_app_backend.controller.auth;

import com.capellax.grocery_app_backend.dto.request.auth.*;
import com.capellax.grocery_app_backend.dto.response.auth.*;
import com.capellax.grocery_app_backend.exception.custom.CustomMailException;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.auth.AuthenticationService;
import com.capellax.grocery_app_backend.service.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("${api.base-uri}/auth")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) throws CustomMailException {
        ApiResponse<RegisterResponse> response = authenticationService.registerUser(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/activate")
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

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        jwtService.validateRefreshToken(request.getRefreshToken());
        String username = jwtService.extractUsernameFromToken(request.getRefreshToken());
        String newAccessToken = jwtService.generateAccessToken(username);
        RefreshTokenResponse response = new RefreshTokenResponse(newAccessToken, request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed successfully."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<ForgotPasswordResponse>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) throws CustomMailException {
        ApiResponse<ForgotPasswordResponse> response = authenticationService.forgotPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        ApiResponse<ResetPasswordResponse> response = authenticationService.resetPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
