package com.example.billiard_management_be.shared.utils;

import com.example.billiard_management_be.shared.dto.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
  public static CustomUserDetails getAuthUserDetails() {
    try {
      return (CustomUserDetails)
          SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
      return null;
    }
  }
}
