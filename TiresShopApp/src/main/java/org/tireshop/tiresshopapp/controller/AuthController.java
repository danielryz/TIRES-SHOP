package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.tireshop.tiresshopapp.service.security.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Logowanie i rejsetracja.")
public class AuthController {
  private final AuthenticationService authService;

  @Operation(summary = "Rejestracja nowego użytkownika.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rejestracja zakończona sukcesem.",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "400",
          description = "Błąd walidacji lub użytkownik już istnieje.",
          content = @Content(mediaType = "aplication/json", examples = @ExampleObject("""
              {
                "error": "Email już istnieje"
              }""")))})

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @Operation(summary = "Logowanie i generowanie tokena JWT.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Zalogowano pomyślnie.",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))),
      @ApiResponse(responseCode = "401", description = "Nieprawidłowy email lub hasło.",
          content = @Content(mediaType = "aplication/json", examples = @ExampleObject(value = """
              {
                "error": "Nieprawidłowy email lub hasło"
              }""")))})
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }
}
