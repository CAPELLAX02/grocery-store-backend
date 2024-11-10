package com.capellax.grocery_app_backend.exception.custom;

import com.capellax.grocery_app_backend.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final ErrorType errorType;

    public CustomRuntimeException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

}
