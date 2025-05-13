package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
import org.tireshop.tiresshopapp.dto.request.create.CreateShippingAddressRequest;
import org.tireshop.tiresshopapp.dto.response.ShippingAddressResponse;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.ShippingAddressService;

@RestController
@RequestMapping("/api/shippingAddress/my_order")
@RequiredArgsConstructor
@Tag(name = "Shipping Address", description = "Shipping Address support.")
public class ShippingAddressController {
  private final ShippingAddressService shippingAddressService;

  @Operation(summary = "Adding shipping address.", description = "PUBLIC")
  @ApiResponses({
      @ApiResponse(responseCode = "201",
          description = "Shipping address has been added successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ShippingAddressResponse.class))),
      @ApiResponse(responseCode = "409",
          description = "Address already in use in order. or No active order found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/{orderId}")
  public ResponseEntity<ShippingAddressResponse> addShippingAddress(@PathVariable Long orderId,
      @Valid @RequestBody CreateShippingAddressRequest request) {
    ShippingAddressResponse response =
        shippingAddressService.addShippingAddressToMyOrder(orderId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Update shipping address.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
          description = "Shipping address has been updated successfully.", content = @Content),
      @ApiResponse(responseCode = "404",
          description = "No shipping address assigned to this order. or No active order found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PreAuthorize("hasRole('USER')")
  @PatchMapping
  ResponseEntity<String> updateShippingAddress(
      @Valid @RequestBody CreateShippingAddressRequest request,
      @RequestHeader(value = "X-Client-Id", required = false) @Parameter(
          description = "Unique client identifier (required if not authenticated)") String clientId) {
    shippingAddressService.updateShippingAddress(request, clientId);
    return ResponseEntity.ok("Shipping address has been updated successfully.");
  }

  @Operation(summary = "Delete Shipping address.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
          description = "Shipping address has been deleted successfully.", content = @Content),
      @ApiResponse(responseCode = "404",
          description = "No shipping address assigned to this order. or No active order found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PreAuthorize("hasRole('USER')")
  @DeleteMapping
  ResponseEntity<String> deleteShippingAddress(@RequestHeader(value = "X-Client-Id",
      required = false)
  @Parameter(
      description = "Unique client identifier (required if not authenticated)") String clientId) {
    shippingAddressService.deleteShippingAddressFromMyOrder(clientId);
    return ResponseEntity.ok("Shipping address has been deleted successfully.");
  }

}
