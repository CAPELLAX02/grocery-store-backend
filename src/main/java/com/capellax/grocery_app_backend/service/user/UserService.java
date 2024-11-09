package com.capellax.grocery_app_backend.service.user;

import com.capellax.grocery_app_backend.dto.request.user.UpdateUserProfileRequest;
import com.capellax.grocery_app_backend.dto.response.user.GetUserProfileResponse;
import com.capellax.grocery_app_backend.dto.response.user.UpdateUserProfileResponse;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import com.capellax.grocery_app_backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<GetUserProfileResponse> getUserProfile() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        String username = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ApiResponse.error("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        GetUserProfileResponse response = new GetUserProfileResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPassword(user.getPassword());
        response.setCart(user.getCart());
        response.setOrders(user.getOrders());

        return ApiResponse.success(response, "User profile fetched successfully.");
    }

    public ApiResponse<UpdateUserProfileResponse> updateUserProfile(
            UpdateUserProfileRequest request
    ) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        String username = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ApiResponse.error("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
        // TODO: user email update will be distinct logic.
        //  A process like email activation process will be held.
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        UpdateUserProfileResponse response = new UpdateUserProfileResponse();
        response.setMessage("User profile updated successfully.");

        return ApiResponse.success(response, "Profile updated successfully.");
    }

}
