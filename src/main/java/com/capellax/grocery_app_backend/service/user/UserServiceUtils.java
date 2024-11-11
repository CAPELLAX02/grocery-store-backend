package com.capellax.grocery_app_backend.service.user;

import com.capellax.grocery_app_backend.dto.response.user.GetUserProfileResponse;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceUtils {

    private final UserRepository userRepository;

    protected User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    }

    protected GetUserProfileResponse buildUserProfileResponse(User user) {
        GetUserProfileResponse response = new GetUserProfileResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());
        response.setCart(user.getCart());
        response.setOrders(user.getOrders());
        return response;
    }

}
