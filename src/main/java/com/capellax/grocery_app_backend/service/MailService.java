package com.capellax.grocery_app_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendActivationCode(String to, String username, String activationCode) {
        sendEmailWithTemplate(
                to,
                username,
                activationCode,
                "Account Activation Code",
                "activation-email-template"
        );
    }

    public void sendResetPasswordCode(String to, String username, String resetCode) {
        sendEmailWithTemplate(
                to,
                username,
                resetCode,
                "Password Reset Code",
                "reset-password-email-template"
        );
    }

    private void sendEmailWithTemplate(
            String to,
            String username,
            String code,
            String subject,
            String templateName
    ) {
        String content = buildEmailContent(
                templateName,
                username,
                code
        );
        sendEmail(
                to,
                subject,
                content
        );
    }

    private String buildEmailContent(
            String templateName,
            String username,
            String code
    ) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("sixDigitCode", code);
        return templateEngine.process(templateName, context);
    }

    private void sendEmail(
            String to,
            String subject,
            String content
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new RuntimeException("Failed to send email", exception);
        }
    }

}
