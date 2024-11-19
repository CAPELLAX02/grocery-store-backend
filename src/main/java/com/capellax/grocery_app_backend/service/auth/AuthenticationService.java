package com.capellax.grocery_app_backend.service.auth;

import com.capellax.grocery_app_backend.dto.request.auth.*;
import com.capellax.grocery_app_backend.dto.response.auth.ForgotPasswordResponse;
import com.capellax.grocery_app_backend.dto.response.auth.LoginResponse;
import com.capellax.grocery_app_backend.dto.response.auth.RegisterResponse;
import com.capellax.grocery_app_backend.dto.response.auth.ResetPasswordResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.auth.helper.ActivationServiceHelper;
import com.capellax.grocery_app_backend.service.auth.helper.LoginServiceHelper;
import com.capellax.grocery_app_backend.service.auth.helper.PasswordServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ActivationServiceHelper activationHelper;
    private final PasswordServiceHelper passwordHelper;
    private final LoginServiceHelper loginHelper;

    public ApiResponse<RegisterResponse> registerUser(RegisterRequest request) {
        return activationHelper.handleUserRegistration(request);
    }

    public ApiResponse<String> activateUser(ActivateAccountRequest request) {
        return activationHelper.handleAccountActivation(request);
    }

    public ApiResponse<LoginResponse> loginUser(LoginRequest request) {
        return loginHelper.handleLogin(request);
    }

    public ApiResponse<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request) {
        return passwordHelper.handleForgotPassword(request);
    }

    public ApiResponse<ResetPasswordResponse> resetPassword(ResetPasswordRequest request) {
        return passwordHelper.handleResetPassword(request);
    }

}
