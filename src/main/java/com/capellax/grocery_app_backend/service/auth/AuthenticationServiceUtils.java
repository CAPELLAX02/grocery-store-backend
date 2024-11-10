package com.capellax.grocery_app_backend.service.auth;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AuthenticationServiceUtils {

    protected String generateActivationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(100000);
        return String.valueOf(code);
    }

    protected String generateResetPasswordCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(100000);
        return String.valueOf(code);
    }

}
