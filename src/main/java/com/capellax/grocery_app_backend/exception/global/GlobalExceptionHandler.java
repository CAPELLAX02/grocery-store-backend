package com.capellax.grocery_app_backend.exception.global;

import com.capellax.grocery_app_backend.exception.custom.CustomMailException;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.response.ErrorDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final GlobalExceptionUtils utils;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException e
    ) {
        List<ErrorDetails> errors = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorDetails(
                        err.getField(),
                        err.getDefaultMessage(),
                        err.getRejectedValue()
                ))
                .toList();
        String message = utils.getSafeMessage(e, "Validation failed");
        HttpStatus status = utils.getHttpStatus(e, BAD_REQUEST);
        ApiResponse<Object> response = ApiResponse.error(message, status, errors);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception e
    ) {
        String message = utils.getSafeMessage(e, "An unexpected error occurred.");
        HttpStatus status = utils.getHttpStatus(e, INTERNAL_SERVER_ERROR);
        ApiResponse<Object> response = ApiResponse.error(message, status);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomRuntimeException(
            CustomRuntimeException e
    ) {
        String message = utils.getSafeMessage(e, "An error occurred while processing the request.");
        HttpStatus status = utils.getHttpStatus(e, INTERNAL_SERVER_ERROR);
        ApiResponse<Object> response = ApiResponse.error(message, status);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(CustomMailException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomMailException(
            CustomMailException e
    ) {
        String message = utils.getSafeMessage(e, "Mail sending failed. Try again later.");
        HttpStatus status = utils.getHttpStatus(e, SERVICE_UNAVAILABLE);
        ApiResponse<Object> response = ApiResponse.error(message, status);
        return new ResponseEntity<>(response, status);
    }

}
