package com.capellax.grocery_app_backend.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailServiceUtils mailServiceUtils;

    public void sendActivationCode(
            String to,
            String username,
            String activationCode
    ) {
        mailServiceUtils.sendEmailWithTemplate(
                to,
                username,
                activationCode,
                "Account Activation Code",
                "activation-email-template"
        );
    }

    public void sendResetPasswordCode(
            String to,
            String username,
            String resetCode
    ) {
        mailServiceUtils.sendEmailWithTemplate(
                to,
                username,
                resetCode,
                "Password Reset Code",
                "reset-password-email-template"
        );
    }



}
