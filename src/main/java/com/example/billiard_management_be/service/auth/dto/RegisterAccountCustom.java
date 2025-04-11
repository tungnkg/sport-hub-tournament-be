package com.example.billiard_management_be.service.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAccountCustom {
    private String fullName;
    private String email;
    private String password;
    private String otp;
}
