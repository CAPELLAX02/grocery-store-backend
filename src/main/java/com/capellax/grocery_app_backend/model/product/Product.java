package com.capellax.grocery_app_backend.model.product;

import com.capellax.grocery_app_backend.model.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private String category;
    private String brand;
    private String weight;
    private List<Review> reviews;
    private Double averageRating;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

    public void calculateAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            this.averageRating = 0.0;
        }
        else {
            this.averageRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
        }
    }

}
