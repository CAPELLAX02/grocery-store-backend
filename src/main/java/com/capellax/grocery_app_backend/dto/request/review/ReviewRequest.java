package com.capellax.grocery_app_backend.dto.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewRequest {

    private String username;

    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank(message = "Review comment cannot be empty")
    private String comment;

}
