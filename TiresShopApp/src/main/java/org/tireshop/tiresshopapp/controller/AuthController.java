package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tireshop.tiresshopapp.dto.response.AuthResponse;
import org.tireshop.tiresshopapp.dto.request.auth.LoginRequest;
import org.tireshop.tiresshopapp.dto.request.auth.RegisterRequest;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.security.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and registration support.")
public class AuthController {
  private final AuthenticationService authService;

  @Operation(summary = "New user registration", description = "PUBLIC.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Registration completed successfully.",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400", description = "Validation error.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "User already exists.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),})

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @Operation(summary = "Login and generate JWT token.", description = "PUBLIC")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "You have logged in successfully.",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid email or password.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),})
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}
