package com.example.billiard_management_be.service.auth.impl;

import com.example.billiard_management_be.dto.request.ChangePasswordRequest;
import com.example.billiard_management_be.service.auth.dto.UserDto;
import com.example.billiard_management_be.entity.User;
import com.example.billiard_management_be.repository.UserRepository;
import com.example.billiard_management_be.service.auth.*;
import com.example.billiard_management_be.shared.constants.ExceptionMessage;
import com.example.billiard_management_be.shared.dto.CustomUserDetails;
import com.example.billiard_management_be.shared.exceptions.NotAuthorizedException;
import com.example.billiard_management_be.shared.utils.AuthUtils;
import com.example.billiard_management_be.shared.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.example.billiard_management_be.shared.constants.ExceptionMessage.OLD_PASSWORD_NOT_TRUE;


@Component
@RequiredArgsConstructor
public class ChangePasswordUseCaseImpl implements IChangePasswordUseCase {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public void changePassword(ChangePasswordRequest request) {
    CustomUserDetails userDetails = AuthUtils.getAuthUserDetails();

    if (Objects.isNull(userDetails) || Objects.isNull(userDetails.getUser())) {
      throw new NotAuthorizedException(ExceptionMessage.NOT_VALID_USER_DETAILS);
    }

    UserDto user = userDetails.getUser();
    String currentPassword = user.getPassword();

    if (!passwordEncoder.matches(request.getOldPassword(), currentPassword)) {
      throw new NotAuthorizedException(OLD_PASSWORD_NOT_TRUE);
    }

    String hashPassword = passwordEncoder.encode(request.getNewPassword());
    user.setPassword(hashPassword);

    userRepository.save(ModelMapperUtils.mapper(user, User.class));
  }
}
