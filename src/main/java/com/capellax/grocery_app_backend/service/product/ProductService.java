package com.capellax.grocery_app_backend.service.product;

import com.capellax.grocery_app_backend.dto.response.product.ProductListResponse;
import com.capellax.grocery_app_backend.dto.response.product.ProductResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.product.Product;
import com.capellax.grocery_app_backend.repository.ProductRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductServiceUtils productServiceUtils;

    public ApiResponse<ProductListResponse> getAllProducts(int offset, int limit) {
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(productServiceUtils::buildProductResponse)
                .toList();

        ProductListResponse response = new ProductListResponse(
                productResponses,
                (int) productPage.getTotalElements(),
                productPage.getTotalPages(),
                pageNumber
        );

        return ApiResponse.success(response, "Products retrieved successfully");
    }

    public ApiResponse<ProductResponse> getProductById(
            String productId
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductResponse response = productServiceUtils.buildProductResponse(product);

        return ApiResponse.success(response, "Product retrieved successfully");
    }

}
