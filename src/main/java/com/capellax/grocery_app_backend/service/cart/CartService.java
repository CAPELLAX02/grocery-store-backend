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
import com.capellax.grocery_app_backend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductService productService;
    private final CartServiceUtils cartServiceUtils;

    public ApiResponse<CartResponse> getCart(
            String username
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        CartResponse cartResponse = cartServiceUtils.buildCartResponse(user.getCart());
        return ApiResponse.success(cartResponse, "Cart fetched successfully");
    }

    public ApiResponse<CartResponse> addItemToCart(
            String username,
            AddItemToCartRequest request
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        Product product = Optional.ofNullable(productService.getProductById(request.getProductId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));

        List<CartItem> cart = user.getCart();
        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());

        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setProductName(product.getName());
            newItem.setQuantity(request.getQuantity());
            newItem.setPrice(product.getPrice());
            cart.add(newItem);
        }

        userRepository.save(user);

        CartResponse updatedCartResponse = cartServiceUtils.buildCartResponse(user.getCart());

        return ApiResponse.success(updatedCartResponse, "Item added to cart successfully");
    }

    public ApiResponse<CartResponse> updateCartItemQuantity(
            String username,
            UpdateCartItemRequest request
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        List<CartItem> cart = user.getCart();

        CartItem item = cart.stream()
                .filter(cartItem -> cartItem.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CART_ITEM_NOT_FOUND));

        item.setQuantity(request.getQuantity());

        userRepository.save(user);
        CartResponse updatedCartResponse = cartServiceUtils.buildCartResponse(user.getCart());
        return ApiResponse.success(updatedCartResponse, "Cart item quantity updated successfully");
    }

    public ApiResponse<CartResponse> removeItemFromCart(
            String username,
            String productId
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        boolean removed = user.getCart()
                .removeIf(item -> item.getProductId().equals(productId));

        if (!removed) {
            throw new CustomRuntimeException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        userRepository.save(user);
        CartResponse updatedCartResponse = cartServiceUtils.buildCartResponse(user.getCart());
        return ApiResponse.success(updatedCartResponse, "Product removed from cart successfully.");
    }

    public ApiResponse<CartResponse> clearCart(
            String username
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        user.getCart().clear();
        userRepository.save(user);
        CartResponse clearedCartResponse = cartServiceUtils.buildCartResponse(user.getCart());
        return ApiResponse.success(clearedCartResponse, "Cart cleared successfully");
    }

}
