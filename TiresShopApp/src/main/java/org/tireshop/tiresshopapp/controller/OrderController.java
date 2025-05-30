package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateOrderRequest;
import org.tireshop.tiresshopapp.dto.response.OrderResponse;
import org.tireshop.tiresshopapp.entity.OrderStatus;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order support")
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Placing an order.", description = "PUBLIC.\n" +
          "To place an order, you must first add products to your cart. If you are logged in, you do not need to provide X-Client-Id (uuid) and quest data.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Order placed successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = OrderResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/public")
  public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request,
      @Parameter(description = "Unique client identifier (required if not authenticated)")
      @RequestHeader(value = "X-Client-Id", required = false) String clientId) {
    OrderResponse order = orderService.createOrder(request, clientId);
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @Operation(summary = "Get all user orders.", description = "USER")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "List of user orders returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = OrderResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<OrderResponse>> getUserOrders() {
    return ResponseEntity.ok(orderService.getUserOrders());
  }

  @Operation(summary = "Get order by ID for user.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Order returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = OrderResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Order Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/public/user/{id}")
  public ResponseEntity<OrderResponse> getUserOrderById(
      @Parameter(description = "Order ID.") @PathVariable Long id,
      @Parameter(description = "Unique client identifier (required if not authenticated)")
      @RequestHeader(value = "X-Client-Id", required = false) String clientId) {
    return ResponseEntity.ok(orderService.getUserOrderById(id, clientId));
  }

  @Operation(summary = "Cancellation of order.", description = "USER.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The order has been canceled.",
          content = @Content(examples = @ExampleObject("The order has been canceled."))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Order not found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Conflict",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/{id}/cancel")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> cancelOrder(
      @Parameter(description = "Order ID.") @PathVariable Long id) {
    orderService.cancelOrder(id);
    return ResponseEntity.ok("The order has been canceled.");
  }

  @Operation(summary = "Gets orders with filters and Sort.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "List of orders returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = OrderResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<OrderResponse>> getOrders(
      @Parameter(description = "User ID.") @RequestParam(required = false) Long userId,
      @Parameter(description = "Order status ENUM.")
      @RequestParam(required = false) OrderStatus status,
      @Parameter(description = "Created at date from.")
      @RequestParam(required = false) LocalDateTime createdAtFrom,
      @Parameter(description = "Created at date to.")
      @RequestParam(required = false) LocalDateTime createdAtTo,
      @Parameter(description = "Is paid?") @RequestParam(required = false) Boolean isPaid,
      @Parameter(description = "Paid at date from.")
      @RequestParam(required = false) LocalDateTime paidAtFrom,
      @Parameter(description = "Paid at date to.")
      @RequestParam(required = false) LocalDateTime paidAtTo,
      @Parameter(description = "Page number for pagination.")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Size of page for pagination.")
      @RequestParam(defaultValue = "20") int sizePerPage,
      @Parameter(description = "Sort by field and direction.")
      @RequestParam(defaultValue = "id,asc") String sort) {
    return ResponseEntity.ok(orderService.getOrders(userId, status, createdAtFrom, createdAtTo,
        isPaid, paidAtFrom, paidAtTo, page, sizePerPage, sort));
  }


  @Operation(summary = "Get order by Admin by ID.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Order data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = OrderResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("admin/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderResponse> getOrderByIdAdmin(
      @Parameter(description = "Order ID.") @PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderByIdAdmin(id));
  }

  @Operation(summary = "Update order status.", description = "ADMIN")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Status has been updated successfully.",
          content = @Content(examples = @ExampleObject("Status has been updated successfully."))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/admin/{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateOrderStatus(
      @Parameter(description = "Order ID.") @PathVariable Long id,
      @Parameter(description = "Order status ENUM.") @RequestParam OrderStatus status) {
    orderService.updateOrderStatus(id, status);
    return ResponseEntity.ok("Status has been updated successfully.");
  }

  @Operation(summary = "Update order paid status.", description = "Public")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Order has been payed successfully.",
          content = @Content(examples = @ExampleObject("Order has been payed successfully."))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Order not found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Conflict.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/public/{orderId}/pay")
  public ResponseEntity<String> payForYourOrder(
      @Parameter(description = "Order ID.") @PathVariable Long orderId,
      @Parameter(description = "Unique client identifier (required if not authenticated)")
      @RequestHeader(value = "X-Client-Id", required = false) String clientId) {
    orderService.payForYourOrder(orderId, clientId);
    return ResponseEntity.ok("Order has been payed successfully.");
  }
}
