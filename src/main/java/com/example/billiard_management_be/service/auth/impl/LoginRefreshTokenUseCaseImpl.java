package com.example.billiard_management_be.service.auth.impl;

import com.example.billiard_management_be.configuration.authen.JwtTokenProvider;
import com.example.billiard_management_be.dto.request.LoginRefreshTokenRequest;
import com.example.billiard_management_be.dto.response.LoginResponse;
import com.example.billiard_management_be.service.auth.dto.UserDto;
import com.example.billiard_management_be.entity.User;
import com.example.billiard_management_be.entity.UserRefreshToken;
import com.example.billiard_management_be.repository.UserRefreshTokenRepository;
import com.example.billiard_management_be.repository.UserRepository;
import com.example.billiard_management_be.service.auth.ILoginRefreshTokenUseCase;
import com.example.billiard_management_be.service.user.UserService;
import com.example.billiard_management_be.shared.exceptions.NotAuthorizedException;
import com.example.billiard_management_be.shared.utils.ModelMapperUtils;
import com.example.billiard_management_be.shared.utils.ObjectUtils;
import com.example.billiard_management_be.shared.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.example.billiard_management_be.shared.constants.ExceptionMessage.*;


@Component
@RequiredArgsConstructor
@Log4j2
public class LoginRefreshTokenUseCaseImpl implements ILoginRefreshTokenUseCase {
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final UserRefreshTokenRepository userRefreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Value("${vn.billiard_management_be.secret.jwt_expiration_ms}")
  private int jwtExpirationMs;

  @Override
  @Transactional
  public LoginResponse login(LoginRefreshTokenRequest request) throws IllegalAccessException {
    boolean isValidToken = jwtTokenProvider.validateJwtToken(request.getToken());
    if (!isValidToken) {
      throw new NotAuthorizedException(REFRESH_TOKEN_NOT_VALID);
    }

    User tokenUser = userRepository.findByEmail(jwtTokenProvider.getUsernameByToken(request.getToken()))
            .orElseThrow(() ->  new NotAuthorizedException(LOGIN_FAIL));



    List<UserRefreshToken> userRefreshTokenList =
            userRefreshTokenRepository.findAllByUserIdAndInvoke(tokenUser.getId(), false);

    if (userRefreshTokenList.isEmpty()) {
      throw new NotAuthorizedException(REFRESH_TOKEN_NOT_VALID);
    }

    UserRefreshToken currentRefreshToken = userRefreshTokenList.get(0);

    if (!passwordEncoder.matches(StringUtils.truncateToken(request.getToken()), StringUtils.truncateToken(currentRefreshToken.getRefreshToken()))) {
      throw new NotAuthorizedException(REFRESH_TOKEN_NOT_VALID);
    }

    UserDto userInfo = ModelMapperUtils.mapper(tokenUser, UserDto.class);
    userService.enrichRole(Collections.singletonList(userInfo));
    return LoginResponse.builder()
        .accessToken(
            jwtTokenProvider.generateJwtToken(
                    userInfo, ObjectUtils.convertUsingReflection(tokenUser)))
        .refreshToken(request.getToken())
        .expiresIn(LocalDateTime.now().plusSeconds(jwtExpirationMs / 1000))
        .userInfo(userInfo)
        .build();
  }
}
