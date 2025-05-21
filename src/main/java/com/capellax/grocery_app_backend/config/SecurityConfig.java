package com.capellax.grocery_app_backend.config;

import com.capellax.grocery_app_backend.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // ------------------------------ PUBLIC ENDPOINTS -------------------------------

                        .requestMatchers(POST,   "/api/v1/auth/register").permitAll()
                        .requestMatchers(PATCH,  "/api/v1/auth/activate").permitAll()
                        .requestMatchers(POST,   "/api/v1/auth/login").permitAll()
                        .requestMatchers(POST,   "/api/v1/auth/forgot-password").permitAll()
                        .requestMatchers(PATCH,  "/api/v1/auth/reset-password").permitAll()
                        .requestMatchers(POST,   "/api/v1/auth/refresh-token").permitAll()

                        .requestMatchers(GET,    "/api/v1/products").permitAll()
                        .requestMatchers(GET,    "/api/v1/products/{id}").permitAll()
                        .requestMatchers(GET,    "/api/v1/products/{productId}/reviews").permitAll()

                        // ------------------------ AUTHENTICATED-ONLY ENDPOINTS -------------------------

                        .requestMatchers(GET,    "/api/v1/cart").authenticated()
                        .requestMatchers(POST,   "/api/v1/cart").authenticated()
                        .requestMatchers(PATCH,  "/api/v1/cart/{productId}").authenticated()
                        .requestMatchers(DELETE, "/api/v1/cart/{productId}").authenticated()
                        .requestMatchers(DELETE, "/api/v1/cart").authenticated()

                        .requestMatchers(POST,  "/api/v1/orders").authenticated()
                        .requestMatchers(GET,   "/api/v1/orders").authenticated()
                        .requestMatchers(GET,   "/api/v1/orders/{orderId}").authenticated()

                        .requestMatchers(POST,  "/api/v1/products/{productId}/reviews").authenticated()
                        .requestMatchers(DELETE,"/api/v1/products/{productId}/reviews").authenticated()

                        .requestMatchers(GET,   "/api/v1/users/profile").authenticated()
                        .requestMatchers(PATCH, "/api/v1/users/profile").authenticated()

                        .requestMatchers(GET,   "/api/v1/wishlist").authenticated()
                        .requestMatchers(POST,  "/api/v1/wishlist").authenticated()
                        .requestMatchers(DELETE,"/api/v1/wishlist/{productId}").authenticated()
                        .requestMatchers(DELETE,"/api/v1/wishlist").authenticated()

                        // ------------------ ALL OTHER REQUESTS REQUIRE AUTHENTICATION ------------------

                        // special endpoints for my friend Furkan who wanted me to add these endpoints.
                        .requestMatchers(GET,   "/furkidolki/premium-endpointler/get-all-users").permitAll()
                        .requestMatchers(DELETE,"/furkidolki/premium-endpointler/delete-all-users").permitAll()
                        .requestMatchers(DELETE,"/furkidolki/premium-endpointler/delete-user/{userId}").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
