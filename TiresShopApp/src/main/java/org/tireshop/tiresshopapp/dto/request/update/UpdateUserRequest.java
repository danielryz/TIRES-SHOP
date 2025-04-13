package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @Size(min = 3, max = 30, message = "Nazwa użytkownika musi mieć od 3 do 30 znaków")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "Nazwa użytkownika może zawierać tylko litery, cyfry, _, - (bez spacji)"
        )
        @Schema(example = "tires_shop1")
        String username,

        @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Hasło musi zawierać wielką literę, małą literę, cyfrę i znak specjalny"
        )
        @Schema(example = "Pa$$word1")
        String password,

        @Schema(example = "Jan")
        String firstName,

        @Schema(example = "Kowalski")
        String lastName,

        @Size(min = 9, max = 14, message = "Niepoprawny Numer")
        @Schema(example = "+48 123456789")
        String phoneNumber
) {
}
