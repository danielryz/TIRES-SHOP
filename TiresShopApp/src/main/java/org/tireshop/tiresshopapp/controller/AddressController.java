package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateAddressRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAddressRequest;
import org.tireshop.tiresshopapp.dto.response.AddressResponse;
import org.tireshop.tiresshopapp.entity.AddressType;
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
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Address list returned"),
      @ApiResponse(responseCode = "403", description = "No authorization")})
  @GetMapping
  public List<AddressResponse> getCurrentUserAllAddresses() {
    return addressService.getCurrentUserAllAddresses();
  }

  @Operation(summary = "Get address by ID.", description = "USER.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Address returned"),
      @ApiResponse(responseCode = "403", description = "No authorization"),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Address not found.\"\"}")))})
  @GetMapping("/{id}")
  public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
    AddressResponse address = addressService.getAddressById(id);
    return ResponseEntity.ok(address);
  }

  @Operation(summary = "Get Address by address type.", description = "USER.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Addresses returned."),
      @ApiResponse(responseCode = "403", description = "No authorization"),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Address not found.\"\"}")))})
  @GetMapping("/type")
  public List<AddressResponse> getAddressByType(@RequestParam(required = false) AddressType type) {
    if (type == null) {
      return addressService.getCurrentUserAllAddresses();
    }
    return addressService.getAddressesByType(type);
  }


  @Operation(summary = "Adding address", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Address has been created successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization")})
  @PostMapping
  public ResponseEntity<String> addAddress(@RequestBody CreateAddressRequest request) {
    addressService.addAddress(request);
    return ResponseEntity.ok("Address has been created successfully.");
  }

  @Operation(summary = "Update address.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Address has been updated successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization"),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Address not found.\"\"}")))})
  @PatchMapping("/{id}")
  public ResponseEntity<String> updateAddress(@PathVariable Long id,
      @RequestBody UpdateAddressRequest request) {
    addressService.updateAddress(id, request);
    return ResponseEntity.ok("Address has been updated successfully.");
  }

  @Operation(summary = "Delete Address", description = "ADMIN")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Address has been deleted successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization"),
      @ApiResponse(responseCode = "404", description = "Address Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Address not found.\"\"}")))})
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
    addressService.deleteAddress(id);
    return ResponseEntity.ok("Address has been deleted successfully.");
  }


}
