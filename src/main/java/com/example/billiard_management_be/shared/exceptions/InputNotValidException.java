package com.example.billiard_management_be.shared.exceptions;

public class InputNotValidException extends RuntimeException {
  public InputNotValidException(String errorMsg) {
    super(errorMsg);
  }
}
