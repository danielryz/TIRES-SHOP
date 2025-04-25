package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateImageRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateImageRequest;
import org.tireshop.tiresshopapp.dto.response.AccessoryResponse;
import org.tireshop.tiresshopapp.dto.response.ImageResponse;
import org.tireshop.tiresshopapp.service.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "Obsługa zdjęć")
public class ImageController {
  private final ImageService imageService;

  @Operation(summary = "Lista wszystkich Zdjęć", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono listę Zdjęć"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono Zdjęć")})
  @GetMapping("/api/image")
  public ResponseEntity<List<ImageResponse>> getAllImages() {
    return ResponseEntity.ok(imageService.getAllImages());
  }

  @Operation(summary = "Zdjęcie", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono zdjęcie"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono Zdjęcia")})
  @GetMapping("/api/image/{id}")
  public ResponseEntity<ImageResponse> getImageById(@PathVariable Long id) {
    return ResponseEntity.ok(imageService.getImageById(id));
  }

  @Operation(summary = "Lista wszystkich Zdjęć produktu", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono listę Zdjęć produktu"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono zdjęć")})
  @GetMapping("/api/image/products/{productId}")
  public ResponseEntity<List<ImageResponse>> getImageByProductId(@PathVariable Long productId) {
    return ResponseEntity.ok(imageService.getImagesByProductId(productId));
  }

  @Operation(summary = "Dodawanie zdjęcia do produktu", description = "Endpoint dla ADMINA")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Dodano zdjęcie"),
      @ApiResponse(responseCode = "404", description = "brak uprawnień")})
  @PostMapping("/api/admin/image")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ImageResponse> createImage(@RequestBody CreateImageRequest request) {
    return ResponseEntity.ok(imageService.createImage(request));
  }

  @Operation(summary = "Update zdjęcia do produktu", description = "Endpoint dla ADMINA")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zaktualizowano zdjęcie"),
      @ApiResponse(responseCode = "404", description = "brak uprawnień")})
  @PatchMapping("/api/admin/image/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ImageResponse> updateImage(@PathVariable Long id,
      @RequestBody UpdateImageRequest request) {
    return ResponseEntity.ok(imageService.updateImage(id, request));
  }

  @Operation(summary = "Usuwanie zdjęcia z produktu", description = "Endpoint dla ADMINA")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zdjęcie zostało usunięte."),
      @ApiResponse(responseCode = "404", description = "brak uprawnień")})
  @DeleteMapping("/api/admin/image/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteImage(@PathVariable Long id) {
    imageService.deleteImage(id);
    return ResponseEntity.ok("Zdjęcie zostało usunięte.");
  }

  @Operation(summary = "Usuwanie wszystkich zdjęć w produkcie", description = "Endpoint dla ADMINA")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Wszystkie zdjęcia zostały usunięte."),
      @ApiResponse(responseCode = "404", description = "brak uprawnień")})
  @DeleteMapping("/api/admin/image/products/{productId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteImageByProductId(@PathVariable Long productId) {
    imageService.deleteImagesByProductId(productId);
    return ResponseEntity.ok("Wszystkie zdjęcia zostały usunięte.");
  }



}
