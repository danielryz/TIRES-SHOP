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
import org.tireshop.tiresshopapp.entity.User;
import org.tireshop.tiresshopapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Pobiera wszystkich użytkowników", description = "Tylko dla administratorów")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista użytkowników zwrócona pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Brak uprawnień",content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject( value = "{\"error\": \"Acces Denied\"}")
            )),
            @ApiResponse(responseCode = "403", description = "Brak Autoryzacji", content = @Content(
                    examples = @ExampleObject(value = " ")
            ))
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @Operation(summary = "Pobiera użytkownika po ID", description = "Tylko dla administratorów")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik znaleziony",content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Brak uprawnień",content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject( value = "{\"error\": \"Acces Denied\"}")
            )),
            @ApiResponse(responseCode = "403", description = "Brak Autoryzacji", content = @Content(
                    examples = @ExampleObject(value = " ")
            )),
            @ApiResponse(responseCode = "404", description = "Użytkownik nie istnieje", content = @Content(
                    examples = @ExampleObject(value = "can't parse JSON.  Raw result:\n" +
                            "\n" +
                            "Użytkownik o ID: 7 nie istnieje")
            ))
    })
    @GetMapping("/{id:[0-9]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @Operation(summary = "Szczegóły zalogowanego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zwrócono dane użytkownika",content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Brak Autoryzacji", content = @Content(
                    examples = @ExampleObject(value = " ")
            ))
    })
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getCurrentUser(){
        try{
            User user = userService.getCurrentUser();
            return ResponseEntity.ok(user);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
