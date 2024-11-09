package com.capellax.grocery_app_backend.service.product;

import com.capellax.grocery_app_backend.model.Product;
import com.capellax.grocery_app_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(
            String id
    ) {
        return productRepository.findById(id).orElse(null);
    }

}
