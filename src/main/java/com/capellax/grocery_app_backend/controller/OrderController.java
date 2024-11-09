package com.capellax.grocery_app_backend.controller;

import com.capellax.grocery_app_backend.dto.response.order.OrderListResponse;
import com.capellax.grocery_app_backend.dto.response.order.OrderResponse;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.security.UserDetailsImpl;
import com.capellax.grocery_app_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ApiResponse<OrderResponse> response = orderService.placeOrder(userDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<OrderListResponse>> getMyOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ApiResponse<OrderListResponse> response = orderService.getMyOrders(userDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getMyOrderById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String orderId
    ) {
        ApiResponse<OrderResponse> response = orderService.getMyOrderById(userDetails.getUsername(), orderId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
