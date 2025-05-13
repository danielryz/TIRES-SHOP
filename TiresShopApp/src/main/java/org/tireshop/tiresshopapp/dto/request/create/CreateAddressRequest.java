package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.tireshop.tiresshopapp.entity.AddressType;

public record CreateAddressRequest(
        @NotBlank @Schema(example = "Warszawska")  String street,
        @NotBlank @Schema(example = "24")  String houseNumber,
        @Schema(example = "1") String apartmentNumber,
        @NotBlank @Schema(example = "30-100")  String postalCode,
        @NotBlank @Schema(example = "Kraków")  String city,
        @NotNull @Schema(example = "SHIPPING") AddressType type
) {
}
