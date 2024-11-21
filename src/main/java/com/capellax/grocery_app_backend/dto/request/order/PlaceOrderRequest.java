package com.capellax.grocery_app_backend.dto.request.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotNull(message = "Order address is required")
    private String address;

}
