package com.capellax.grocery_app_backend.model.review;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Review {

    private String username;
    private Integer rating;
    private String comment;
    private LocalDate date;

}
