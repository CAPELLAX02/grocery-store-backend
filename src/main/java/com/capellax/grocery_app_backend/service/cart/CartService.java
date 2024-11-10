package com.capellax.grocery_app_backend.service.cart;

import com.capellax.grocery_app_backend.dto.request.cart.AddItemToCartRequest;
import com.capellax.grocery_app_backend.dto.request.cart.UpdateCartItemRequest;
import com.capellax.grocery_app_backend.dto.response.cart.CartResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorType;
import com.capellax.grocery_app_backend.model.CartItem;
import com.capellax.grocery_app_backend.model.Product;
import com.capellax.grocery_app_backend.model.User;
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

    // TODO: Return "cart" as data in every cart service method.

    public ApiResponse<CartResponse> getCart(
            String username
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        CartResponse cartResponse = cartServiceUtils.buildCartResponse(user.getCart());
        return ApiResponse.success(cartResponse, "Cart fetched successfully");
    }

    public ApiResponse<String> addItemToCart(
            String username,
            AddItemToCartRequest request
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        Product product = Optional.ofNullable(productService.getProductById(request.getProductId()))
                .orElseThrow(() -> new CustomRuntimeException(ErrorType.PRODUCT_NOT_FOUND));

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

        return ApiResponse.success(null, "Item added to cart successfully");
    }

    public ApiResponse<String> updateCartItemQuantity(
            String username,
            UpdateCartItemRequest request
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        List<CartItem> cart = user.getCart();

        CartItem item = cart.stream()
                .filter(cartItem -> cartItem.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorType.CART_ITEM_NOT_FOUND));

        item.setQuantity(request.getQuantity());

        userRepository.save(user);

        return ApiResponse.success(null, "Cart item quantity updated successfully");
    }

    public ApiResponse<String> removeItemFromCart(
            String username,
            String productId
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        boolean removed = user.getCart()
                .removeIf(item -> item.getProductId().equals(productId));

        if (!removed) {
            throw new CustomRuntimeException(ErrorType.CART_ITEM_NOT_FOUND);
        }

        userRepository.save(user);

        return ApiResponse.success(null, "Product removed from cart successfully.");
    }

    public ApiResponse<String> clearCart(
            String username
    ) {
        User user = cartServiceUtils.getUserByUsername(username);
        user.getCart().clear();
        userRepository.save(user);
        return ApiResponse.success(null, "Cart cleared successfully");
    }

}
