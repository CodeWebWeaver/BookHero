package com.parkhomovsky.bookstore.exception;

public class UserNotAuthenticatedException extends Exception {
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
