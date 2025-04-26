package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.response.AccessoryResponse;
import org.tireshop.tiresshopapp.service.AccessoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Accessory", description = "Product Accessory support.")
public class AccessoryController {

  private final AccessoryService accessoryService;

  @Operation(summary = "List of all Accessory.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Accessory list returned.")})
  @GetMapping("/api/accessory")
  public List<AccessoryResponse> getAllAccessory() {
    return accessoryService.getAllAccessory();
  }

  @Operation(summary = "Returns data of accessory by ID.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Accessory data returned."),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Accessory with id 1 not found.\"\"}")))})
  @GetMapping("/api/accessory/{id}")
  public ResponseEntity<AccessoryResponse> getAccessoryById(@PathVariable Long id) {
    AccessoryResponse accessory = accessoryService.getAccessoryById(id);
    return ResponseEntity.ok(accessory);
  }

  @Operation(summary = "List of Accessory with type filter.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Accessory list returned."),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Accessory with filter ACCESSORY_TYPE not found.\"\"}")))})
  @GetMapping("/api/accessory/type")
  public List<AccessoryResponse> getAccessoryByAccessoryType(
      @RequestParam(required = false) String accessoryType) {
    if (accessoryType == null) {
      return accessoryService.getAllAccessory();
    }
    return accessoryService.getAccessoryByAccessoryType(accessoryType);
  }

  @Operation(summary = "Adding Accessory Product.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The accessory has been created."),
      @ApiResponse(responseCode = "403", description = "No authorization.")})
  @PostMapping("/api/admin/accessory")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<AccessoryResponse> createNewAccessory(
      @RequestBody CreateAccessoryRequest request) {
    AccessoryResponse accessory = accessoryService.createNewAccessory(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(accessory);
  }

  @Operation(summary = "Accessory edition.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory updated successfully.",
          content = @Content(examples = @ExampleObject(value = "Accessory updated successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization."),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Accessory with id 1 not found.\"\"}")))})
  @PatchMapping("/api/admin/accessory/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateAccessory(@PathVariable Long id,
      @RequestBody CreateAccessoryRequest request) {
    accessoryService.updateAccessory(id, request);
    return ResponseEntity.status(HttpStatus.OK).body("Accessory updated successfully.");
  }

  @Operation(summary = "Delete Accessory.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory deleted successfully.",
          content = @Content(examples = @ExampleObject(value = "Accessory deleted successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization."),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Accessory with id 1 not found.\"\"}")))})
  @DeleteMapping("/api/admin/accessory/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteAccessory(@PathVariable Long id) {
    accessoryService.deleteAccessory(id);
    return ResponseEntity.ok("Accessory deleted successfully.");
  }


}

