package org.tireshop.tiresshopapp.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Username is required.")
        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters long.")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "Username can only contain letters, numbers, _, - (no spaces)."
        )
        @Schema(example = "tires_shop1")
        String username,

        @Email(message = "Incorrect email address format")
        @NotBlank(message = "Email is required.")
        @Schema(example = "tires@tiresshop.pl")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, message = "The password must be at least 8 characters long.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "The password must contain an uppercase letter, a lowercase letter, a number, and a special character."
        )
        @Schema(example = "Pa$$word1")
        String password
) {
}
