package com.capellax.grocery_app_backend.dto.request.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
