package com.capellax.grocery_app_backend.controller.wishlist;

import com.capellax.grocery_app_backend.dto.request.whishlist.AddToWishlistRequest;
import com.capellax.grocery_app_backend.dto.response.whishlist.WishlistResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.security.user.UserDetailsImpl;
import com.capellax.grocery_app_backend.service.whishlist.WishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishListService wishListService;

    @GetMapping
    public ResponseEntity<ApiResponse<WishlistResponse>> getMyWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<WishlistResponse> response = wishListService.getMyWishList(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishlistResponse>> addToMyWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddToWishlistRequest request
    ) {
        String username = userDetails.getUsername();
        ApiResponse<WishlistResponse> response = wishListService.addToMyWishList(username, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<WishlistResponse>> removeFromMyWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String productId
    ) {
        String username = userDetails.getUsername();
        ApiResponse<WishlistResponse> response = wishListService.removeFromMyWishList(username, productId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<WishlistResponse>> clearMyWishlist(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<WishlistResponse> response = wishListService.clearMyWishlist(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
