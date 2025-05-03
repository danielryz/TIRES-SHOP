package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.update.UpdateUserPasswordRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateUserRequest;
import org.tireshop.tiresshopapp.dto.response.UserResponse;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.InvalidPasswordException;
import org.tireshop.tiresshopapp.exception.RoleNotFoundException;
import org.tireshop.tiresshopapp.exception.UserNotFoundException;
import org.tireshop.tiresshopapp.repository.RoleRepository;
import org.tireshop.tiresshopapp.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  // GET
  public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream().map(this::toMapResponse).toList();
  }

  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    return toMapResponse(user);
  }

  public User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String email;
    if (principal instanceof UserDetails userDetails) {
      email = userDetails.getUsername();
    } else {
      email = principal.toString();
    }
    return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
  }

  // DELETE
  @Transactional
  public void deleteCurrentUserData() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    user.setFirstName(null);
    user.setLastName(null);
    user.setPhoneNumber(null);

    userRepository.save(user);

  }

  // PATCH
  @Transactional
  public void updateCurrentUser(UpdateUserRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    if (request.username() != null && !request.username().isBlank()) {
      user.setUsername(request.username());
    }
    if (request.firstName() != null && !request.firstName().isBlank()) {
      user.setFirstName(request.firstName());
    }
    if (request.lastName() != null && !request.lastName().isBlank()) {
      user.setLastName(request.lastName());
    }
    if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
      user.setPhoneNumber(request.phoneNumber());
    }
    userRepository.save(user);
  }

  @Transactional
  public void updateCurrentUserPassword(UpdateUserPasswordRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new InvalidPasswordException();
    }

    if (request.newPassword() == null || request.newPassword().isBlank()) {
      throw new IllegalArgumentException("New password must not be blank.");
    }

    user.setPassword(passwordEncoder.encode(request.newPassword()));
    userRepository.save(user);
  }

  @Transactional
  public void addRoleToUser(Long userId, Long roleId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    Role role =
        roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
    user.getRoles().add(role);
    userRepository.save(user);
  }

  // DELETE
  @Transactional
  public void removeUserRole(Long userId, Long roleId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }
    if (!roleRepository.existsById(roleId)) {
      throw new RoleNotFoundException(roleId);
    }
    userRepository.deleteUserRole(userId, roleId);
  }

  @Transactional
  public void deleteUserById(Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);
  }

  @Transactional
  public void deleteCurrentUser() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    userRepository.delete(user);
  }

  public UserResponse toMapResponse(User user) {

    List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();

    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(),
        user.getLastName(), user.getPhoneNumber(), roleNames);
  }

}
