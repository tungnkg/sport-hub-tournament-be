package com.example.billiard_management_be.service.auth;


import com.example.billiard_management_be.dto.request.ChangePasswordRequest;

public interface IChangePasswordUseCase {
  void changePassword(ChangePasswordRequest request);
}
