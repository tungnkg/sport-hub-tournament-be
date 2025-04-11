package com.example.billiard_management_be.controller;

import com.example.billiard_management_be.dto.request.LoginRefreshTokenRequest;
import com.example.billiard_management_be.dto.request.LoginRequest;
import com.example.billiard_management_be.dto.request.RegisterRequest;
import com.example.billiard_management_be.dto.response.BaseResponse;
import com.example.billiard_management_be.dto.response.LoginResponse;
import com.example.billiard_management_be.service.auth.dto.UserDto;
import com.example.billiard_management_be.service.auth.IAuthUseCase;
import com.example.billiard_management_be.service.auth.ILoginRefreshTokenUseCase;
import com.example.billiard_management_be.shared.factory.ResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthUseCase iAuthUseCase;
    private final ILoginRefreshTokenUseCase iLoginRefreshTokenUseCase;

    @PostMapping("login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request)
            throws IllegalAccessException {
        return ResponseFactory.success(iAuthUseCase.login(request));
    }

    @PostMapping("register")
    public ResponseEntity<BaseResponse<UserDto>> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseFactory.success(iAuthUseCase.register(request));
    }

    @PostMapping("refresh-login")
    public ResponseEntity<BaseResponse<LoginResponse>> refreshTokenLogin(@RequestBody @Valid LoginRefreshTokenRequest request) throws IllegalAccessException {
        return ResponseFactory.success(iLoginRefreshTokenUseCase.login(request));
    }
}
