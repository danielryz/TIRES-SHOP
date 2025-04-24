package org.tireshop.tiresshopapp.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.AddressType;

public record UpdateAddressRequest(
        @Schema(example = "Warszawska")  String street,
        @Schema(example = "24")  String houseNumber,
        @Schema(example = "1") String apartmentNumber,
        @Schema(example = "30-100")  String postalCode,
        @Schema(example = "Kraków")  String city
) {
}
