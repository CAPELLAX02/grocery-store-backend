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
        try {
            RegisterResponse response = authenticationService.registerUser(registerRequest);
            return ResponseEntity.ok(ApiResponse.success(
                    response,
                    "Registration successfull. Please check your email for the activation code.")
            );

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(
                            "Registration failed. Please try again later.",
                            HttpStatus.INTERNAL_SERVER_ERROR
                            )
                    );
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<ApiResponse<String>> activateUser(
            @Valid @RequestBody ActivateAccountRequest activateAccountRequest
    ) {
        boolean isActivated = authenticationService.activateUser(
                activateAccountRequest.getEmail(),
                activateAccountRequest.getActivationCode()
        );

        if (isActivated) {
            return ResponseEntity.ok(ApiResponse.success(
                    null,
                    "Account activated successfully."
                    )
            );

        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(
                            "Invalid activation code or email.",
                            HttpStatus.BAD_REQUEST
                            )
                    );
        }
    }


}
