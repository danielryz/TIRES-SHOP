package org.tireshop.tiresshopapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.tireshop.tiresshopapp.service.UserService;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleRepository roleRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private SecurityContext securityContext;
  @Mock
  private Authentication authentication;
  @Mock
  private UserDetails userDetails;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void shouldGetUserById_WhenExists() {
    User user = new User();
    user.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    UserResponse result = userService.getUserById(1L);

    assertNotNull(result);
    assertEquals(1L, result.id());
  }

  @Test
    void shouldThrow_WhenUserNotFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

  @Test
  void shouldReturnPagedUsers_WhenFiltered() {
    // Arrange
    String email = "user@example.com";
    String username = "user";
    String firstName = "John";
    String lastName = "Doe";
    String role = "ADMIN";
    String phoneNumber = "123456789";
    int page = 0;
    int sizePerPage = 10;
    String sort = "email,asc";

    User user = new User();
    user.setId(1L);
    user.setUsername(username);
    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setPhoneNumber(phoneNumber);
    Role userRole = new Role();
    userRole.setName(role);
    user.setRoles(Set.of(userRole));

    Page<User> userPage = new PageImpl<>(List.of(user));

    Sort mockSort = Sort.by("email").ascending();
    Pageable expectedPageable = PageRequest.of(page, sizePerPage, mockSort);

    when(userRepository.findAll(any(Specification.class), eq(expectedPageable)))
        .thenReturn(userPage);

    // Act
    Page<UserResponse> result = userService.getUsers(email, username, firstName, lastName, role,
        phoneNumber, page, sizePerPage, sort);

    // Assert
    assertEquals(1, result.getTotalElements());

    UserResponse userResponse = result.getContent().get(0);
    assertEquals(username, userResponse.username());
    assertEquals(email, userResponse.email());
    assertEquals(firstName, userResponse.firstName());
    assertEquals(lastName, userResponse.lastName());
    assertEquals(phoneNumber, userResponse.phoneNumber());
    assertEquals(List.of(role), userResponse.roles());

    verify(userRepository, times(1)).findAll(any(Specification.class), eq(expectedPageable));
  }

  @Test
  void shouldReturnCurrentUser_WhenAuthenticated() {
    User user = new User();
    user.setEmail("test@example.com");
    when(userDetails.getUsername()).thenReturn("test@example.com");
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    User currentUser = userService.getCurrentUser();

    assertNotNull(currentUser);
    assertEquals("test@example.com", currentUser.getEmail());
  }

  @Test
  void shouldReturnCurrentUser_WhenUserDetailsPresent() {
    // Arrange
    UserDetails userDetails = mock(UserDetails.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("user@example.com");

    User user = new User();
    user.setEmail("user@example.com");

    when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

    // Act
    User result = userService.getCurrentUser();

    // Assert
    assertNotNull(result);
    assertEquals("user@example.com", result.getEmail());
  }

  @Test
  void shouldThrowUserNotFoundException_WhenUserDetailsMissing() {
    // Arrange
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(123); // jakiś dziwny obiekt

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
  }

  @Test
  void shouldThrowUserNotFoundException_WhenUserNotFoundInRepository() {
    // Arrange
    UserDetails userDetails = mock(UserDetails.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn("missing@example.com");

    when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
  }

  @Test
    void shouldReturnNull_WhenAnonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        User result = userService.getCurrentUser();
        assertNull(result);
    }

  @Test
  void shouldUpdateCurrentUserFields() {
    // Arrange
    String email = "user@example.com";
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn(email);

    User user = new User();
    user.setEmail(email);
    user.setUsername("oldUsername");
    user.setFirstName("Old");
    user.setLastName("Name");
    user.setPhoneNumber("000000000");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    UpdateUserRequest request = new UpdateUserRequest("newUsername", "New", "User", "123456789");

    // Act
    userService.updateCurrentUser(request);

    // Assert
    assertEquals("newUsername", user.getUsername());
    assertEquals("New", user.getFirstName());
    assertEquals("User", user.getLastName());
    assertEquals("123456789", user.getPhoneNumber());

    verify(userRepository).save(user);
  }

  @Test
  void shouldNotUpdateBlankFields() {
    // Arrange
    String email = "user@example.com";
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn(email);

    User user = new User();
    user.setEmail(email);
    user.setUsername("existingUsername");
    user.setFirstName("Existing");
    user.setLastName("User");
    user.setPhoneNumber("999999999");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // Blank update
    UpdateUserRequest request = new UpdateUserRequest("  ", null, "", " ");

    // Act
    userService.updateCurrentUser(request);

    // Assert - no change
    assertEquals("existingUsername", user.getUsername());
    assertEquals("Existing", user.getFirstName());
    assertEquals("User", user.getLastName());
    assertEquals("999999999", user.getPhoneNumber());

    verify(userRepository).save(user);
  }

  @Test
  void shouldDeleteCurrentUserData() {
    User user = new User();
    user.setEmail("test@example.com");
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("test@example.com");
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    userService.deleteCurrentUserData();

    assertNull(user.getFirstName());
    assertNull(user.getLastName());
    assertNull(user.getPhoneNumber());
    verify(userRepository).save(user);
  }

  @Test
  void shouldUpdateUserPassword_WhenOldPasswordMatches() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("encoded-password");

    UpdateUserPasswordRequest request = new UpdateUserPasswordRequest("old-pass", "new-pass");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("test@example.com");
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("old-pass", "encoded-password")).thenReturn(true);
    when(passwordEncoder.encode("new-pass")).thenReturn("encoded-new-pass");

    userService.updateCurrentUserPassword(request);

    assertEquals("encoded-new-pass", user.getPassword());
    verify(userRepository).save(user);
  }

  @Test
  void shouldThrow_WhenOldPasswordDoesNotMatch() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("encoded-password");

    UpdateUserPasswordRequest request = new UpdateUserPasswordRequest("wrong", "new");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("test@example.com");
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "encoded-password")).thenReturn(false);

    assertThrows(InvalidPasswordException.class,
        () -> userService.updateCurrentUserPassword(request));
  }

  @Test
  void shouldThrow_WhenNewPasswordBlank() {
    User user = new User();
    user.setEmail("test@example.com");
    user.setPassword("encoded-password");

    UpdateUserPasswordRequest request = new UpdateUserPasswordRequest("old-pass", " ");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("test@example.com");
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("old-pass", "encoded-password")).thenReturn(true);

    assertThrows(IllegalArgumentException.class,
        () -> userService.updateCurrentUserPassword(request));
  }

  @Test
  void shouldAddRoleToUser() {
    User user = new User();
    user.setRoles(new HashSet<>());
    Role role = new Role();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(roleRepository.findById(2L)).thenReturn(Optional.of(role));

    userService.addRoleToUser(1L, 2L);

    assertTrue(user.getRoles().contains(role));
    verify(userRepository).save(user);
  }

  @Test
  void shouldThrow_WhenRoleNotFound() {
    User user = new User();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(roleRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(RoleNotFoundException.class, () -> userService.addRoleToUser(1L, 2L));
  }

  @Test
  void shouldThrowRoleNotFoundException_WhenRoleDoesNotExist() {
    // Arrange
    Long userId = 1L;
    Long roleId = 2L;

    when(userRepository.existsById(userId)).thenReturn(true);
    when(roleRepository.existsById(roleId)).thenReturn(false);

    // Act & Assert
    assertThrows(RoleNotFoundException.class, () -> userService.removeUserRole(userId, roleId));
  }


  @Test
    void shouldRemoveUserRole() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(roleRepository.existsById(2L)).thenReturn(true);

        userService.removeUserRole(1L, 2L);

        verify(userRepository).deleteUserRole(1L, 2L);
    }

  @Test
    void shouldThrow_WhenUserNotExistsOnRoleRemove() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.removeUserRole(1L, 2L));
    }

  @Test
    void shouldDeleteUserById() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUserById(1L);
        verify(userRepository).deleteById(1L);
    }

  @Test
    void shouldThrow_WhenDeletingNonexistentUser() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
    }

  @Test
  void shouldDeleteCurrentUser() {
    User user = new User();
    user.setEmail("user@example.com");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("user@example.com");
    when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

    userService.deleteCurrentUser();

    verify(userRepository).delete(user);
  }
}
