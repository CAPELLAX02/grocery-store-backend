package com.capellax.grocery_app_backend.service.jwt;

import com.capellax.grocery_app_backend.config.EnvironmentConfig;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtServiceUtils {

    private final EnvironmentConfig environmentConfig;

    protected Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(environmentConfig.getJwtSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    protected Claims extractAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            throw new CustomRuntimeException(ErrorCode.INVALID_TOKEN);
        }
    }

}
