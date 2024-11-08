package com.capellax.grocery_app_backend.dto.response.user;

import com.capellax.grocery_app_backend.model.CartItem;
import com.capellax.grocery_app_backend.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class GetUserProfileResponse {

    private String username;
    private String email;
    private String password;
    private List<CartItem> cart;
    private List<Order> orders;

}
