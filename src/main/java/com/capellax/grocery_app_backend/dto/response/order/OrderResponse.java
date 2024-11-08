package com.capellax.grocery_app_backend.dto.response.order;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponse {

    private String orderId;
    private LocalDate date;
    private Double totalAmount;
    private List<OrderItemResponse> items;

}
