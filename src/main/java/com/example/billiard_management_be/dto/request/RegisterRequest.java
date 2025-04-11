package com.example.billiard_management_be.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {

  @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = " Sai định dạng số email")
  @NotNull(message = "email cannot be null")
  @Size(max = 255, message = "email không quá 255 ký tự")
  private String email;

  @NotNull(message = "name cannot be null")
  @Size(max = 255, message = "name không quá 255 ký tự")
  private String name;

  @NotNull(message = "nick_name cannot be null")
  @Size(max = 255, message = "nick_name không quá 255 ký tự")
  private String nickName;

  @NotNull(message = "password cannot be null")
  @Pattern(
          regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
          message = "Mật khẩu không đúng định dạng")
  @Size(min = 8, max = 255, message = "Mật khẩu phải có ít nhất 8 ký tự và nhiều nhất 255 ký tự")
  private String password;
  @NotNull(message = "password cannot be null")
  private String confirmPassword;

  private String gender;

  private String level;

  @NotNull(message = "source cannot be null")
  private String role;
}
