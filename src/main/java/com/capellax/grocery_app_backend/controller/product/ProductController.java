package com.capellax.grocery_app_backend.controller.product;

import com.capellax.grocery_app_backend.dto.response.product.ProductListResponse;
import com.capellax.grocery_app_backend.dto.response.product.ProductResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("${api.base-uri}/products")
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponse>> getAllProducts() {
        ApiResponse<ProductListResponse> response = productService.getAllProducts();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable String productId
    ) {
        ApiResponse<ProductResponse> response = productService.getProductById(productId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
