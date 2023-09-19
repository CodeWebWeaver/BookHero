package com.parkhomovsky.bookstore.exception;

public class EmptyShoppingCartException extends Exception {
    public EmptyShoppingCartException(String message) {
        super(message);
    }
}
