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
import org.tireshop.tiresshopapp.dto.request.create.CreateProductRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateProductRequest;
import org.tireshop.tiresshopapp.dto.response.ProductResponse;
import org.tireshop.tiresshopapp.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "Obsługa prodóktów")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "Lista wszystkich produktów", description = "Endpoint publiczny")
  @ApiResponse(responseCode = "200", description = "Zwrócono listę produktów")
  @GetMapping("/api/products")
  public List<ProductResponse> getAllProducts() {
    return productService.getAllProducts();
  }


  @Operation(summary = "Szczegóły produktu", description = "Zwraca dane jednego produktu po ID")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono dane produktu"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu")})
  @GetMapping("/api/products/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
    ProductResponse product = productService.getProductById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono produktu"));
    return ResponseEntity.ok(product);
  }

  @Operation(summary = "Dodaj nowy produkt", description = "Tylko dla administratorów")
  @ApiResponses({@ApiResponse(responseCode = "201", description = "Produkt został utworzony"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/api/admin/products")
  public ResponseEntity<ProductResponse> createNewProduct(
      @RequestBody CreateProductRequest request) {
    ProductResponse response = productService.createProduct(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Częściowa edycja produktu",
      description = "Aktualizacja wybranych pól (tylko admin)")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zaktualizowano produkt"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu")})
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/api/admin/products/{id}")
  public ResponseEntity<?> updateProduct(@PathVariable Long id,
      @RequestBody UpdateProductRequest request) {
    try {
      ProductResponse response = productService.updateProduct(id, request);
      return ResponseEntity.ok(response);
    } catch (ResponseStatusException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @Operation(summary = "Usuń produkt", description = "Tylko dla administratorów")
  @ApiResponse(responseCode = "200", description = "Produkt został usunięty")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/api/admin/products/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.ok("Usunięto produkt");
  }


}
