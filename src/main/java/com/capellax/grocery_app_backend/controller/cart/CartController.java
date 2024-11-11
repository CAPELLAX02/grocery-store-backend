package com.capellax.grocery_app_backend.controller.cart;

import com.capellax.grocery_app_backend.dto.request.cart.AddItemToCartRequest;
import com.capellax.grocery_app_backend.dto.request.cart.UpdateCartItemRequest;
import com.capellax.grocery_app_backend.dto.response.cart.CartResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.security.user.UserDetailsImpl;
import com.capellax.grocery_app_backend.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-uri}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<CartResponse> response = cartService.getCart(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(
            @Valid @RequestBody AddItemToCartRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<CartResponse> response = cartService.addItemToCart(username, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItemQuantity(
            @PathVariable String productId,
            @Valid @RequestBody UpdateCartItemRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<CartResponse> response = cartService.updateCartItemQuantity(username, productId, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeItemFromCart(
            @PathVariable String productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<CartResponse> response = cartService.removeItemFromCart(username, productId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<CartResponse>> clearCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        ApiResponse<CartResponse> response = cartService.clearCart(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
