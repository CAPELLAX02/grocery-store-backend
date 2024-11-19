package com.capellax.grocery_app_backend.service.cart;

import com.capellax.grocery_app_backend.dto.request.cart.AddItemToCartRequest;
import com.capellax.grocery_app_backend.dto.request.cart.UpdateCartItemRequest;
import com.capellax.grocery_app_backend.dto.response.cart.CartResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.cart.CartItem;
import com.capellax.grocery_app_backend.model.product.Product;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.service.cart.helper.CartHelper;
import com.capellax.grocery_app_backend.service.cart.helper.ProductHelper;
import com.capellax.grocery_app_backend.service.cart.helper.UserHelper;
import com.capellax.grocery_app_backend.service.cart.utils.CartUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartHelper cartHelper;
    private final ProductHelper productHelper;
    private final UserHelper userHelper;

    public ApiResponse<CartResponse> getCart(
            String username
    ) {
        User user = userHelper.getUserByUsername(username);
        CartResponse response = cartHelper.buildCartResponse(user.getCart());
        return ApiResponse.success(response, "Cart retrieved successfully");
    }

    public ApiResponse<CartResponse> addItemToCart(
            String username,
            AddItemToCartRequest request
    ) {
        User user = userHelper.getUserByUsername(username);
        Product product = productHelper.getProductById(request.getProductId());
        cartHelper.addItemToCart(user, product, request.getQuantity());
        CartResponse response = cartHelper.buildCartResponse(user.getCart());
        return ApiResponse.success(response, "Item added to cart successfully");
    }

    public ApiResponse<CartResponse> updateCartItemQuantity(
            String username,
            String productId,
            UpdateCartItemRequest request
    ) {
        User user = userHelper.getUserByUsername(username);
        cartHelper.updateCartItemQuantity(user, productId, request.getQuantity());
        CartResponse response = cartHelper.buildCartResponse(user.getCart());
        return ApiResponse.success(response, "Cart item quantity updated successfully");
    }

    public ApiResponse<CartResponse> removeItemFromCart(
            String username,
            String productId
    ) {
        User user = userHelper.getUserByUsername(username);
        cartHelper.removeItemFromCart(user, productId);
        CartResponse response = cartHelper.buildCartResponse(user.getCart());
        return ApiResponse.success(response, "Product removed from cart successfully.");
    }

    public ApiResponse<CartResponse> clearCart(
            String username
    ) {
        User user = userHelper.getUserByUsername(username);
        cartHelper.clearCart(user);
        CartResponse response = cartHelper.buildCartResponse(user.getCart());
        return ApiResponse.success(response, "Cart cleared successfully");
    }

}
