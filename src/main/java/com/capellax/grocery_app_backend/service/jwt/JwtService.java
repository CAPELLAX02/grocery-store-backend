package com.capellax.grocery_app_backend.service.jwt;

import com.capellax.grocery_app_backend.config.EnvironmentConfig;
import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtServiceUtils jwtServiceUtils;
    private final EnvironmentConfig environmentConfig;

    public <T> T extractClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = jwtServiceUtils.extractAllClaimsFromToken(token);
            return claimsResolver.apply(claims);

        } catch (CustomRuntimeException e) {
            throw new CustomRuntimeException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String extractUsernameFromToken(String token) {
        return extractClaimsFromToken(token, Claims::getSubject);
    }

    public Date extractExpirationFromToken(String token) {
        return extractClaimsFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpirationFromToken(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + environmentConfig.getJwtExpiration()))
                .signWith(jwtServiceUtils.getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateToken(String username) {
        return createToken(new HashMap<>(), username);
    }

}
