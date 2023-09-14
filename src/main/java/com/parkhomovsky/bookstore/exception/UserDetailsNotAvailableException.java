package com.parkhomovsky.bookstore.exception;

public class UserDetailsNotAvailableException extends RuntimeException {
  public UserDetailsNotAvailableException(String message) {
    super(message);
  }
}
