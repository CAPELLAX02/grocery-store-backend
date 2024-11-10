package com.capellax.grocery_app_backend.exception;

import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.response.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException exception
    ) {
        List<ErrorDetails> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorDetails(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .toList();

        ApiResponse<Object> response = ApiResponse.error(
                "Validation failed!",
                HttpStatus.BAD_REQUEST,
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
            Exception exception
    ) {
        ApiResponse<Object> response = ApiResponse.error(
                "An unexpected error occured",
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomRuntimeException(
            CustomRuntimeException exception
    ) {
        ApiResponse<Object> response = ApiResponse.error(
                exception.getMessage(),
                exception.getErrorType().getStatus()
        );
        return new ResponseEntity<>(response, exception.getErrorType().getStatus());
    }

}
