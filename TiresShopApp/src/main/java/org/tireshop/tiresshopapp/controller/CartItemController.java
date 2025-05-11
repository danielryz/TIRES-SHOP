package org.tireshop.tiresshopapp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
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
@Tag(name = "Cart", description = "Cart support.")
public class CartItemController {
  private final CartItemService cartItemService;

  @Operation(summary = "Gets the user's shopping cart.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Cart returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CartItemResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<CartItemResponse>> getCartForCurrentUser() {
    return ResponseEntity.ok(cartItemService.getCartForCurrentUser());
  }

  @Operation(summary = "Returns the cart summary.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Cart returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CartSummaryResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @GetMapping("/summary")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<CartSummaryResponse> getCartSummary() {
    return ResponseEntity.ok(cartItemService.getCartSummary());
  }

  @Operation(summary = "Add items to Cart.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Items has been added successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Product not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> addCartItem(@RequestBody AddToCartRequest request) {
    cartItemService.addCartItem(request);
    return ResponseEntity.ok("Items has been added successfully.");// 404, 409
  }

  @Operation(summary = "Update cart items.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Cart has been updated successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "The item in the cart does not exist.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> updateCartItem(@PathVariable Long id,
      @RequestBody UpdateCartItemRequest request) {
    cartItemService.updateCartItem(id, request);
    return ResponseEntity.ok("Cart has been updated successfully.");// 404
  }

  @Operation(summary = "Delete Product from cart. ", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Item has been deleted successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "The item in the cart does not exist.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> deleteCartItem(@PathVariable Long id) {
    cartItemService.deleteCartItem(id);
    return ResponseEntity.ok("Item has been deleted successfully.");// 404
  }

  @Operation(summary = "Delete Cart.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Cart has been cleared successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),})
  @DeleteMapping()
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> clearCartForCurrentUser() {
    cartItemService.clearCartForUser(cartItemService.getCurrentUser());
    return ResponseEntity.ok("Cart has been cleared successfully.");
  }

}
