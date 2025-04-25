package org.tireshop.tiresshopapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ShippingAddressResponse(
        @Schema(example = "134") Long id,
        @Schema(example = "ul. Długa 15") String street,
        @Schema(example = "15A") String houseNumber,
        @Schema(example = "3") String apartmentNumber,
        @Schema(example = "00-123") String postalCode,
        @Schema(example = "Warszawa") String city
) {
}
