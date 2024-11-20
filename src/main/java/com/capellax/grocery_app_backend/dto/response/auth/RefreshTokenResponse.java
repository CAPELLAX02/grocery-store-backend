package com.capellax.grocery_app_backend.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponse {

    private String accessToken;
    private String refreshToken;

}
