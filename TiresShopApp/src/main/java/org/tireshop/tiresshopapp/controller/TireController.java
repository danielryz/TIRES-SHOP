package org.tireshop.tiresshopapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tireshop.tiresshopapp.dto.request.create.CreateTireRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateTireRequest;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.TireService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tire", description = "Product Tire support.")
public class TireController {

  private final TireService tireService;

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

  @Operation(summary = "List of Tire with season filter.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tire list returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TireResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tire Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/tire/season")
  public List<TireResponse> getTireBySeason(@RequestParam(required = false) String season) {
    if (season == null) {
      return tireService.getAllTire();
    }
    return tireService.getTireBySeason(season);
  }

  @Operation(summary = "List of Tire with size filter.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Tire list returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TireResponse.class))),
      @ApiResponse(responseCode = "404", description = "Tire Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/tire/size")
  public List<TireResponse> getTireBySize(@RequestParam(required = false) String size) {
    if (size == null) {
      return tireService.getAllTire();
    }
    return tireService.getTireBySize(size);
  }

  @Operation(summary = "Adding Tire Product.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "The tire has been created.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = TireResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @PostMapping("/api/admin/tire")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<TireResponse> createNewTire(@RequestBody CreateTireRequest request) {
    TireResponse response = tireService.createNewTire(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
