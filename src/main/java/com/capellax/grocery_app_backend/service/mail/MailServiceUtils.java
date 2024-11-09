package com.capellax.grocery_app_backend.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class MailServiceUtils {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    protected void sendEmailWithTemplate(
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

    protected String buildEmailContent(
            String templateName,
            String username,
            String code
    ) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("sixDigitCode", code);
        return templateEngine.process(templateName, context);
    }

    protected void sendEmail(
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
