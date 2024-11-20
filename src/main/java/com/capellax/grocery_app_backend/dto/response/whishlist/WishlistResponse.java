package com.capellax.grocery_app_backend.dto.response.whishlist;

import com.capellax.grocery_app_backend.dto.response.product.ProductResponseMinimized;
import lombok.Data;

import java.util.List;

@Data
public class WishlistResponse {

    private List<ProductResponseMinimized> wishlist;

}
