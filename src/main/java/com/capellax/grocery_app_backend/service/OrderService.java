package com.capellax.grocery_app_backend.service;

import com.capellax.grocery_app_backend.dto.response.order.OrderItemResponse;
import com.capellax.grocery_app_backend.dto.response.order.OrderListResponse;
import com.capellax.grocery_app_backend.dto.response.order.OrderResponse;
import com.capellax.grocery_app_backend.model.CartItem;
import com.capellax.grocery_app_backend.model.Order;
import com.capellax.grocery_app_backend.model.OrderItem;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;

    public ApiResponse<OrderResponse> placeOrder(String username) {
        User user = getUserByUsername(username);

        if (user.getCart() == null || user.getCart().isEmpty()) {
            return ApiResponse.error("Cart is empty. Cannot create an order.", HttpStatus.BAD_REQUEST);
        }

        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setDate(LocalDate.now());
        order.setTotal(calculateTotalAmount(user.getCart()));
        order.setOrderItems(user.getCart().stream()
                .map(this::convertToOrderItem)
                .toList()
        );

        user.getOrders().add(order);
        user.getCart().clear();
        userRepository.save(user);

        OrderResponse response = buildOrderResponse(order);
        return ApiResponse.success(response, "Order created successfully.");
    }

    public ApiResponse<OrderListResponse> getMyOrders(String username) {
        User user = getUserByUsername(username);

        if (user.getOrders() == null || user.getOrders().isEmpty()) {
            return ApiResponse.error("No orders found for this user.", HttpStatus.NOT_FOUND);
        }

        List<OrderResponse> orders = user.getOrders()
                .stream()
                .map(this::buildOrderResponse)
                .toList();

        OrderListResponse response = new OrderListResponse();
        response.setOrders(orders);
        return ApiResponse.success(response, "Orders fetched successfully.");
    }


    public ApiResponse<OrderResponse> getMyOrderById(String username, String orderId) {
        User user = getUserByUsername(username);

        Order order = user.getOrders()
                .stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found."));

        OrderResponse response = buildOrderResponse(order);
        return ApiResponse.success(response, "Order fetched successfully.");
    }

    private OrderItem convertToOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(cartItem.getProductId());
        orderItem.setProductName(cartItem.getProductName());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getPrice());
        return orderItem;
    }

    private Double calculateTotalAmount(List<CartItem> cartItems) {
        BigDecimal total = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
    }

    private OrderResponse buildOrderResponse(Order order) {
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

    private OrderItemResponse buildOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(orderItem.getProductId());
        response.setProductName(orderItem.getProductName());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        return response;
    }

}
