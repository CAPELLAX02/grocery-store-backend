package com.capellax.grocery_app_backend.repository;

import com.capellax.grocery_app_backend.model.token.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(String userId);

}
