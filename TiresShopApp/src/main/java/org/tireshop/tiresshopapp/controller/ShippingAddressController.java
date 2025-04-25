package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateAddressRequest;
import org.tireshop.tiresshopapp.dto.request.create.CreateShippingAddressRequest;
import org.tireshop.tiresshopapp.service.ShippingAddressService;

@RestController
@RequestMapping("/api/shippingAddress/myorder")
@RequiredArgsConstructor
@Tag(name = "Shipping Address", description = "Dodawanie i edycja adresu dostawy do zamówienia.")
public class ShippingAddressController {
  private final ShippingAddressService shippingAddressService;

  @Operation(summary = "Dodawanie Adresu", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Dodano Adres"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PostMapping
  public ResponseEntity<String> addShippingAddress(
      @RequestBody CreateShippingAddressRequest request) {
    shippingAddressService.addShippingAddressToMyOrder(request);
    return ResponseEntity.ok("Dodano Adres dostawy do zamówienia");
  }

  @Operation(summary = "Edycja Adresu", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Adres zaktualizowany"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PreAuthorize("hasRole('USER')")
  @PatchMapping
  ResponseEntity<String> updateShippingAddress(@RequestBody CreateShippingAddressRequest request) {
    shippingAddressService.updateShippingAddress(request);
    return ResponseEntity.ok("Zaktualizowano adres dostawy");
  }

  @Operation(summary = "Usuwanie Adresu", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Adres usunięty"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PreAuthorize("hasRole('USER')")
  @DeleteMapping
  ResponseEntity<String> deleteShippingAddress() {
    shippingAddressService.deleteShippingAddressFromMyOrder();
    return ResponseEntity.ok("Usunięto adres dostawy");
  }

}
