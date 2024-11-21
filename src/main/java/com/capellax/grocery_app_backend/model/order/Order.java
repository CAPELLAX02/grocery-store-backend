package com.capellax.grocery_app_backend.model.order;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Order {

    private String orderId;
    private String address;
    private LocalDate date;
    private Double total;
    private List<OrderItem> orderItems;

}
