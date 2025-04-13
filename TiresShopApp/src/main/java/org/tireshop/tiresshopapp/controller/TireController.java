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
import org.tireshop.tiresshopapp.dto.request.CreateTireRequest;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.service.TireService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tire", description = "Opbsługa Produktów Opony")
public class TireController {

    private final TireService tireService;

    @Operation(summary = "Lista wszystkich Opon", description = "Endpoint publiczny")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zwrócono listę opon"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono opon")
    })
    @GetMapping("/api/tire")
    public List<TireResponse> getAllTire() {
        return tireService.getAllTire();
    }

    @Operation(summary = "Opona po id", description = "Endpoint publiczny")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zwrócono opone"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono opony")
    })
    @GetMapping("/api/tire/{id}")
    public ResponseEntity<TireResponse> getTireById(@PathVariable Long id) {
        TireResponse tire = tireService.getTireById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nie znaleziono opony"));
        return ResponseEntity.ok(tire);
    }

    @Operation(summary = "Opona po sezonie", description = "Endpoint publiczny")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zwrócono opone"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono opony")
    })
    @GetMapping("/api/tire/{season}")
    public List<TireResponse> getTireBySeason(@PathVariable String season) {
        return tireService.getTireBySeason(season);
    }

    @Operation(summary = "Opona po id", description = "Endpoint publiczny")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zwrócono opone"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono opony")
    })
    @GetMapping("/api/tire/{size}")
    public List<TireResponse> getTireBySize(@PathVariable String size) {
        return tireService.getTireBySize(size);
    }

    @Operation(summary = "Dodawanie opon", description = "ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dodano opone"),
            @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")
    })
    @PostMapping("/api/admin/tire")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TireResponse> createNewTire(@RequestBody CreateTireRequest request) {
        TireResponse tire = tireService.createNewTire(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tire);
    }

    @Operation(summary = "Edycja opony", description = "ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Edytowano opone"),
            @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")
    })
    @PatchMapping("/api/admin/tire/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTire(@PathVariable Long id, @RequestBody CreateTireRequest request) {
        try {
            TireResponse tire = tireService.updateTire(id, request);
            return ResponseEntity.ok(tire);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Usuwanie opony", description = "ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usunięto opone"),
            @ApiResponse(responseCode = "403", description = "Brak autoryzacji lub uprawnień")
    })
    @DeleteMapping("/api/admin/tire")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTire(@PathVariable Long id) {
        tireService.deleteTire(id);
        return ResponseEntity.ok("Usunięto opone");
    }


}
