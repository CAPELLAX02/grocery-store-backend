package com.capellax.grocery_app_backend.controller;

import com.capellax.grocery_app_backend.dto.request.ActivateAccountRequest;
import com.capellax.grocery_app_backend.dto.request.LoginRequest;
import com.capellax.grocery_app_backend.dto.request.RegisterRequest;
import com.capellax.grocery_app_backend.dto.response.LoginResponse;
import com.capellax.grocery_app_backend.dto.response.RegisterResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        ApiResponse<RegisterResponse> response = authenticationService.registerUser(registerRequest);
        return new ResponseEntity<>(response, response.getStatus() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/activate")
    public ResponseEntity<ApiResponse<String>> activateUser(
            @Valid @RequestBody ActivateAccountRequest activateAccountRequest
    ) {
        ApiResponse<String> response = authenticationService.activateUser(activateAccountRequest);
        HttpStatus status = response.getStatus() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        ApiResponse<LoginResponse> response = authenticationService.loginUser(loginRequest);
        HttpStatus status = response.getStatus() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

}
