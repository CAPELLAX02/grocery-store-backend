package com.capellax.grocery_app_backend.service.order;

import com.capellax.grocery_app_backend.dto.response.order.OrderListResponse;
import com.capellax.grocery_app_backend.dto.response.order.OrderResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.order.Order;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderServiceUtils orderServiceUtils;

    public ApiResponse<OrderResponse> placeOrder(
            String username,
            String address
    ) {
        User user = orderServiceUtils.getUserByUsername(username);

        if (user.getCart() == null || user.getCart().isEmpty()) {
            throw new CustomRuntimeException(ErrorCode.CART_IS_EMPTY);
        }

        Order order = new Order();
        order.setOrderId(orderServiceUtils.generateOrderId());
        order.setAddress(address);
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
            throw new CustomRuntimeException(ErrorCode.ORDERS_NOT_FOUND);
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
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_NOT_FOUND));

        OrderResponse response = orderServiceUtils.buildOrderResponse(order);
        return ApiResponse.success(response, "Order fetched successfully.");
    }

}
