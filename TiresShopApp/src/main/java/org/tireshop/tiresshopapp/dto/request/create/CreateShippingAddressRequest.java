package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dane adresu dostawy")
public record CreateShippingAddressRequest(

        @NotBlank  @Schema(example = "ul. Długa") String street,
        @NotBlank @Schema(example = "12A") String houseNumber,
        @Schema(example = "5") String apartmentNumber,
        @NotBlank @Schema(example = "00-123") String postalCode,
        @NotBlank @Schema(example = "Warszawa") String city
) {}

