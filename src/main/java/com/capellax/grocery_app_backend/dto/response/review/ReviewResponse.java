package com.capellax.grocery_app_backend.dto.response.review;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReviewResponse {

    private String username;
    private Integer rating;
    private String comment;
    private LocalDate date;

}
