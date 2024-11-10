package com.capellax.grocery_app_backend.service.user;

import com.capellax.grocery_app_backend.dto.request.user.UpdateUserProfileRequest;
import com.capellax.grocery_app_backend.dto.response.user.GetUserProfileResponse;
import com.capellax.grocery_app_backend.dto.response.user.UpdateUserProfileResponse;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import com.capellax.grocery_app_backend.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceUtils userServiceUtils;

    public ApiResponse<GetUserProfileResponse> getUserProfile(
            String username
    ) {
        User user = userServiceUtils.getUserByUsername(username);
        GetUserProfileResponse response = userServiceUtils.buildUserProfileResponse(user);
        return ApiResponse.success(response, "User profile fetched successfully.");
    }

    public ApiResponse<UpdateUserProfileResponse> updateUserProfile(
            String username,
            UpdateUserProfileRequest request
    ) {
        User user = userServiceUtils.getUserByUsername(username);

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
