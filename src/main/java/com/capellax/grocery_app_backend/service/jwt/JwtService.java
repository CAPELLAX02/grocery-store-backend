package com.capellax.grocery_app_backend.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration}")
    private Long jwtExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaimsFromToken(
            String token
    ) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Invalid or expired token");
        }
    }

    public <T> T extractClaimsFromToken(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        final Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsernameFromToken(
            String token
    ) {
        return extractClaimsFromToken(token, Claims::getSubject);
    }

    public Date extractExpirationFromToken(
            String token
    ) {
        return extractClaimsFromToken(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(
            String token
    ) {
        return extractExpirationFromToken(token).before(new Date());
    }

    public Boolean validateToken(
            String token,
            UserDetails userDetails
    ) {
        final String username = extractUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String createToken(
            Map<String, Object> claims,
            String username
    ) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(
            String username
    ) {
        return createToken(new HashMap<>(), username);
    }

}
