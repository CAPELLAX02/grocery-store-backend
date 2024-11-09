package com.capellax.grocery_app_backend.service.order;

import com.capellax.grocery_app_backend.dto.response.order.OrderListResponse;
import com.capellax.grocery_app_backend.dto.response.order.OrderResponse;
import com.capellax.grocery_app_backend.model.Order;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderServiceUtils orderServiceUtils;

    public ApiResponse<OrderResponse> placeOrder(
            String username
    ) {
        User user = orderServiceUtils.getUserByUsername(username);

        if (user.getCart() == null || user.getCart().isEmpty()) {
            return ApiResponse.error("Cart is empty. Cannot create an order.", HttpStatus.BAD_REQUEST);
        }

        Order order = new Order();
        order.setOrderId(orderServiceUtils.generateOrderId());
        order.setDate(LocalDate.now());
        order.setTotal(orderServiceUtils.calculateTotalAmount(user.getCart()));
        order.setOrderItems(user.getCart().stream()
                .map(this.orderServiceUtils::convertToOrderItem)
                .toList()
        );

        user.getOrders().add(order);
        user.getCart().clear();
        userRepository.save(user);

        OrderResponse response = orderServiceUtils.buildOrderResponse(order);
        return ApiResponse.success(response, "Order created successfully.");
    }

    public ApiResponse<OrderListResponse> getMyOrders(
            String username
    ) {
        User user = orderServiceUtils.getUserByUsername(username);

        if (user.getOrders() == null || user.getOrders().isEmpty()) {
            return ApiResponse.error("No orders found for this user.", HttpStatus.NOT_FOUND);
        }

        List<OrderResponse> orders = user.getOrders()
                .stream()
                .map(this.orderServiceUtils::buildOrderResponse)
                .toList();

        OrderListResponse response = new OrderListResponse();
        response.setOrders(orders);
        return ApiResponse.success(response, "Orders fetched successfully.");
    }


    public ApiResponse<OrderResponse> getMyOrderById(
            String username,
            String orderId
    ) {
        User user = orderServiceUtils.getUserByUsername(username);

        Order order = user.getOrders()
                .stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found."));

        OrderResponse response = orderServiceUtils.buildOrderResponse(order);
        return ApiResponse.success(response, "Order fetched successfully.");
    }

}
