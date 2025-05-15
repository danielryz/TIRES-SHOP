package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.tireshop.tiresshopapp.dto.request.create.CreateTireRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateTireRequest;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.TireService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tire", description = "Product Tire support.")
public class TireController {

  private final TireService tireService;
// do usunięcia po przpięciu się na ten lepszy
  @Operation(summary = "List of all Tire.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Tire list returned.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = TireResponse.class)))})
  @GetMapping("/api/tire")
  public List<TireResponse> getAllTire() {
    return tireService.getAllTire();
  }

  @Operation(summary = "Returns data of tire by ID.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tire data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TireResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tire Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/tire/{id}")
  public ResponseEntity<TireResponse> getTireById(@PathVariable Long id) {
    TireResponse tire = tireService.getTireById(id);
    return ResponseEntity.ok(tire);
  }

  @Operation(summary = "List of Tire with filter and sort.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tire list returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TireResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tire Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/tire")
  public ResponseEntity<Page<TireResponse>> getTires(
          @RequestParam(required = false) String name,
          @RequestParam(required = false) String season,
          @RequestParam(required = false) String size,
          @RequestParam(required = false) BigDecimal minPrice,
          @RequestParam(required = false) BigDecimal maxPrice,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int sizePerPage,
          @RequestParam(defaultValue = "id,asc") String[] sort
  ){
    return ResponseEntity.ok(tireService.getTires(name, season, size, minPrice, maxPrice, page, sizePerPage, sort));
  }

  @Operation(summary = "Adding Tire Products.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "The tires has been created.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TireResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @PostMapping("/api/admin/tire")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<TireResponse>> createNewTire(@RequestBody List<CreateTireRequest> requests) {
    List<TireResponse> responses = tireService.createNewTire(requests);
    return ResponseEntity.status(HttpStatus.CREATED).body(responses);
  }

  @Operation(summary = "Tire edition.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tire updated successfully.",
          content = @Content(examples = @ExampleObject(value = "Tire updated successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Tire Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/api/admin/tire/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateTire(@PathVariable Long id,
      @RequestBody UpdateTireRequest request) {
    tireService.updateTire(id, request);
    return ResponseEntity.ok("Tire updated successfully.");
  }

  @Operation(summary = "Delete Tire.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tire deleted successfully.",
          content = @Content(examples = @ExampleObject(value = "Tire deleted successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Tire Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/tire/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteTire(@PathVariable Long id) {
    tireService.deleteTire(id);
    return ResponseEntity.ok("Tire deleted successfully.");
  }


}
