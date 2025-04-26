package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateProductRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateProductRequest;
import org.tireshop.tiresshopapp.dto.response.ProductResponse;
import org.tireshop.tiresshopapp.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product support.")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "List of all products.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "Product list returned.")
  @GetMapping("/api/products")
  public List<ProductResponse> getAllProducts() {
    return productService.getAllProducts();
  }


  @Operation(summary = "Returns data of product by ID.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Product data returned."),
      @ApiResponse(responseCode = "404", description = "Product Not Found",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Product with id 1 not found\"\"}")))})
  @GetMapping("/api/products/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
    ProductResponse product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

  @Operation(summary = "Adding a product.", description = "ADMIN.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "The product has been created."),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject()))})
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/api/admin/products")
  public ResponseEntity<ProductResponse> createNewProduct(
      @RequestBody CreateProductRequest request) {
    ProductResponse response = productService.createProduct(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Product edition.", description = "ADMIN.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Product updated successfully.", content = @Content(examples = @ExampleObject(value = "Product updated successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "Product Not Found.",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Product with id 1 not found\"\"}")))})
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/api/admin/products/{id}")
  public ResponseEntity<String> updateProduct(@PathVariable Long id,
      @RequestBody UpdateProductRequest request) {
    productService.updateProduct(id, request);
    return ResponseEntity.ok("Product updated successfully.");
  }

  @Operation(summary = "Delete product.", description = "ADMIN.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Product deleted successfully.", content = @Content(examples = @ExampleObject(value = "Product deleted successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.",
          content = @Content(examples = @ExampleObject())),
      @ApiResponse(responseCode = "404", description = "Product Not Found",
          content = @Content(examples = @ExampleObject(
              value = "{\"error\": \"404 NOT_FOUND \\ \"Product with id 1 not found.\"\"}")))})
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/api/admin/products/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.ok("Product deleted successfully.");
  }

}
