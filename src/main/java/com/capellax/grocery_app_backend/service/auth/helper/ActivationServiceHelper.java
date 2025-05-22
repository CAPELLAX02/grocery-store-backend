package com.capellax.grocery_app_backend.service.auth.helper;

import com.capellax.grocery_app_backend.dto.request.auth.ActivateAccountRequest;
import com.capellax.grocery_app_backend.dto.request.auth.RegisterRequest;
import com.capellax.grocery_app_backend.dto.response.auth.RegisterResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.auth.utils.AuthenticationServiceUtils;
import com.capellax.grocery_app_backend.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ActivationServiceHelper {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final AuthenticationServiceUtils utils;

    public ApiResponse<RegisterResponse> handleUserRegistration(RegisterRequest request) {
        Optional<User> existingUserOpt = userRepository.findByEmail(request.getEmail());

        if (existingUserOpt.isPresent()) {
            User user = existingUserOpt.get();

            if (user.isEnabled()) {
                throw new CustomRuntimeException(ErrorCode.USER_ALREADY_EXISTS);
            }

            if (utils.isCodeStillValid(user.getActivationCodeExpiryDate())) {
                return ApiResponse.success(
                        null,
                        "Activation code already sent. Please check your email."
                );
            }

            String newActivationCode = utils.generateActivationCode();
            user.setActivationCode(newActivationCode);
            user.setActivationCodeExpiryDate(utils.newExpiryDate());

            userRepository.save(user);
            mailService.sendActivationCode(user.getEmail(), user.getUsername(), newActivationCode);

            return ApiResponse.success(null, "New activation code sent.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(utils.encodePassword(request.getPassword()));
        user.setEnabled(false);

        String activationCode = utils.generateActivationCode();
        user.setActivationCode(activationCode);
        user.setActivationCodeExpiryDate(utils.newExpiryDate());

        userRepository.save(user);
        mailService.sendActivationCode(user.getEmail(), user.getUsername(), activationCode);

        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setMessage("Please check your email for the activation code");

        return ApiResponse.success(response, "User registered successfully.");
    }

    public ApiResponse<String> handleAccountActivation(ActivateAccountRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        boolean codeMismatch = !request.getActivationCode().equals(user.getActivationCode());
        boolean codeExpired  = utils.isCodeExpired(user.getActivationCodeExpiryDate());

        if (codeMismatch || codeExpired) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_ACTIVATION_CODE);
        }

        user.setEnabled(true);
        user.setActivationCode(null);
        user.setActivationCodeExpiryDate(null);
        userRepository.save(user);

        return ApiResponse.success(null, "Account activated successfully.");
    }
}
