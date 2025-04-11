package com.example.billiard_management_be.service.auth;


import com.example.billiard_management_be.dto.request.LoginRefreshTokenRequest;
import com.example.billiard_management_be.dto.response.LoginResponse;

public interface ILoginRefreshTokenUseCase {
  LoginResponse login(LoginRefreshTokenRequest request) throws IllegalAccessException;
}
