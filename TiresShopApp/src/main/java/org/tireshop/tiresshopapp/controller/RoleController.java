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
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Pobieranie Roli.")
public class RoleController {
  private final RoleService roleService;

  @Operation(summary = "Pobiera wszystkie role.", description = "Tylko dla administratorów.")
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

  @Operation(summary = "Pobiera Role po ID.", description = "Tylko dla administratorów.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rola znaleziona.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Role.class))),
      @ApiResponse(responseCode = "400", description = "Brak uprawnień albo rola nie istnieje.",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Acces Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "Brak Autoryzacji",
          content = @Content(examples = @ExampleObject(value = " ")))})
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
    return ResponseEntity.ok(roleService.getRoleById(id));
  }

}
