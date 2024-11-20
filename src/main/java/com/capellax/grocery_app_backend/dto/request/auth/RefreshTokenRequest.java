package com.capellax.grocery_app_backend.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

}
