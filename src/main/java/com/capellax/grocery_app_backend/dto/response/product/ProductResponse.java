package com.capellax.grocery_app_backend.dto.response.product;

import com.capellax.grocery_app_backend.model.review.Review;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private String category;
    private String brand;
    private String weight;
    private List<Review> reviews;

}
