package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dane do stworzenia zamówienia")
public record CreateOrderRequest(
        @NotBlank @Schema(example = "Jan") String guestFirstName,
        @NotBlank @Schema(example = "Kowalski") String guestLastName,
        @NotBlank @Schema(example = "jan.kowalski@example.com") String guestEmail,
        @NotBlank @Schema(example = "+40 123 456 789") String guestPhoneNumber,
        @NotBlank @Schema(example = "ul. Długa") String street,
        @NotBlank @Schema(example = "12A") String houseNumber,
        @Schema(example = "5") String apartmentNumber,
        @NotBlank @Schema(example = "00-123") String postalCode,
        @NotBlank @Schema(example = "Warszawa") String city
        ) {
}
