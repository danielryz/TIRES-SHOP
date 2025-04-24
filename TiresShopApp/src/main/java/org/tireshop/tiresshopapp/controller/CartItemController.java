package org.tireshop.tiresshopapp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.AddToCartRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateCartItemRequest;
import org.tireshop.tiresshopapp.dto.response.CartItemResponse;
import org.tireshop.tiresshopapp.dto.response.CartSummaryResponse;
import org.tireshop.tiresshopapp.service.CartItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Obsługa Koszyka")
public class CartItemController {
  private final CartItemService cartItemService;

  @Operation(summary = "Pobiera koszyk dla uzytkownika", description = "Zwraca produkty koszyka")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono koszyk"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono koszyka")})
  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<CartItemResponse>> getCartForCurrentUser() {
    return ResponseEntity.ok(cartItemService.getCartForCurrentUser());
  }

  @Operation(summary = "Szczegóły produktu", description = "Zwraca dane jednego produktu po ID")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono dane produktu"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu")})
  @GetMapping("/summary")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<CartSummaryResponse> getCartSummary() {
    return ResponseEntity.ok(cartItemService.getCartSummary());
  }

  @Operation(summary = "Dodanie do koszyka", description = "Dodaje produkty do koszyka")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Dodano produkt"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu")})
  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<CartItemResponse> addCartItem(@RequestBody AddToCartRequest request) {
    return ResponseEntity.ok(cartItemService.addCartItem(request));
  }

  @Operation(summary = "Aktualizacja koszyka", description = "Zmiana ilości produktu w koszyku")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zmieniono ilość produktu"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu")})
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long id,
      @RequestBody UpdateCartItemRequest request) {
    return ResponseEntity.ok(cartItemService.updateCartItem(id, request));
  }

  @Operation(summary = "Usuwanie produktu z koszyka", description = "Usunięcie produktu z koszyka")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Produkt usunięty z koszyka"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu")})
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> deleteCartItem(@PathVariable Long id) {
    cartItemService.deleteCartItem(id);
    return ResponseEntity.ok("Produkt usunięty z koszyka");
  }

  @Operation(summary = "Usuwanięcie koszyka", description = "Wyszczyszczenie całego koszyka")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Wyczszczono koszyk"),
      @ApiResponse(responseCode = "404", description = "Błąd")})
  @DeleteMapping()
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> clearCartForCurrentUser() {
    cartItemService.clearCartForUser(cartItemService.getCurrentUser());
    return ResponseEntity.ok("Wyczyszczono koszyk");
  }

}
