package com.example.billiard_management_be.repository;

import com.example.billiard_management_be.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findAllByUserIdIn(List<Integer> userIds);
}