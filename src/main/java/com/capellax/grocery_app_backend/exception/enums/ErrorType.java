package com.capellax.grocery_app_backend.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorType {

    // AUTHENTICATION ERRORS
    USER_ALREADY_EXISTS("User already exists", CREATED),
    USER_NOT_FOUND("User not found", NOT_FOUND),
    INVALID_ACTIVATION_CODE("Invalid activation code", BAD_REQUEST),
    INVALID_CREDENTIALS("Invalid username or password", UNAUTHORIZED),
    TOKEN_CREATION_FAILED("Token creation failed", INTERNAL_SERVER_ERROR),
    INVALID_TOKEN("Invalid or expired token", UNAUTHORIZED),
    ACCOUNT_NOT_ACTIVATED("Account is not activated", FORBIDDEN),

    // CART ERRORS
    CART_ITEM_NOT_FOUND("Cart item not found", NOT_FOUND),
    CART_MODIFICATION_FAILED("Filed to modify the cart", BAD_REQUEST),
    CART_EMPTY("Cart is empty", BAD_REQUEST),
    CART_PRODUCT_NOT_AVAILABLE("Product in cart is not available", BAD_REQUEST),

    // ORDER ERRORS
    ORDER_NOT_FOUND("Order not found", NOT_FOUND),
    ORDER_CREATION_FAILED("Failed to create order", INTERNAL_SERVER_ERROR),
    ORDER_EMPTY("Cannot place an order with an empty cart", BAD_REQUEST),
    ORDER_USER_MISMATCH("Order does not belong to the current user", FORBIDDEN),

    // PRODUCT ERRORS
    PRODUCT_NOT_FOUND("Product not found", NOT_FOUND),
    PRODUCT_OUT_OF_STOCK("Product out of stock", BAD_REQUEST),

    // MAIL ERRORS
    MAIL_SEND_FAILED("Failed to send email", INTERNAL_SERVER_ERROR),
    ACTIVATION_MAIL_SEND_FAILED("Failed to send activation email", INTERNAL_SERVER_ERROR),
    PASSWORD_RESET_MAIL_SEND_FAILED("Failed to send password reset email", INTERNAL_SERVER_ERROR),

    // USER ERRORS
    USER_UPDATE_FAILED("Failed to update user profile", INTERNAL_SERVER_ERROR),
    USERNAME_TAKEN("Username is already taken", CONFLICT),
    INVALID_PASSWORD("Invalid password", BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    ErrorType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
