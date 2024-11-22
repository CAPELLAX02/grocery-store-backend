package com.capellax.grocery_app_backend.service.jwt;

import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${security.jwt.access-token.expiration}")
    private Long accessTokenExpiration; // 24 hours

    @Value("${security.jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration; // 7 days

    public <T> T extractClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = jwtServiceUtils.extractAllClaimsFromToken(token);
            return claimsResolver.apply(claims);

        } catch (CustomRuntimeException e) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_ACCESS_TOKEN);
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

    public String createToken(Map<String, Object> claims, String username, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(jwtServiceUtils.getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return createToken(claims, username, accessTokenExpiration);
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, username, refreshTokenExpiration);
    }

    public Boolean validateAccessToken(String token, String username) {
        final String extractedUsername = extractUsernameFromToken(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public void validateRefreshToken(String token) {
        Claims claims = jwtServiceUtils.extractAllClaimsFromToken(token);
        if (!"refresh".equals(claims.get("type"))) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_REFRESH_TOKEN);
        }
        if (isTokenExpired(token)) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_REFRESH_TOKEN);
        }
    }

}
