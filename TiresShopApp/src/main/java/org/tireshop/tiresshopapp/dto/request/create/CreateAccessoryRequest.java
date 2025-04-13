package org.tireshop.tiresshopapp.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tireshop.tiresshopapp.entity.AccessoryType;
import org.tireshop.tiresshopapp.entity.ProductType;

import java.math.BigDecimal;

public record CreateAccessoryRequest(
        @Schema(example = "Komplet śrub do kół")
        String name,
        @Schema(example = "60,00")
        BigDecimal price,
        @Schema(example = "Zestaw 4 śrub wysokiej jakości")
        String description,
        @Schema(example = "15")
        int stock,
        @Schema(example = "ACCESSORY")
        ProductType type,
        @Schema(example = "ALUMINIUM")
        AccessoryType accessoryType
) {
}
