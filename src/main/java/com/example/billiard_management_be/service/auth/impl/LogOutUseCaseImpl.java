package com.example.billiard_management_be.service.auth.impl;

import com.example.billiard_management_be.service.auth.dto.UserDto;
import com.example.billiard_management_be.entity.UserRefreshToken;
import com.example.billiard_management_be.repository.UserRefreshTokenRepository;
import com.example.billiard_management_be.service.auth.ILogOutUseCase;
import com.example.billiard_management_be.shared.dto.CustomUserDetails;
import com.example.billiard_management_be.shared.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LogOutUseCaseImpl implements ILogOutUseCase {
  private final UserRefreshTokenRepository userRefreshTokenRepository;

  @Override
  @Transactional
  public void logout() {
    CustomUserDetails userDetails = AuthUtils.getAuthUserDetails();

    if (Objects.isNull(userDetails) || Objects.isNull(userDetails.getUser())) {
      return;
    }

    UserDto user = userDetails.getUser();

    List<UserRefreshToken> oldToken =
            userRefreshTokenRepository.findAllByUserIdAndInvoke(user.getId(), false);
    oldToken.forEach(e -> e.setInvoke(true));
    userRefreshTokenRepository.saveAll(oldToken);
  }
}
