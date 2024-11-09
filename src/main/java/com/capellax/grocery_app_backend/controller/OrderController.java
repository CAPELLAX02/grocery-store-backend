package com.capellax.grocery_app_backend.controller;

import com.capellax.grocery_app_backend.security.UserDetailsImpl;
import com.capellax.grocery_app_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // TODO: Implement the main endpoint logic


    private String getAuthenticatedUsername() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userDetails.getUsername();
    }

}
