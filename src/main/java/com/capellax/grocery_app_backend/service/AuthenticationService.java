package com.capellax.grocery_app_backend.service;

import com.capellax.grocery_app_backend.dto.request.*;
import com.capellax.grocery_app_backend.dto.response.ForgotPasswordResponse;
import com.capellax.grocery_app_backend.dto.response.LoginResponse;
import com.capellax.grocery_app_backend.dto.response.RegisterResponse;
import com.capellax.grocery_app_backend.dto.response.ResetPasswordResponse;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtService jwtService;

    public ApiResponse<RegisterResponse> registerUser(
            RegisterRequest registerRequest
    ) {
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.isEnabled()) {
                return ApiResponse.error("User already registered and activated.", HttpStatus.CONFLICT);
            }
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        String activationCode = generateActivationCode();
        user.setActivationCode(activationCode);
        user.setEnabled(false);

        userRepository.save(user);

        mailService.sendActivationCode(user.getEmail(), user.getUsername(), activationCode);

        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setMessage("Please check your email (" + user.getEmail() + ") for the activation code");

        return ApiResponse.success(response, "User registered successfully. Activation code sent via email.");
    }

    private String generateActivationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(100000);
        return String.valueOf(code);
    }

    public ApiResponse<String> activateUser(
            ActivateAccountRequest activateAccountRequest
    ) {
        Optional<User> userOptional = userRepository.findByEmail(activateAccountRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ApiResponse.error("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (user.getActivationCode().equals(activateAccountRequest.getActivationCode())) {
            user.setEnabled(true);
            user.setActivationCode(null);
            userRepository.save(user);
            return ApiResponse.success(null, "Account activated successfully.");

        } else {
            return ApiResponse.error("Invalid activation code or email.", HttpStatus.BAD_REQUEST);
        }
    }

    public ApiResponse<LoginResponse> loginUser(
            LoginRequest loginRequest
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails.getUsername());

            LoginResponse response = new LoginResponse();
            response.setUsername(userDetails.getUsername());
            response.setToken(token);

            return ApiResponse.success(response, "Logged in successfully");

        } catch (BadCredentialsException e) {
            return ApiResponse.error(
                    "Invalid username or password.",
                    HttpStatus.BAD_REQUEST
            );

        } catch (Exception e) {
            return ApiResponse.error(
                    "An unexpected error occurred during login. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ApiResponse<ForgotPasswordResponse> forgotPassword(
            ForgotPasswordRequest forgotPasswordRequest
    ) {
        Optional<User> userOptional = userRepository.findByEmail(forgotPasswordRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ApiResponse.error("User with this email does not exist.", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        String resetPasswordCode = generateResetPasswordCode();
        user.setActivationCode(resetPasswordCode); // maybe .setResetPasswordCode later on
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

    private String generateResetPasswordCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(100000);
        return String.valueOf(code);
    }

    public ApiResponse<ResetPasswordResponse> resetPassword(
            ResetPasswordRequest resetPasswordRequest
    ) {
        Optional<User> userOptional = userRepository.findByActivationCode(resetPasswordRequest.getResetPasswordCode());

        if (userOptional.isEmpty()) {
            return ApiResponse.error("Invalid or expired reset code.", HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setActivationCode(null);
        userRepository.save(user);

        ResetPasswordResponse response = new ResetPasswordResponse();
        response.setMessage("Password reset successfully.");

        return ApiResponse.success(response, "Password reset successfully.");
    }

}
