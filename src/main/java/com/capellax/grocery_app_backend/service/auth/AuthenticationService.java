package com.capellax.grocery_app_backend.service.auth;

import com.capellax.grocery_app_backend.dto.request.auth.*;
import com.capellax.grocery_app_backend.dto.response.auth.ForgotPasswordResponse;
import com.capellax.grocery_app_backend.dto.response.auth.LoginResponse;
import com.capellax.grocery_app_backend.dto.response.auth.RegisterResponse;
import com.capellax.grocery_app_backend.dto.response.auth.ResetPasswordResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.jwt.JwtService;
import com.capellax.grocery_app_backend.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    ) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        // Logic for already existing user
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (Boolean.TRUE.equals(user.isEnabled())) {
                throw new CustomRuntimeException(ErrorCode.USER_ALREADY_EXISTS);
            }

            if (user.getActivationCodeExpiryDate() != null &&
                    user.getActivationCodeExpiryDate().isAfter(LocalDateTime.now())) {
                return ApiResponse.success(null, "Activation code already sent. Please check your email (" + user.getEmail() + ") box or spam.");
            }

            String newActivationCode = authenticationServiceUtils.generateActivationCode();
            user.setActivationCode(newActivationCode);
            user.setActivationCodeExpiryDate(authenticationServiceUtils.activationCodeExpiryDate);

            userRepository.save(user);
            mailService.sendActivationCode(user.getEmail(), user.getUsername(), newActivationCode);

            return ApiResponse.success(null, "New activation code sent to (" + user.getEmail() + ")");
        }

        // Registration for brand-new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String activationCode = authenticationServiceUtils.generateActivationCode();
        user.setActivationCode(activationCode);
        user.setActivationCodeExpiryDate(authenticationServiceUtils.activationCodeExpiryDate);
        user.setEnabled(false);

        userRepository.save(user);
        mailService.sendActivationCode(user.getEmail(), user.getUsername(), activationCode);

        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setMessage("Please check your email (" + user.getEmail() + ") for the activation code");

        return ApiResponse.success(response, "User registered successfully. Activation code sent via email.");
    }

    public ApiResponse<String> activateUser(
            ActivateAccountRequest request
    ) {
        User user = userRepository.findByEmailAndActivationCode(
                        request.getEmail(),
                        request.getActivationCode()
                )
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        if (!user.getActivationCode().equals(request.getActivationCode())) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_ACTIVATION_CODE);
        }

        if (user.getActivationCodeExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_ACTIVATION_CODE);
        }

        user.setEnabled(true);
        user.setActivationCode(null);
        user.setActivationCodeExpiryDate(null);
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
            throw new CustomRuntimeException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public ApiResponse<ForgotPasswordResponse> forgotPassword(
            ForgotPasswordRequest request
    ) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        String resetPasswordCode = authenticationServiceUtils.generateResetPasswordCode();
        user.setResetPasswordCode(resetPasswordCode);
        user.setResetPasswordCodeExpiryDate(authenticationServiceUtils.resetPasswordCodeExpiryDate);

        userRepository.save(user);

        mailService.sendResetPasswordCode(
                user.getEmail(),
                user.getUsername(),
                resetPasswordCode
        );

        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setMessage("Password reset code sent to " + user.getEmail());

        return ApiResponse.success(response, "Password reset code sent successfully.");
    }

    public ApiResponse<ResetPasswordResponse> resetPassword(
            ResetPasswordRequest request
    ) {
        User user = userRepository.findByEmailAndResetPasswordCode(
                        request.getEmail(),
                        request.getResetPasswordCode()
                )
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_RESET_PASSWORD_CODE));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordCode(null);
        user.setResetPasswordCodeExpiryDate(null);

        userRepository.save(user);

        return ApiResponse.success(null, "Password reset successfully.");
    }

}
