package com.capellax.grocery_app_backend.service.user;

import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceUtils {

    private final UserRepository userRepository;

    protected User getUserByUsername(
            String username
    ) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
