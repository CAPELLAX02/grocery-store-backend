package com.capellax.grocery_app_backend.service.review;

import com.capellax.grocery_app_backend.dto.response.review.ReviewResponse;
import com.capellax.grocery_app_backend.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewServiceUtils {
    protected ReviewResponse buildReviewResponse(Review review) {
        return new ReviewResponse(
                review.getUsername(),
                review.getRating(),
                review.getComment(),
                review.getDate()
        );
    }
}
