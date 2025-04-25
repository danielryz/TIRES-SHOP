package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateOrderRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateOrderStatusRequest;
import org.tireshop.tiresshopapp.dto.response.OrderResponse;
import org.tireshop.tiresshopapp.entity.OrderStatus;
import org.tireshop.tiresshopapp.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Zarządzanie zamówieniami")
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Składanie zamówienia", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Złożono zamówienia"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PostMapping("/public")
  public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
    return ResponseEntity.ok(orderService.createOrder(request));
  }

  @Operation(summary = "Pobieranie zamówień przez użytkownika", description = "Endpoint dla USER")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista zamówień"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<OrderResponse>> getUserOrders() {
    return ResponseEntity.ok(orderService.getUserOrders());
  }

  @Operation(summary = "Pobieranie zamówienia o id przez użytkownika",
      description = "Endpoint dla USER")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zamówienie"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/user/{id}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<OrderResponse> getUserOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getUserOrderById(id));
  }

  @Operation(summary = "Anulowanie zamówienia przez użytkownika ",
      description = "Endpoint dla USER")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zamówienie zostało anulowane"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PatchMapping("/{id}/cancel")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
    orderService.cancelOrder(id);
    return ResponseEntity.ok("Zamówienie zostało anulowane");
  }

  @Operation(summary = "Pobieranie zamówień przez Admina po Statusie",
      description = "Endpoint dla ADMINA")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista zamówień"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<OrderResponse>> getAllOrdersByStatus(
      @RequestParam(required = false) OrderStatus status) {
    return ResponseEntity.ok(orderService.getAllOrdersByStatus(status));
  }

  @Operation(summary = "Pobranie zamówienia przez Admina po id",
      description = "Endpoint dla ADMINA")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zamówienie"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("admin/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderResponse> getOrderByIdAdmin(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderByIdAdmin(id));
  }

  @Operation(summary = "Edycja statusu zamówienia", description = "Endpoint dla ADMINA")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Status zamówienia został zaktualizowany"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PatchMapping("/admin/{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateOrderStatus(@PathVariable Long id,
      @RequestBody UpdateOrderStatusRequest request) {
    orderService.updateOrderStatus(id, request);
    return ResponseEntity.ok("Status zamówienia został zaktualizowany");
  }
}
