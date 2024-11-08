package com.capellax.grocery_app_backend.dto.response.order;

import lombok.Data;

@Data
public class OrderItemResponse {

    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;

}
