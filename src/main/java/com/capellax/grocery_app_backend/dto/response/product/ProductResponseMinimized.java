package com.capellax.grocery_app_backend.dto.response.product;

import lombok.Data;

@Data
public class ProductResponseMinimized {

    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

}
