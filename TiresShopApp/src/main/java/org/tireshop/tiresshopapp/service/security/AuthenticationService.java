package org.tireshop.tiresshopapp.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.AuthResponse;
import org.tireshop.tiresshopapp.dto.LoginRequest;
import org.tireshop.tiresshopapp.dto.RegisterRequest;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.entity.User;
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

    public AuthResponse register(RegisterRequest request){
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rola USER nie istnieje"));
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email już istnieje");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Nazwa użytkownika już istnieje");
        }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRoles(Collections.singleton(userRole));
    user.setEnabled(true);

    userRepository.save(user);

    String token = jwtService.generateToken(mapToUserDetails(user));
    return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Nieprawidłowy email lub hasło"));

        String token = jwtService.generateToken(mapToUserDetails(user));
        return new AuthResponse(token);
    }

    private UserDetails mapToUserDetails(User user){
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()
        );
    }
}
