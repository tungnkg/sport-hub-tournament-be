package com.example.billiard_management_be.shared.dto;

import com.example.billiard_management_be.service.auth.dto.RoleDto;
import com.example.billiard_management_be.service.auth.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private UserDto user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<RoleDto> roles = user.getRoles();
        if (roles.isEmpty()) return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleDto role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getActive();
    }
}