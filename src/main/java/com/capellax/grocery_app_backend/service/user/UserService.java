package com.capellax.grocery_app_backend.service.user;

import com.capellax.grocery_app_backend.dto.request.user.UpdateUserProfileRequest;
import com.capellax.grocery_app_backend.dto.response.user.GetUserProfileResponse;
import com.capellax.grocery_app_backend.dto.response.user.UpdateUserProfileResponse;
import com.capellax.grocery_app_backend.model.product.Product;
import com.capellax.grocery_app_backend.model.user.User;
import com.capellax.grocery_app_backend.repository.ProductRepository;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceUtils userServiceUtils;
    private final ProductRepository productRepository;

    public ApiResponse<GetUserProfileResponse> getUserProfile(
            String username
    ) {
        User user = userServiceUtils.getUserByUsername(username);
        GetUserProfileResponse response = userServiceUtils.buildUserProfileResponse(user);
        return ApiResponse.success(response, "User profile fetched successfully.");
    }

    public ApiResponse<UpdateUserProfileResponse> updateUserProfile(
            String currentUsername,
            UpdateUserProfileRequest request
    ) {
        User user = userServiceUtils.getUserByUsername(currentUsername);

        String oldUsername = user.getUsername();
        String newUsername = request.getUsername();

        if (!oldUsername.equals(newUsername)) {
            user.setUsername(newUsername);

            List<Product> products = productRepository.findAllByReviews_Username(oldUsername);
            for (Product product : products) {
                product.getReviews().forEach(review -> {
                    if (review.getUsername().equals(oldUsername)) {
                        review.setUsername(newUsername);
                    }
                });
                productRepository.save(product);
            }
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // TODO: user email update will be distinct logic.
        //  A process like email activation process will be held.

        UpdateUserProfileResponse response = new UpdateUserProfileResponse();
        response.setMessage("User profile updated successfully.");

        return ApiResponse.success(response, "Profile updated successfully.");
    }

}
