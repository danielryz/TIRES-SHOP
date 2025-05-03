package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserPasswordRequest(

        @Schema(example = "Pa$$word1")
        String password,

        @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "The password must contain an uppercase letter, a lowercase letter, a number, and a special character."
        )
        @Schema(example = "Pa$$word1")
        String newPassword
) {
}
