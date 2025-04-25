package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.update.UpdateRoleRequest;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Obsługa encji rola, pobieranie, dodawnanie, usuwanie, update.")
public class RoleController {
  private final RoleService roleService;

  @Operation(summary = "Pobiera wszystkie role", description = "Tylko dla administratorów")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista ról zwrócona pomyślnie",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Role.class))),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji",
          content = @Content(examples = @ExampleObject(value = " ")))})
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Role>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRoles());
  }

  @Operation(summary = "Pobiera Role po ID", description = "Tylko dla administratorów")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rola znaleziona",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Role.class))),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji",
          content = @Content(examples = @ExampleObject(value = " "))),
      @ApiResponse(responseCode = "404", description = "Rola nie istnieje",
          content = @Content(examples = @ExampleObject()))})
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
    return ResponseEntity.ok(roleService.getRoleById(id));
  }

  @Operation(summary = "Dodaje nową rolę", description = "Tylko dla administratorów")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Rola dodana"),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji",
          content = @Content(examples = @ExampleObject(value = " ")))})
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Role> createRole(@RequestBody String name) {
    return ResponseEntity.ok(roleService.createRole(name));
  }

  @Operation(summary = "Update roli", description = "Tylko dla administratorów")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Rola zmieniona"),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji",
          content = @Content(examples = @ExampleObject(value = " ")))})
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Role> updateRole(@PathVariable Long id,
      @RequestBody UpdateRoleRequest request) {
    Role updated = roleService.updateRole(id, request.name());
    return ResponseEntity.ok(updated);
  }


  @Operation(summary = "Usunięcie roli", description = "Tylko dla administratorów")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Rola usunięta"),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji",
          content = @Content(examples = @ExampleObject(value = " ")))})
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteRole(@PathVariable Long id) {
    roleService.deleteRole(id);
    return ResponseEntity.ok("Rola o id" + id + " usunięta");
  }


}
