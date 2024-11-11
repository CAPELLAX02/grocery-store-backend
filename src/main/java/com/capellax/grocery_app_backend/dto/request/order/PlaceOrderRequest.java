package com.capellax.grocery_app_backend.dto.request.order;

import com.capellax.grocery_app_backend.model.cart.CartItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {

    @NotNull (message = "Cart items cannot be null")
    private List<CartItem> cartItems;

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

}
