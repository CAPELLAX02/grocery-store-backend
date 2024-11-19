package com.capellax.grocery_app_backend.service.auth.helper;

import com.capellax.grocery_app_backend.dto.request.auth.LoginRequest;
import com.capellax.grocery_app_backend.dto.response.auth.LoginResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginServiceHelper {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ApiResponse<LoginResponse> handleLogin(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String token = jwtService.generateToken(request.getUsername());
            LoginResponse response = new LoginResponse();
            response.setUsername(request.getUsername());
            response.setToken(token);

            return ApiResponse.success(response, "Logged in successfully.");
        } catch (BadCredentialsException e) {
            throw new CustomRuntimeException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

}
