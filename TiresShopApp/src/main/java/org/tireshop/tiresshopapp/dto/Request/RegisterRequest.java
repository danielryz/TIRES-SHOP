package org.tireshop.tiresshopapp.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    @Size(min = 3, max = 30, message = "Nazwa użytkownika musi mieć od 3 do 30 znaków")
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "Nazwa użytkownika może zawierać tylko litery, cyfry, _, - (bez spacji)"
    )
    @Schema(example = "tires_shop1")
    private String username;

    @Email(message = "Nieprawidłowy format adresu email")
    @NotBlank(message = "Email wymagany")
    @Schema(example = "tires@tiresshop.pl")
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Hasło musi zawierać wielką literę, małą literę, cyfrę i znak specjalny"
    )
    @Schema(example = "Pa$$word1")
    private String password;
}
