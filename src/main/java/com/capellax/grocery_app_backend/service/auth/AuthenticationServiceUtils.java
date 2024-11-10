package com.capellax.grocery_app_backend.service.auth;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AuthenticationServiceUtils {

    private static final SecureRandom secureRandom = new SecureRandom();

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
