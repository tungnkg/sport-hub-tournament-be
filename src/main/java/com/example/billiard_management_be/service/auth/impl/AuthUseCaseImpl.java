package com.example.billiard_management_be.service.auth.impl;

import com.example.billiard_management_be.configuration.authen.JwtTokenProvider;
import com.example.billiard_management_be.dto.request.LoginRequest;
import com.example.billiard_management_be.dto.request.RegisterRequest;
import com.example.billiard_management_be.dto.response.LoginResponse;
import com.example.billiard_management_be.service.auth.dto.RegisterAccountCustom;
import com.example.billiard_management_be.service.auth.dto.RoleDto;
import com.example.billiard_management_be.service.auth.dto.UserDto;
import com.example.billiard_management_be.entity.Role;
import com.example.billiard_management_be.entity.User;
import com.example.billiard_management_be.entity.UserRefreshToken;
import com.example.billiard_management_be.entity.UserRole;
import com.example.billiard_management_be.repository.RoleRepository;
import com.example.billiard_management_be.repository.UserRefreshTokenRepository;
import com.example.billiard_management_be.repository.UserRepository;
import com.example.billiard_management_be.repository.UserRoleRepository;
import com.example.billiard_management_be.service.auth.IAuthUseCase;
import com.example.billiard_management_be.service.mail.EmailService;
import com.example.billiard_management_be.service.mail.dto.RequestSendMail;
import com.example.billiard_management_be.service.user.UserService;
import com.example.billiard_management_be.shared.constants.ExceptionMessage;
import com.example.billiard_management_be.shared.exceptions.InputNotValidException;
import com.example.billiard_management_be.shared.exceptions.NotAuthorizedException;
import com.example.billiard_management_be.shared.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@Component
@RequiredArgsConstructor
@Log4j2
public class AuthUseCaseImpl implements IAuthUseCase {
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final UserRepository userRepository;
  private final UserRefreshTokenRepository userRefreshTokenRepository;
  private final UserRoleRepository userRoleRepository;
  private final RoleRepository roleRepository;
  private final EmailService emailService;
  private final Integer TTL_TIME_AMOUNT = 5;
  private final String VERIFY_OTP_FAIL_COUNT_KEY_PREFIX = "verify_otp_register_account_fail_count";
  private final String REGISTER_ACCOUNT_KEY_PREFIX = "register_account";
  private final String REGISTER_ACCOUNT_SESSION_KEY_PREFIX = "register_account_session";
  private final Integer SESSION_EXPIRATION_TIME = 10*60;

  @Value("${vn.billiard_management_be.secret.jwt_expiration_ms}")
  private int jwtExpirationMs;

  private static final String PASSWORD_REGEX =
      "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

  @Override
  @Transactional
  public LoginResponse login(LoginRequest request) throws IllegalAccessException {
    Optional<User> user = userRepository.findByEmail(request.getEmail());
    if (user.isEmpty()) {
      throw new NotAuthorizedException(ExceptionMessage.USER_IS_NOT_EXIST);
    }


    if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
      throw new NotAuthorizedException(ExceptionMessage.PASSWORD_INCORRECT);
    }

    UserDto userDto = ModelMapperUtils.mapper(user.get(), UserDto.class);
    userService.enrichRole(Stream.of(userDto).filter(Objects::nonNull).toList());
    String token =
        jwtTokenProvider.generateJwtToken(userDto, ObjectUtils.convertUsingReflection(userDto));
    String refreshToken =
        jwtTokenProvider.generateRefreshToken(userDto, ObjectUtils.convertUsingReflection(userDto));
    processRefreshToken(userDto, refreshToken);

    if (Objects.isNull(userDto.getRoles()) || userDto.getRoles().isEmpty()) {
      userDto.setRoles(List.of(RoleDto.builder().id(3).name("PLAYER").build()));
    }

    return LoginResponse.builder()
        .accessToken(token)
        .refreshToken(refreshToken)
        .expiresIn(LocalDateTime.now().plusSeconds(jwtExpirationMs / 1000))
        .userInfo(userDto)
        .build();
  }

  @Override
  @Transactional
  public UserDto register(RegisterRequest request) {
    User user = ModelMapperUtils.mapper(request, User.class);

    Pattern pattern = Pattern.compile(PASSWORD_REGEX);
    if (!pattern.matcher(request.getPassword()).matches()) {
      throw new InputNotValidException(ExceptionMessage.INVALID_PASSWORD);
    }
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    user = this.userRepository.save(user);

    Role role = roleRepository.findByName(request.getRole())
            .orElseThrow(() -> new InputNotValidException(ExceptionMessage.ROLE_NOT_FOUND));
    userRoleRepository.save(UserRole.builder().userId(user.getId()).roleId(role.getId()).build());
    return ModelMapperUtils.mapper(user, UserDto.class);
  }

  private void processRefreshToken(UserDto user, String refreshToken) {
    List<UserRefreshToken> oldToken =
        userRefreshTokenRepository.findAllByUserIdAndInvoke(user.getId(), false);
    oldToken.forEach(e -> e.setInvoke(true));
    userRefreshTokenRepository.saveAll(oldToken);

    UserRefreshToken userRefreshToken =
        UserRefreshToken.builder()
            .userId(user.getId())
            .invoke(false)
            .refreshToken(passwordEncoder.encode((StringUtils.truncateToken(refreshToken))))
            .build();

    userRefreshTokenRepository.save(userRefreshToken);
  }


//  private void sendRegisterOTP(RegisterAccountCustom registerAccountCustom) throws JsonProcessingException {
//    String registerAccountValue = JsonUtils.toJson(registerAccountCustom);
//    //cache information and OTP
//    CacheUtils.setIfNotExists(String.format("%s_%s_OTP", REGISTER_ACCOUNT_KEY_PREFIX, registerAccountCustom.getEmail()),
//            registerAccountValue, TTL_TIME_AMOUNT, TimeUnit.MINUTES);
//    //cache session
//    CacheUtils.setIfNotExists(String.format("%s_%s", REGISTER_ACCOUNT_SESSION_KEY_PREFIX, registerAccountCustom.getEmail()),
//            registerAccountValue, SESSION_EXPIRATION_TIME, TimeUnit.MINUTES);
//    log.info("Send register account OTP for user with email {} at {}", registerAccountCustom.getEmail(), Instant.now());
//
//    //send mail
//    Map<String, Object> model = new HashMap<>();
//    model.put("toFullName", registerAccountCustom.getFullName());
//    model.put("otp", registerAccountCustom.getOtp());
//    model.put("otpTtl", TTL_TIME_AMOUNT);
//    RequestSendMail requestSendMail = RequestSendMail.builder()
//            .to(registerAccountCustom.getEmail())
//            .subject("Xác thực đăng ký tài khoản Moocs")
//            .templateName("otp-register-account-template.html")
//            .body(model)
//            .build();
//    emailService.sendNewMail(requestSendMail);
//  }

}
