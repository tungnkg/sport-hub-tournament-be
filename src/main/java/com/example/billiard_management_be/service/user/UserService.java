package com.example.billiard_management_be.service.user;

import com.example.billiard_management_be.service.auth.dto.RoleDto;
import com.example.billiard_management_be.service.auth.dto.UserDto;
import com.example.billiard_management_be.entity.User;
import com.example.billiard_management_be.entity.UserRole;
import com.example.billiard_management_be.repository.RoleRepository;
import com.example.billiard_management_be.repository.UserRepository;
import com.example.billiard_management_be.repository.UserRoleRepository;
import com.example.billiard_management_be.shared.dto.CustomUserDetails;
import com.example.billiard_management_be.shared.utils.ModelMapperUtils;
import com.example.billiard_management_be.shared.utils.ModelTransformUtils;
import com.example.billiard_management_be.shared.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserRoleRepository userRoleRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(email);

    if (user.isEmpty()) {
      throw new UsernameNotFoundException(email);
    }
    UserDto userDto = ModelMapperUtils.mapper(user.get(), UserDto.class);
    enrichRole(Collections.singletonList(userDto));
    return new CustomUserDetails(userDto);
  }

  public void enrichRole(List<UserDto> users) {
    if (users == null || users.isEmpty()) return;
    List<Integer> userIds = ModelTransformUtils.getAttribute(users, UserDto::getId);
    List<UserRole> userRoles = userRoleRepository.findAllByUserIdIn(userIds);

    List<Integer> allRoleIds = ModelTransformUtils.getAttribute(userRoles, UserRole::getRoleId);

    List<RoleDto> roles = ModelMapperUtils.mapList(roleRepository.findAllByIdIn(allRoleIds), RoleDto.class);

    Map<Integer, List<Integer>> mapUserIdWithRoleIds =
        userRoles.stream()
            .collect(
                Collectors.groupingBy(
                    UserRole::getUserId,
                    Collectors.mapping(UserRole::getRoleId, Collectors.toList())));

    for (UserDto user : users) {
      List<Integer> roleIds = mapUserIdWithRoleIds.getOrDefault(user.getId(), Collections.emptyList());
      if (roleIds.isEmpty()) continue;
      List<RoleDto> roleOfUsers = roles.stream().filter(e -> roleIds.contains(e.getId())).toList();
      user.setRoles(roleOfUsers);
    }
  }

  public boolean isOrganizer() {
    return hasRole("ORGANIZER");
  }

  public boolean hasRole(String authRole) {
    UserDto user = SecurityUtils.getCurrentUserDetails();
    if (user == null) {
      return false;
    }

    List<RoleDto> roles = user.getRoles();
    for (RoleDto role : roles) {
      if (Objects.equals(role.getName(), authRole)) {
        return true;
      }
    }
    return false;
  }
}
