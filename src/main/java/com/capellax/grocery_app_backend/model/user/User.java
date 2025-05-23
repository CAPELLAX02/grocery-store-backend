package com.capellax.grocery_app_backend.model.user;

import com.capellax.grocery_app_backend.model.cart.CartItem;
import com.capellax.grocery_app_backend.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private String activationCode;
    private String resetPasswordCode;
    private LocalDateTime activationCodeExpiryDate;
    private LocalDateTime resetPasswordCodeExpiryDate;
    private boolean enabled;
    private List<CartItem> cart = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<String> wishlist = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedAt;

}
