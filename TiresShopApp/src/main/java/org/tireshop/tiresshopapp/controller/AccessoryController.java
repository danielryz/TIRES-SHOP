package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.tireshop.tiresshopapp.dto.response.AccessoryFilterResponse;
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

  @Operation(summary = "Returns data of accessory by ID.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AccessoryResponse.class))),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/accessory/{id}")
  public ResponseEntity<AccessoryResponse> getAccessoryById(
      @Parameter(description = "Accessory ID.") @PathVariable Long id) {
    AccessoryResponse accessory = accessoryService.getAccessoryById(id);
    return ResponseEntity.ok(accessory);
  }

  @Operation(summary = "List of Accessory with filter and sort.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "Accessory list returned.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = AccessoryResponse.class)))
  @GetMapping("/api/accessories")
  public ResponseEntity<Page<AccessoryResponse>> getAccessories(
      @Parameter(description = "Name of accessory") @RequestParam(required = false) String name,
      @Parameter(description = "Accessory Type ENUM")
      @RequestParam(required = false) List<AccessoryType> accessoryType,
      @Parameter(description = "Min value of price for accessory.")
      @RequestParam(required = false) BigDecimal minPrice,
      @Parameter(description = "Max value of price for accessory.")
      @RequestParam(required = false) BigDecimal maxPrice,
      @Parameter(description = "Page number for pagination.")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Size of page for pagination.")
      @RequestParam(defaultValue = "10") int sizePerPage,
      @Parameter(description = "Sort by field and direction.")
      @RequestParam(defaultValue = "id,asc") String sort) {
    return ResponseEntity.ok(accessoryService.getAccessory(accessoryType, name, minPrice, maxPrice,
        page, sizePerPage, sort));
  }

  @Operation(summary = "Adding Accessory Products.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "The accessories has been created.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AccessoryResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
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
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/api/admin/accessory/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateAccessory(
      @Parameter(description = "Accessory ID.") @PathVariable Long id,
      @RequestBody UpdateAccessoryRequest request) {
    accessoryService.updateAccessory(id, request);
    return ResponseEntity.ok("Accessory updated successfully.");
  }

  @Operation(summary = "Delete Accessory.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Accessory deleted successfully.",
          content = @Content(examples = @ExampleObject(value = "Accessory deleted successfully."))),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Accessory Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/accessory/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteAccessory(
      @Parameter(description = "Accessory ID.") @PathVariable Long id) {
    accessoryService.deleteAccessory(id);
    return ResponseEntity.ok("Accessory deleted successfully.");
  }

  @Operation(summary = "Get Filters for Accessory.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "Filters returned successfully.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = AccessoryFilterResponse.class)))
  @GetMapping("api/accessory/filters")
  public AccessoryFilterResponse getFilterOptions() {
    return accessoryService.getAvailableFilterOptions();
  }
}

