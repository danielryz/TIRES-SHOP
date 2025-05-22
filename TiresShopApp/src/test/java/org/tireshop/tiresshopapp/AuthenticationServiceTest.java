package org.tireshop.tiresshopapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.tireshop.tiresshopapp.dto.request.auth.LoginRequest;
import org.tireshop.tiresshopapp.dto.request.auth.RegisterRequest;
import org.tireshop.tiresshopapp.dto.response.AuthResponse;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.RoleNotFoundException;
import org.tireshop.tiresshopapp.exception.UserAlreadyExistException;
import org.tireshop.tiresshopapp.repository.RoleRepository;
import org.tireshop.tiresshopapp.repository.UserRepository;
import org.tireshop.tiresshopapp.service.security.AuthenticationService;
import org.tireshop.tiresshopapp.service.security.JwtService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleRepository roleRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtService jwtService;
  @Mock
  private AuthenticationManager authenticationManager;

  @InjectMocks
  private AuthenticationService authenticationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldRegisterUserSuccessfully() {
    RegisterRequest request = new RegisterRequest("newuser", "new@example.com", "password123");
    Role role = new Role();
    role.setName("ROLE_USER");

    when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
    when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
    when(userRepository.existsByUsername("newuser")).thenReturn(false);
    when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
    when(jwtService.generateToken(any(UserDetails.class))).thenReturn("mocked-jwt");

    AuthResponse response = authenticationService.register(request);

    assertNotNull(response);
    assertEquals("mocked-jwt", response.token());

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("newuser", savedUser.getUsername());
    assertEquals("new@example.com", savedUser.getEmail());
    assertEquals("encodedPassword", savedUser.getPassword());
    assertTrue(savedUser.isEnabled());
    assertTrue(savedUser.getRoles().contains(role));
  }

  @Test
  void shouldThrowWhenEmailAlreadyExists() {
    RegisterRequest request = new RegisterRequest("user", "exists@example.com", "password");
    Role role = new Role();
    role.setName("ROLE_USER");

    when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
    when(userRepository.existsByEmail("exists@example.com")).thenReturn(true);

    assertThrows(UserAlreadyExistException.class, () -> authenticationService.register(request));
  }

  @Test
  void shouldThrowWhenUsernameAlreadyExists() {
    RegisterRequest request = new RegisterRequest("existinguser", "new@example.com", "password");
    Role role = new Role();
    role.setName("ROLE_USER");

    when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
    when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
    when(userRepository.existsByUsername("existinguser")).thenReturn(true);

    assertThrows(UserAlreadyExistException.class, () -> authenticationService.register(request));
  }


  @Test
  void shouldThrowWhenRoleNotFound() {
    RegisterRequest request = new RegisterRequest("user", "user@example.com", "password");

    when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

    assertThrows(RoleNotFoundException.class, () -> authenticationService.register(request));
  }

  @Test
  void shouldLoginSuccessfully() {
    LoginRequest request = new LoginRequest("user@example.com", "password");
    User user = new User();
    user.setEmail("user@example.com");
    user.setPassword("encoded");

    Role role = new Role();
    role.setName("ROLE_USER");
    user.setRoles(Collections.singleton(role));

    when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
    when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

    authenticationService.login(request);

    verify(authenticationManager)
        .authenticate(new UsernamePasswordAuthenticationToken("user@example.com", "password"));

    AuthResponse response = authenticationService.login(request);
    assertNotNull(response);
    assertEquals("jwt-token", response.token());
  }

  @Test
  void shouldThrowWhenLoginFailsDueToInvalidEmail() {
    LoginRequest request = new LoginRequest("unknown@example.com", "password");

    when(authenticationManager.authenticate(any())).thenReturn(null);
    when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

    assertThrows(BadCredentialsException.class, () -> authenticationService.login(request));
  }
}
