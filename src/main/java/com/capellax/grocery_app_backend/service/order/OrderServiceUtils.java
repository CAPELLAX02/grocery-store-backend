package com.capellax.grocery_app_backend.service.order;

import com.capellax.grocery_app_backend.dto.response.order.OrderItemResponse;
import com.capellax.grocery_app_backend.dto.response.order.OrderResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorType;
import com.capellax.grocery_app_backend.model.CartItem;
import com.capellax.grocery_app_backend.model.Order;
import com.capellax.grocery_app_backend.model.OrderItem;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderServiceUtils {

    private final UserRepository userRepository;

    protected OrderItem convertToOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(cartItem.getProductId());
        orderItem.setProductName(cartItem.getProductName());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getPrice());
        return orderItem;
    }

    protected Double calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    protected User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException(ErrorType.USER_NOT_FOUND));
    }

    protected String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
    }

    protected OrderResponse buildOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setDate(order.getDate());
        response.setTotalAmount(order.getTotal());
        response.setItems(order.getOrderItems().stream()
                .map(this::buildOrderItemResponse)
                .toList()
        );
        return response;
    }

    protected OrderItemResponse buildOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(orderItem.getProductId());
        response.setProductName(orderItem.getProductName());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        return response;
    }

}
