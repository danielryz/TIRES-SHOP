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
import org.tireshop.tiresshopapp.dto.request.UpdateUserRequest;
import org.tireshop.tiresshopapp.dto.request.UpdateUserRolesRequest;
import org.tireshop.tiresshopapp.dto.response.UserResponse;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Obsługa użytkownika, pobieranie danych o użytkowniku, update użytkownika, usuwanie konta")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Pobiera wszystkich użytkowników", description = "Tylko dla administratorów")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista użytkowników zwrócona pomyślnie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))), @ApiResponse(responseCode = "400", description = "Brak uprawnień", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))), @ApiResponse(responseCode = "403", description = "Brak Autoryzacji", content = @Content(examples = @ExampleObject(value = " ")))})
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Pobiera użytkownika po ID", description = "Tylko dla administratorów")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Użytkownik znaleziony", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))), @ApiResponse(responseCode = "400", description = "Brak uprawnień", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))), @ApiResponse(responseCode = "403", description = "Brak Autoryzacji", content = @Content(examples = @ExampleObject(value = " "))), @ApiResponse(responseCode = "404", description = "Użytkownik nie istnieje", content = @Content(examples = @ExampleObject(value = """
            can't parse JSON.  Raw result:
            
            Użytkownik o ID: 7 nie istnieje""")))})
    @GetMapping("/{id:[0-9]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(userService.toUserResponse(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Szczegóły zalogowanego użytkownika")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Zwrócono dane użytkownika", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))), @ApiResponse(responseCode = "403", description = "Brak Autoryzacji", content = @Content(examples = @ExampleObject(value = " ")))})
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(userService.toUserResponse(user));

    }

    @Operation(summary = "Aktualizacja danych konta (username, password")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Dane zostały zmienione", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "401", description = "Brak autoryzacji", content = @Content(examples = @ExampleObject()))})
    @PatchMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<User> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @Operation(summary = "Aktualizacja ról użytkownika (Admin")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Rola została zmieniona", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "404", description = "Użytkownik lub rola nie istnieje", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "403", description = "Brak uprawnień (ADMIN)", content = @Content(examples = @ExampleObject()))})
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUsersRoles(@PathVariable Long id, @RequestBody UpdateUserRolesRequest request) {
        return ResponseEntity.ok(userService.updateUserRoles(id, request.roles()));
    }

    @Operation(summary = "Usuwa konkretną rolę użytkownika (ADMIN)", description = "Przekaż nazwe roli jako parametr zapytania: DELETE /api/users/5/role?role=ROLE_ADMIN")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Rola usunięta", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "404", description = "Użytkownik lub rola nie istnieje", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "403", description = "Brak uprawnień", content = @Content(examples = @ExampleObject()))})
    @DeleteMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserRole(@PathVariable Long id, @RequestParam String role) {
        userService.removeUserRole(id, role);
        return ResponseEntity.ok("Rola " + role + " usunięta u użytkownika: " + id);
    }

    @Operation(summary = "Usuwa konkretnego użytkownika (ADMIN)", description = "Usunięcie jednego użytkownika o id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Użytkownik usunięty", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "404", description = "Użytkownik nie istnieje", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "403", description = "Brak uprawnień", content = @Content(examples = @ExampleObject()))})
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Użytkownik o ID " + id + " został usunięty");

    }

    @Operation(summary = "Usunięcie konta", description = "Usunięcie swojego konta")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Konto zostało usunięte", content = @Content(examples = @ExampleObject())), @ApiResponse(responseCode = "401", description = "Brak autoryzacji", content = @Content(examples = @ExampleObject())),})
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCurrentUser() {
        userService.deleteCurrentUser();
        return ResponseEntity.ok("Twoje konto zostało usunięte");
    }

}
