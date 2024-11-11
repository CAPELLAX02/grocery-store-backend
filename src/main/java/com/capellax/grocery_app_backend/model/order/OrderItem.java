package com.capellax.grocery_app_backend.model.order;

import lombok.Data;

@Data
public class OrderItem {

    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;

}
