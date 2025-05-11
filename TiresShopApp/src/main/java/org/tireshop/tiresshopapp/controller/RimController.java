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
import org.tireshop.tiresshopapp.dto.request.create.CreateRimRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateRimRequest;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.exception.ErrorResponse;
import org.tireshop.tiresshopapp.service.RimService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rim", description = "Product Rim support.")
public class RimController {

  private final RimService rimService;

  @Operation(summary = "List of all Rim.", description = "PUBLIC.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Rim list returned.",
      content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = RimResponse.class)))})
  @GetMapping("/api/rim")
  public List<RimResponse> getAllRim() {
    return rimService.getAllRim();
  }

  @Operation(summary = "Returns data of rim by ID.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim data returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = RimResponse.class))),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/rim/{id}")
  public ResponseEntity<RimResponse> getRimById(@PathVariable Long id) {
    RimResponse rim = rimService.getRimById(id);
    return ResponseEntity.ok(rim);
  }

  @Operation(summary = "List of Rim with material filter.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim list returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = RimResponse.class))),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/rim/material")
  public List<RimResponse> getRimByMaterial(@RequestParam(required = false) String material) {
    if (material == null) {
      return rimService.getAllRim();
    }
    return rimService.getRimByMaterial(material);
  }

  @Operation(summary = "List of Rim with size filter.", description = "PUBLIC.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim list returned.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = RimResponse.class))),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @GetMapping("/api/rim/size")
  public List<RimResponse> getRimBySize(@RequestParam(required = false) String size) {
    if (size == null) {
      return rimService.getAllRim();
    }
    return rimService.getRimBySize(size);
  }

  @Operation(summary = "Adding Rim Product.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "The rim has been created.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = RimResponse.class))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content())})
  @PostMapping("/api/admin/rim")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RimResponse> createNewRim(@RequestBody CreateRimRequest request) {
    RimResponse rim = rimService.createNewRim(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(rim);
  }

  @Operation(summary = "Rim edition.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim updated successfully.",
          content = @Content(examples = @ExampleObject(value = "Rim updated successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @PatchMapping("/api/admin/rim/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> updateRim(@PathVariable Long id,
      @RequestBody UpdateRimRequest request) {
    rimService.updateRim(id, request);
    return ResponseEntity.ok("Rim updated successfully.");
  }

  @Operation(summary = "Delete Rim.", description = "ADMIN.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Rim deleted successfully.",
          content = @Content(examples = @ExampleObject(value = "Rim deleted successfully."))),
      @ApiResponse(responseCode = "403", description = "No authorization.", content = @Content()),
      @ApiResponse(responseCode = "404", description = "Rim Not Found.",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))})
  @DeleteMapping("/api/admin/rim/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteRim(@PathVariable Long id) {
    rimService.deleteRim(id);
    return ResponseEntity.ok("Rim deleted successfully.");
  }


}
