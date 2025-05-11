package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateAddressRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAddressRequest;
import org.tireshop.tiresshopapp.dto.response.AddressResponse;
import org.tireshop.tiresshopapp.entity.AddressType;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.AddressService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Address", description = "Address Support.")
public class AddressController {

  private final AddressService addressService;

  @Operation(summary = "List of all addresses of the current user.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Address list returned",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AddressResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization", content = @Content())})
  @GetMapping
  public List<AddressResponse> getCurrentUserAllAddresses() {
    return addressService.getCurrentUserAllAddresses();
  }

  @Operation(summary = "Get address by ID.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Address returned",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AddressResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization", content = @Content),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/{id}")
  public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
    AddressResponse address = addressService.getAddressById(id);
    return ResponseEntity.ok(address);
  }

  @Operation(summary = "Get Address by address type.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Addresses returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AddressResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/type")
  public List<AddressResponse> getAddressByType(@RequestParam(required = false) AddressType type) {
    if (type == null) {
      return addressService.getCurrentUserAllAddresses();
    }
    return addressService.getAddressesByType(type);
  }


  @Operation(summary = "Adding address", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Address has been created successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = AddressResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization", content = @Content())})
  @PostMapping
  public ResponseEntity<AddressResponse> addAddress(@RequestBody CreateAddressRequest request) {
    AddressResponse response = addressService.addAddress(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Update address.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Address has been updated successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/{id}")
  public ResponseEntity<String> updateAddress(@PathVariable Long id,
      @RequestBody UpdateAddressRequest request) {
    addressService.updateAddress(id, request);
    return ResponseEntity.ok("Address has been updated successfully.");
  }

  @Operation(summary = "Delete Address", description = "ADMIN")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Address has been deleted successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
    addressService.deleteAddress(id);
    return ResponseEntity.ok("Address has been deleted successfully.");
  }


}
