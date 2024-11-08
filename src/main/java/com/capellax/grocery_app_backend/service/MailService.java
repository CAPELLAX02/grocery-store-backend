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

    public void sendActivationCode(
            String to,
            String activationCode
    ) {
        String subject = "Account Activation Code";
        String content = buildEmailContent("activation-email-template", activationCode);
        sendEmail(to, subject, content);
    }

    private String buildEmailContent(
            String templateName,
            String activationCode
    ) {
        Context context = new Context();
        context.setVariable("activationCode", activationCode);
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
