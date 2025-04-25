package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Dane adresu dostawy")
public record CreateShippingAddressRequest(
        @Schema(example = "ul. Długa") String street,
        @Schema(example = "12A") String houseNumber,
        @Schema(example = "5") String apartmentNumber,
        @Schema(example = "00-123") String postalCode,
        @Schema(example = "Warszawa") String city
) {}

