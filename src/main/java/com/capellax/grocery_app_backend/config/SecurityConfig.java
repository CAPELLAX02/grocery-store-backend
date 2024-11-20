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

                        .requestMatchers(GET,    "/v3/api-docs/**").permitAll()
                        .requestMatchers(GET,    "/swagger-ui/**").permitAll()
                        .requestMatchers(GET,    "/swagger-ui.html").permitAll()
                        .requestMatchers(GET,    "/swagger-ui/index.html").permitAll()

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

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
