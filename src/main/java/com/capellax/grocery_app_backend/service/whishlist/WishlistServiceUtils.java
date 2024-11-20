package com.capellax.grocery_app_backend.service.whishlist;

import com.capellax.grocery_app_backend.dto.response.product.ProductResponseMinimized;
import com.capellax.grocery_app_backend.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistServiceUtils {
    protected ProductResponseMinimized mapToProductResponseMinimized(Product product) {
        ProductResponseMinimized response = new ProductResponseMinimized();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
}
