package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters long.")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "Username can only contain letters, numbers, _, - (no spaces)."
        )
        @Schema(example = "tires_shop1")
        String username,

        @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "The password must contain an uppercase letter, a lowercase letter, a number, and a special character."
        )
        @Schema(example = "Pa$$word1")
        String password,

        @Schema(example = "Jan")
        String firstName,

        @Schema(example = "Kowalski")
        String lastName,

        @Size(min = 9, max = 14, message = "Invalid Number.")
        @Schema(example = "+48 123456789")
        String phoneNumber
) {
}
