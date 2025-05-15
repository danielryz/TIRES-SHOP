package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAccessoryRequest;
import org.tireshop.tiresshopapp.dto.response.AccessoryResponse;
import org.tireshop.tiresshopapp.entity.AccessoryType;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.AccessoryService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Accessory", description = "Product Accessory support.")
public class AccessoryController {

  private final AccessoryService accessoryService;

  @Operation(summary = "List of all Accessory.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Accessory list returned.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = AccessoryResponse.class)))})
  @GetMapping("/api/accessory")
  public List<AccessoryResponse> getAllAccessory() {
    return accessoryService.getAllAccessory();
  }

  @Operation(summary = "Returns data of accessory by ID.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AccessoryResponse.class))),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/accessory/{id}")
  public ResponseEntity<AccessoryResponse> getAccessoryById(@PathVariable Long id) {
    AccessoryResponse accessory = accessoryService.getAccessoryById(id);
    return ResponseEntity.ok(accessory);
  }

  @Operation(summary = "List of Accessory with filter and sort.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory list returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AccessoryResponse.class))),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/accessories")
  public ResponseEntity<Page<AccessoryResponse>> getAccessories(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) AccessoryType type,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int sizePerPage,
      @RequestParam(defaultValue = "id,asc") String[] sort) {
    return ResponseEntity
        .ok(accessoryService.getAccessory(type, name, minPrice, maxPrice, page, sizePerPage, sort));
  }

  @Operation(summary = "Adding Accessory Products.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "The accessories has been created.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AccessoryResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @PostMapping("/api/admin/accessory")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<AccessoryResponse>> createNewAccessory(
      @RequestBody List<CreateAccessoryRequest> requests) {
    List<AccessoryResponse> responses = accessoryService.createNewAccessory(requests);
    return ResponseEntity.status(HttpStatus.CREATED).body(responses);
  }

  @Operation(summary = "Accessory edition.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory updated successfully.",
          content = @Content(examples = @ExampleObject(value = "Accessory updated successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/api/admin/accessory/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateAccessory(@PathVariable Long id,
      @RequestBody UpdateAccessoryRequest request) {
    accessoryService.updateAccessory(id, request);
    return ResponseEntity.ok("Accessory updated successfully.");
  }

  @Operation(summary = "Delete Accessory.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory deleted successfully.",
          content = @Content(examples = @ExampleObject(value = "Accessory deleted successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/accessory/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteAccessory(@PathVariable Long id) {
    accessoryService.deleteAccessory(id);
    return ResponseEntity.ok("Accessory deleted successfully.");
  }
}

