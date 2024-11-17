package com.capellax.grocery_app_backend.service.auth;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Component
public class AuthenticationServiceUtils {

    private static final SecureRandom secureRandom = new SecureRandom();

    protected LocalDateTime activationCodeExpiryDate = LocalDateTime.now().plusMinutes(15);
    protected LocalDateTime resetPasswordCodeExpiryDate = LocalDateTime.now().plusMinutes(15);

    private String generateRandomCode() {
        int code = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public String generateActivationCode() {
        return generateRandomCode();
    }

    public String generateResetPasswordCode() {
        return generateRandomCode();
    }

}
