package com.capellax.grocery_app_backend.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductListResponse {

    private List<ProductResponse> products;
    private int totalProducts;
    private int totalPages;
    private int currentPage;

}
