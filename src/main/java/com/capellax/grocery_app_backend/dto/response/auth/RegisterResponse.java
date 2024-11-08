package com.capellax.grocery_app_backend.dto.response.auth;

import lombok.Data;

@Data
public class RegisterResponse {

    private String userId;
    private String email;
    private String message;

}
