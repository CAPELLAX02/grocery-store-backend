package com.capellax.grocery_app_backend.repository;

import com.capellax.grocery_app_backend.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationCode(String resetPasswordCode);
    Optional<User> findByResetPasswordCode(String resetPasswordCode);

}
