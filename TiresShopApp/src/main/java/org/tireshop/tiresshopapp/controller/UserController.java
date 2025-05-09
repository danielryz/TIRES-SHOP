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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.update.UpdateUserPasswordRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateUserRequest;
import org.tireshop.tiresshopapp.dto.response.UserResponse;
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "User",
    description = "User support, downloading user data, user update, account deletion.")
public class UserController {

  private final UserService userService;

  @Operation(summary = "Gets a list of all users.", description = "ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User list returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Access Denied\"}"))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject()))})
  @GetMapping("/api/admin/users")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @Operation(summary = "Gets User by ID.", description = "ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              examples = @ExampleObject(value = "{\"error\": \"Access Denied.\"}"))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "User Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"User with id 1 not found.\"\"}")))})
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
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject()))})
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
              examples = @ExampleObject(value = "{\"error\": \"Invalid current password. \"}"))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject()))})
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
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400", description = "Invalid data.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"Username must be between 3 and 30 characters long\"}"))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject()))})
  @PatchMapping("/api/users/me/update")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {
    userService.updateCurrentUser(request);
    return ResponseEntity.ok("The data has been changed successfully.");
  }

  @Operation(summary = "Deleted your account details.", description = "USER.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "The data has been deleted successfully.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject()))})
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
          content = @Content(examples = @ExampleObject(value = "{\"error\": \"Access Denied.\"}"))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "User or Role Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"User with id 1 not found.\"\"}")))})
  @PostMapping("/api/admin/users/{id}/{roleId}/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> addRoleToUser(@PathVariable Long id, @PathVariable Long roleId) {
    userService.addRoleToUser(id, roleId);
    return ResponseEntity.ok("The Role has been added successfully.");
  }

  @Operation(summary = "Delete the user role.", description = "ADMIN")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rola deleted.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400", description = "User or Role Not Found.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "403", description = "Access Denied..",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "User or Role Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"User with id 1 not found.\"\"}")))})
  @DeleteMapping("/api/admin/users/{id}/{roleId}/role")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> removeUserRole(@PathVariable Long id, @PathVariable Long roleId) {
    userService.removeUserRole(id, roleId);
    return ResponseEntity.ok("Role has been deleted successfully.");
  }

  @Operation(summary = "Delete user.", description = "ADMIN.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User has been deleted successfully.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "400", description = "Access Denied.",
          content = @Content(examples = @ExampleObject(value = "{\"error\": \"Access Denied.\"}"))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "User Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"User with id 1 not found.\"\"}")))})
  @DeleteMapping("/api/admin/users/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok("User has been deleted successfully.");

  }

  @Operation(summary = "Delete your account.", description = "USER.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Your account has been deleted successfully.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject())),})
  @DeleteMapping("/api/users/me/delete")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> deleteCurrentUser() {
    userService.deleteCurrentUser();
    return ResponseEntity.ok("Your account has been deleted successfully.");
  }

}
