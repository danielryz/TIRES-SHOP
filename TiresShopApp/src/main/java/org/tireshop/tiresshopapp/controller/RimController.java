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
import org.tireshop.tiresshopapp.dto.request.create.CreateRimRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateRimRequest;
import org.tireshop.tiresshopapp.dto.response.RimFilterResponse;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.RimService;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rim", description = "Product Rim support.")
public class RimController {

  private final RimService rimService;

  @Operation(summary = "Returns data of rim by ID.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = RimResponse.class))),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/rim/{id}")
  public ResponseEntity<RimResponse> getRimById(
      @Parameter(description = "Rim ID.") @PathVariable Long id) {
    RimResponse rim = rimService.getRimById(id);
    return ResponseEntity.ok(rim);
  }

  @Operation(summary = "List of Rim with filter and sort.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "Rim list returned.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = RimResponse.class)))
  @GetMapping("/api/rims")
  public ResponseEntity<Page<RimResponse>> getRims(
      @Parameter(description = "Name of rim.") @RequestParam(required = false) String name,
      @Parameter(description = "Material of rim.")
      @RequestParam(required = false) List<String> material,
      @Parameter(description = "Size of rim.") @RequestParam(required = false) List<String> size,
      @Parameter(description = "Bolt Pattern for rim.")
      @RequestParam(required = false) List<String> boltPattern,
      @Parameter(description = "Min value of price for rim.")
      @RequestParam(required = false) BigDecimal minPrice,
      @Parameter(description = "Max value of price for rim.")
      @RequestParam(required = false) BigDecimal maxPrice,
      @Parameter(description = "Page number for pagination.")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Size of page for pagination.")
      @RequestParam(defaultValue = "10") int sizePerPage,
      @Parameter(description = "Sort by field and direction.")
      @RequestParam(defaultValue = "id,asc") String sort) {
    return ResponseEntity.ok(rimService.getRims(material, size, boltPattern, name, minPrice,
        maxPrice, page, sizePerPage, sort));
  }

  @Operation(summary = "Adding Rim Products.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "The rims has been created.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = RimResponse.class))),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PostMapping("/api/admin/rim")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<RimResponse>> createNewRim(
      @RequestBody List<CreateRimRequest> requests) {
    List<RimResponse> responses = rimService.createNewRim(requests);
    return ResponseEntity.status(HttpStatus.CREATED).body(responses);
  }

  @Operation(summary = "Rim edition.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim updated successfully.",
          content = @Content(examples = @ExampleObject(value = "Rim updated successfully."))),
      @ApiResponse(responseCode = "400", description = "Bad Request.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/api/admin/rim/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateRim(@Parameter(description = "Rim ID.") @PathVariable Long id,
      @RequestBody UpdateRimRequest request) {
    rimService.updateRim(id, request);
    return ResponseEntity.ok("Rim updated successfully.");
  }

  @Operation(summary = "Delete Rim.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim deleted successfully.",
          content = @Content(examples = @ExampleObject(value = "Rim deleted successfully."))),
      @ApiResponse(responseCode = "401", description = "Unauthorized.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Access Denied.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/rim/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteRim(
      @Parameter(description = "Rim ID.") @PathVariable Long id) {
    rimService.deleteRim(id);
    return ResponseEntity.ok("Rim deleted successfully.");
  }

  @Operation(summary = "Get Filters for Rim.", description = "PUBLIC.")
  @ApiResponse(responseCode = "200", description = "Filters returned successfully.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = RimFilterResponse.class)))
  @GetMapping("api/rim/filters")
  public RimFilterResponse getFilterOptions() {
    return rimService.getAvailableFilterOptions();
  }
}
