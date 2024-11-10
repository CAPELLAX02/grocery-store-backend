package com.capellax.grocery_app_backend.exception.custom;

import org.springframework.mail.MailException;

public class CustomMailException extends MailException {
    public CustomMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
