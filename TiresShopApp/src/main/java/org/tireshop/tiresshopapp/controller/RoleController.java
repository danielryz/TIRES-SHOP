package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.entity.Role;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Role support")
public class RoleController {
  private final RoleService roleService;

  @Operation(summary = "Get all roles.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Role list returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Role.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<Role>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRoles());
  }

  @Operation(summary = "Get role by ID.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Role returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Role.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Role Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Role> getRoleById(
      @Parameter(description = "Role ID.") @PathVariable Long id) {
    return ResponseEntity.ok(roleService.getRoleById(id));
  }

}
