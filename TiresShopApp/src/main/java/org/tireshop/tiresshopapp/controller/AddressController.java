package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.tireshop.tiresshopapp.dto.request.create.CreateAddressRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateAddressRequest;
import org.tireshop.tiresshopapp.dto.response.AddressResponse;
import org.tireshop.tiresshopapp.entity.AddressType;
import org.tireshop.tiresshopapp.exception.ResourceNotFoundException;
import org.tireshop.tiresshopapp.service.AddressService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Address", description = "Opbsługa Adresów użytkownika zalogowanego")
public class AddressController {

  private final AddressService addressService;

  @Operation(summary = "Lista wszystkich Adresów", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono listę Adresów"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono Adresów")})
  @GetMapping
  public List<AddressResponse> getCurrentUserAllAddresses() {
    return addressService.getCurrentUserAllAddresses();
  }

  @Operation(summary = "Adres po id", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono Adresy"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono Adresu")})
  @GetMapping("/{id}")
  public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
    AddressResponse address = addressService.getAddressById(id);
    return ResponseEntity.ok(address);
  }

  @Operation(summary = "Adres po sezonie", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono Adres"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono Adresu")})
  @GetMapping("/type")
  public List<AddressResponse> getAddressByType(@RequestParam(required = false) AddressType type) {
    if (type == null) {
      return addressService.getCurrentUserAllAddresses();
    }
    List<AddressResponse> address = addressService.getAddressesByType(type);
    if (address.isEmpty()) {
      throw new ResourceNotFoundException("Brak adresów dla typu " + type);
    }
    return address;
  }


  @Operation(summary = "Dodawanie Adres", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Dodano Adres"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PostMapping
  public ResponseEntity<AddressResponse> addAddress(@RequestBody CreateAddressRequest request) {
    AddressResponse address = addressService.addAddress(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(address);
  }

  @Operation(summary = "Edycja Adresy", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Edytowano Adres"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PatchMapping("/{id}")
  public ResponseEntity<?> updateAddress(@PathVariable Long id,
      @RequestBody UpdateAddressRequest request) {
    try {
      AddressResponse address = addressService.updateAddress(id, request);
      return ResponseEntity.ok(address);
    } catch (ResponseStatusException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @Operation(summary = "Usuwanie Adresu", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Usunięto Adres"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
    addressService.deleteAddress(id);
    return ResponseEntity.ok("Usunięto Adres");
  }


}
