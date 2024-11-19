package com.capellax.grocery_app_backend.controller.product;

import com.capellax.grocery_app_backend.dto.response.product.ProductListResponse;
import com.capellax.grocery_app_backend.dto.response.product.ProductResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("${api.base-uri}/products")
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProductListResponse>> getAllProducts(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        ApiResponse<ProductListResponse> response = productService.getAllProducts(offset, limit);
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
