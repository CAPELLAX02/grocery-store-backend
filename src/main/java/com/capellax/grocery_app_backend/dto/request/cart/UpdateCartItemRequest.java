package com.capellax.grocery_app_backend.dto.request.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
