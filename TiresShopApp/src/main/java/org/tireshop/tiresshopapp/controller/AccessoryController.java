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
import org.tireshop.tiresshopapp.dto.request.AccessoryRequest;
import org.tireshop.tiresshopapp.dto.response.AccessoryResponse;
import org.tireshop.tiresshopapp.dto.response.RimResponse;
import org.tireshop.tiresshopapp.exception.GlobalExceptionHandler;
import org.tireshop.tiresshopapp.exception.ResourceNotFoundException;
import org.tireshop.tiresshopapp.service.AccessoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Accessory", description = "Obsługa Produktów Akcesoria")
public class AccessoryController {

    private final AccessoryService accessoryService;

    @Operation(summary = "Lista wszystkich Akcesoriów", description = "Endpoint publiczny")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono listę akcesoriów"), @ApiResponse(responseCode = "404", description = "Nie znaleziono akcesoriów")})
    @GetMapping("/api/accessory")
    public List<AccessoryResponse> getAllAccessory() {
        return accessoryService.getAllAccessory();
    }

    @Operation(summary = "Felga po id", description = "Endpoint publiczny")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono akcesorium"), @ApiResponse(responseCode = "404", description = "Nie znaleziono akcesorium")})
    @GetMapping("/api/accessory/{id}")
    public ResponseEntity<AccessoryResponse> getAccessoryById(@PathVariable Long id) {
        AccessoryResponse accessory = accessoryService.getAccessoryById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono akcesorium"));
        return ResponseEntity.ok(accessory);
    }

    @Operation(summary = "Felga po typie akcesorium", description = "Endpoint publiczny")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Zwrócono akcesorium"), @ApiResponse(responseCode = "404", description = "Nie znaleziono akcesorium")})
    @GetMapping("/api/accessory/type")
    public List<AccessoryResponse> getAccessoryByAccessoryType(@RequestParam(required = false) String accessoryType) {
        if (accessoryType == null) {
            return accessoryService.getAllAccessory();
        }
        List<AccessoryResponse> accessory = accessoryService.getAccessoryByAccessoryType(accessoryType);
        if (accessory.isEmpty()) {
            throw new ResourceNotFoundException("Brak akcesorów dla typu = " + accessoryType);
        }
        return accessory;
    }

    @Operation(summary = "Dodawanie akcesorium", description = "ADMIN")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Dodano akcesorium"), @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
    @PostMapping("/api/admin/accessory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccessoryResponse> createNewAccessory(@RequestBody AccessoryRequest request) {
        AccessoryResponse accessory = accessoryService.createNewAccessory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(accessory);
    }

    @Operation(summary = "Edycja akcesorium", description = "ADMIN")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Edytowano akcesorium"), @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
    @PatchMapping("/api/admin/accessory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAccessory(@PathVariable Long id, @RequestBody AccessoryRequest request) {
        try {
            AccessoryResponse accessory = accessoryService.updateAccessory(id, request);
            return ResponseEntity.ok(accessory);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Usuwanie akcesorium", description = "ADMIN")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usunięto akcesorium"), @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")})
    @DeleteMapping("/api/admin/accessory/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.ok("Usunięto akcesorium");
    }


}

