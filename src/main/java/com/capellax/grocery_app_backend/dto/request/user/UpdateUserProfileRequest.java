package com.capellax.grocery_app_backend.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    // TODO: implement separate email reset logic later on
//    @NotBlank(message = "Email cannot be blank")
//    @Email(message = "Email should be valid")
//    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

}
