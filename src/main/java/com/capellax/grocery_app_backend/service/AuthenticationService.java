package com.capellax.grocery_app_backend.service;

import com.capellax.grocery_app_backend.dto.request.RegisterRequest;
import com.capellax.grocery_app_backend.dto.response.RegisterResponse;
import com.capellax.grocery_app_backend.model.User;
import com.capellax.grocery_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public RegisterResponse registerUser(
            RegisterRequest registerRequest
    ) {
        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        String activationCode = generateActivationCode();
        user.setActivationCode(activationCode);
        user.setEnabled(false);

        userRepository.save(user);

        mailService.sendActivationCode(user.getEmail(), activationCode);

        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setMessage("Please check your email (" + user.getEmail() + ") for the activation code");

        return response;
    }

    public boolean activateUser(
            String email,
            String activationCode
    ) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();

        if (user.getActivationCode().equals(activationCode)) {
            user.setEnabled(true);
            user.setActivationCode(null);
            userRepository.save(user);
            return true;

        } else {
            return false;
        }
    }

    private String generateActivationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(100000);
        return String.valueOf(code);
    }

}
