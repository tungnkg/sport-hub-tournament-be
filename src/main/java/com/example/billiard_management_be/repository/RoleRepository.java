package com.example.billiard_management_be.repository;

import com.example.billiard_management_be.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByIdIn(List<Integer> allRoleIds);
    Optional<Role> findByName(String name);

}