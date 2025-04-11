package com.example.billiard_management_be.repository;

import com.example.billiard_management_be.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Integer> {
    List<UserRefreshToken> findAllByUserIdAndInvoke(Integer id, boolean b);
}