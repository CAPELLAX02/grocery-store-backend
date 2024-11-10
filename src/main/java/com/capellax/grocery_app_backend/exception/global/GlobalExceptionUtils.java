package com.capellax.grocery_app_backend.exception.global;

import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class GlobalExceptionUtils {

    public HttpStatus getHttpStatus(Exception exception, HttpStatus defaultStatus) {
        if (exception instanceof MethodArgumentNotValidException) return HttpStatus.BAD_REQUEST;
        if (exception instanceof CustomRuntimeException customException) {
            return customException.getErrorCode() != null
                    ? customException.getErrorCode().getStatus()
                    : defaultStatus;
        }
        return defaultStatus;
    }

    public String getSafeMessage(Exception exception, String defaultMessage) {
        return exception.getMessage() == null || exception.getMessage().isEmpty()
                ? defaultMessage
                : exception.getMessage();
    }

}
