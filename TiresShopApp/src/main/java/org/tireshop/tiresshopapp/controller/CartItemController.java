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
import org.springframework.http.ResponseEntity;
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
  @ApiResponse(responseCode = "200", description = "Cart returned successfully.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = CartItemResponse.class)))
  @GetMapping
  public ResponseEntity<List<CartItemResponse>> getCartForCurrentUser(
      @Parameter(description = "Unique client identifier (required if not authenticated)")
      @RequestHeader(value = "X-Client-Id", required = false) String clientId) {
    List<CartItemResponse> cartItems = cartItemService.getCartForCurrentUser(clientId);
    return ResponseEntity.ok(cartItems);
  }

  @Operation(summary = "Returns the cart summary.", description = "USER.")
  @ApiResponse(responseCode = "200", description = "Cart returned successfully.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = CartSummaryResponse.class)))
  @GetMapping("/summary")
  public ResponseEntity<CartSummaryResponse> getCartSummary(
      @Parameter(description = "Unique client identifier (required if not authenticated)")
      @RequestHeader(value = "X-Client-Id", required = false) String clientId) {
    return ResponseEntity.ok(cartItemService.getCartSummary(clientId));
  }

  @Operation(summary = "Add items to Cart.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Items has been added successfully.",
          content = @Content(
              examples = @ExampleObject(value = "Items has been added successfully."))),
      @ApiResponse(responseCode = "404", description = "Product not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Not enough stock to add to cart.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping
  public ResponseEntity<String> addCartItem(@RequestBody AddToCartRequest request,
      @Parameter(description = "Unique client identifier (required if not authenticated)")
      @RequestHeader(value = "X-Client-Id", required = false) String clientId) {
    cartItemService.addCartItem(request, clientId);
    return ResponseEntity.ok("Items has been added successfully.");
  }

  @Operation(summary = "Update cart items.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Cart has been updated successfully.",
          content = @Content(
              examples = @ExampleObject(value = "Cart has been updated successfully."))),
      @ApiResponse(responseCode = "404", description = "The item in the cart does not exist.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Not enough stock to add to cart.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/{id}")
  public ResponseEntity<String> updateCartItem(
      @Parameter(description = "Cart Item ID") @PathVariable Long id,
      @RequestBody UpdateCartItemRequest request) {
    cartItemService.updateCartItem(id, request);
    return ResponseEntity.ok("Cart has been updated successfully.");
  }

  @Operation(summary = "Delete Product from cart. ", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Item has been deleted successfully.",
          content = @Content()),
      @ApiResponse(responseCode = "404", description = "The item in the cart does not exist.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCartItem(
      @Parameter(description = "Cart Item ID") @PathVariable Long id) {
    cartItemService.deleteCartItem(id);
    return ResponseEntity.ok("Item has been deleted successfully.");
  }

  @Operation(summary = "Delete Cart.", description = "USER.")
  @ApiResponse(responseCode = "200", description = "Cart has been cleared successfully.",
      content = @Content(examples = @ExampleObject(value = "Cart has been cleared successfully.")))
  @DeleteMapping()
  public ResponseEntity<String> clearCartForUser(
      @Parameter(description = "Unique client identifier (required if not authenticated)")
      @RequestHeader(value = "X-Client-Id", required = false) String clientId) {
    cartItemService.clearCartForUser(clientId);
    return ResponseEntity.ok("Cart has been cleared successfully.");
  }

}
