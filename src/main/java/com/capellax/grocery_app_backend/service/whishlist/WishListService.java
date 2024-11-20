package com.capellax.grocery_app_backend.service.whishlist;

import com.capellax.grocery_app_backend.dto.request.whishlist.AddToWishlistRequest;
import com.capellax.grocery_app_backend.dto.response.product.ProductResponseMinimized;
import com.capellax.grocery_app_backend.dto.response.whishlist.WishlistResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.product.Product;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.ProductRepository;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistServiceUtils utils;

    public ApiResponse<WishlistResponse> getMyWishList(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        List<ProductResponseMinimized> wishlistItems = user.getWishlist().stream()
                .map(productId -> productRepository.findById(productId)
                        .map(utils::mapToProductResponseMinimized)
                        .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND))
                )
                .toList();

        WishlistResponse response = new WishlistResponse();
        response.setWishlist(wishlistItems);

        return ApiResponse.success(response, "Wishlist retrieved successfully.");
    }

    public ApiResponse<WishlistResponse> addToMyWishList(String username, AddToWishlistRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        if (user.getWishlist().contains(request.getProductId())) {
            throw new CustomRuntimeException(ErrorCode.PRODUCT_ALREADY_IN_WISHLIST);
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));

        user.getWishlist().add(product.getId());
        userRepository.save(user);

        return ApiResponse.success(getMyWishList(username).getData(), "Product added to wishlist successfully.");
    }

    public ApiResponse<WishlistResponse> removeFromMyWishList(String username, String productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        if (!user.getWishlist().remove(productId)) {
            throw new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND_IN_WISHLIST);
        }

        userRepository.save(user);

        return ApiResponse.success(getMyWishList(username).getData(), "Product removed from wishlist successfully.");
    }

    public ApiResponse<WishlistResponse> clearMyWishlist(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

        user.getWishlist().clear();
        userRepository.save(user);

        WishlistResponse response = new WishlistResponse();
        response.setWishlist(List.of());

        return ApiResponse.success(response, "Wishlist cleared successfully.");
    }
}
