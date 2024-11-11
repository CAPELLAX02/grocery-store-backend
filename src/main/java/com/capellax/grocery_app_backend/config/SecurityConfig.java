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

                        // --------------------------- PUBLIC ENDPOINTS ----------------------------

                        // Register user with "enabled: false" & Send activation code to their email
                        .requestMatchers(POST, "/api/v1/auth/register").permitAll()
                        // Activate user account using the activation code
                        .requestMatchers(POST, "/api/v1/auth/activate").permitAll()
                        // Log the user in & Get the authentication token
                        .requestMatchers(POST, "/api/v1/auth/login").permitAll()
                        // Send user reset-password code via email
                        .requestMatchers(POST, "/api/v1/auth/forgot-password").permitAll()
                        // Reset user's password using the reset-password code
                        .requestMatchers(POST, "/api/v1/auth/reset-password").permitAll()

                        // Fetch all products
                        .requestMatchers(GET, "/api/v1/products").permitAll()
                        // Fetch product by ID
                        .requestMatchers(GET, "/api/v1/products/{id}").permitAll()
                        // Fetch product reviews
                        .requestMatchers(GET, "/api/v1/products/{productId}/reviews").permitAll()

                        // --------------------- AUTHENTICATED-ONLY ENDPOINTS ----------------------

                        // Fetch my shopping cart
                        .requestMatchers(GET, "/api/v1/cart").authenticated()
                        // Add item to my cart
                        .requestMatchers(POST, "/api/v1/cart").authenticated()
                        // Update cart item quantity
                        .requestMatchers(PUT, "/api/v1/cart/{productId}").authenticated()
                        // Delete a cart item
                        .requestMatchers(DELETE, "/api/v1/cart/{productId}").authenticated()
                        // Clear all my cart items
                        .requestMatchers(DELETE, "/api/v1/cart").authenticated()

                        // Place order
                        .requestMatchers(POST, "/api/v1/orders").authenticated()
                        // Fetch my orders
                        .requestMatchers(GET, "/api/v1/orders").authenticated()
                        // Fetch my order by ID
                        .requestMatchers(GET, "/api/v1/orders/{orderId}").authenticated()

                        // Add product review
                        .requestMatchers(POST, "/api/v1/products/{productId}/reviews").authenticated()
                        // Delete my product view
                        .requestMatchers(DELETE, "/api/v1/products/{productId}/reviews").authenticated()

                        // Fetch my profile
                        .requestMatchers(GET, "/api/v1/users/profile").authenticated()
                        // Update my profile
                        .requestMatchers(PUT, "/api/v1/users/profile").authenticated()


                        // --------------- ALL OTHER REQUESTS REQUIRE AUTHENTICATION ---------------
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
