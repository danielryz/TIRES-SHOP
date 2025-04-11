package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UserResponse(
        @Schema(example = "1", description = "Unikalny identyfikator użytkownika")
        Long id,

        @Schema(example = "tires_shop1", description = "Unikalna nazwa użytkownika")
        String username,

        @Schema(example = "tires@tiresshop.pl", description = "Adres email użytkownika")
        String email,

        @Schema(example = "Jan", description = "Imię użytkownika")
        String firstName,

        @Schema(example = "Kowalski", description = "Nazwisko użytkownika")
        String lastName,

        @Schema(example = "+48 123456789", description = "Numer telefonu")
        String phoneNumber,

        @Schema(description = "Role przypisane do użytkownika")
        List<String> roles
) {
}

