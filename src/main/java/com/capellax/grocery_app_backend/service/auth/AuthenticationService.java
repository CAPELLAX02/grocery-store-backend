package com.capellax.grocery_app_backend.service.auth;

import com.capellax.grocery_app_backend.dto.request.auth.*;
import com.capellax.grocery_app_backend.dto.response.auth.ForgotPasswordResponse;
import com.capellax.grocery_app_backend.dto.response.auth.LoginResponse;
import com.capellax.grocery_app_backend.dto.response.auth.RegisterResponse;
import com.capellax.grocery_app_backend.dto.response.auth.ResetPasswordResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorType;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.jwt.JwtService;
import com.capellax.grocery_app_backend.service.mail.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtService jwtService;
    private final AuthenticationServiceUtils authenticationServiceUtils;

    public ApiResponse<RegisterResponse> registerUser(
            RegisterRequest request
    ) throws MessagingException {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new CustomRuntimeException(ErrorType.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String activationCode = authenticationServiceUtils.generateActivationCode();
        user.setActivationCode(activationCode);
        user.setEnabled(false);

        userRepository.save(user);

        try {
            mailService.sendActivationCode(
                    user.getEmail(),
                    user.getUsername(),
                    activationCode
            );

        } catch (MailException e) {
            throw new MessagingException("Failed to send activation code", e);
        }

        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setMessage("Please check your email (" + user.getEmail() + ") for the activation code");

        return ApiResponse.success(response, "User registered successfully. Activation code sent via email.");
    }

    public ApiResponse<String> activateUser(
            ActivateAccountRequest request
    ) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomRuntimeException(ErrorType.USER_NOT_FOUND));

        if (!user.getActivationCode().equals(request.getActivationCode())) {
            throw new CustomRuntimeException(ErrorType.INVALID_ACTIVATION_CODE);
        }

        user.setEnabled(true);
        user.setActivationCode(null);
        userRepository.save(user);

        return ApiResponse.success(null, "Account activated successfully");
    }

    public ApiResponse<LoginResponse> loginUser(
            LoginRequest request
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails.getUsername());

            LoginResponse response = new LoginResponse();
            response.setUsername(userDetails.getUsername());
            response.setToken(token);

            return ApiResponse.success(response, "Logged in successfully");

        } catch (BadCredentialsException e) {
            throw new CustomRuntimeException(ErrorType.INVALID_CREDENTIALS);
        }
    }

    public ApiResponse<ForgotPasswordResponse> forgotPassword(
            ForgotPasswordRequest request
    ) throws MessagingException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomRuntimeException(ErrorType.USER_NOT_FOUND));

        String resetPasswordCode = authenticationServiceUtils.generateResetPasswordCode();
        user.setActivationCode(resetPasswordCode); // maybe .setResetPasswordCode later on

        userRepository.save(user);

        try {
            mailService.sendResetPasswordCode(
                    user.getEmail(),
                    user.getUsername(),
                    resetPasswordCode
            );

        } catch (MailException e) {
            throw new MessagingException("Failed to send reset password code", e);
        }

        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setMessage("Password reset code sent to " + user.getEmail());

        return ApiResponse.success(response, "Password reset code sent successfully.");
    }

    public ApiResponse<ResetPasswordResponse> resetPassword(
            ResetPasswordRequest request
    ) {
        User user = userRepository.findByActivationCode(request.getResetPasswordCode())
                .orElseThrow(() -> new CustomRuntimeException(ErrorType.INVALID_RESET_PASSWORD_CODE));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setActivationCode(null);

        userRepository.save(user);

        ResetPasswordResponse response = new ResetPasswordResponse();
        response.setMessage("Password reset successfully.");

        return ApiResponse.success(response, "Password reset successfully.");
    }

}
