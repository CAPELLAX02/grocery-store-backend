package com.capellax.grocery_app_backend.service;

import com.capellax.grocery_app_backend.dto.request.cart.AddItemToCartRequest;
import com.capellax.grocery_app_backend.dto.request.cart.UpdateCartItemRequest;
import com.capellax.grocery_app_backend.dto.response.cart.CartResponse;
import com.capellax.grocery_app_backend.model.CartItem;
import com.capellax.grocery_app_backend.model.Product;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductService productService;

    public ApiResponse<CartResponse> getCart(
            String username
    ) {
        User user = getUserByUsername(username);
        CartResponse cartResponse = buildCartResponse(user.getCart());
        return ApiResponse.success(cartResponse, "Cart fetched successfully");
    }

    public ApiResponse<String> addItemToCart(
            String username,
            AddItemToCartRequest request
    ) {
        User user = getUserByUsername(username);
        Product product = productService.getProductById(request.getProductId());

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

        return ApiResponse.success(null, "Cart added successfully");
    }

    public ApiResponse<String> updateCartItemQuantity(
            String username,
            UpdateCartItemRequest request
    ) {
        User user = getUserByUsername(username);
        List<CartItem> cart = user.getCart();

        CartItem item = cart.stream()
                .filter(cartItem -> cartItem.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        item.setQuantity(request.getQuantity());

        userRepository.save(user);

        return ApiResponse.success(null, "Cart updated successfully");
    }

    public ApiResponse<String> removeItemFromCart(
            String username,
            String productId
    ) {
        User user = getUserByUsername(username);
        boolean removed = user.getCart()
                .removeIf(item -> item.getProductId().equals(productId));

        if (!removed) {
            return ApiResponse.error("Product not found in cart", HttpStatus.NOT_FOUND);
        }

        userRepository.save(user);

        return ApiResponse.success(null, "Product removed from cart successfully.");
    }

    public ApiResponse<String> clearCart(
            String username
    ) {
        User user = getUserByUsername(username);
        user.getCart().clear();
        userRepository.save(user);
        return ApiResponse.success(null, "Cart cleared successfully");
    }




    private User getUserByUsername(
            String username
    ) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private CartResponse buildCartResponse(
            List<CartItem> cartItems
    ) {
        Double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        CartResponse response = new CartResponse();
        response.setCartItems(cartItems);
        response.setTotalPrice(totalPrice);

        return response;
    }

}
