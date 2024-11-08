package com.capellax.grocery_app_backend.dto.response.auth;

import lombok.Data;

@Data
public class LoginResponse {

    private String username;
    private String token;

}
