package com.capellax.grocery_app_backend.security.jwt;

import com.capellax.grocery_app_backend.security.user.UserDetailsServiceImpl;
import com.capellax.grocery_app_backend.service.jwt.JwtService;
import com.capellax.grocery_app_backend.service.jwt.JwtServiceUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtServiceUtils jwtServiceUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtService.extractUsernameFromToken(token);

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (isAccessToken(token)) {
                    if (jwtService.validateAccessToken(token, userDetails.getUsername())) {
                        setUpSpringAuthentication(request, userDetails);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : null;
    }

    private void setUpSpringAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private boolean isAccessToken(String token) {
        Claims claims = jwtServiceUtils.extractAllClaimsFromToken(token);
        String tokenType = (String) claims.get("type");
        return "access".equals(tokenType);
    }

}
