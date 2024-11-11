package com.capellax.grocery_app_backend.service.product;

import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.product.Product;
import com.capellax.grocery_app_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return Optional.of(productRepository.findAll())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public Product getProductById(
            String id
    ) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCTS_NOT_FOUND));
    }

}
