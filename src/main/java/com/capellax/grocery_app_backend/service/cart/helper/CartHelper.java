package com.capellax.grocery_app_backend.service.cart.helper;

import com.capellax.grocery_app_backend.dto.response.cart.CartResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.cart.CartItem;
import com.capellax.grocery_app_backend.model.product.Product;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.service.cart.utils.CartUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartHelper {

    private final UserRepository userRepository;
    private final CartUtils utils;

    public void addItemToCart(User user, Product product, int quantity) {
        List<CartItem> cart = user.getCart();
        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cart.add(newItem);
        }

        userRepository.save(user);
    }

    public void updateCartItemQuantity(User user, String productId, int quantity) {
        CartItem item = user.getCart().stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CART_ITEM_NOT_FOUND));
        item.setQuantity(quantity);
        userRepository.save(user);
    }

    public void removeItemFromCart(User user, String productId) {
        boolean removed = user.getCart().removeIf(item -> item.getProductId().equals(productId));
        if (!removed) {
            throw new CustomRuntimeException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        userRepository.save(user);
    }

    public void clearCart(User user) {
        user.getCart().clear();
        userRepository.save(user);
    }

    public CartResponse buildCartResponse(List<CartItem> cartItems) {
        BigDecimal total = utils.calculateCartTotal(cartItems);
        CartResponse response = new CartResponse();
        response.setCartItems(cartItems);
        response.setTotalPrice(total.setScale(2, RoundingMode.HALF_UP).doubleValue());
        return response;
    }

}
