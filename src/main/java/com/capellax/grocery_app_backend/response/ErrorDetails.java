package com.capellax.grocery_app_backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    private String field;
    private String errorMessage;
    private Object rejectedValue;

}
