package com.capellax.grocery_app_backend.exception.custom;

import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
