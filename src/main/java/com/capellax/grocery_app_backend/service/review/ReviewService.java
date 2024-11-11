package com.capellax.grocery_app_backend.service.review;

import com.capellax.grocery_app_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ProductRepository productRepository;
    private final ReviewServiceUtils reviewServiceUtils;

    // TODO

}
