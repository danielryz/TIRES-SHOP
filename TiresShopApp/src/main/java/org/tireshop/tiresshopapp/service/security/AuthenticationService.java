package org.tireshop.tiresshopapp.service.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.response.AuthResponse;
import org.tireshop.tiresshopapp.dto.request.auth.LoginRequest;
import org.tireshop.tiresshopapp.dto.request.auth.RegisterRequest;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.RoleNotFoundException;
import org.tireshop.tiresshopapp.exception.UserAlreadyExistException;
import org.tireshop.tiresshopapp.repository.RoleRepository;
import org.tireshop.tiresshopapp.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    String role = "ROLE_USER";
    Role userRole =
        roleRepository.findByName(role).orElseThrow(() -> new RoleNotFoundException(role));
    if (userRepository.existsByEmail(request.email())) {
      throw UserAlreadyExistException.byEmail(request.email());
    }

    if (userRepository.existsByUsername(request.username())) {
      throw UserAlreadyExistException.byUsername(request.username());
    }

    User user = new User();
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setRoles(Collections.singleton(userRole));
    user.setEnabled(true);

    userRepository.save(user);

    String token = jwtService.generateToken(mapToUserDetails(user));
    return new AuthResponse(token);
  }

  public AuthResponse login(LoginRequest request) {
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    User user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

    String token = jwtService.generateToken(mapToUserDetails(user));
    return new AuthResponse(token);
  }

  private UserDetails mapToUserDetails(User user) {
    return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
        .password(user.getPassword())
        .authorities(user.getRoles().stream().map(Role::getName).toArray(String[]::new)).build();
  }
}
