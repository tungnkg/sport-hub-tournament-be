package com.example.billiard_management_be.shared.exceptions;

public class NotAuthorizedException extends RuntimeException {
  public NotAuthorizedException(String errorMsg) {
    super(errorMsg);
  }
}
