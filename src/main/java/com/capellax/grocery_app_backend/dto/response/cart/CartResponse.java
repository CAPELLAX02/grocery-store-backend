package com.capellax.grocery_app_backend.dto.response.cart;

import com.capellax.grocery_app_backend.model.CartItem;
import lombok.Data;

import java.util.List;

@Data
public class CartResponse {

    private List<CartItem> cartItems;
    private Double totalPrice;

}
