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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.update.UpdateUserRequest;
import org.tireshop.tiresshopapp.dto.response.UserResponse;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.repository.RoleRepository;
import org.tireshop.tiresshopapp.service.RoleService;
import org.tireshop.tiresshopapp.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User",
    description = "Obsługa użytkownika, pobieranie danych o użytkowniku, update użytkownika, usuwanie konta.")
public class UserController {

  private final UserService userService;
  private final RoleRepository roleRepository;
  private final RoleService roleService;

  @Operation(summary = "Pobiera wszystkich użytkowników (ADMIN).",
      description = "Zwraca listę użytkowników.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista użytkowników zwrócona pomyślnie.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień.",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji.",
          content = @Content(examples = @ExampleObject()))})
  @GetMapping("/api/admin/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Operation(summary = "Pobiera użytkownika po ID (ADMIN).",
      description = "Zwraca użytkownika o id.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Użytkownik znaleziony.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "Użytkownik nie istnieje",
          content = @Content(examples = @ExampleObject()))})
  @GetMapping("/api/admin/users/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    try {
      User user = userService.getUserById(id);
      return ResponseEntity.ok(userService.toMapResponse(user));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @Operation(summary = "Szczegóły zalogowanego użytkownika.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Zwrócono dane użytkownika.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji.",
          content = @Content(examples = @ExampleObject()))})
  @GetMapping("/api/users/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserResponse> getCurrentUser() {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(userService.toMapResponse(user));

  }


  @Operation(summary = "Aktualizacja danych konta.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Dane zostały zmienione.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"Nazwa użytkownika musi mieć od 3 do 30 znaków\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji.",
          content = @Content(examples = @ExampleObject()))})
  @PatchMapping("/api/users/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
    userService.updateCurrentUser(request);
    return ResponseEntity.ok("Dane zostały zmienione");
  }

  @Operation(summary = "Dodanie roli użytkownika.", description = "Tylko dla administratorów.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rola została dodana.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400",
          description = "Brak uprawnień, Użytkownik lub rola nie istnieje.",
          content = @Content(examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak uprawnień.",
          content = @Content(examples = @ExampleObject()))

  })
  @PostMapping("/api/admin/users/{id}/{roleId}/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> addRoleToUser(@PathVariable Long id, @PathVariable Long roleId) {
    userService.addRoleToUser(id, roleId);
    return ResponseEntity.ok("Rola została dodana.");
  }

  @Operation(summary = "Usuwa konkretną rolę użytkownika (ADMIN).",
      description = "Przekaż nazwe roli jako parametr zapytania: DELETE /api/users/5/role?role=ROLE_ADMIN")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rola usunięta.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400", description = "Użytkownik lub rola nie istnieje.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "403", description = "Brak uprawnień.",
          content = @Content(examples = @ExampleObject()))})
  @DeleteMapping("/api/admin/users/{id}/{roleId}/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> removeUserRole(@PathVariable Long id, @PathVariable Long roleId) {
    userService.removeUserRole(id, roleId);
    return ResponseEntity.ok("Rola o id:" + roleId + " usunięta u użytkownika: " + id);
  }

  @Operation(summary = "Usuwa konto konkretnego użytkownika (ADMIN).",
      description = "Usunięcie jednego użytkownika o id.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Użytkownik usunięty.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400",
          description = "Brak uprawnień lub Użytkownik nie istnieje.",
          content = @Content(examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak uprawnień.",
          content = @Content(examples = @ExampleObject()))})
  @DeleteMapping("/api/admin/users/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok("Użytkownik o ID " + id + " został usunięty.");

  }

  @Operation(summary = "Usunięcie konta.", description = "Usunięcie swojego konta.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Twoje konto zostało usunięte.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji.",
          content = @Content(examples = @ExampleObject())),})
  @DeleteMapping("/api/users/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> deleteCurrentUser() {
    userService.deleteCurrentUser();
    return ResponseEntity.ok("Twoje konto zostało usunięte.");
  }

}
