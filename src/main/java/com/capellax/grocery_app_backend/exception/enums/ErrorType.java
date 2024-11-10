package com.capellax.grocery_app_backend.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    // AUTHENTICATION ERRORS
    USER_ALREADY_EXISTS("User already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    INVALID_ACTIVATION_CODE("Invalid activation code", HttpStatus.BAD_REQUEST),
    INVALID_RESET_PASSWORD_CODE("Invalid activation code", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("Invalid username or password", HttpStatus.UNAUTHORIZED),
    TOKEN_CREATION_FAILED("Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN("Invalid or expired token", HttpStatus.UNAUTHORIZED),
    ACCOUNT_NOT_ACTIVATED("Account is not activated", HttpStatus.FORBIDDEN),

    // CART ERRORS
    CART_ITEM_NOT_FOUND("Cart item not found", HttpStatus.NOT_FOUND),
    CART_MODIFICATION_FAILED("Filed to modify the cart", HttpStatus.BAD_REQUEST),
    CART_IS_EMPTY("Cart is empty", HttpStatus.BAD_REQUEST),
    CART_PRODUCT_NOT_AVAILABLE("Product in cart is not available", HttpStatus.BAD_REQUEST),

    // ORDER ERRORS
    ORDER_NOT_FOUND("Order not found", HttpStatus.NOT_FOUND),
    ORDERS_NOT_FOUND("No orders found for this user", HttpStatus.NOT_FOUND),
    ORDER_CREATION_FAILED("Failed to create order", HttpStatus.INTERNAL_SERVER_ERROR),
    ORDER_EMPTY("Cannot place an order with an empty cart", HttpStatus.BAD_REQUEST),
    ORDER_USER_MISMATCH("Order does not belong to the current user", HttpStatus.FORBIDDEN),

    // PRODUCT ERRORS
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
    PRODUCTS_NOT_FOUND("Products not found", HttpStatus.NOT_FOUND),
    PRODUCT_OUT_OF_STOCK("Product out of stock", HttpStatus.BAD_REQUEST),

    // MAIL ERRORS
    // are going to be thrown as separate "custom mail exceptions" signatures when methods are defined

    // USER ERRORS
    USER_UPDATE_FAILED("Failed to update user profile", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_TAKEN("Username is already taken", HttpStatus.CONFLICT),
    INVALID_PASSWORD("Invalid password", HttpStatus.BAD_REQUEST),

    // OTHER GENERAL ERRORS
    INTERNAL_SERVER_ERROR("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("Bad request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS("Unauthorized access", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("Access denied", HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    ErrorType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
