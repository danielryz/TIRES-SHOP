package org.tireshop.tiresshopapp.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @Schema(example = "1", description = "Unikalny identyfikator użytkownika")
    private Long id;

    @Schema(example = "tires_shop1", description = "Unikalna nazwa użytkownika")
    private String username;

    @Schema(example = "tires@tiresshop.pl", description = "Adres email użytkownika")
    private String email;

    @Schema(example = "Jan", description = "Imię użytkownika")
    private String firstName;

    @Schema(example = "Kowalski", description = "Nazwisko użytkownika")
    private String lastName;

    @Schema(example = "+48 123456789", description = "Numer telefonu")
    private String phoneNumber;

    @Schema(description = "Role przypisane do użytkownika")
    private List<String> roles;
}

