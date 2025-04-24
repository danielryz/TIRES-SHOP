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
import org.tireshop.tiresshopapp.dto.request.create.CreateRimRequest;
import org.tireshop.tiresshopapp.dto.request.update.UpdateRimRequest;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.exception.ResourceNotFoundException;
import org.tireshop.tiresshopapp.service.RimService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rim", description = "Opbsługa Produktów Felgi")
public class RimController {

  private final RimService rimService;
  private String size;

  @Operation(summary = "Lista wszystkich Felg", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono listę felg"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono felg")})
  @GetMapping("/api/rim")
  public List<RimResponse> getAllRim() {
    return rimService.getAllRim();
  }

  @Operation(summary = "Felga po id", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono felge"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono felgi")})
  @GetMapping("/api/rim/{id}")
  public ResponseEntity<RimResponse> getRimById(@PathVariable Long id) {
    RimResponse rim = rimService.getRimById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono felgi"));
    return ResponseEntity.ok(rim);
  }

  @Operation(summary = "Felga po materiale", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono felge"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono felgi")})
  @GetMapping("/api/rim/material")
  public List<RimResponse> getRimByMaterial(@RequestParam(required = false) String material) {
    if (size == null) {
      return rimService.getAllRim();
    }
    List<RimResponse> rims = rimService.getRimByMaterial(material);
    if (rims.isEmpty()) {
      throw new ResourceNotFoundException("Brak felg dla materiału = " + material);
    }
    return rims;
  }

  @Operation(summary = "Felga po id", description = "Endpoint publiczny")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono felge"),
      @ApiResponse(responseCode = "404", description = "Nie znaleziono felgi")})
  @GetMapping("/api/rim/size")
  public List<RimResponse> getRimBySize(@RequestParam(required = false) String size) {
    if (size == null) {
      return rimService.getAllRim();
    }
    List<RimResponse> rims = rimService.getRimBySize(size);
    if (rims.isEmpty()) {
      throw new ResourceNotFoundException("Brak felg dla rozmiaru = " + size);
    }
    return rims;
  }

  @Operation(summary = "Dodawanie felgi", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Dodano felge"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PostMapping("/api/admin/rim")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RimResponse> createNewRim(@RequestBody CreateRimRequest request) {
    RimResponse rim = rimService.createNewRim(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(rim);
  }

  @Operation(summary = "Edycja felgi", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Edytowano felge"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @PatchMapping("/api/admin/rim/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> updateRim(@PathVariable Long id, @RequestBody UpdateRimRequest request) {
    try {
      RimResponse rim = rimService.updateRim(id, request);
      return ResponseEntity.ok(rim);
    } catch (ResponseStatusException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @Operation(summary = "Usuwanie felgi", description = "ADMIN")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Usunięto felge"),
      @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
  @DeleteMapping("/api/admin/rim/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteRim(@PathVariable Long id) {
    rimService.deleteRim(id);
    return ResponseEntity.ok("Usunięto felge");
  }


}
