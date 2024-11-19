package com.capellax.grocery_app_backend.repository;

import com.capellax.grocery_app_backend.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
        extends MongoRepository<Product, String> {

    Page<Product> findAll(Pageable pageable);

}
