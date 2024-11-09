package com.capellax.grocery_app_backend.service.cart;

import com.capellax.grocery_app_backend.dto.response.cart.CartResponse;
import com.capellax.grocery_app_backend.model.CartItem;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartServiceUtils {

    private final UserRepository userRepository;

    protected User getUserByUsername(
            String username
    ) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    protected CartResponse buildCartResponse(
            List<CartItem> cartItems
    ) {
        BigDecimal total = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Double totalPrice = total.setScale(2, RoundingMode.HALF_UP).doubleValue();

        CartResponse response = new CartResponse();
        response.setCartItems(cartItems);
        response.setTotalPrice(totalPrice);

        return response;
    }

}
