package com.capellax.grocery_app_backend.model.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Id
    private String id;

    private String token;
    private String userId;
    private Instant expiryDate;

}
