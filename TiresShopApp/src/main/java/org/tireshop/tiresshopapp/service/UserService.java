package org.tireshop.tiresshopapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import org.tireshop.tiresshopapp.exception.UsernameAlreadyExistException;
import org.tireshop.tiresshopapp.repository.RoleRepository;
import org.tireshop.tiresshopapp.repository.UserRepository;
import org.tireshop.tiresshopapp.specifications.UserSpecification;
import org.tireshop.tiresshopapp.util.SortUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  // GET
  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    return toMapResponse(user);
  }

  public Page<UserResponse> getUsers(String email, String username, String firstName,
      String lastName, String role, String phoneNumber, int page, int sizePerPage, String sort) {
    Specification<User> specification = Specification
        .where(UserSpecification.hasEmailContaining(email))
        .and(UserSpecification.hasUsernameContaining(username))
        .and(UserSpecification.hasFirstNameContaining(firstName))
        .and(UserSpecification.hasLastNameContaining(lastName)).and(UserSpecification.hasRole(role))
        .and(UserSpecification.hasPhoneNumberContaining(phoneNumber));

    Sort sorting = SortUtils.parseSort(sort);
    Pageable pageable = PageRequest.of(page, sizePerPage, sorting);
    Page<User> users = userRepository.findAll(specification, pageable);

    return users.map(this::toMapResponse);
  }


  public User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetails userDetails) {
      String email = userDetails.getUsername();
      return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    } else if ("anonymousUser".equals(principal)) {
      return null;
    } else {
      throw new UserNotFoundException();
    }

  }

  // DELETE
  @Transactional
  public void deleteCurrentUserData() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
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
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    if (userRepository.existsByUsername(request.username())
        && !user.getUsername().equals(request.username())) {
      throw new UsernameAlreadyExistException();
    }

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
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

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
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

    userRepository.delete(user);
  }

  public UserResponse toMapResponse(User user) {

    List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();

    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(),
        user.getLastName(), user.getPhoneNumber(), roleNames);
  }

}
