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
import org.tireshop.tiresshopapp.dto.request.create.CreateOrderRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateOrderStatusRequest;
import org.tireshop.tiresshopapp.dto.response.OrderResponse;
import org.tireshop.tiresshopapp.entity.OrderStatus;
import org.tireshop.tiresshopapp.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order support")
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Placing an order.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Order placed successfully."),
      @ApiResponse(responseCode = "404",
          description = "Cart is empty. You cannot place an order.")})
  @PostMapping("/public")
  public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
    return ResponseEntity.ok(orderService.createOrder(request));
  }

  @Operation(summary = "Get all user orders.", description = "USER")
  @ApiResponse(responseCode = "200", description = "List of user orders returned successfully.")
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<OrderResponse>> getUserOrders() {
    return ResponseEntity.ok(orderService.getUserOrders());
  }

  @Operation(summary = "Get order by ID for user.", description = "USER.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Order returned successfully."),
      @ApiResponse(responseCode = "404", description = "Order Not Found.",
          content = @Content(examples = @ExampleObject(value = ("Order with id 1 not found."))))})
  @GetMapping("/user/{id}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<OrderResponse> getUserOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getUserOrderById(id));
  }

  @Operation(summary = "Cancellation of order.", description = "USER.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "The order has been canceled."),
      @ApiResponse(responseCode = "400", description = "Access Denied")})
  @PatchMapping("/{id}/cancel")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
    orderService.cancelOrder(id);
    return ResponseEntity.ok("The order has been canceled.");
  }

  @Operation(summary = "Gets orders by Admin by Status.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "List of orders returned successfully."),
      @ApiResponse(responseCode = "400", description = "Not found")})
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<OrderResponse>> getAllOrdersByStatus(
      @RequestParam(required = false) OrderStatus status) {
    return ResponseEntity.ok(orderService.getAllOrdersByStatus(status));
  }

  @Operation(summary = "Get order by Admin by ID.", description = "ADMIN.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Order data returned."),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("admin/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderResponse> getOrderByIdAdmin(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderByIdAdmin(id));
  }

  @Operation(summary = "Update order status.", description = "Endpoint dla ADMINA")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Status has been updated successfully."),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PatchMapping("/admin/{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateOrderStatus(@PathVariable Long id,
      @RequestBody UpdateOrderStatusRequest request) {
    orderService.updateOrderStatus(id, request);
    return ResponseEntity.ok("Status has been updated successfully.");
  }
}
