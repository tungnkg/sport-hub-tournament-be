package com.example.billiard_management_be.service.auth.dto;

import com.example.billiard_management_be.shared.utils.ModelTransformUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class UserDto {
    private Integer id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    private String avatar;
    private String gender;
    private String introduce;
    private String nickName;
    private String level;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder.Default private Boolean active = true;

    @Builder.Default
    List<RoleDto> roles = new ArrayList<>();

    public Boolean isAdmin() {
        if (Objects.isNull(roles) || roles.isEmpty()) return false;
        List<String> roleStrings = ModelTransformUtils.getAttribute(roles, RoleDto::getName);
        return roleStrings.contains("ADMIN");
    }
}
