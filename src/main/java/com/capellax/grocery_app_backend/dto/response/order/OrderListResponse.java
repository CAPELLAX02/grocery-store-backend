package com.capellax.grocery_app_backend.dto.response.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderListResponse {

    private List<OrderResponse> orders;

}
