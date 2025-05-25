package com.capellax.grocery_app_backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;
    private Instant timestamp;
    private List<ErrorDetails> errors;

    public static <T> ApiResponse<T> success(
            T data,
            String message
    ) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(Instant.now());
        return response;
    }

    public static <T> ApiResponse<T> error(
            String message,
            HttpStatus status,
            List<ErrorDetails> errors
    ) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status.value());
        response.setMessage(message);
        response.setTimestamp(Instant.now());
        response.setErrors(errors);
        return response;
    }

    public static <T> ApiResponse<T> error(
            String message,
            HttpStatus status
    ) {
        return error(message, status, null);
    }

}
