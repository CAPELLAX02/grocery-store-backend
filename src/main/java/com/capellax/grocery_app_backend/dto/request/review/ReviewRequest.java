package com.capellax.grocery_app_backend.dto.request.review;

import lombok.Data;

@Data
public class ReviewRequest {

    private String username;
    private Integer rating;
    private String comment;

}
