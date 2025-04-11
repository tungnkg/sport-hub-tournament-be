package com.example.billiard_management_be.service.auth;


import com.example.billiard_management_be.dto.request.LoginRequest;
import com.example.billiard_management_be.dto.request.RegisterRequest;
import com.example.billiard_management_be.dto.response.LoginResponse;
import com.example.billiard_management_be.service.auth.dto.UserDto;

public interface IAuthUseCase {
  LoginResponse login(LoginRequest request) throws IllegalAccessException;

  UserDto register(RegisterRequest request);
}
