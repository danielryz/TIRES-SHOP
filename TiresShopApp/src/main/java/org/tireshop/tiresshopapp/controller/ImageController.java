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
import org.springframework.web.multipart.MultipartFile;
import org.tireshop.tiresshopapp.dto.request.create.AddImagesRequest;
import org.tireshop.tiresshopapp.dto.request.create.CreateImageRequest;
import org.tireshop.tiresshopapp.dto.response.ImageResponse;
import org.tireshop.tiresshopapp.service.ImageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image", description = "Image support.")
public class ImageController {
  private final ImageService imageService;

  @Operation(summary = "Get list of all images.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "List of images returned successfully.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ImageResponse.class)))
  @GetMapping("/api/image")
  public ResponseEntity<List<ImageResponse>> getAllImages() {
    return ResponseEntity.ok(imageService.getAllImages());
  }

  @Operation(summary = "Get one image by ID.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Image returned successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ImageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Image Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/image/{id}")
  public ResponseEntity<ImageResponse> getImageById(@PathVariable Long id) {
    return ResponseEntity.ok(imageService.getImageById(id));
  }

  @Operation(summary = "List of all product images.", description = "PUBLIC")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "List of images returned successfully",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ImageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Product Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/image/products/{productId}")
  public ResponseEntity<List<ImageResponse>> getImageByProductId(@PathVariable Long productId) {
    return ResponseEntity.ok(imageService.getImagesByProductId(productId));
  }

  @Operation(summary = "Adding a photo to a product.", description = "ADMIN.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Image added successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Product Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/api/admin/image")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> createImage(@RequestBody CreateImageRequest request) {
    imageService.createImage(request);
    return ResponseEntity.ok("Image added successfully.");
  }

  @Operation(summary = "Adding a photos to a product.", description = "ADMIN.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Images added successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Product Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/api/admin/images/product/{productId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> addImagesToProduct(@PathVariable Long productId,
      @RequestBody List<AddImagesRequest> requests) {
    imageService.addImagesToProduct(productId, requests);
    return ResponseEntity.ok("Images added successfully.");
  }

  @Operation(summary = "Delete product image.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Image has been deleted successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Image Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/image/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteImage(@PathVariable Long id) {
    imageService.deleteImage(id);
    return ResponseEntity.ok("Image has been deleted successfully.");
  }

  @Operation(summary = "Delete all product images.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "All image has been deleted successfully."),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Product Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/image/products/{productId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteImageByProductId(@PathVariable Long productId) {
    imageService.deleteImagesByProductId(productId);
    return ResponseEntity.ok("All image has been deleted successfully.");
  }

  @Operation(summary = "Upload image file to product.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Image uploaded successfully.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ImageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Product Not Found.",
          content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "500", description = "Internal Server Error.",
          content = @Content(mediaType = "application/json"))})
  @PostMapping("/api/admin/image/upload/{productId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ImageResponse> uploadImageFile(@RequestParam("file") MultipartFile file,
      @PathVariable Long productId) throws IOException {
    ImageResponse savedImage = imageService.saveImage(file, productId);
    return ResponseEntity.ok(savedImage);
  }

}


