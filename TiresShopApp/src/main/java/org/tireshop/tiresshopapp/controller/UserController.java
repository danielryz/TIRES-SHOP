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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.update.UpdateUserPasswordRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateUserRequest;
import org.tireshop.tiresshopapp.dto.response.UserResponse;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User",
    description = "User support, downloading user data, user update, account deletion.")
public class UserController {

  private final UserService userService;

  @Operation(summary = "Gets a list of users with filters and sort.", description = "ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Users list returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @GetMapping("/api/admin/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<UserResponse>> getUsers(
          @RequestParam(required = false) String email,
          @RequestParam(required = false) String username,
          @RequestParam(required = false) String firstName,
          @RequestParam(required = false) String lastName,
          @RequestParam(required = false) String phoneNumber,
          @RequestParam(required = false) String role,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int sizePerPage,
          @RequestParam(defaultValue = "id,asc") String[] sort
  ) {
    return ResponseEntity.ok(userService.getUsers(email, username, firstName, lastName, role, phoneNumber, page, sizePerPage, sort));
  }

  @Operation(summary = "Gets User by ID.", description = "ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "User Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/admin/users/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    UserResponse user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @Operation(summary = "Details of the logged in user.", description = "USER.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @GetMapping("/api/users/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserResponse> getCurrentUser() {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(userService.toMapResponse(user));
  }


  @Operation(summary = "Updating your password.", description = "USER.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Password has been updated successfully.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "403", description = "Invalid data.",
          content = @Content(
              examples = @ExampleObject(value = "{\"error\": \"Invalid current password. \"}")))})
  @PatchMapping("/api/users/me/change-password")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> updateCurrentUserPassword(
      @Valid @RequestBody UpdateUserPasswordRequest request) {
    userService.updateCurrentUserPassword(request);
    return ResponseEntity.ok("Password has been updated successfully.");
  }

  @Operation(summary = "Updating your account details.", description = "USER.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The data has been changed successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "400", description = "Invalid data.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @PatchMapping("/api/users/me/update")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
    userService.updateCurrentUser(request);
    return ResponseEntity.ok("The data has been changed successfully.");
  }

  @Operation(summary = "Deleted your account details.", description = "USER.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The data has been deleted successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @DeleteMapping("/api/users/me/delete-user-details")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> deleteCurrentUserData() {
    userService.deleteCurrentUserData();
    return ResponseEntity.ok("The data has been deleted successfully.");
  }

  @Operation(summary = "\n" + "Adding a user role.", description = "ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The Role has been added successfully.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "User or Role Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/api/admin/users/{id}/role/{roleId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> addRoleToUser(@PathVariable Long id, @PathVariable Long roleId) {
    userService.addRoleToUser(id, roleId);
    return ResponseEntity.ok("The Role has been added successfully.");
  }

  @Operation(summary = "Delete the user role.", description = "ADMIN")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rola deleted.", content = @Content()),
      @ApiResponse(responseCode = "400", description = "User or Role Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "User or Role Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/users/{id}/role/{roleId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> removeUserRole(@PathVariable Long id, @PathVariable Long roleId) {
    userService.removeUserRole(id, roleId);
    return ResponseEntity.ok("Role has been deleted successfully.");
  }

  @Operation(summary = "Delete user.", description = "ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User has been deleted successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "401", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "User Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/users/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok("User has been deleted successfully.");

  }

  @Operation(summary = "Delete your account.", description = "USER.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Your account has been deleted successfully.", content = @Content()),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @DeleteMapping("/api/users/me/delete")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> deleteCurrentUser() {
    userService.deleteCurrentUser();
    return ResponseEntity.ok("Your account has been deleted successfully.");
  }

}
