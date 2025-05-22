package com.capellax.grocery_app_backend.service.auth.helper;

import com.capellax.grocery_app_backend.dto.request.auth.ForgotPasswordRequest;
import com.capellax.grocery_app_backend.dto.request.auth.ResetPasswordRequest;
import com.capellax.grocery_app_backend.dto.response.auth.ForgotPasswordResponse;
import com.capellax.grocery_app_backend.dto.response.auth.ResetPasswordResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.auth.utils.AuthenticationServiceUtils;
import com.capellax.grocery_app_backend.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordServiceHelper {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final AuthenticationServiceUtils utils;

    public ApiResponse<ForgotPasswordResponse> handleForgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        if (utils.isCodeStillValid(user.getResetPasswordCodeExpiryDate())) {
            return ApiResponse.success(
                    null,
                    "Reset password code already sent. Please check your email."
            );
        }

        String resetPasswordCode = utils.generateResetPasswordCode();
        user.setResetPasswordCode(resetPasswordCode);
        user.setResetPasswordCodeExpiryDate(utils.newExpiryDate());

        userRepository.save(user);
        mailService.sendResetPasswordCode(user.getEmail(), user.getUsername(), resetPasswordCode);

        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setMessage("Password reset code sent to " + user.getEmail());

        return ApiResponse.success(response, "Password reset code sent successfully.");
    }

    public ApiResponse<ResetPasswordResponse> handleResetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        boolean codeMismatch = !request.getResetPasswordCode().equals(user.getResetPasswordCode());
        boolean codeExpired  = utils.isCodeExpired(user.getResetPasswordCodeExpiryDate());

        if (codeMismatch || codeExpired) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_RESET_PASSWORD_CODE);
        }

        user.setPassword(utils.encodePassword(request.getNewPassword()));
        user.setResetPasswordCode(null);
        user.setResetPasswordCodeExpiryDate(null);
        userRepository.save(user);

        ResetPasswordResponse response = new ResetPasswordResponse();
        response.setMessage("Password reset successfully.");

        return ApiResponse.success(response, "Password reset successfully.");
    }
}
