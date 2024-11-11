package com.capellax.grocery_app_backend.model.cart;

import lombok.Data;

@Data
public class CartItem {

    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;

}
