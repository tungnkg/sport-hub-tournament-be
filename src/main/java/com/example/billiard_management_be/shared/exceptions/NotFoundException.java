package com.example.billiard_management_be.shared.exceptions;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String errorMsg) {
    super(errorMsg);
  }
}
