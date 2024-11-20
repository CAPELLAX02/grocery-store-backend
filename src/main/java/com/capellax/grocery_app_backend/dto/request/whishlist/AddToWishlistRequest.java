package com.capellax.grocery_app_backend.dto.request.whishlist;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddToWishlistRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

}
