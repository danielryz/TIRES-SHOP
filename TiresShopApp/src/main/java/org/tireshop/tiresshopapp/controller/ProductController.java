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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateProductRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateProductRequest;
import org.tireshop.tiresshopapp.dto.response.BaseProductResponse;
import org.tireshop.tiresshopapp.dto.response.ProductResponse;
import org.tireshop.tiresshopapp.entity.ProductType;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product support.")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "List of products with filters and sort.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "Product list returned.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ProductResponse.class)))
  @GetMapping("/api/products")
  public ResponseEntity<Page<BaseProductResponse>> getProducts(
      @Parameter(description = "Name of product.") @RequestParam(required = false) String name,
      @Parameter(description = "Min value of price for product.")
      @RequestParam(required = false) BigDecimal minPrice,
      @Parameter(description = "Max value of price for product.")
      @RequestParam(required = false) BigDecimal maxPrice,
      @Parameter(description = "Product Type ENUM.")
      @RequestParam(required = false) ProductType type,
      @Parameter(description = "Page number for pagination.")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Size of page for pagination.")
      @RequestParam(defaultValue = "10") int sizePerPage,
      @Parameter(description = "Sort by field and direction.")
      @RequestParam(defaultValue = "id,asc") String sort) {
    return ResponseEntity
        .ok(productService.getProducts(name, minPrice, maxPrice, type, page, sizePerPage, sort));
  }

  @Operation(summary = "List of search product.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "Product list returned.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ProductResponse.class)))
  @GetMapping("/api/products/search")
  public ResponseEntity<Page<BaseProductResponse>> searchProducts(
      @Parameter(description = "Query for search product.")
      @RequestParam(required = false) String query,
      @Parameter(description = "Page number for pagination.")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Size of page for pagination.")
      @RequestParam(defaultValue = "10") int sizePerPage,
      @Parameter(description = "Sort by field and direction.")
      @RequestParam(defaultValue = "id,asc") String sort) {
    Page<BaseProductResponse> results =
        productService.searchProducts(query, page, sizePerPage, sort);
    return ResponseEntity.ok(results);
  }

  @Operation(summary = "Returns data of product by ID.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Product data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponse.class))),
      @ApiResponse(responseCode = "404", description = "Product Not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/products/{id}")
  public ResponseEntity<BaseProductResponse> getProductById(
      @Parameter(description = "Product ID.") @PathVariable Long id) {
    BaseProductResponse product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }



  @Operation(summary = "Adding a products.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "The products has been created.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ProductResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/api/admin/products")
  public ResponseEntity<List<BaseProductResponse>> createNewProducts(
      @RequestBody List<CreateProductRequest> requests) {
    List<BaseProductResponse> responses = productService.createProduct(requests);
    return ResponseEntity.status(HttpStatus.CREATED).body(responses);
  }

  @Operation(summary = "Product edition.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Product updated successfully.",
          content = @Content(examples = @ExampleObject(value = "Product updated successfully."))),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Product Not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/api/admin/products/{id}")
  public ResponseEntity<String> updateProduct(
      @Parameter(description = "Product ID.") @PathVariable Long id,
      @RequestBody UpdateProductRequest request) {
    productService.updateProduct(id, request);
    return ResponseEntity.ok("Product updated successfully.");
  }

  @Operation(summary = "Delete product.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Product deleted successfully.",
          content = @Content(examples = @ExampleObject(value = "Product deleted successfully."))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Product Not Found",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/api/admin/products/{id}")
  public ResponseEntity<String> deleteProduct(
      @Parameter(description = "Product ID.") @PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.ok("Product deleted successfully.");
  }

}
