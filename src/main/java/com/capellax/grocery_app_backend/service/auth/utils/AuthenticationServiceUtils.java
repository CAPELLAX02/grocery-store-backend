package com.capellax.grocery_app_backend.service.auth.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class AuthenticationServiceUtils {

    private static final SecureRandom secureRandom = new SecureRandom();

    public LocalDateTime newExpiryDate() {
        return LocalDateTime.now().plusMinutes(15);
    }

    public String generateActivationCode() {
        return generateRandomCode();
    }

    public String generateResetPasswordCode() {
        return generateRandomCode();
    }

    public boolean isCodeStillValid(LocalDateTime expiryDate) {
        return expiryDate != null && expiryDate.isAfter(LocalDateTime.now());
    }

    public boolean isCodeExpired(LocalDateTime expiryDate) {
        return expiryDate == null || expiryDate.isBefore(LocalDateTime.now());
    }

    public String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    private String generateRandomCode() {
        int code = secureRandom.nextInt(900_000) + 100_000;
        return String.valueOf(code);
    }
}
