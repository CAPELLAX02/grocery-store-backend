package com.capellax.grocery_app_backend.service.product;

import com.capellax.grocery_app_backend.dto.response.product.ProductResponse;
import com.capellax.grocery_app_backend.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductServiceUtils {

    protected ProductResponse buildProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setCategory(product.getCategory());
        response.setBrand(product.getBrand());
        response.setWeight(product.getWeight());
        response.setReviews(product.getReviews());
        return response;
    }

}
